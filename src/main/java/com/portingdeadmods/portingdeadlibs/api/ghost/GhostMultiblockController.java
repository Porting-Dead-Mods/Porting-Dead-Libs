package com.portingdeadmods.portingdeadlibs.api.ghost;

import com.portingdeadmods.portingdeadlibs.api.blocks.PDLEntityBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

public abstract class GhostMultiblockController extends PDLEntityBlock {
    public GhostMultiblockController(Properties properties) {
        super(properties);
    }

    @Override
    protected void affectNeighborsAfterRemoval(BlockState state, ServerLevel level, BlockPos pos, boolean movedByPiston) {
        if (level.getBlockEntity(pos) instanceof GhostMultiblockControllerBE controllerBE) {
            if (!controllerBE.partPositions.isEmpty()) {
                controllerBE.partPositions.forEach(partPos -> {
                    if (level.getBlockEntity(partPos) instanceof SimpleGhostMultiblockPartBE) {
                        level.removeBlock(partPos, false);
                    }
                });
            }
        }
        super.affectNeighborsAfterRemoval(state, level, pos, movedByPiston);
    }

}
