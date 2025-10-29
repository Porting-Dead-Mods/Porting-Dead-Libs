package com.portingdeadmods.portingdeadlibs.utils.capabilities;

import it.unimi.dsi.fastutil.ints.Int2IntFunction;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.items.ItemStackHandler;

import java.util.function.BiPredicate;
import java.util.function.Consumer;

public final class HandlerUtils {
    public static ItemStackHandler newItemStackHandler(BiPredicate<Integer, ItemStack> validator, Int2IntFunction slotLimit, Consumer<Integer> onChange, int slots) {
        return new ItemStackHandler(slots) {
            @Override
            public int getSlotLimit(int slot) {
                return slotLimit.applyAsInt(slot);
            }

            @Override
            public boolean isItemValid(int slot, ItemStack stack) {
                return validator.test(slot, stack);
            }

            @Override
            protected void onContentsChanged(int slot) {
                onChange.accept(slot);
            }
        };
    }
}
