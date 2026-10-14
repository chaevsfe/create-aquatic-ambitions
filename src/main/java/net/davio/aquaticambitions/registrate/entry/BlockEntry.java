package net.davio.aquaticambitions.registrate.entry;

import net.minecraft.resources.Identifier;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

public class BlockEntry<T extends Block> extends ItemProviderEntry<Block, T> {
    public BlockEntry(Identifier id, T value) {
        super(id, value);
    }

    public BlockState getDefaultState() {
        return get().defaultBlockState();
    }

    public boolean has(BlockState state) {
        return state.is(get());
    }

    @Override
    public Item asItem() {
        return get().asItem();
    }
}
