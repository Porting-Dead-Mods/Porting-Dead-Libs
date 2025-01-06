package com.portingdeadmods.portingdeadlibs.api.blockentities.multiblocks;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import org.jetbrains.annotations.Nullable;

public interface MultiblockPartEntity extends SavesControllerPosBlockEntity, FakeBlockEntity {
    BlockPos getControllerPos();

    default BlockEntity self() {
        return (BlockEntity) this;
    }

    @Override
    default boolean actualBlockEntity() {
        return self().getBlockPos().equals(getActualBlockEntityPos());
    }

    @Override
    default @Nullable BlockPos getActualBlockEntityPos() {
        return getControllerPos();
    }
}