package net.davio.aquaticambitions.ponder.util.fakeConduitEntity;

import com.zurrtum.create.api.behaviour.BlockEntityBehaviour;
import com.zurrtum.create.foundation.blockEntity.SmartBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

import java.util.List;

public class FakeActiveConduitBlockEntity extends SmartBlockEntity {

    public FakeActiveConduitBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    @Override
    public void addBehaviours(List<BlockEntityBehaviour<?>> behaviours) {
    }

    @Override
    public void tick() {

        if (!isVirtual()) {
            spawnParticles();
        }
    }

    protected void spawnParticles() {
        if (level == null)
            return;

        RandomSource r = level.getRandom();

        if (r.nextInt(5) == 0) {
            Vec3 vec31 = new Vec3(getBlockPos().getX()+0.5f, getBlockPos().getY()+2f, getBlockPos().getZ()+0.5f);
            float f3 = (-0.5F + r.nextFloat())*2f;
            float f4 = (-1.25f + r.nextFloat());
            float f5 = (-0.5F + r.nextFloat())*2f;
            Vec3 vec32 = new Vec3((double)f3, (double)f4, (double)f5);
            level.addParticle(ParticleTypes.NAUTILUS, vec31.x, vec31.y, vec31.z, vec32.x, vec32.y, vec32.z);
        }
    }
}
