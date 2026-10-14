package net.davio.aquaticambitions.ponder.util.fakeConduitEntity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.zurrtum.create.client.catnip.animation.AnimationTickHolder;
import com.zurrtum.create.client.catnip.render.CachedBuffers;
import com.zurrtum.create.client.catnip.render.SuperByteBuffer;
import com.zurrtum.create.client.catnip.render.SuperByteBufferRenderState;
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
public class FakeConduitRenderer implements BlockEntityRenderer<FakeActiveConduitBlockEntity, FakeConduitRenderer.FakeConduitRenderState> {

    public FakeConduitRenderer(BlockEntityRendererProvider.Context context) {}

    @Override
    public FakeConduitRenderState createRenderState() {
        return new FakeConduitRenderState();
    }

    @Override
    public void extractRenderState(FakeActiveConduitBlockEntity be, FakeConduitRenderState state, float partialTicks, Vec3 cameraPos, @Nullable CrumblingOverlay crumblingOverlay) {
        BlockEntityRenderState.extractBase(be, state, crumblingOverlay);
        state.parts.clear();
        extractShared(state.parts, be.getLevel(), be.getBlockState());
    }

    private static void extractShared(List<SuperByteBufferRenderState> parts, Level level, BlockState blockState) {

        float time = AnimationTickHolder.getRenderTime(level);
        float eyeY = Mth.DEG_TO_RAD*15*(Mth.sin(((time * 4f) % 360)*Mth.DEG_TO_RAD));
        float rotateYZ = Mth.DEG_TO_RAD*(time * 2f) % 360;
        float rotateX = Mth.DEG_TO_RAD*30*(Mth.sin(((time * 2f) % 360)*Mth.DEG_TO_RAD));

        SuperByteBuffer headBuffer = CachedBuffers.partial(CAAPartials.CONDUIT_EYE, blockState);
        headBuffer.translate(0, eyeY, 0);
        parts.add(draw(headBuffer, 45));

        SuperByteBuffer cageBuffer = CachedBuffers.partial(CAAPartials.CONDUIT_CAGE, blockState);
        cageBuffer
                .translate(0, eyeY, 0)
                .rotateXCentered(rotateX)
                .rotateZCentered(rotateYZ);
        parts.add(draw(cageBuffer, rotateYZ));

        SuperByteBuffer windBuffer = CachedBuffers.partial(CAAPartials.CONDUIT_WIND, blockState);
        windBuffer
        .translate(0, 0, 0);
        parts.add(draw(windBuffer, 0));
    }

    private static SuperByteBufferRenderState draw(SuperByteBuffer buffer, float horizontalAngle) {
        return buffer.rotateCentered(horizontalAngle, Direction.UP)
                .light(LightCoordsUtil.FULL_BRIGHT)
                .extractRenderState();
    }

    @Override
    public void submit(FakeConduitRenderState state, PoseStack ms, SubmitNodeCollector queue, CameraRenderState cameraState) {
        for (SuperByteBufferRenderState part : state.parts)
            part.submit(ms, queue);
    }

    public static class FakeConduitRenderState extends BlockEntityRenderState {
        public final List<SuperByteBufferRenderState> parts = new ArrayList<>();
    }
}
