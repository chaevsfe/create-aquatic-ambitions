package net.davio.aquaticambitions.content.processing.conduit;

import com.mojang.blaze3d.vertex.PoseStack;
import com.zurrtum.create.catnip.math.AngleHelper;
import com.zurrtum.create.client.catnip.animation.AnimationTickHolder;
import com.zurrtum.create.client.catnip.render.CachedBuffers;
import com.zurrtum.create.client.catnip.render.SuperByteBuffer;
import com.zurrtum.create.client.catnip.render.SuperByteBufferRenderState;
import net.davio.aquaticambitions.content.processing.conduit.MechanicalConduitBlock.ConduitPowerLevel;
import net.davio.aquaticambitions.registry.CAAPartials;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.blockentity.state.BlockEntityRenderState;
import net.minecraft.client.renderer.feature.ModelFeatureRenderer.CrumblingOverlay;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.core.Direction;
import net.minecraft.util.LightCoordsUtil;
import net.minecraft.util.Mth;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

@Environment(EnvType.CLIENT)
public class MechanicalConduitRenderer implements BlockEntityRenderer<MechanicalConduitBlockEntity, MechanicalConduitRenderer.ConduitRenderState> {

    public static final int FULL_BLOCK_LIGHT = LightCoordsUtil.pack(15, 0);

    public MechanicalConduitRenderer(BlockEntityRendererProvider.Context context) {}

    @Override
    public ConduitRenderState createRenderState() {
        return new ConduitRenderState();
    }

    @Override
    public void extractRenderState(MechanicalConduitBlockEntity be, ConduitRenderState state, float partialTicks, Vec3 cameraPos, @Nullable CrumblingOverlay crumblingOverlay) {
        BlockEntityRenderState.extractBase(be, state, crumblingOverlay);
        state.parts.clear();

        ConduitPowerLevel conduitLevel = be.getConduitLevelFromBlock();

        Level level = be.getLevel();
        BlockState blockState = be.getBlockState();
        float animation = be.eyeAnimation.getValue(partialTicks) * .175f;
        float eyeAngle = AngleHelper.rad(be.eyeAngle.getValue(partialTicks));
        int hashCode = be.hashCode();

        extractShared(state.parts, level, blockState, conduitLevel, animation, eyeAngle, hashCode);
    }

    private static void extractShared(List<SuperByteBufferRenderState> parts, Level level, BlockState blockState,
                                      ConduitPowerLevel conduitLevel, float animation, float playerTrackAngle, int hashCode) {

        float time = AnimationTickHolder.getRenderTime(level);
        float renderTick = time + (hashCode % 13) * 16f;
        float offsetMult = conduitLevel.isAwakened() ? 64 : 16;
        float offset = Mth.sin((float) ((renderTick / 16f) % (2 * Math.PI))) / offsetMult;
        float eyeY = offset - (animation * .75f) + (conduitLevel.isAwakened() ? 0.2f : 0);
        float rotateYZ = Mth.DEG_TO_RAD * (time * 2f) % 360;
        float rotateX = Mth.DEG_TO_RAD * 30 * (Mth.sin(((time * 2f) % 360) * Mth.DEG_TO_RAD));

        SuperByteBuffer eyeBuffer = CachedBuffers.partial(CAAPartials.CONDUIT_EYE, blockState);
        eyeBuffer.translate(0, eyeY, 0);
        parts.add(draw(eyeBuffer, playerTrackAngle));

        if (conduitLevel.isAwakened()) {
            SuperByteBuffer cageBuffer = CachedBuffers.partial(CAAPartials.CONDUIT_CAGE, blockState);
            cageBuffer
                    .translate(0, eyeY, 0)
                    .rotateXCentered(rotateX)
                    .rotateZCentered(rotateYZ);
            parts.add(draw(cageBuffer, rotateYZ));

        } else {
            SuperByteBuffer inactiveConduitBuffer = CachedBuffers.partial(CAAPartials.INACTIVE_CONDUIT, blockState);
            inactiveConduitBuffer.translate(0, eyeY, 0);
            parts.add(draw(inactiveConduitBuffer, playerTrackAngle));
        }
    }

    private static SuperByteBufferRenderState draw(SuperByteBuffer buffer, float horizontalAngle) {
        return buffer.rotateCentered(horizontalAngle, Direction.UP)
                .light(FULL_BLOCK_LIGHT)
                .extractRenderState();
    }

    @Override
    public void submit(ConduitRenderState state, PoseStack ms, SubmitNodeCollector queue, CameraRenderState cameraState) {
        for (SuperByteBufferRenderState part : state.parts)
            part.submit(ms, queue);
    }

    public static class ConduitRenderState extends BlockEntityRenderState {
        public final List<SuperByteBufferRenderState> parts = new ArrayList<>();
    }
}
