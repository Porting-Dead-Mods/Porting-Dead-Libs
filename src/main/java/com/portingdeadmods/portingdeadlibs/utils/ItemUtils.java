package com.portingdeadmods.portingdeadlibs.utils;

import com.portingdeadmods.nautec.capabilities.NTCapabilities;
import com.portingdeadmods.nautec.capabilities.power.IPowerStorage;
import net.minecraft.util.FastColor;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.items.wrapper.PlayerMainInvWrapper;

import static net.neoforged.neoforge.items.ItemHandlerHelper.insertItemStacked;

public final class ItemUtils {
    public static final int ITEM_POWER_INPUT = 128;

    public static final int POWER_BAR_COLOR = FastColor.ARGB32.color(94, 133, 164);

    public static int powerForDurabilityBar(ItemStack itemStack) {
        IPowerStorage powerStorage = itemStack.getCapability(NTCapabilities.PowerStorage.ITEM);
        if (powerStorage != null) {
            int powerStored = powerStorage.getPowerStored();
            int powerCapacity = powerStorage.getPowerCapacity();
            float chargeRatio = (float) powerStored / powerCapacity;
            return Math.round(13.0F - ((1 - chargeRatio) * 13.0F));
        }
        return 0;
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
