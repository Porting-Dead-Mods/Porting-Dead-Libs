package com.portingdeadmods.portingdeadlibs.api.ghost;

import com.portingdeadmods.portingdeadlibs.api.blockentities.multiblocks.SavesControllerPosBlockEntity;
import net.minecraft.core.BlockPos;

public interface GhostMultiblockPartBE extends SavesControllerPosBlockEntity {
    BlockPos getControllerPos();
}
