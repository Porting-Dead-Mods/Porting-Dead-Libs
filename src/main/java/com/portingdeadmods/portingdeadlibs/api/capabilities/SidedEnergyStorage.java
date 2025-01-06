package com.portingdeadmods.portingdeadlibs.api.capabilities;

import com.portingdeadmods.portingdeadlibs.api.utils.IOAction;
import it.unimi.dsi.fastutil.Pair;
import it.unimi.dsi.fastutil.ints.IntList;
import net.neoforged.neoforge.energy.IEnergyStorage;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;
import org.jetbrains.annotations.NotNull;

public record SidedEnergyStorage(IEnergyStorage innerHandler,
                                 IOAction action) implements IEnergyStorage {
    public SidedEnergyStorage(IEnergyStorage innerHandler, Pair<IOAction, int[]> actionSlotsPair) {
        this(innerHandler, actionSlotsPair != null ? actionSlotsPair.left() : IOAction.NONE);
    }

    @Override
    public int receiveEnergy(int toReceive, boolean simulate) {
        return action == IOAction.INSERT || action == IOAction.BOTH ? innerHandler.receiveEnergy(toReceive, simulate) : 0;
    }

    @Override
    public int extractEnergy(int toExtract, boolean simulate) {
        return action == IOAction.EXTRACT || action == IOAction.BOTH ? innerHandler.receiveEnergy(toExtract, simulate) : 0;
    }

    @Override
    public int getEnergyStored() {
        return innerHandler.getEnergyStored();
    }

    @Override
    public int getMaxEnergyStored() {
        return innerHandler.getMaxEnergyStored();
    }

    @Override
    public boolean canExtract() {
        return innerHandler.canExtract() && (action == IOAction.EXTRACT || action == IOAction.BOTH);
    }

    @Override
    public boolean canReceive() {
        return innerHandler.canReceive() && (action == IOAction.INSERT || action == IOAction.BOTH);
    }
}
