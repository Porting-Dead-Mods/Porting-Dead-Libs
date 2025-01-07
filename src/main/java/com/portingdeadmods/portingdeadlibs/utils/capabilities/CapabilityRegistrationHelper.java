package com.portingdeadmods.portingdeadlibs.utils.capabilities;

import com.portingdeadmods.portingdeadlibs.api.data.PDLDataComponents;
import com.portingdeadmods.portingdeadlibs.api.items.IEnergyItem;
import com.portingdeadmods.portingdeadlibs.api.items.IFluidItem;
import com.portingdeadmods.portingdeadlibs.api.blockentities.ContainerBlockEntity;
import com.portingdeadmods.portingdeadlibs.api.items.IItemItem;
import net.minecraft.core.BlockPos;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.neoforged.neoforge.energy.ComponentEnergyStorage;
import net.neoforged.neoforge.fluids.capability.templates.FluidHandlerItemStack;
import net.neoforged.neoforge.items.ComponentItemHandler;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public final class CapabilityRegistrationHelper {
	public static void registerItemCaps(RegisterCapabilitiesEvent event, DeferredRegister<Item> registry) {
		for (DeferredHolder<Item, ? extends Item> item : registry.getEntries()) {
			if (item instanceof IFluidItem fluidItem) {
				event.registerItem(Capabilities.FluidHandler.ITEM,
						(stack, ctx) -> new FluidHandlerItemStack(PDLDataComponents.FLUID, stack, fluidItem.getFluidCapacity()),
						item.get());
			}

			if (item instanceof IEnergyItem energyItem) {
				event.registerItem(Capabilities.EnergyStorage.ITEM,
						(stack, ctx) -> new ComponentEnergyStorage(stack, PDLDataComponents.ENERGY.get(), energyItem.getEnergyCapacity(), energyItem.getMaxInput(), energyItem.getMaxOutput()),
						item.get());
			}

			if (item instanceof IItemItem itemItem) {
				event.registerItem(Capabilities.ItemHandler.ITEM,
						(stack, ctx) -> new ComponentItemHandler(stack, DataComponents.CONTAINER, itemItem.getSlots()),
						item.get());
			}
		}
	}

	public static void registerBECaps(RegisterCapabilitiesEvent event, DeferredRegister<BlockEntityType<?>> registry) {
		for (DeferredHolder<BlockEntityType<?>, ? extends BlockEntityType<?>> be : registry.getEntries()) {
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
