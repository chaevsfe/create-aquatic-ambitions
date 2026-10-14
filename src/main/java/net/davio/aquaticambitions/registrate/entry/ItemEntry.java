package net.davio.aquaticambitions.registrate.entry;

import net.minecraft.resources.Identifier;
import net.minecraft.world.item.Item;

public class ItemEntry<T extends Item> extends ItemProviderEntry<Item, T> {
    public ItemEntry(Identifier id, T value) {
        super(id, value);
    }

    @Override
    public Item asItem() {
        return get();
    }
}
