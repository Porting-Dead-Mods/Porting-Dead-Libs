//package com.portingdeadmods.portingdeadlibs.api.blocks;
//
//import net.minecraft.core.Direction;
//import net.minecraft.world.item.context.BlockPlaceContext;
//import net.minecraft.world.level.block.BaseEntityBlock;
//import net.minecraft.world.level.block.Block;
//import net.minecraft.world.level.block.RenderShape;
//import net.minecraft.world.level.block.state.BlockState;
//import net.minecraft.world.level.block.state.StateDefinition;
//import net.minecraft.world.level.block.state.properties.BlockStateProperties;
//import net.minecraft.world.level.block.state.properties.EnumProperty;
//import org.jetbrains.annotations.Nullable;
//
//public abstract class RotatableEntityBlock extends BaseEntityBlock {
//    public static final EnumProperty<Direction> FACING = BlockStateProperties.HORIZONTAL_FACING;
//
//    public RotatableEntityBlock(Properties properties) {
//        super(properties);
//    }
//
//    @Override
//    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> p_49915_) {
//        p_49915_.add(FACING);
//    }
//
//    @Nullable
//    @Override
//    public BlockState getStateForPlacement(BlockPlaceContext p_49820_) {
//        return defaultBlockState().setValue(FACING, p_49820_.getHorizontalDirection().getOpposite());
//    }
//
//    @Override
//    public RenderShape getRenderShape(BlockState p_49232_) {
//        return RenderShape.MODEL;
//    }
//}