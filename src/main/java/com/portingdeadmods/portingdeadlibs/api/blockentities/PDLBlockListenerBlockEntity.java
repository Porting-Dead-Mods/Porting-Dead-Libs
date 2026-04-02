package com.portingdeadmods.portingdeadlibs.api.blockentities;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.phys.BlockHitResult;

import javax.annotation.Nullable;

public interface PDLBlockListenerBlockEntity {
    default BlockEntity self() {
        return (BlockEntity) this;
    }

    default void onRemove() {
    }

    default void onPlace() {
    }

    default void onNeighborChanged() {
    }

    default InteractionResult use(Player player, ItemStack itemStack, @Nullable InteractionHand hand, BlockHitResult hitResult) {
        if (this.self() instanceof MenuProvider menuProvider) {
            player.openMenu(menuProvider, self().getBlockPos());
        }
        return InteractionResult.SUCCESS;
    }

}
