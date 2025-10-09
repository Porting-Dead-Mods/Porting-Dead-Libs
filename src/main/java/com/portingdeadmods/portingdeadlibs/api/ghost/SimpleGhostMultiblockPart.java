package com.portingdeadmods.portingdeadlibs.api.ghost;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public abstract class SimpleGhostMultiblockPart extends BaseEntityBlock implements GhostMultiblockPart {
    protected SimpleGhostMultiblockPart(Properties properties) {
        super(properties);
    }

    @Override
    protected @NotNull InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hitResult) {
        if (level.isClientSide()) {
            return InteractionResult.SUCCESS;
        }

        if (level.getBlockEntity(pos) instanceof GhostMultiblockPartBE partBE) {
            BlockEntity controllerBE = level.getBlockEntity(partBE.getControllerPos());
            if (controllerBE instanceof GhostMultiblockControllerBE && player instanceof ServerPlayer serverPlayer) {
                serverPlayer.openMenu((GhostMultiblockControllerBE) controllerBE, partBE.getControllerPos());
                return InteractionResult.CONSUME;
            }
        }
        return InteractionResult.PASS;
    }

    @Override
    public void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean isMoving) {
        if (!state.is(newState.getBlock())) {
            onPartRemoved(state, level, pos, newState);
        }
        super.onRemove(state, level, pos, newState, isMoving);
    }

    @Override
    public void onPartRemoved(BlockState state, Level level, BlockPos pos, BlockState newState) {
        if (level.getBlockEntity(pos) instanceof SimpleGhostMultiblockPartBE partBE) {
            BlockPos controllerPos = partBE.getControllerPos();
            if (level.getBlockState(controllerPos).getBlock() instanceof GhostMultiblockController) {
                level.removeBlock(controllerPos, false);
            }
        }
    }

    @Override
    public @NotNull RenderShape getRenderShape(@NotNull BlockState state) {
        return RenderShape.INVISIBLE;
    }
}
