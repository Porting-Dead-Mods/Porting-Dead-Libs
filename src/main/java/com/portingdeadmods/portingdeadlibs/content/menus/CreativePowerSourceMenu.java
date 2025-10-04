package com.portingdeadmods.portingdeadlibs.content.menus;

import com.portingdeadmods.portingdeadlibs.content.blockentities.CreativePowerSourceBlockEntity;
import com.portingdeadmods.portingdeadlibs.registries.PDLMenuTypes;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.level.block.entity.BlockEntity;

public class CreativePowerSourceMenu extends CreativeBaseMenu<CreativePowerSourceBlockEntity> {
	// The energy per tick is stored in data.get(0)
	// The interval is stored in data.get(1)

	public CreativePowerSourceMenu(int containerId, Inventory inv, CreativePowerSourceBlockEntity blockEntity, ContainerData data) {
		super(PDLMenuTypes.CREATIVE_POWER_SOURCE.get(), containerId, inv, blockEntity, data);

		// No reference slot needed for power source

		// Add player inventory and hotbar
		addPlayerInventory(inv);
		addPlayerHotbar(inv);
	}

	// Factory method for client-side creation
	public static CreativePowerSourceMenu create(int containerId, Inventory inv, FriendlyByteBuf extraData) {
		BlockEntity entity = inv.player.level().getBlockEntity(extraData.readBlockPos());
		if (entity instanceof CreativePowerSourceBlockEntity creativePowerSource) {
			return new CreativePowerSourceMenu(containerId, inv, creativePowerSource, creativePowerSource.getContainerData());
		}
		throw new IllegalStateException("Invalid block entity!");
	}

	public int getEnergyPerTick() {
		return getOutputSize();
	}

	public void setEnergyPerTick(int energyPerTick) {
		setOutputSize(energyPerTick);
	}
}