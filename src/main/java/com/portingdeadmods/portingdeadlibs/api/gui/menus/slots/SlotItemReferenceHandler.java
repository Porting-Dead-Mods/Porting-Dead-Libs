package com.portingdeadmods.portingdeadlibs.api.gui.menus.slots;

import com.portingdeadmods.portingdeadlibs.api.capabilities.itemref.IItemReferenceHandler;
import net.minecraft.world.item.ItemStack;

public class SlotItemReferenceHandler extends AbstractSlot {
	private final IItemReferenceHandler refHandler;
	private final int width;
	private final int height;

	public SlotItemReferenceHandler(IItemReferenceHandler refHandler, int index, int x, int y, int width, int height) {
		super(index, x, y);
		this.refHandler = refHandler;
		this.width = width;
		this.height = height;
	}

	public ItemStack getItemStack() {
		return refHandler.getReferenceInSlot(slot);
	}

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}

	public IItemReferenceHandler getRefHandler() {
		return refHandler;
	}
}
