package net.davio.aquaticambitions.content.processing.conduit;

import com.zurrtum.create.api.behaviour.BlockEntityBehaviour;
import com.zurrtum.create.catnip.animation.LerpedFloat;
import com.zurrtum.create.catnip.math.AngleHelper;
import com.zurrtum.create.client.flywheel.api.visualization.VisualizationManager;
import com.zurrtum.create.foundation.blockEntity.behaviour.BehaviourType;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.util.Mth;

@Environment(EnvType.CLIENT)
public class MechanicalConduitAnimation extends BlockEntityBehaviour<MechanicalConduitBlockEntity> {

    public static final BehaviourType<MechanicalConduitAnimation> TYPE = new BehaviourType<>("mechanical_conduit_animation");

    public MechanicalConduitAnimation(MechanicalConduitBlockEntity blockEntity) {
        super(blockEntity);
    }

    @Override
    public BehaviourType<?> getType() {
        return TYPE;
    }

    @Override
    public void tick() {
        super.tick();
        if (shouldTickAnimation()) {
            tickAnimation(blockEntity);
        }
    }

    private boolean shouldTickAnimation() {
        return !VisualizationManager.supportsVisualization(getLevel());
    }

    public static void tickAnimation(MechanicalConduitBlockEntity be) {
        float eyeTarget = 0;
        LocalPlayer player = Minecraft.getInstance().player;
        if (player != null && !player.isInvisible()) {
            double x;
            double z;
            if (be.isVirtual()) {
                x = -4;
                z = -10;
            } else {
                x = player.getX();
                z = player.getZ();
            };
            double dx = x - (be.getBlockPos().getX() + 0.5);
            double dz = z - (be.getBlockPos().getZ() + 0.5);
            eyeTarget = AngleHelper.deg(-Mth.atan2(dz, dx)) - 90;
        };
        eyeTarget = be.eyeAngle.getValue() + AngleHelper.getShortestAngleDiff(be.eyeAngle.getValue(), eyeTarget);
        be.eyeAngle.chase(eyeTarget, .25f, LerpedFloat.Chaser.exp(5));
        be.eyeAngle.tickChaser();

        be.eyeAnimation.chase(0, 0.25f, LerpedFloat.Chaser.exp(0.25f));
        be.eyeAnimation.tickChaser();
    }
}
