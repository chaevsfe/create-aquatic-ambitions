package net.davio.aquaticambitions.content.processing.conduit;

import com.zurrtum.create.catnip.math.AngleHelper;
import com.zurrtum.create.client.catnip.animation.AnimationTickHolder;
import com.zurrtum.create.client.flywheel.api.instance.Instance;
import com.zurrtum.create.client.flywheel.api.visual.DynamicVisual;
import com.zurrtum.create.client.flywheel.api.visual.TickableVisual;
import com.zurrtum.create.client.flywheel.api.visualization.VisualizationContext;
import com.zurrtum.create.client.flywheel.lib.instance.InstanceTypes;
import com.zurrtum.create.client.flywheel.lib.instance.TransformedInstance;
import com.zurrtum.create.client.flywheel.lib.model.Models;
import com.zurrtum.create.client.flywheel.lib.transform.Translate;
import com.zurrtum.create.client.flywheel.lib.visual.AbstractBlockEntityVisual;
import com.zurrtum.create.client.flywheel.lib.visual.SimpleDynamicVisual;
import com.zurrtum.create.client.flywheel.lib.visual.SimpleTickableVisual;
import net.davio.aquaticambitions.content.processing.conduit.MechanicalConduitBlock.ConduitPowerLevel;
import net.davio.aquaticambitions.registry.CAAPartials;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.util.Mth;
import org.jetbrains.annotations.Nullable;

import java.util.function.Consumer;

@Environment(EnvType.CLIENT)
public class MechanicalConduitVisual extends AbstractBlockEntityVisual<MechanicalConduitBlockEntity> implements SimpleDynamicVisual, SimpleTickableVisual {

    private ConduitPowerLevel conduitPowerLevel;


    private final TransformedInstance eye;
    @Nullable
    private TransformedInstance inactiveConduit;
    @Nullable
    private TransformedInstance cage;

    public MechanicalConduitVisual(VisualizationContext ctx, MechanicalConduitBlockEntity blockEntity, float partialTick) {
        super(ctx, blockEntity, partialTick);

        conduitPowerLevel = ConduitPowerLevel.IDLE;

        eye = instancerProvider().instancer(InstanceTypes.TRANSFORMED, Models.partial(CAAPartials.CONDUIT_EYE))
                .createInstance();

        eye.light(MechanicalConduitRenderer.FULL_BLOCK_LIGHT);

        animate(partialTick);
    }

    @Override
    public void tick(TickableVisual.Context context) {
        MechanicalConduitAnimation.tickAnimation(blockEntity);
    }

    @Override
    public void beginFrame(DynamicVisual.Context ctx) {
        if (!isVisible(ctx.frustum()) || doDistanceLimitThisFrame(ctx)) {
            return;
        }

        animate(ctx.partialTick());
    }

    private void animate(float partialTicks){
        float animation = blockEntity.eyeAnimation.getValue(partialTicks) * .175f;

        ConduitPowerLevel conduitPowerLevel = blockEntity.getConduitLevelFromBlock();
        boolean isActive = conduitPowerLevel.isAwakened();

        if (conduitPowerLevel != this.conduitPowerLevel || (cage == null & inactiveConduit == null)) {

            if (isActive) {

                cage = instancerProvider().instancer(InstanceTypes.TRANSFORMED, Models.partial(CAAPartials.CONDUIT_CAGE))
                        .createInstance();

                cage.light(MechanicalConduitRenderer.FULL_BLOCK_LIGHT);

                if (inactiveConduit != null) {
                    inactiveConduit.delete();
                    inactiveConduit = null;
                }

            } else {

                inactiveConduit = instancerProvider().instancer(InstanceTypes.TRANSFORMED, Models.partial(CAAPartials.INACTIVE_CONDUIT))
                        .createInstance();

                inactiveConduit.light(MechanicalConduitRenderer.FULL_BLOCK_LIGHT);

                if (cage != null) {
                    cage.delete();
                    cage = null;
                }
            }

            this.conduitPowerLevel = conduitPowerLevel;
        }

        var hashCode = blockEntity.hashCode();
        float time = AnimationTickHolder.getRenderTime();
        float renderTick = time + (hashCode % 13) *16f;
        float offset = Mth.sin((float) ((renderTick / 16f) % (2 * Math.PI))) / 16;
        float mainY = offset - (animation * .6f);
        float activeYOffset = .2f;
        float rotateYZ = Mth.DEG_TO_RAD*(time * 2f) % 360;
        float rotateX = Mth.DEG_TO_RAD*30*(Mth.sin(((time * 2f) % 360)*Mth.DEG_TO_RAD));


        float eyeAngle = AngleHelper.rad(blockEntity.eyeAngle.getValue(partialTicks));

        if (eye != null) {
        eye.setIdentityTransform()
                .translate(getVisualPosition())
                .translateY(mainY + (isActive? activeYOffset : 0))
                .translate(Translate.CENTER)
                .rotateY(eyeAngle)
                .translateBack(Translate.CENTER)
                .setChanged();
        }

        if (cage != null) {
            cage.setIdentityTransform()
                    .translate(getVisualPosition())
                    .translateY(mainY+activeYOffset)
                    .translate(Translate.CENTER)
                    .rotateY(rotateYZ)
                    .rotateZ(rotateYZ)
                    .rotateX(rotateX)
                    .translateBack(Translate.CENTER)
                    .setChanged();
        }

        if (inactiveConduit != null) {
            inactiveConduit.setIdentityTransform()
                    .translate(getVisualPosition())
                    .translateY(mainY)
                    .translate(Translate.CENTER)
                    .rotateY(eyeAngle)
                    .translateBack(Translate.CENTER)
                    .setChanged();
        }

    }

    @Override
    public void updateLight(float partialTick) {
    }

    @Override
    public void collectCrumblingInstances(Consumer<@Nullable Instance> consumer) {

    }
    @Override
    protected void _delete() {
        eye.delete();
        if (cage != null) {
            cage.delete();
        }
        if (inactiveConduit != null) {
            inactiveConduit.delete();
        }
    }

}
