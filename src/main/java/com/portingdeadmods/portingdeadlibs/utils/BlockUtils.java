package com.portingdeadmods.portingdeadlibs.utils;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.neoforged.neoforge.capabilities.BlockCapability;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public final class BlockUtils {
    public static BlockPos[] getBlocksAroundSelfHorizontal(BlockPos blockPos) {
        return new BlockPos[]{
                blockPos.offset(0, 0, -1),
                blockPos.offset(0, 0, 1),
                blockPos.offset(-1, 0, 0),
                blockPos.offset(1, 0, 0),
        };
    }

    public static BlockPos[] getBlocksAroundSelf3x3(BlockPos selfPos) {
        return new BlockPos[] {
                selfPos.offset(1, 0, 0),
                selfPos.offset(0, 0, 1),
                selfPos.offset(-1, 0, 0),
                selfPos.offset(0, 0, -1),
                selfPos.offset(1, 0, -1),
                selfPos.offset(-1, 0, 1),
                selfPos.offset(1, 0, 1),
                selfPos.offset(-1, 0, -1),
        };
    }

    public static BlockState rotateBlock(BlockState state, DirectionProperty prop, Comparable<?> currentValue) {
        List<Direction> directions = prop.getPossibleValues().stream().toList();
        int currentDirectionIndex = directions.indexOf(currentValue);
        int nextDirectionIndex = (currentDirectionIndex + 1) % directions.size();
        Direction nextDirection = directions.get(nextDirectionIndex);
        return state.setValue(prop, nextDirection);
    }
}
