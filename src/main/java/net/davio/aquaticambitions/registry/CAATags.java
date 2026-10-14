package net.davio.aquaticambitions.registry;

import com.zurrtum.create.Create;
import net.davio.aquaticambitions.CreateAquaticAmbitions;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.FluidState;

import java.util.Locale;

import static net.davio.aquaticambitions.CreateAquaticAmbitions.MODID;

public class CAATags {
    public static <T> TagKey<T> optionalTag(ResourceKey<? extends Registry<T>> registry, Identifier id) {
        return TagKey.create(registry, id);
    }

    public static <T> TagKey<T> commonTag(ResourceKey<? extends Registry<T>> registry, String path) {
        return optionalTag(registry, Identifier.fromNamespaceAndPath("c", path));
    }

    public static <T> TagKey<T> modTag(ResourceKey<? extends Registry<T>> registry, String path) {
        return optionalTag(registry, CreateAquaticAmbitions.asResource(path));
    }

    public enum NameSpace {
        MOD(MODID),
        COMMON("c"),
        CREATE(Create.MOD_ID);

        public final String id;

        NameSpace(String id) {
            this.id = id;
        }
    }

    private static Identifier id(NameSpace namespace, String path, Enum<?> constant) {
        return Identifier.fromNamespaceAndPath(namespace.id, path == null ? constant.name().toLowerCase(Locale.ROOT) : path);
    }

    public enum CAABlockTags {

        FAN_PROCESSING_CATALYSTS_CHANNELING(NameSpace.MOD, "fan_processing_catalysts/channeling");

        public final TagKey<Block> tag;

        CAABlockTags(NameSpace namespace, String path) {
            tag = optionalTag(Registries.BLOCK, id(namespace, path, this));
        }

        public boolean matches(Block block) {
            return block.builtInRegistryHolder()
                    .is(tag);
        }

        public boolean matches(ItemStack stack) {
            return stack != null && stack.getItem() instanceof BlockItem blockItem && matches(blockItem.getBlock());
        }

        public boolean matches(BlockState state) {
            return state.is(tag);
        }
    }

    public enum CAAFluidTags {

        CONDUIT_FUEL(NameSpace.MOD, "mechanical_conduit_fuel"),
        CLEARS_EFFECTS(NameSpace.MOD, "conduit_effects/clears_effects"),
        GIVES_FIRE_RES(NameSpace.MOD, "conduit_effects/gives_fire_resistance"),
        GIVES_HASTE(NameSpace.MOD, "conduit_effects/gives_haste"),
        GIVES_INFESTED(NameSpace.MOD, "conduit_effects/gives_infested"),
        GIVES_INVIS(NameSpace.MOD, "conduit_effects/gives_invisibility"),
        GIVES_JUMP(NameSpace.MOD, "conduit_effects/gives_jump"),
        GIVES_LUCK(NameSpace.MOD, "conduit_effects/gives_luck"),
        GIVES_NIGHT_VISION(NameSpace.MOD, "conduit_effects/gives_night_vision"),
        GIVES_OOZING(NameSpace.MOD, "conduit_effects/gives_oozing"),
        GIVES_POISON(NameSpace.MOD, "conduit_effects/gives_poison"),
        GIVES_REGEN(NameSpace.MOD, "conduit_effects/gives_regen"),
        GIVES_RESISTANCE(NameSpace.MOD, "conduit_effects/gives_resistance"),
        GIVES_SLOW_FALL(NameSpace.MOD, "conduit_effects/gives_slow_fall"),
        GIVES_SLOWNESS(NameSpace.MOD, "conduit_effects/gives_slowness"),
        GIVES_SPEED(NameSpace.MOD, "conduit_effects/gives_speed"),
        GIVES_STRENGTH(NameSpace.MOD, "conduit_effects/gives_strength"),
        GIVES_WATER_BREATHING(NameSpace.MOD, "conduit_effects/gives_water_breathing"),
        GIVES_WEAKNESS(NameSpace.MOD, "conduit_effects/gives_weakness"),
        GIVES_WEAVING(NameSpace.MOD, "conduit_effects/gives_weaving"),
        GIVES_WIND(NameSpace.MOD, "conduit_effects/gives_wind"),
        GIVES_WITHER(NameSpace.MOD, "conduit_effects/gives_wither"),
        GIVES_SATURATION(NameSpace.MOD, "conduit_effects/gives_saturation"),
        SETS_ON_FIRE(NameSpace.MOD, "conduit_effects/sets_on_fire"),
        FAN_PROCESSING_CATALYSTS_CHANNELING(NameSpace.MOD, "fan_processing_catalysts/channeling");

        public final TagKey<Fluid> tag;

        CAAFluidTags(NameSpace namespace, String path) {
            tag = optionalTag(Registries.FLUID, id(namespace, path, this));
        }

        @SuppressWarnings("deprecation")
        public boolean matches(Fluid fluid) {
            return fluid.is(tag);
        }

        public boolean matches(FluidState state) {
            return state.is(tag);
        }
    }
}
