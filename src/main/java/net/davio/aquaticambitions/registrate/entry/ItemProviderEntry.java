package net.davio.aquaticambitions.registrate.entry;

import net.minecraft.resources.Identifier;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;

public abstract class ItemProviderEntry<R, T extends R> extends RegistryEntry<R, T> implements ItemLike {
    protected ItemProviderEntry(Identifier id, T value) {
        super(id, value);
    }

    @Override
    public abstract Item asItem();

    public ItemStack asStack() {
        return new ItemStack(asItem());
    }

    public ItemStack asStack(int count) {
        return new ItemStack(asItem(), count);
    }

    public boolean isIn(ItemStack stack) {
        return stack.is(asItem());
    }
}
