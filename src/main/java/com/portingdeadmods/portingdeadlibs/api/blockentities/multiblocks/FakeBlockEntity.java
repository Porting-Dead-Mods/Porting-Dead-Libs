package com.portingdeadmods.portingdeadlibs.api.blockentities.multiblocks;

import net.minecraft.core.BlockPos;
import org.jetbrains.annotations.Nullable;

public interface FakeBlockEntity {
    boolean actualBlockEntity();

    @Nullable BlockPos getActualBlockEntityPos();
}