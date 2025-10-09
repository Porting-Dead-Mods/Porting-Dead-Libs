package com.portingdeadmods.portingdeadlibs.api.ghost;

import net.minecraft.core.BlockPos;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public interface GhostMultiblockPart {
    void onPartRemoved(BlockState state, Level level, BlockPos pos, BlockState newState);
}
