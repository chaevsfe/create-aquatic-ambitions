package net.davio.aquaticambitions;

import com.mojang.logging.LogUtils;
import net.davio.aquaticambitions.compat.CAARecipeSync;
import net.davio.aquaticambitions.content.logistics.CAAItemAttributes;
import net.davio.aquaticambitions.content.processing.conduit.MechanicalConduitBlockEntity;
import net.davio.aquaticambitions.infrastructure.config.CAAConfig;
import net.davio.aquaticambitions.infrastructure.loot.CAALootModifiers;
import net.davio.aquaticambitions.registrate.Registrate;
import net.davio.aquaticambitions.registry.*;
import net.fabricmc.api.ModInitializer;
import net.minecraft.resources.Identifier;
import org.slf4j.Logger;

public class CreateAquaticAmbitions implements ModInitializer {
    public static final String MODID = "create_aquatic_ambitions";

    public static final Logger LOGGER = LogUtils.getLogger();

    public static final Registrate REGISTRATE = Registrate.create(MODID);

    public static void registerBlocks() {
        CAABlocks.register();
    }

    @Override
    public void onInitialize() {
        CreateAquaticAmbitionsPlugin.verifyBlocksRegistered();

        CAAConfig.register();
        CAAItems.register();
        CAABlockEntityTypes.register();
        MechanicalConduitBlockEntity.registerFluidStorage();
        CAACreativeTab.register();
        CAARecipeTypes.register();
        CAAFanProcessingTypes.register();
        CAAItemAttributes.register();
        CAARegistries.register();
        CAALootModifiers.register();
        CAARecipeSync.register();
    }

    public static Identifier asResource(String path) {
        return Identifier.fromNamespaceAndPath(MODID, path);
    }
}
