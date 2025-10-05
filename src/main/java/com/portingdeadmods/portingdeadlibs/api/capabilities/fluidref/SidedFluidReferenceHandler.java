package com.portingdeadmods.portingdeadlibs.api.capabilities.fluidref;

import com.portingdeadmods.portingdeadlibs.api.utils.IOAction;
import com.portingdeadmods.portingdeadlibs.utils.Utils;
import it.unimi.dsi.fastutil.Pair;
import it.unimi.dsi.fastutil.ints.IntList;
import net.neoforged.neoforge.fluids.FluidStack;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public record SidedFluidReferenceHandler(IFluidReferenceHandler innerHandler,
                                         IOAction action,
                                         IntList slots) implements IFluidReferenceHandler {
    public SidedFluidReferenceHandler(IFluidReferenceHandler innerHandler, Pair<IOAction, int[]> actionSlotsPair) {
        this(innerHandler, actionSlotsPair != null ? actionSlotsPair.left() : IOAction.NONE, actionSlotsPair != null ? Utils.intArrayToList(actionSlotsPair.right()) : IntList.of());
    }

    @Override
    public int getSize() {
        return innerHandler.getSize();
    }

    @Override
    public @NotNull FluidStack getReferenceInSlot(int i) {
        return innerHandler.getReferenceInSlot(i);
    }

    @Override
    public List<FluidStack> getAllReferences() {
        return innerHandler.getAllReferences();
    }

    @Override
    public void setReferenceInSlot(int i, FluidStack fluidStack) {
        if ((action == IOAction.INSERT || action == IOAction.BOTH) && slots.contains(i)) {
            innerHandler.setReferenceInSlot(i, fluidStack);
        }
    }

    @Override
    public int getSlotLimit(int i) {
        return innerHandler.getSlotLimit(i);
    }

    @Override
    public boolean isReferenceValid(int slot, @NotNull FluidStack fluidStack) {
        return (action == IOAction.INSERT || action == IOAction.BOTH) && slots.contains(slot) && innerHandler.isReferenceValid(slot, fluidStack);
    }
}
