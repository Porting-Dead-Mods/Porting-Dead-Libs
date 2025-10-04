package com.portingdeadmods.portingdeadlibs.content.menus;

import com.portingdeadmods.portingdeadlibs.api.blockentities.ContainerBlockEntity;
import com.portingdeadmods.portingdeadlibs.api.gui.menus.PDLAbstractContainerMenu;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.ContainerData;
import org.jetbrains.annotations.NotNull;

public abstract class CreativeBaseMenu<T extends ContainerBlockEntity> extends PDLAbstractContainerMenu<T> {
	protected final ContainerData data;

	public CreativeBaseMenu(net.minecraft.world.inventory.MenuType<?> menuType, int containerId, @NotNull Inventory inv, @NotNull T blockEntity, ContainerData data) {
		super(menuType, containerId, inv, blockEntity);
		this.data = data;
		addDataSlots(data);
	}

	public int getOutputSize() {
		return data.get(0);
	}

	public int getInterval() {
		return data.get(1);
	}

	public void setOutputSize(int stackSize) {
		// Ensure stack size is between 1-64
		this.data.set(0, Math.min(64, Math.max(1, stackSize)));
	}

	public void setInterval(int interval) {
		// Ensure interval is between 1-100
		this.data.set(1, Math.min(100, Math.max(1, interval)));
	}

	@Override
	protected int getMergeableSlotCount() {
		return 0; // No actual item slots that need merging
	}
}