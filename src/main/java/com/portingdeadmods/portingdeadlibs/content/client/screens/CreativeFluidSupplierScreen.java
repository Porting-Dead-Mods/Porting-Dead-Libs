package com.portingdeadmods.portingdeadlibs.content.client.screens;

import com.portingdeadmods.portingdeadlibs.PortingDeadLibs;
import com.portingdeadmods.portingdeadlibs.api.client.screens.PDLAbstractContainerScreen;
import com.portingdeadmods.portingdeadlibs.content.blockentities.CreativeFluidSupplierBlockEntity;
import com.portingdeadmods.portingdeadlibs.content.menus.CreativeFluidSupplierMenu;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import org.jetbrains.annotations.NotNull;

public class CreativeFluidSupplierScreen extends PDLAbstractContainerScreen<CreativeFluidSupplierMenu> {
	public CreativeFluidSupplierScreen(CreativeFluidSupplierMenu menu, Inventory playerInventory, Component title) {
		super(menu, playerInventory, title);
	}

	@Override
	public @NotNull ResourceLocation getBackgroundTexture() {
		return PortingDeadLibs.rl("textures/gui/place_holder.png");
	}
}
