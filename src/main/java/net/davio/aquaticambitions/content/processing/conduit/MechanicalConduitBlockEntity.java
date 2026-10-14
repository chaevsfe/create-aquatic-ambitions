package net.davio.aquaticambitions.content.processing.conduit;

import com.zurrtum.create.AllFluids;
import com.zurrtum.create.api.behaviour.BlockEntityBehaviour;
import com.zurrtum.create.catnip.animation.LerpedFloat;
import com.zurrtum.create.catnip.math.AngleHelper;
import com.zurrtum.create.foundation.blockEntity.SmartBlockEntity;
import com.zurrtum.create.foundation.blockEntity.behaviour.CachedFluidInventoryBehaviour;
import com.zurrtum.create.foundation.blockEntity.behaviour.fluid.SmartFluidTankBehaviour;
import com.zurrtum.create.foundation.blockEntity.behaviour.scrollValue.ServerScrollOptionBehaviour;
import com.zurrtum.create.infrastructure.fluids.FluidStack;
import net.davio.aquaticambitions.content.processing.conduit.MechanicalConduitBlock.ConduitPowerLevel;
import net.davio.aquaticambitions.infrastructure.config.CAAConfig;
import net.davio.aquaticambitions.registry.CAABlockEntityTypes;
import net.davio.aquaticambitions.registry.CAARegistries;
import net.davio.aquaticambitions.registry.CAATags;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidStorage;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.Identifier;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.monster.cubemob.AbstractCubeMob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.alchemy.PotionContents;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Contract;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class MechanicalConduitBlockEntity extends SmartBlockEntity {

    public static final int DROPLETS_PER_MILLIBUCKET = 81;
    public static final int DROPLETS_PER_BUCKET = 1000 * DROPLETS_PER_MILLIBUCKET;

    protected SmartFluidTankBehaviour tank;

    private int awakenedTicks = 0;

    private static int tankCapacity() {
        return CAAConfig.server().conduitCage.conduitFluidCapacity.get() * DROPLETS_PER_MILLIBUCKET;
    }

    private static int awakenedTicksLimit() {
        return CAAConfig.server().conduitCage.awakenedTimeLimit.get() * 20;
    }

    private Map<Identifier, MechanicalConduitEffect> conduitEffectsMap = null;

    protected LerpedFloat eyeAnimation;
    protected LerpedFloat eyeAngle;
    protected LerpedFloat cageAngle;

    protected ServerScrollOptionBehaviour<EntitySelectionMode> entityTypeSelector;

    public MechanicalConduitBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);

        eyeAnimation = LerpedFloat.linear();
        eyeAngle = LerpedFloat.angular();
        cageAngle = LerpedFloat.angular();

        eyeAngle.startWithValue((AngleHelper.horizontalAngle(Direction.NORTH) + 180) % 360);
        cageAngle.startWithValue((AngleHelper.horizontalAngle(Direction.NORTH) + 180) % 360);
    }

    @Override
    public void addBehaviours(List<BlockEntityBehaviour<?>> behaviours) {

        tank = new SmartFluidTankBehaviour(SmartFluidTankBehaviour.INPUT, this, 1, tankCapacity(), true)
                .forbidExtraction();
        behaviours.add(tank);
        behaviours.add(new CachedFluidInventoryBehaviour<>(this, be -> be.tank.getCapability()));

        behaviours.add(entityTypeSelector = new ServerScrollOptionBehaviour<>(EntitySelectionMode.class, this) {
            @Override
            public void read(ValueInput view, boolean clientPacket) {
                super.read(view, clientPacket);
                value = Mth.clamp(value, 0, options.length - 1);
            }
        });

    }

    public static void registerFluidStorage() {
        FluidStorage.SIDED.registerForBlockEntity((be, side) -> side == null || side == Direction.DOWN ? CachedFluidInventoryBehaviour.get(be, side) : null,
                CAABlockEntityTypes.MECHANICAL_CONDUIT.get());
    }

    public Map<Identifier, MechanicalConduitEffect> getConduitEffects() {
        if (conduitEffectsMap == null && level != null) {
            ensureEffects(level.registryAccess());
        }
        return conduitEffectsMap != null ? conduitEffectsMap : Map.of();
    }

    private void ensureEffects(HolderLookup.Provider registries) {
        if (conduitEffectsMap != null) return;
        registries.lookup(CAARegistries.CONDUIT_EFFECT).ifPresent(registry -> {
            Map<Identifier, MechanicalConduitEffect> map = new LinkedHashMap<>();
            registry.listElements()
                    .filter(ref -> ref.value().enabled())
                    .forEach(ref -> map.put(ref.key().identifier(), new MechanicalConduitEffect(ref.value())));
            conduitEffectsMap = map;
        });
    }

    private void consumeFluid() {

        FluidStack fluidStack = tank.getPrimaryHandler().getFluid();
        if (fluidStack.isEmpty())
            return;

        int limit = awakenedTicksLimit();
        for(MechanicalConduitEffect conduitEffect : getConduitEffects().values()) {
            if (fluidStack.isIn(conduitEffect.getFluidTag()) || potionHasEffect(fluidStack, conduitEffect))
                {
                conduitEffect.addTicks(getConversionRate(fluidStack)*fluidStack.getAmount());
                if (conduitEffect.getTicks() > limit)
                    conduitEffect.setTicks(limit);
            }
            if (conduitEffect.getTicks() >= limit) {
                tank.forbidInsertion();
            }
        }

        tank.getPrimaryHandler().extractAny(tankCapacity());

        notifyUpdate();
        updateBlockState();
        sendData();
    }

    private boolean potionHasEffect(FluidStack fluidStack, MechanicalConduitEffect conduitEffect) {
        if (conduitEffect.getEffect() == null) {
            return false;
        }
        PotionContents potionContents = fluidStack.get(DataComponents.POTION_CONTENTS);
        if (potionContents == null) {
            return false;
        }
        for(MobEffectInstance effect :potionContents.getAllEffects()) {
            if (effect.is(conduitEffect.getEffect())) {
                conduitEffect.setAmplifier(effect.getAmplifier());
                return true;
            }
        }
        return false;
    }

    @Override
    public void tick() {
        super.tick();
        if (this.level == null) return;

        if (level.isClientSide()) {
            if (!isVirtual()) {
                spawnParticles(getConduitLevelFromBlock());
            }
            return;
        }

        consumeFluid();

        int maxAwakanedTicks = 0;

        for(MechanicalConduitEffect conduitEffect : getConduitEffects().values()) {
            if (conduitEffect.isActive()) {
                conduitEffect.subtractTicks();

                switch (conduitEffect.getBehavior()) {
                    case CLEAR_EFFECTS -> this.clearEffects();
                    case SET_ON_FIRE -> this.setOnFire();
                    case APPLY -> {
                        if (conduitEffect.getEffect() != null) {
                            this.applyEffects(conduitEffect.getEffect(), conduitEffect.getAmplifier());
                        }
                    }
                }

                if (conduitEffect.getTicks() < awakenedTicksLimit()) {
                    tank.allowInsertion();
                }

                if (conduitEffect.getTicks() > maxAwakanedTicks) {
                    maxAwakanedTicks = conduitEffect.getTicks();
                }

            } else {
                if (conduitEffect.getAmplifier() > 0) {
                    conduitEffect.resetAmplifier();
                }
            }

            awakenedTicks = maxAwakanedTicks;
        }
        updateBlockState();
        sendData();
    }

    @Override
    protected void write(ValueOutput view, boolean clientPacket) {
        view.putInt("awakenedTimeRemaining", awakenedTicks);

        if (conduitEffectsMap == null && level != null)
            ensureEffects(level.registryAccess());
        if (conduitEffectsMap != null) {
            CompoundTag effectsTag = new CompoundTag();
            for (Map.Entry<Identifier, MechanicalConduitEffect> entry : conduitEffectsMap.entrySet()) {
                effectsTag.putInt(entry.getKey().toString(), entry.getValue().getTicks());
            }
            view.store("ConduitEffects", CompoundTag.CODEC, effectsTag);
        }

        super.write(view, clientPacket);
    }

    @Override
    protected void read(ValueInput view, boolean clientPacket) {
        awakenedTicks = view.getIntOr("awakenedTimeRemaining", 0);
        super.read(view, clientPacket);

        ensureEffects(view.lookup());
        if (conduitEffectsMap == null)
            return;
        CompoundTag effectsTag = view.read("ConduitEffects", CompoundTag.CODEC).orElseGet(CompoundTag::new);
        for (String key : effectsTag.keySet()) {
            Identifier id = Identifier.tryParse(key);
            MechanicalConduitEffect effect = id == null ? null : conduitEffectsMap.get(id);
            if (effect != null) {
                effect.setTicks(effectsTag.getIntOr(key, 0));
            }
        }
    }

    public ConduitPowerLevel getConduitLevelFromBlock() {
        return MechanicalConduitBlock.getConduitLevelOf(getBlockState());
    }

    public void updateBlockState(){
        setConduitLevelOfBlock(getConduitLevel());
    }

    public void setConduitLevelOfBlock(ConduitPowerLevel conduitPowerLevel) {
        if (this.level == null) return;
        ConduitPowerLevel inBlockState = getConduitLevelFromBlock();
        if (inBlockState == conduitPowerLevel)
            return;
        level.setBlockAndUpdate(worldPosition, getBlockState().setValue(MechanicalConduitBlock.CONDUIT_POWER_LEVEL, conduitPowerLevel));
        notifyUpdate();
    }

    public ConduitPowerLevel getConduitLevel() {
        if (awakenedTicks > 0) {
            return ConduitPowerLevel.AWAKENED;
        } else {
            return ConduitPowerLevel.IDLE;
        }
    }

    protected void spawnParticles(ConduitPowerLevel conduitPowerLevel) {
        if (level == null)
            return;
        if (conduitPowerLevel == ConduitPowerLevel.IDLE)
            return;

        RandomSource r = level.getRandom();

        if (conduitPowerLevel.isAwakened() && r.nextInt(5) == 0) {
            Vec3 vec31 = new Vec3(getBlockPos().getX()+0.5f, getBlockPos().getY()+2f, getBlockPos().getZ()+0.5f);
            float f3 = (-0.5F + r.nextFloat())*2f;
            float f4 = (-1.25f + r.nextFloat());
            float f5 = (-0.5F + r.nextFloat())*2f;
            Vec3 vec32 = new Vec3((double)f3, (double)f4, (double)f5);
            level.addParticle(ParticleTypes.NAUTILUS, vec31.x, vec31.y, vec31.z, vec32.x, vec32.y, vec32.z);
        }
    }

    private static final int APPLIED_EFFECT_DURATION = 260;
    private static final int EFFECT_REFRESH_THRESHOLD = 210;

    private void applyEffects(Holder<MobEffect> effect, int amplifier  ) {
        int range = CAAConfig.server().conduitCage.conduitCageRange.get();
        List<LivingEntity> list = getLivingEntities(range);

        if (!list.isEmpty()) {
            for(LivingEntity entity : list) {
                if (entityMatchesSelector(entity, entityTypeSelector.get())){
                    if (this.getBlockPos().closerThan(entity.blockPosition(), (double)range)) {
                        MobEffectInstance existing = entity.getEffect(effect);
                        if (existing == null || existing.getDuration() < EFFECT_REFRESH_THRESHOLD) {
                            entity.addEffect(new MobEffectInstance(effect, APPLIED_EFFECT_DURATION, amplifier, true, true));
                        }
                    }
                }
            }
        }
    }

    private void clearEffects() {
        int range = CAAConfig.server().conduitCage.conduitCageRange.get();

        List<LivingEntity> list = getLivingEntities(range);
        if (!list.isEmpty()) {
            for(LivingEntity entity : list) {
                if (entityMatchesSelector(entity, entityTypeSelector.get())){
                    if (this.getBlockPos().closerThan(entity.blockPosition(), (double)range)) {
                        entity.removeAllEffects();
                    }
                }
            }
        }
    }

    private void setOnFire() {
        int range = CAAConfig.server().conduitCage.conduitCageRange.get();

        List<LivingEntity> list = getLivingEntities(range);
        if (!list.isEmpty()) {
            for(LivingEntity entity : list) {
                if (entityMatchesSelector(entity, entityTypeSelector.get())){
                    if (this.getBlockPos().closerThan(entity.blockPosition(), (double)range)) {
                        entity.setRemainingFireTicks(5*20);
                    }
                }
            }
        }
    }

    public List<LivingEntity> getLivingEntities(int range) {
        if (this.level == null) return List.of();
        int k = this.getBlockPos().getX();
        int l = this.getBlockPos().getY();
        int i1 = this.getBlockPos().getZ();
        AABB aabb = (new AABB(k, l, i1, k + 1, l + 1, i1 + 1)).inflate(range).expandTowards(0.0F, this.level.getHeight(), 0.0F);
        return this.level.getEntitiesOfClass(LivingEntity.class, aabb);
    }

    public float getConversionRate(FluidStack fluidStack) {

        float conversionRate;

        if (CAATags.CAAFluidTags.CONDUIT_FUEL.matches(fluidStack.getFluid())) {
            conversionRate  = CAAConfig.server().conduitCage.waterAwakenConversionRate.get()*20/(float) DROPLETS_PER_BUCKET;
        } else if (fluidStack.getFluid().isSame(AllFluids.POTION)) {
            conversionRate = CAAConfig.server().conduitCage.potionAwakenConversionRate.get()*20/(float) DROPLETS_PER_BUCKET;
        } else {
            conversionRate = CAAConfig.server().conduitCage.fluidAwakenConversionRate.get()*20/(float) DROPLETS_PER_BUCKET;
        }

        return conversionRate;
    }

    @Contract(pure = true)
    public static boolean entityMatchesSelector(LivingEntity entity, EntitySelectionMode mode) {
        return switch (mode) {
            case EVERYONE -> true;
            case PLAYERS -> entity instanceof Player;
            case MONSTERS -> entity instanceof Enemy;
            case FRIENDLY_MOBS -> isFriendlyMob(entity);
            case PLAYERS_FRIENDLY_MOBS -> isFriendlyMob(entity) || (entity instanceof Player);
        };
    }

    private static boolean isFriendlyMob(LivingEntity entity) {
        return entity instanceof PathfinderMob && !(entity instanceof Monster) && !(entity instanceof AbstractCubeMob);
    }

    public String tickToDuration(int tickAmount) {
        if (tickAmount >= awakenedTicksLimit() - 10) return "∞";
        int totalSeconds = tickAmount / 20;
        int hours = totalSeconds / 3600;
        int minutes = (totalSeconds / 60)%60;
        int seconds = totalSeconds % 60;
        if (hours > 0) {
            return String.format("%02d:%02d:%02d",hours, minutes, seconds);}
        else{
            return String.format("%02d:%02d", minutes, seconds);
        }
    }

    public enum EntitySelectionMode {
        EVERYONE,
        PLAYERS,
        FRIENDLY_MOBS,
        PLAYERS_FRIENDLY_MOBS,
        MONSTERS;
    }
}
