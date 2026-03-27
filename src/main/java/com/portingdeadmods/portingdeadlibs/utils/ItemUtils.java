package com.portingdeadmods.portingdeadlibs.utils;

import net.minecraft.util.ARGB;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.transfer.access.ItemAccess;
import net.neoforged.neoforge.transfer.energy.EnergyHandler;

public final class ItemUtils {
    private static final int ENERGY_BAR_COLOR = ARGB.color(203, 10, 10);

    public static int getEnergyForDurabilityBar(ItemStack itemStack) {
        EnergyHandler energyHandler = itemStack.getCapability(Capabilities.Energy.ITEM, ItemAccess.forStack(itemStack));
        if (energyHandler != null) {
            int powerStored = energyHandler.getAmountAsInt();
            int powerCapacity = energyHandler.getCapacityAsInt();
            float chargeRatio = (float) powerStored / powerCapacity;
            return Math.round(13.0F - ((1 - chargeRatio) * 13.0F));
        }
        return 0;
    }

    public static int getEnergyBarColor() {
        return ENERGY_BAR_COLOR;
    }
}
