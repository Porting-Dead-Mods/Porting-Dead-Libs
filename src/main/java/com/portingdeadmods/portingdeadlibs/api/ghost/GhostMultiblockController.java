package com.portingdeadmods.portingdeadlibs.api.ghost;

import com.portingdeadmods.portingdeadlibs.api.blocks.ContainerBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

public abstract class GhostMultiblockController extends ContainerBlock {
    public GhostMultiblockController(Properties properties) {
        super(properties);
    }

    @Override
    public void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean isMoving) {
        if (level.getBlockEntity(pos) instanceof GhostMultiblockControllerBE controllerBE) {
            if (controllerBE.partPos.isInitialized()) {
                controllerBE.partPos.getOrThrow().forEach(partPos -> {
                    if (level.getBlockEntity(partPos) instanceof SimpleGhostMultiblockPartBE) {
                        level.removeBlock(partPos, false);
                    }
                });
            }
        }
        super.onRemove(state, level, pos, newState, isMoving);
    }
}
