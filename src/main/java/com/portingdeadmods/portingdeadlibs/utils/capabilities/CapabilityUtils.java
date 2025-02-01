package com.portingdeadmods.portingdeadlibs.utils.capabilities;

import net.minecraft.world.level.block.entity.BlockEntity;
import net.neoforged.neoforge.capabilities.BlockCapability;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;
import net.neoforged.neoforge.items.IItemHandler;
import org.jetbrains.annotations.Nullable;

public final class CapabilityUtils {
    public static <T, C> @Nullable T blockEntityCapability(BlockCapability<T, C> cap, BlockEntity blockEntity) {
        return blockEntity.getLevel().getCapability(cap, blockEntity.getBlockPos(), blockEntity.getBlockState(), blockEntity, null);
    }

    public static IFluidHandler fluidHandler(BlockEntity blockEntity) {
        return blockEntityCapability(Capabilities.FluidHandler.BLOCK, blockEntity);
    }

    public static IItemHandler itemHandler(BlockEntity blockEntity) {
        return blockEntityCapability(Capabilities.ItemHandler.BLOCK, blockEntity);
    }
}
