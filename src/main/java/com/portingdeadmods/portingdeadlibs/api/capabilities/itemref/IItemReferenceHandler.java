package com.portingdeadmods.portingdeadlibs.api.capabilities.itemref;

import net.minecraft.world.item.ItemStack;

import java.util.List;

public interface IItemReferenceHandler {
    int getSize();

    ItemStack getReferenceInSlot(int var1);

    List<ItemStack> getAllReferences();

    void setReferenceInSlot(int var1, ItemStack var2);

    int getSlotLimit(int var1);

    boolean isReferenceValid(int var1, ItemStack var2);

    default void validateSlotIndex(int slot) {
        if (slot < 0 || slot >= this.getSize()) {
            throw new RuntimeException("Slot " + slot + " not in valid range - [0," + this.getSize() + ")");
        }
    }
}
