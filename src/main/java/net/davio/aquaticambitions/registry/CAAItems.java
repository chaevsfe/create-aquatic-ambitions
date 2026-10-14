package net.davio.aquaticambitions.registry;

import net.davio.aquaticambitions.registrate.entry.ItemEntry;
import net.minecraft.world.item.Item;

import static net.davio.aquaticambitions.CreateAquaticAmbitions.REGISTRATE;

public class CAAItems {

    public static final ItemEntry<Item>
        PRISMARINE_ALLOY = ingredient("prismarine_alloy"),
        PRISMARINE_ROD = ingredient("prismarine_alloy_rod"),
        CALCIUM_RICH_POWDER = ingredient("calcium_rich_powder"),
        SPIKY_SHELL = ingredient("spiky_shell"),
        SUSPICIOUS_ROCK = ingredient("suspicious_rock");

    private static ItemEntry<Item> ingredient(String name) {
        return REGISTRATE.item(name, Item::new)
                .register();
    }

    public static void register() {
    }
}
