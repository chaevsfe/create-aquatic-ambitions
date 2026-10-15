package net.davio.aquaticambitions.client;

import com.mojang.blaze3d.platform.Lighting;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import com.zurrtum.create.AllBlocks;
import com.zurrtum.create.client.AllPartialModels;
import com.zurrtum.create.client.catnip.animation.AnimationTickHolder;
import com.zurrtum.create.client.catnip.gui.render.BlockBakedQuadOutput;
import com.zurrtum.create.client.flywheel.lib.model.baked.ModelRenderHelper;
import com.zurrtum.create.client.flywheel.lib.model.baked.PartialModel;
import com.zurrtum.create.client.flywheel.lib.model.baked.SinglePosVirtualBlockGetter;
import com.zurrtum.create.client.foundation.gui.render.FanRenderer;
import net.davio.aquaticambitions.registry.CAAPartials;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.render.pip.PictureInPictureRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.dispatch.BlockStateModel;
import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;

@Environment(EnvType.CLIENT)
public class ConduitFanRenderer extends PictureInPictureRenderer<ConduitFanRenderState> {
    private final BlockBakedQuadOutput output;
    private SinglePosVirtualBlockGetter world;

    public ConduitFanRenderer(MultiBufferSource.BufferSource bufferSource) {
        super(bufferSource);
        output = new BlockBakedQuadOutput(bufferSource);
    }

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
    protected void renderToTexture(ConduitFanRenderState state, PoseStack matrices) {
        Minecraft.getInstance().gameRenderer.getLighting().setupFor(Lighting.Entry.ENTITY_IN_UI);
        output.setPoseStack(matrices);
        world = SinglePosVirtualBlockGetter.createFullBright();

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
        partial(AllPartialModels.ENCASED_FAN_INNER);
        matrices.popPose();

        matrices.pushPose();
        matrices.rotateAround(Axis.YP.rotationDegrees(180), 0.5f, 0.5f, 0.5f);
        BlockState fan = AllBlocks.ENCASED_FAN.defaultBlockState();
        draw(fan, Minecraft.getInstance().getModelManager().getBlockStateModelSet().get(fan));
        matrices.popPose();

        matrices.translate(0, 0, 2);

        float height = getConduitHeight();
        matrices.pushPose();
        matrices.translate(0, height, 0);
        matrices.rotateAround(Axis.XP.rotationDegrees(getConduitAngle(true)), 0.5f, 0.5f, 0.5f);
        matrices.rotateAround(Axis.YP.rotationDegrees(getConduitAngle(false)), 0.5f, 0.5f, 0.5f);
        partial(CAAPartials.CONDUIT_CAGE);
        matrices.popPose();

        matrices.pushPose();
        matrices.translate(0, height, 0);
        matrices.rotateAround(Axis.YP.rotationDegrees(-22.5f), 0.5f, 0.5f, 0.5f);
        partial(CAAPartials.CONDUIT_EYE);
        matrices.popPose();

        partial(CAAPartials.CONDUIT_WIND);

        output.clear();
        world = null;
    }

    private void partial(PartialModel model) {
        draw(Blocks.AIR.defaultBlockState(), model.get());
    }

    private void draw(BlockState state, BlockStateModel model) {
        world.blockState(state);
        output.updateBuffer(model);
        ModelRenderHelper.getHelper(output).tesselateBlock(0, 0, 0, world, BlockPos.ZERO, state, model, 42L);
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
