package net.davio.aquaticambitions.registrate.entry;

import net.minecraft.core.BlockPos;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

public class BlockEntityEntry<T extends BlockEntity> extends RegistryEntry<BlockEntityType<?>, BlockEntityType<T>> {
    public BlockEntityEntry(Identifier id, BlockEntityType<T> value) {
        super(id, value);
    }

    public boolean is(@Nullable BlockEntity blockEntity) {
        return blockEntity != null && blockEntity.getType() == get();
    }

    public boolean isValid(BlockState state) {
        return get().isValid(state);
    }

    @Nullable
    public T at(BlockGetter level, BlockPos pos) {
        return get().getBlockEntity(level, pos);
    }
}
