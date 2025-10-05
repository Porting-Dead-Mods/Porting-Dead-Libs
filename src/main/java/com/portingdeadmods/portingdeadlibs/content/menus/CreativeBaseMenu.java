package com.portingdeadmods.portingdeadlibs.content.menus;

import com.portingdeadmods.portingdeadlibs.api.blockentities.ContainerBlockEntity;
import com.portingdeadmods.portingdeadlibs.api.gui.menus.PDLAbstractContainerMenu;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.ContainerData;
import org.jetbrains.annotations.NotNull;

public abstract class CreativeBaseMenu<T extends ContainerBlockEntity> extends PDLAbstractContainerMenu<T> {
	public CreativeBaseMenu(net.minecraft.world.inventory.MenuType<?> menuType, int containerId, @NotNull Inventory inv, @NotNull T blockEntity, ContainerData data) {
		super(menuType, containerId, inv, blockEntity);
		// TODO: Finish
	}
}