package com.portingdeadmods.portingdeadlibs.api.items;

public interface IEnergyItem {
    int getEnergyCapacity();

    int getMaxTransfer();

    default int getMaxInput() {
        return getMaxTransfer();
    }

    default int getMaxOutput() {
        return getMaxTransfer();
    }
}
