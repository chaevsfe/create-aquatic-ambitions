package net.davio.aquaticambitions.content.processing.conduit;

import com.zurrtum.create.catnip.math.AngleHelper;
import com.zurrtum.create.catnip.math.VecHelper;
import com.zurrtum.create.client.foundation.blockEntity.behaviour.ValueBoxTransform;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

@Environment(EnvType.CLIENT)
public class MechanicalConduitModeSlot extends ValueBoxTransform.Sided {

    @Override
    public Vec3 getLocalOffset(BlockState state) {
        Direction side = getSide();
        float horizontalAngle = AngleHelper.horizontalAngle(side);
        Vec3 southLocation = VecHelper.voxelSpace(8, 3, 14.5f);
        return VecHelper.rotateCentered(southLocation, horizontalAngle, Direction.Axis.Y);
    }

    @Override
    protected Vec3 getSouthLocation() {
        return Vec3.ZERO;
    }

    }
