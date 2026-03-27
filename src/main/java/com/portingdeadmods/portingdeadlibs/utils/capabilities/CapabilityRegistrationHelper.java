package com.portingdeadmods.portingdeadlibs.utils.capabilities;

import com.portingdeadmods.portingdeadlibs.api.blockentities.ResourceHandlerBlockEntity;
import com.portingdeadmods.portingdeadlibs.api.data.PDLDataComponents;
import com.portingdeadmods.portingdeadlibs.api.ghost.SimpleGhostMultiblockPartBE;
import com.portingdeadmods.portingdeadlibs.api.items.IEnergyItem;
import com.portingdeadmods.portingdeadlibs.api.items.IFluidItem;
import com.portingdeadmods.portingdeadlibs.api.items.IItemItem;
import net.minecraft.core.BlockPos;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.transfer.access.ItemAccess;
import net.neoforged.neoforge.transfer.energy.ItemAccessEnergyHandler;
import net.neoforged.neoforge.transfer.fluid.ItemAccessFluidHandler;
import net.neoforged.neoforge.transfer.item.ItemAccessItemHandler;

public final class CapabilityRegistrationHelper {
	public static void registerItemCaps(RegisterCapabilitiesEvent event, DeferredRegister<Item> registry) {
		for (DeferredHolder<Item, ? extends Item> item : registry.getEntries()) {
			if (item instanceof IFluidItem fluidItem) {
				event.registerItem(Capabilities.Fluid.ITEM,
						(stack, ctx) -> new ItemAccessFluidHandler(ctx, PDLDataComponents.FLUID.get(), fluidItem.getFluidCapacity()),
						item.get());
			}

			if (item instanceof IEnergyItem energyItem) {
				event.registerItem(Capabilities.Energy.ITEM,
						(stack, ctx) -> new ItemAccessEnergyHandler(ctx, PDLDataComponents.ENERGY.get(), energyItem.getEnergyCapacity(), energyItem.getMaxInput(), energyItem.getMaxOutput()),
						item.get());
			}

			if (item instanceof IItemItem itemItem) {
				event.registerItem(Capabilities.Item.ITEM,
						(stack, ctx) -> new ItemAccessItemHandler(ctx, DataComponents.CONTAINER, itemItem.getSlots()),
						item.get());
			}
		}
	}

	public static void registerBECaps(RegisterCapabilitiesEvent event, DeferredRegister<BlockEntityType<?>> registry) {
		for (DeferredHolder<BlockEntityType<?>, ? extends BlockEntityType<?>> be : registry.getEntries()) {
			Block validBlock = be.get().getValidBlocks().stream().iterator().next();
			BlockEntity testBE = be.get().create(BlockPos.ZERO, validBlock.defaultBlockState());
			if (testBE instanceof ResourceHandlerBlockEntity containerBE) {
				if (containerBE.getHandler(Capabilities.Item.BLOCK) != null) {
					event.registerBlockEntity(Capabilities.Item.BLOCK, be.get(), (blockEntity, dir) -> ((ResourceHandlerBlockEntity) blockEntity).getHandlerOnSide(Capabilities.Item.BLOCK, dir));
				}

				if (containerBE.getHandler(Capabilities.Fluid.BLOCK) != null) {
					event.registerBlockEntity(Capabilities.Fluid.BLOCK, be.get(), (blockEntity, dir) -> ((ResourceHandlerBlockEntity) blockEntity).getHandlerOnSide(Capabilities.Fluid.BLOCK, dir));
				}

				if (containerBE.getHandler(Capabilities.Energy.BLOCK) != null) {
					event.registerBlockEntity(Capabilities.Energy.BLOCK, be.get(), (blockEntity, dir) -> ((ResourceHandlerBlockEntity) blockEntity).getHandlerOnSide(Capabilities.Energy.BLOCK, dir));
				}
			}
			if (testBE instanceof SimpleGhostMultiblockPartBE partBE) {
				event.registerBlockEntity(Capabilities.Item.BLOCK, be.get(), (blockEntity, dir) -> ((SimpleGhostMultiblockPartBE) blockEntity).tryAndGetCapability(Capabilities.Item.BLOCK));
				event.registerBlockEntity(Capabilities.Fluid.BLOCK, be.get(), (blockEntity, dir) -> ((SimpleGhostMultiblockPartBE) blockEntity).tryAndGetCapability(Capabilities.Fluid.BLOCK));
				event.registerBlockEntity(Capabilities.Energy.BLOCK, be.get(), (blockEntity, dir) -> ((SimpleGhostMultiblockPartBE) blockEntity).tryAndGetCapability(Capabilities.Energy.BLOCK));
			}
		}
	}
}
