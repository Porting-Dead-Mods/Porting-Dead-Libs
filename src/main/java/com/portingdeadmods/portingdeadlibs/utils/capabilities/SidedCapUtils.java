package com.portingdeadmods.portingdeadlibs.utils.capabilities;

import com.google.common.collect.ImmutableMap;
import com.portingdeadmods.portingdeadlibs.api.utils.IOAction;
import it.unimi.dsi.fastutil.Pair;
import net.minecraft.core.Direction;

public final class SidedCapUtils {
    public static ImmutableMap<Direction, Pair<IOAction, int[]>> allBoth(int ...slots) {
        return ImmutableMap.of(
                Direction.NORTH, Pair.of(IOAction.BOTH, slots),
                Direction.EAST, Pair.of(IOAction.BOTH, slots),
                Direction.SOUTH, Pair.of(IOAction.BOTH, slots),
                Direction.WEST, Pair.of(IOAction.BOTH, slots),
                Direction.UP, Pair.of(IOAction.BOTH, slots),
                Direction.DOWN, Pair.of(IOAction.BOTH, slots)
        );
    }

    public static ImmutableMap<Direction, Pair<IOAction, int[]>> allInsert(int ...slots) {
        return ImmutableMap.of(
                Direction.NORTH, Pair.of(IOAction.INSERT, slots),
                Direction.EAST, Pair.of(IOAction.INSERT, slots),
                Direction.SOUTH, Pair.of(IOAction.INSERT, slots),
                Direction.WEST, Pair.of(IOAction.INSERT, slots),
                Direction.UP, Pair.of(IOAction.INSERT, slots),
                Direction.DOWN, Pair.of(IOAction.INSERT, slots)
        );
    }

    public static ImmutableMap<Direction, Pair<IOAction, int[]>> allExtract(int ...slots) {
        return ImmutableMap.of(
                Direction.NORTH, Pair.of(IOAction.EXTRACT, slots),
                Direction.EAST, Pair.of(IOAction.EXTRACT, slots),
                Direction.SOUTH, Pair.of(IOAction.EXTRACT, slots),
                Direction.WEST, Pair.of(IOAction.EXTRACT, slots),
                Direction.UP, Pair.of(IOAction.EXTRACT, slots),
                Direction.DOWN, Pair.of(IOAction.EXTRACT, slots)
        );
    }
}
