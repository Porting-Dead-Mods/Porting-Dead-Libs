package com.portingdeadmods.portingdeadlibs.registries;

import com.portingdeadmods.portingdeadlibs.PortingDeadLibs;
import com.portingdeadmods.portingdeadlibs.content.blockentities.CreativeFluidSupplierBlockEntity;
import com.portingdeadmods.portingdeadlibs.content.blocks.CreativeFluidSupplierBlock;
import com.portingdeadmods.portingdeadlibs.content.blocks.CreativeItemSupplierBlock;
import com.portingdeadmods.portingdeadlibs.content.blocks.CreativePowerSourceBlock;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Supplier;

public class PDLBlocks {
	public static final DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks(PortingDeadLibs.MODID);

	public static final DeferredBlock<CreativePowerSourceBlock> CREATIVE_POWER_SUPPLY = registerBlockAndItem("creative_power_supply",
			CreativePowerSourceBlock::new,
			BlockBehaviour.Properties.ofFullCopy(Blocks.BEDROCK));

	public static final DeferredBlock<CreativeItemSupplierBlock> CREATIVE_ITEM_SUPPLY = registerBlockAndItem("creative_item_supply",
			CreativeItemSupplierBlock::new,
			BlockBehaviour.Properties.ofFullCopy(Blocks.BEDROCK));

	public static final DeferredBlock<CreativeFluidSupplierBlock> CREATIVE_FLUID_SUPPLY = registerBlockAndItem("creative_fluid_supply",
			CreativeFluidSupplierBlock::new,
			BlockBehaviour.Properties.ofFullCopy(Blocks.BEDROCK));

	private static <T extends Block> DeferredBlock<T> registerBlockAndItem(String name, Function<BlockBehaviour.Properties, T> blockConstructor, BlockBehaviour.Properties properties) {
		return registerBlockAndItem(name, blockConstructor, properties, true, true);
	}

	private static <T extends Block> DeferredBlock<T> registerBlockAndItem(String name, Function<BlockBehaviour.Properties, T> blockConstructor, BlockBehaviour.Properties properties, boolean addToTab, boolean genItemModel) {
		DeferredBlock<T> block = BLOCKS.registerBlock(name, blockConstructor, properties);
		DeferredItem<BlockItem> blockItem = PDLItems.registerItem(name, props -> new BlockItem(block.get(), props), new Item.Properties(), addToTab);
		if (genItemModel) {
			PDLItems.BLOCK_ITEMS.add(blockItem);
		}
		return block;
	}

	private static <T extends Block> DeferredBlock<T> registerBlockAndItem(String name, Function<BlockBehaviour.Properties, T> blockConstructor, BlockBehaviour.Properties properties, BiFunction<T, Item.Properties, BlockItem> blockItemConstructor) {
		DeferredBlock<T> block = BLOCKS.registerBlock(name, blockConstructor, properties);
		DeferredItem<BlockItem> blockItem = PDLItems.registerItem(name, props -> blockItemConstructor.apply(block.get(), props), new Item.Properties());
		PDLItems.BLOCK_ITEMS.add(blockItem);
		return block;
	}
}
