package com.portingdeadmods.portingdeadlibs.api.data.transfer;

import net.neoforged.neoforge.transfer.energy.SimpleEnergyHandler;

import java.util.function.Consumer;

public class PDLSimpleEnergyHandler extends SimpleEnergyHandler {
    private Consumer<Integer> onChangeFunction;

    public PDLSimpleEnergyHandler(int capacity) {
        super(capacity);
    }

    public PDLSimpleEnergyHandler(int capacity, int maxTransfer) {
        super(capacity, maxTransfer);
    }

    public PDLSimpleEnergyHandler(int capacity, int maxInsert, int maxExtract) {
        super(capacity, maxInsert, maxExtract);
    }

    public void setOnChangeFunction(Consumer<Integer> onChangeFunction) {
        this.onChangeFunction = onChangeFunction;
    }

    @Override
    protected void onEnergyChanged(int previousAmount) {
        if (this.onChangeFunction != null) {
            this.onChangeFunction.accept(previousAmount);
        }
    }

}
