package net.davio.aquaticambitions.ponder.util.fakeConduitEntity;

import com.zurrtum.create.foundation.block.IBE;
import com.zurrtum.create.foundation.block.ProperWaterloggedBlock;
import net.davio.aquaticambitions.registry.CAABlockEntityTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.ScheduledTickAccess;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.material.FluidState;

public class FakeActiveConduitBlock extends Block implements IBE<FakeActiveConduitBlockEntity>, ProperWaterloggedBlock {

    public FakeActiveConduitBlock(Properties props) {
        super(props);
        registerDefaultState(defaultBlockState().setValue(WATERLOGGED, false));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(WATERLOGGED);
    }

    @Override
    public Class<FakeActiveConduitBlockEntity> getBlockEntityClass() {
        return FakeActiveConduitBlockEntity.class;
    }

    @Override
    public BlockEntityType<? extends FakeActiveConduitBlockEntity> getBlockEntityType() {
        return CAABlockEntityTypes.FAKE_CONDUIT.get();
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        BlockState defaultState = defaultBlockState();
        return withWater(defaultState, context);
    }

    @Override
    protected BlockState updateShape(BlockState pState, LevelReader pLevel, ScheduledTickAccess pTickAccess, BlockPos pCurrentPos,
                                     Direction pDirection, BlockPos pNeighborPos, BlockState pNeighborState, RandomSource pRandom) {
        updateWater(pLevel, pTickAccess, pState, pCurrentPos);
        return pState;
    }

    @Override
    protected FluidState getFluidState(BlockState pState) {
        return fluidState(pState);
    }
}
