package com.portingdeadmods.portingdeadlibs.api.gui.menus.slots;

import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;

public class SlotFluidHandler extends AbstractSlot {
    private final IFluidHandler fluidHandler;
    private final int width;
    private final int height;

    public SlotFluidHandler(IFluidHandler fluidHandler, int index, int x, int y, int width, int height) {
        super(index, x, y);
        this.fluidHandler = fluidHandler;
        this.width = width;
        this.height = height;
    }

    public FluidStack getFluidStack() {
        return fluidHandler.getFluidInTank(slot);
    }

    public int getFluidCapacity() {
        return fluidHandler.getTankCapacity(slot);
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public IFluidHandler getFluidHandler() {
        return fluidHandler;
    }
}
