package com.portingdeadmods.portingdeadlibs.api.gui.menus.slots;

import com.portingdeadmods.portingdeadlibs.api.capabilities.fluidref.IFluidReferenceHandler;
import net.neoforged.neoforge.fluids.FluidStack;

public class SlotFluidReferenceHandler extends AbstractSlot {
	private final IFluidReferenceHandler refHandler;
	private final int width;
	private final int height;

	public SlotFluidReferenceHandler(IFluidReferenceHandler refHandler, int index, int x, int y, int width, int height) {
		super(index, x, y);
		this.refHandler = refHandler;
		this.width = width;
		this.height = height;
	}

	public FluidStack getFluidStack() {
		return refHandler.getReferenceInSlot(slot);
	}

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}

	public IFluidReferenceHandler getRefHandler() {
		return refHandler;
	}
}
