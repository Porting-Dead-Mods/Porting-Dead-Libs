package com.portingdeadmods.portingdeadlibs.api.blocks;

import com.portingdeadmods.portingdeadlibs.api.blockentities.PDLBlockEntity;
import com.portingdeadmods.portingdeadlibs.api.blockentities.PDLBlockListenerBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.NotNull;
import org.jspecify.annotations.Nullable;

public abstract class PDLEntityBlock extends Block implements EntityBlock {
    public PDLEntityBlock(BlockBehaviour.Properties properties) {
        super(properties);
        registerDefaultState(switch (this.getRotationType()) {
            case FACING -> this.defaultBlockState().setValue(BlockStateProperties.FACING, Direction.NORTH);
            case HORIZONTAL_FACING ->
                    this.defaultBlockState().setValue(BlockStateProperties.HORIZONTAL_FACING, Direction.NORTH);
            default -> this.defaultBlockState();
        });
    }

    protected abstract BlockEntityType<? extends PDLBlockEntity> getBlockEntityType();

    protected abstract boolean tickingEnabled();

    protected abstract RotationType getRotationType();

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);

        switch (this.getRotationType()) {
            case FACING -> builder.add(BlockStateProperties.FACING);
            case HORIZONTAL_FACING -> builder.add(BlockStateProperties.HORIZONTAL_FACING);
        }

    }

    @org.jetbrains.annotations.Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        BlockState state = super.getStateForPlacement(context);
        if (state != null) {
            return switch (this.getRotationType()) {
                case HORIZONTAL_FACING ->
                        state.setValue(BlockStateProperties.HORIZONTAL_FACING, context.getHorizontalDirection().getOpposite());
                case FACING ->
                        state.setValue(BlockStateProperties.FACING, context.getNearestLookingDirection().getOpposite());
                default -> state;
            };
        }
        return null;
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
        return this.getBlockEntityType().create(blockPos, blockState);
    }

    protected boolean triggerEvent(BlockState state, Level level, BlockPos pos, int b0, int b1) {
        super.triggerEvent(state, level, pos, b0, b1);
        BlockEntity blockEntity = level.getBlockEntity(pos);
        return blockEntity == null ? false : blockEntity.triggerEvent(b0, b1);
    }

    protected @Nullable MenuProvider getMenuProvider(BlockState state, Level level, BlockPos pos) {
        BlockEntity blockEntity = level.getBlockEntity(pos);
        return blockEntity instanceof MenuProvider ? (MenuProvider) blockEntity : null;
    }

    @Override
    public @NotNull RenderShape getRenderShape(BlockState p_49232_) {
        return RenderShape.MODEL;
    }

    @org.jetbrains.annotations.Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> blockEntityType) {
        if (!tickingEnabled()) return null;

        return createTickerHelper(blockEntityType, getBlockEntityType(), (level1, pos1, state1, entity1) -> entity1.tick());
    }

    protected static <E extends BlockEntity, A extends BlockEntity> @Nullable BlockEntityTicker<A> createTickerHelper(BlockEntityType<A> actual, BlockEntityType<E> expected, BlockEntityTicker<? super E> ticker) {
        return expected == actual ? (BlockEntityTicker<A>) ticker : null;
    }

    @Override
    protected void onPlace(BlockState state, Level level, BlockPos pos, BlockState oldState, boolean movedByPiston) {
        super.onPlace(state, level, pos, oldState, movedByPiston);

        if (level.getBlockEntity(pos) instanceof PDLBlockListenerBlockEntity blockEntityListener) {
            blockEntityListener.onPlace();
        }

    }

    @Override
    public void onNeighborChange(BlockState state, LevelReader level, BlockPos pos, BlockPos neighbor) {
        super.onNeighborChange(state, level, pos, neighbor);

        if (level.getBlockEntity(pos) instanceof PDLBlockListenerBlockEntity blockEntityListener) {
            blockEntityListener.onNeighborChanged();
        }
    }

    @Override
    protected void affectNeighborsAfterRemoval(BlockState state, ServerLevel level, BlockPos pos, boolean movedByPiston) {
        super.affectNeighborsAfterRemoval(state, level, pos, movedByPiston);

        if (level.getBlockEntity(pos) instanceof PDLBlockListenerBlockEntity blockEntityListener) {
            blockEntityListener.onRemove();
        }
    }

    @Override
    protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hitResult) {
        return super.useWithoutItem(state, level, pos, player, hitResult);
    }

    public enum RotationType {
        NONE,
        HORIZONTAL_FACING,
        FACING,
    }

}
