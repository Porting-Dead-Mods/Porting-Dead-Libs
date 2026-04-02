package com.portingdeadmods.portingdeadlibs.api.data.components;

import net.minecraft.core.component.DataComponentType;
import net.neoforged.neoforge.transfer.TransferPreconditions;
import net.neoforged.neoforge.transfer.access.ItemAccess;
import net.neoforged.neoforge.transfer.energy.ItemAccessEnergyHandler;
import net.neoforged.neoforge.transfer.item.ItemResource;
import net.neoforged.neoforge.transfer.transaction.TransactionContext;

public class PDLItemAccessEnergyHandler extends ItemAccessEnergyHandler {
    public PDLItemAccessEnergyHandler(ItemAccess itemAccess, DataComponentType<Integer> energyComponent, int capacity) {
        super(itemAccess, energyComponent, capacity);
    }

    public PDLItemAccessEnergyHandler(ItemAccess itemAccess, DataComponentType<Integer> energyComponent, int capacity, int maxTransfer) {
        super(itemAccess, energyComponent, capacity, maxTransfer);
    }
    public PDLItemAccessEnergyHandler(ItemAccess itemAccess, DataComponentType<Integer> energyComponent, int capacity, int maxInsert, int maxExtract) {
        super(itemAccess, energyComponent, capacity, maxInsert, maxExtract);
    }

    public void set(int energy, TransactionContext context) {
        int amount = itemAccess.getAmount();
        ItemResource resource = itemAccess.getResource();
        ItemResource updatedResource = update(resource, energy);
        this.itemAccess.exchange(updatedResource, amount, context);
    }

}
