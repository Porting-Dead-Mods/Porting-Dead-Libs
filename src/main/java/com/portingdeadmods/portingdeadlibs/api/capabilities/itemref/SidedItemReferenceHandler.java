package com.portingdeadmods.portingdeadlibs.api.capabilities.itemref;

import com.portingdeadmods.portingdeadlibs.api.utils.IOAction;
import com.portingdeadmods.portingdeadlibs.utils.Utils;
import it.unimi.dsi.fastutil.Pair;
import it.unimi.dsi.fastutil.ints.IntList;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public record SidedItemReferenceHandler(IItemReferenceHandler innerHandler,
                                        IOAction action,
                                        IntList slots) implements IItemReferenceHandler {
    public SidedItemReferenceHandler(IItemReferenceHandler innerHandler, Pair<IOAction, int[]> actionSlotsPair) {
        this(innerHandler, actionSlotsPair != null ? actionSlotsPair.left() : IOAction.NONE, actionSlotsPair != null ? Utils.intArrayToList(actionSlotsPair.right()) : IntList.of());
    }

    @Override
    public int getSize() {
        return innerHandler.getSize();
    }

    @Override
    public @NotNull ItemStack getReferenceInSlot(int i) {
        return innerHandler.getReferenceInSlot(i);
    }

    @Override
    public List<ItemStack> getAllReferences() {
        return innerHandler.getAllReferences();
    }

    @Override
    public void setReferenceInSlot(int i, ItemStack itemStack) {
        if ((action == IOAction.INSERT || action == IOAction.BOTH) && slots.contains(i)) {
            innerHandler.setReferenceInSlot(i, itemStack);
        }
    }

    @Override
    public int getSlotLimit(int i) {
        return innerHandler.getSlotLimit(i);
    }

    @Override
    public boolean isReferenceValid(int slot, @NotNull ItemStack itemStack) {
        return (action == IOAction.INSERT || action == IOAction.BOTH) && slots.contains(slot) && innerHandler.isReferenceValid(slot, itemStack);
    }
}
