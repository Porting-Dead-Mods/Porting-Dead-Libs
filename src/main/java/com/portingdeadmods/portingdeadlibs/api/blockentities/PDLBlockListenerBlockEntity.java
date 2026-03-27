package com.portingdeadmods.portingdeadlibs.api.blockentities;

import net.minecraft.world.InteractionResult;

public interface PDLBlockListenerBlockEntity {
    default void onRemove() {
    }

    default void onPlace() {
    }

    default void onNeighborChanged() {
    }

    default InteractionResult use() {
        return InteractionResult.SUCCESS;
    }

}
