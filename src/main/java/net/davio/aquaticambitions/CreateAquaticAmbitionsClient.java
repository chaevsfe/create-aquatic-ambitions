package net.davio.aquaticambitions;

import com.zurrtum.create.client.AllBlockEntityBehaviours;
import com.zurrtum.create.client.AllBlockEntityRenders;
import com.zurrtum.create.client.catnip.lang.FontHelper;
import com.zurrtum.create.client.foundation.item.ItemDescription;
import com.zurrtum.create.client.foundation.item.KineticStats;
import com.zurrtum.create.client.foundation.item.TooltipModifier;
import com.zurrtum.create.client.ponder.foundation.PonderIndex;
import net.davio.aquaticambitions.content.processing.conduit.EntitySelectionModeScroll;
import net.davio.aquaticambitions.content.processing.conduit.MechanicalConduitAnimation;
import net.davio.aquaticambitions.content.processing.conduit.MechanicalConduitGoggles;
import net.davio.aquaticambitions.content.processing.conduit.MechanicalConduitRenderer;
import net.davio.aquaticambitions.content.processing.conduit.MechanicalConduitVisual;
import net.davio.aquaticambitions.ponder.CAAPonderPlugin;
import net.davio.aquaticambitions.ponder.util.fakeConduitEntity.FakeConduitRenderer;
import net.davio.aquaticambitions.registry.CAABlockEntityTypes;
import net.davio.aquaticambitions.registry.CAAPartials;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.Item;

@Environment(EnvType.CLIENT)
public class CreateAquaticAmbitionsClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        CAAPartials.init();
        registerRenderers();
        registerBehaviours();
        registerTooltipModifiers();
        PonderIndex.addPlugin(new CAAPonderPlugin());
    }

    private static void registerRenderers() {
        AllBlockEntityRenders.visual(CAABlockEntityTypes.MECHANICAL_CONDUIT.get(), MechanicalConduitRenderer::new,
                MechanicalConduitVisual::new, be -> true);
        AllBlockEntityRenders.render(CAABlockEntityTypes.FAKE_CONDUIT.get(), FakeConduitRenderer::new);
    }

    private static void registerBehaviours() {
        AllBlockEntityBehaviours.add(CAABlockEntityTypes.MECHANICAL_CONDUIT.get(),
                EntitySelectionModeScroll::new, MechanicalConduitGoggles::new, MechanicalConduitAnimation::new);
    }

    private static void registerTooltipModifiers() {
        for (Item item : BuiltInRegistries.ITEM) {
            if (!CreateAquaticAmbitions.MODID.equals(BuiltInRegistries.ITEM.getKey(item).getNamespace()))
                continue;
            TooltipModifier.REGISTRY.register(item, new ItemDescription.Modifier(item, FontHelper.Palette.STANDARD_CREATE)
                    .andThen(TooltipModifier.mapNull(KineticStats.create(item))));
        }
    }
}
