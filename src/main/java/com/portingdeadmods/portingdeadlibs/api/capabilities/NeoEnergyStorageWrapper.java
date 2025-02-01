package com.portingdeadmods.portingdeadlibs.api.capabilities;

import net.neoforged.neoforge.energy.IEnergyStorage;

public class NeoEnergyStorageWrapper implements EnergyStorageWrapper{
    private final IEnergyStorage energyStorage;

    public NeoEnergyStorageWrapper(IEnergyStorage energyStorage) {
        this.energyStorage = energyStorage;
    }

    @Override
    public int getEnergyStored() {
        return energyStorage.getEnergyStored();
    }

    @Override
    public int getEnergyCapacity() {
        return energyStorage.getMaxEnergyStored();
    }
}
