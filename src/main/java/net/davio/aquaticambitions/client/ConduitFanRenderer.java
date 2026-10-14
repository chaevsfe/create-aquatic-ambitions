package net.davio.aquaticambitions.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import com.zurrtum.create.AllBlocks;
import com.zurrtum.create.client.AllPartialModels;
import com.zurrtum.create.client.catnip.animation.AnimationTickHolder;
import com.zurrtum.create.client.catnip.render.CachedBuffers;
import com.zurrtum.create.client.flywheel.lib.model.baked.PartialModel;
import com.zurrtum.create.client.foundation.gui.render.FanRenderer;
import com.zurrtum.create.client.foundation.gui.render.GuiBlockRenderer;
import net.davio.aquaticambitions.registry.CAAPartials;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.util.Mth;
import net.minecraft.world.level.block.Blocks;

@Environment(EnvType.CLIENT)
public class ConduitFanRenderer extends GuiBlockRenderer<ConduitFanRenderState> {

    public static float getConduitAngle(boolean xAxis) {
        if (xAxis){
            return 30*(Mth.sin(((AnimationTickHolder.getRenderTime() * 2f) % 360)*(float)Math.PI/180));
        } else {
            return (AnimationTickHolder.getRenderTime() * 2f) % 360;
        }
    }

    public static float getConduitHeight() {
        return (0.2f*(Mth.sin(((AnimationTickHolder.getRenderTime() * 4f) % 360)*(float)Math.PI/180)));
    }

    @Override
    protected void renderToTexture(ConduitFanRenderState state, PoseStack matrices, SubmitNodeCollector queue) {
        matrices.scale(1, 1, -1);
        matrices.mulPose(Axis.XP.rotationDegrees(-15.5f));
        matrices.mulPose(Axis.YP.rotationDegrees(22.5f));
        matrices.translate(-0.92f, -0.75f, -0.5f);
        matrices.scale(1, -1, 1);

        matrices.pushPose();
        matrices.translate(0.5f, 0.5f, 0.5f);
        matrices.mulPose(Axis.ZP.rotationDegrees(FanRenderer.getCurrentAngle() * 16));
        matrices.mulPose(Axis.XP.rotationDegrees(180));
        matrices.translate(-0.5f, -0.5f, -0.5f);
        CachedBuffers.partial(AllPartialModels.ENCASED_FAN_INNER, Blocks.AIR.defaultBlockState()).submit(matrices, queue);
        matrices.popPose();

        matrices.pushPose();
        matrices.rotateAround(Axis.YP.rotationDegrees(180), 0.5f, 0.5f, 0.5f);
        CachedBuffers.block(AllBlocks.ENCASED_FAN.defaultBlockState()).submit(matrices, queue);
        matrices.popPose();

        matrices.translate(0, 0, 2);

        float height = getConduitHeight();
        matrices.pushPose();
        matrices.translate(0, height, 0);
        matrices.rotateAround(Axis.XP.rotationDegrees(getConduitAngle(true)), 0.5f, 0.5f, 0.5f);
        matrices.rotateAround(Axis.YP.rotationDegrees(getConduitAngle(false)), 0.5f, 0.5f, 0.5f);
        submit(CAAPartials.CONDUIT_CAGE, matrices, queue);
        matrices.popPose();

        matrices.pushPose();
        matrices.translate(0, height, 0);
        matrices.rotateAround(Axis.YP.rotationDegrees(-22.5f), 0.5f, 0.5f, 0.5f);
        submit(CAAPartials.CONDUIT_EYE, matrices, queue);
        matrices.popPose();

        submit(CAAPartials.CONDUIT_WIND, matrices, queue);
    }

    private static void submit(PartialModel model, PoseStack matrices, SubmitNodeCollector queue) {
        CachedBuffers.partial(model, Blocks.AIR.defaultBlockState()).submit(matrices, queue);
    }

    @Override
    protected String getTextureLabel() {
        return "Conduit Fan";
    }

    @Override
    public Class<ConduitFanRenderState> getRenderStateClass() {
        return ConduitFanRenderState.class;
    }
}
