package net.davio.aquaticambitions.ponder;

import com.zurrtum.create.client.ponder.api.registration.PonderSceneRegistrationHelper;
import net.davio.aquaticambitions.ponder.scenes.ConduitScenes;
import net.davio.aquaticambitions.ponder.scenes.MechanicalConduitScenes;
import net.davio.aquaticambitions.registrate.entry.ItemProviderEntry;
import net.davio.aquaticambitions.registrate.entry.RegistryEntry;
import net.davio.aquaticambitions.registry.CAABlocks;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;

@Environment(EnvType.CLIENT)
public class CAAPonderScenes {

    public static void register(PonderSceneRegistrationHelper<Identifier> helper) {
        PonderSceneRegistrationHelper<ItemProviderEntry<?, ?>> HELPER = helper.withKeyFunction(RegistryEntry::getId);

        HELPER.forComponents(CAABlocks.MECHANICAL_CONDUIT)
                .addStoryBoard("mechanical_conduit/awaken", MechanicalConduitScenes::awaken);
        HELPER.forComponents(CAABlocks.MECHANICAL_CONDUIT)
                .addStoryBoard("mechanical_conduit/processing", MechanicalConduitScenes::processing);
        HELPER.forComponents(CAABlocks.MECHANICAL_CONDUIT)
                .addStoryBoard("mechanical_conduit/effects", MechanicalConduitScenes::effects);

        PonderSceneRegistrationHelper<Item> VANILLA = helper.withKeyFunction(b -> BuiltInRegistries.ITEM.getKey(b));

        VANILLA.forComponents(Items.CONDUIT)
                .addStoryBoard("conduit/processing", ConduitScenes::processing);
    }
}
