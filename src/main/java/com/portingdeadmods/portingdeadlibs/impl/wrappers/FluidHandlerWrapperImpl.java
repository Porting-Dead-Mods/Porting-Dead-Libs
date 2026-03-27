package com.portingdeadmods.portingdeadlibs.impl.wrappers;

import com.portingdeadmods.portingdeadlibs.api.wrappers.FluidHandlerWrapper;
import net.neoforged.neoforge.transfer.ResourceHandler;
import net.neoforged.neoforge.transfer.fluid.FluidResource;

public class FluidHandlerWrapperImpl implements FluidHandlerWrapper {
    private final ResourceHandler<FluidResource> fluidHandler;

    public FluidHandlerWrapperImpl(ResourceHandler<FluidResource> fluidHandler) {
        this.fluidHandler = fluidHandler;
    }

    @Override
    public int getFluidAmount() {
        return this.fluidHandler.getAmountAsInt(0);
    }

    @Override
    public int getFluidCapacity() {
        return this.fluidHandler.getCapacityAsInt(0, this.getFluidResource());
    }

    @Override
    public FluidResource getFluidResource() {
        return this.fluidHandler.getResource(0);
    }

}
