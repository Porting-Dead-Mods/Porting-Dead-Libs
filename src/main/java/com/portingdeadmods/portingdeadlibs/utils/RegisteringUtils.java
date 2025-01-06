package com.portingdeadmods.portingdeadlibs.utils;

import com.portingdeadmods.portingdeadlibs.api.data.PDLDataComponents;
import com.portingdeadmods.portingdeadlibs.api.items.IFluidItem;
import com.portingdeadmods.portingdeadlibs.api.blockentities.ContainerBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.neoforged.neoforge.fluids.capability.templates.FluidHandlerItemStack;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.function.Supplier;

public final class RegisteringUtils {
	public static void registerLibCapabilities(RegisterCapabilitiesEvent event) {
		registerLibItemCaps(event);
	}

	private static void registerLibItemCaps(RegisterCapabilitiesEvent event) {
		for (Item item : BuiltInRegistries.ITEM) {
			if (item instanceof IFluidItem fluidItem) {
				event.registerItem(Capabilities.FluidHandler.ITEM, (stack, ctx) -> new FluidHandlerItemStack(PDLDataComponents.FLUID, stack, fluidItem.getFluidCapacity()), item);
			}
		}
	}

	public static void registerBECaps(RegisterCapabilitiesEvent event, DeferredRegister<BlockEntityType<?>> BETs) {
		for (DeferredHolder<BlockEntityType<?>, ? extends BlockEntityType<?>> be : BETs.getEntries()) {
			Block validBlock = be.get().getValidBlocks().stream().iterator().next();
			BlockEntity testBE = be.get().create(BlockPos.ZERO, validBlock.defaultBlockState());
			if (testBE instanceof ContainerBlockEntity containerBE) {
				if (containerBE.getItemHandler() != null) {
					event.registerBlockEntity(Capabilities.ItemHandler.BLOCK, be.get(), (blockEntity, dir) -> ((ContainerBlockEntity) blockEntity).getItemHandlerOnSide(dir));
				}

				if (containerBE.getFluidHandler() != null) {
					event.registerBlockEntity(Capabilities.FluidHandler.BLOCK, be.get(), (blockEntity, dir) -> ((ContainerBlockEntity) blockEntity).getFluidHandlerOnSide(dir));
				}
			}
		}
	}
}
