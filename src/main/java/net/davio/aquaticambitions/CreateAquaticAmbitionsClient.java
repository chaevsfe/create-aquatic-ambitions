package net.davio.aquaticambitions;

import com.zurrtum.create.client.AllBlockEntityBehaviours;
import com.zurrtum.create.client.AllBlockEntityRenders;
import net.davio.aquaticambitions.content.processing.conduit.MechanicalConduitAnimation;
import net.davio.aquaticambitions.content.processing.conduit.MechanicalConduitRenderer;
import net.davio.aquaticambitions.content.processing.conduit.MechanicalConduitVisual;
import net.davio.aquaticambitions.ponder.util.fakeConduitEntity.FakeConduitRenderer;
import net.davio.aquaticambitions.registry.CAABlockEntityTypes;
import net.davio.aquaticambitions.registry.CAAPartials;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
public class CreateAquaticAmbitionsClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        CAAPartials.init();
        registerRenderers();
        registerBehaviours();
    }

    private static void registerRenderers() {
        AllBlockEntityRenders.visual(CAABlockEntityTypes.MECHANICAL_CONDUIT.get(), MechanicalConduitRenderer::new,
                MechanicalConduitVisual::new, be -> true);
        AllBlockEntityRenders.render(CAABlockEntityTypes.FAKE_CONDUIT.get(), FakeConduitRenderer::new);
    }

    private static void registerBehaviours() {
        AllBlockEntityBehaviours.add(CAABlockEntityTypes.MECHANICAL_CONDUIT.get(),
                MechanicalConduitAnimation::new);
    }
}
