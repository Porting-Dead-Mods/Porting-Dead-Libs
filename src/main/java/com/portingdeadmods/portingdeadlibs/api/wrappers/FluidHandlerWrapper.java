package com.portingdeadmods.portingdeadlibs.api.wrappers;

import net.neoforged.neoforge.transfer.fluid.FluidResource;

public interface FluidHandlerWrapper {
    int getFluidAmount();

    int getFluidCapacity();

    FluidResource getFluidResource();
}
