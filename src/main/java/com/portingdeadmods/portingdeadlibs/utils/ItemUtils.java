package com.portingdeadmods.portingdeadlibs.utils;

import net.minecraft.util.FastColor;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.client.extensions.common.IClientFluidTypeExtensions;
import net.neoforged.neoforge.energy.IEnergyStorage;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.items.wrapper.PlayerMainInvWrapper;

import static net.neoforged.neoforge.items.ItemHandlerHelper.insertItemStacked;

public final class ItemUtils {
    private static final int ENERGY_BAR_COLOR = FastColor.ARGB32.color(203, 10, 10);

    public static int getEnergyForDurabilityBar(ItemStack itemStack) {
        IEnergyStorage energyStorage = itemStack.getCapability(Capabilities.EnergyStorage.ITEM);
        if (energyStorage != null) {
            int powerStored = energyStorage.getEnergyStored();
            int powerCapacity = energyStorage.getMaxEnergyStored();
            float chargeRatio = (float) powerStored / powerCapacity;
            return Math.round(13.0F - ((1 - chargeRatio) * 13.0F));
        }
        return 0;
    }

    public static int getFluidForDurabilityBar(ItemStack itemStack) {
        IFluidHandler fluidHandler = itemStack.getCapability(Capabilities.FluidHandler.ITEM);
        if (fluidHandler != null) {
            int powerStored = fluidHandler.getFluidInTank(0).getAmount();
            int powerCapacity = fluidHandler.getTankCapacity(0);
            float chargeRatio = (float) powerStored / powerCapacity;
            return Math.round(13.0F - ((1 - chargeRatio) * 13.0F));
        }
        return 0;
    }

    public static int getEnergyBarColor() {
        return ENERGY_BAR_COLOR;
    }

    public static int getFluidBarColor(ItemStack itemStack) {
        IFluidHandler fluidHandler = itemStack.getCapability(Capabilities.FluidHandler.ITEM);
        return IClientFluidTypeExtensions.of(fluidHandler.getFluidInTank(0).getFluid()).getTintColor();
    }

    public static void giveItemToPlayerNoSound(Player player, ItemStack stack) {
        if (stack.isEmpty()) return;

        int preferredSlot = -1;

        IItemHandler inventory = new PlayerMainInvWrapper(player.getInventory());
        Level level = player.level();

        // try adding it into the inventory
        ItemStack remainder = stack;
        // insert into preferred slot first
        if (preferredSlot >= 0 && preferredSlot < inventory.getSlots()) {
            remainder = inventory.insertItem(preferredSlot, stack, false);
        }
        // then into the inventory in general
        if (!remainder.isEmpty()) {
            remainder = insertItemStacked(inventory, remainder, false);
        }

        // drop remaining itemstack into the level
        if (!remainder.isEmpty() && !level.isClientSide) {
            ItemEntity entityitem = new ItemEntity(level, player.getX(), player.getY() + 0.5, player.getZ(), remainder);
            entityitem.setPickUpDelay(40);
            entityitem.setDeltaMovement(entityitem.getDeltaMovement().multiply(0, 1, 0));

            level.addFreshEntity(entityitem);
        }
    }
}
