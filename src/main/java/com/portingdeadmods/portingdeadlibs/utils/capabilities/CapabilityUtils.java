package com.portingdeadmods.portingdeadlibs.utils.capabilities;

import net.minecraft.world.level.block.entity.BlockEntity;
import net.neoforged.neoforge.capabilities.BlockCapability;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.transfer.ResourceHandler;
import net.neoforged.neoforge.transfer.fluid.FluidResource;
import net.neoforged.neoforge.transfer.item.ItemResource;
import org.jetbrains.annotations.Nullable;

public final class CapabilityUtils {
    public static <T, C> @Nullable T blockEntityCapability(BlockCapability<T, C> cap, BlockEntity blockEntity) {
        return blockEntity.getLevel().getCapability(cap, blockEntity.getBlockPos(), blockEntity.getBlockState(), blockEntity, null);
    }

    public static ResourceHandler<FluidResource> fluidHandler(BlockEntity blockEntity) {
        return blockEntityCapability(Capabilities.Fluid.BLOCK, blockEntity);
    }

    public static ResourceHandler<ItemResource> itemHandler(BlockEntity blockEntity) {
        return blockEntityCapability(Capabilities.Item.BLOCK, blockEntity);
    }
}
