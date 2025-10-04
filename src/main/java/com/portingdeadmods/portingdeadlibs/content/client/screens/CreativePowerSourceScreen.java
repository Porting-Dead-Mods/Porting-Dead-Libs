package com.portingdeadmods.portingdeadlibs.content.client.screens;

import com.portingdeadmods.portingdeadlibs.PortingDeadLibs;
import com.portingdeadmods.portingdeadlibs.api.client.screens.PDLAbstractContainerScreen;
import com.portingdeadmods.portingdeadlibs.content.menus.CreativePowerSourceMenu;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import org.jetbrains.annotations.NotNull;

public class CreativePowerSourceScreen extends PDLAbstractContainerScreen<CreativePowerSourceMenu> {
	public CreativePowerSourceScreen(CreativePowerSourceMenu menu, Inventory playerInventory, Component title) {
		super(menu, playerInventory, title);
	}

	@Override
	public @NotNull ResourceLocation getBackgroundTexture() {
		return PortingDeadLibs.rl("textures/gui/place_holder.png");
	}
}
