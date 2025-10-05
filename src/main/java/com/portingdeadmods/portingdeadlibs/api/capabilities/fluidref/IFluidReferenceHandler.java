package com.portingdeadmods.portingdeadlibs.api.capabilities.fluidref;

import net.neoforged.neoforge.fluids.FluidStack;

import java.util.List;

public interface IFluidReferenceHandler {
    int getSize();

    FluidStack getReferenceInSlot(int var1);

    List<FluidStack> getAllReferences();

    void setReferenceInSlot(int var1, FluidStack var2);

    int getSlotLimit(int var1);

    boolean isReferenceValid(int var1, FluidStack var2);

    default void validateSlotIndex(int slot) {
        if (slot < 0 || slot >= this.getSize()) {
            throw new RuntimeException("Slot " + slot + " not in valid range - [0," + this.getSize() + ")");
        }
    }
}
