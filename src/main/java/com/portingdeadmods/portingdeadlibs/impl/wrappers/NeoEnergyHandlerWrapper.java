package com.portingdeadmods.portingdeadlibs.impl.wrappers;

import com.portingdeadmods.portingdeadlibs.api.wrappers.EnergyHandlerWrapper;
import net.neoforged.neoforge.transfer.energy.EnergyHandler;

public class NeoEnergyHandlerWrapper implements EnergyHandlerWrapper {
    private final EnergyHandler energyHandler;

    public NeoEnergyHandlerWrapper(EnergyHandler energyStorage) {
        this.energyHandler = energyStorage;
    }

    @Override
    public int getEnergyStored() {
        return energyHandler.getAmountAsInt();
    }

    @Override
    public int getEnergyCapacity() {
        return energyHandler.getCapacityAsInt();
    }
}
