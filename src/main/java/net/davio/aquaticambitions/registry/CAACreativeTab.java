package net.davio.aquaticambitions.registry;

import net.davio.aquaticambitions.CreateAquaticAmbitions;
import net.fabricmc.fabric.api.creativetab.v1.FabricCreativeModeTab;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.CreativeModeTab;

public class CAACreativeTab {

    public static final ResourceKey<CreativeModeTab> MAIN_TAB_KEY =
            ResourceKey.create(Registries.CREATIVE_MODE_TAB, CreateAquaticAmbitions.asResource("tab"));

    public static final CreativeModeTab MAIN_TAB = Registry.register(BuiltInRegistries.CREATIVE_MODE_TAB, MAIN_TAB_KEY,
        FabricCreativeModeTab.builder()
            .title(Component.translatableWithFallback("itemGroup.create_aquatic_ambitions.base", "Create Aquatic Ambitions"))
            .icon(CAAItems.PRISMARINE_ALLOY::asStack)
            .displayItems(((itemDisplayParameters, output) -> {

                output.accept(CAAItems.PRISMARINE_ALLOY.get());
                output.accept(CAABlocks.PRISMARINE_ALLOY_BLOCK.asItem());
                output.accept(CAAItems.PRISMARINE_ROD.get());
                output.accept(CAABlocks.MECHANICAL_CONDUIT.asItem());
                output.accept(CAAItems.SUSPICIOUS_ROCK.get());
                output.accept(CAAItems.SPIKY_SHELL.get());
                output.accept(CAAItems.CALCIUM_RICH_POWDER.get());

            }))
            .build());

    public static void register() {
    }
}
