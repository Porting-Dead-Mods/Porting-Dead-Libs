package com.portingdeadmods.portingdeadlibs.api.blockentities.multiblocks;

import com.portingdeadmods.portingdeadlibs.api.multiblocks.MultiblockData;
import net.minecraft.nbt.CompoundTag;

public interface MultiblockEntity {
    MultiblockData getMultiblockData();

    void setMultiblockData(MultiblockData data);

    default CompoundTag saveMBData() {
        return getMultiblockData().serializeNBT();
    }

    default MultiblockData loadMBData(CompoundTag tag) {
        return MultiblockData.deserializeNBT(tag);
    }
}