package net.davio.aquaticambitions.registry;

import net.davio.aquaticambitions.CreateAquaticAmbitions;
import net.davio.aquaticambitions.content.processing.conduit.MechanicalConduitEffectDefinition;
import net.fabricmc.fabric.api.event.registry.DynamicRegistries;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;

public class CAARegistries {
    public static final ResourceKey<Registry<MechanicalConduitEffectDefinition>> CONDUIT_EFFECT = key("conduit_effect");

    private static <T> ResourceKey<Registry<T>> key(String name) {
        return ResourceKey.createRegistryKey(CreateAquaticAmbitions.asResource(name));
    }

    public static void register() {
        DynamicRegistries.registerSynced(CONDUIT_EFFECT, MechanicalConduitEffectDefinition.CODEC, MechanicalConduitEffectDefinition.CODEC);
    }

    private CAARegistries() {
    }
}
