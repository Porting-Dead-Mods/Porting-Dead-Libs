package com.portingdeadmods.portingdeadlibs.registries;

import com.portingdeadmods.portingdeadlibs.PortingDeadLibs;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.ItemLike;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.function.Supplier;

public class PDLItems {
	public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(PortingDeadLibs.MODID);
	public static final List<ItemLike> CREATIVE_TAB_ITEMS = new ArrayList<>();
	public static final List<Supplier<BlockItem>> BLOCK_ITEMS = new ArrayList<>();

	public static <T extends Item> DeferredItem<T> registerItem(String name, Function<Item.Properties, T> itemConstructor, Item.Properties properties) {
		return registerItem(name, itemConstructor, properties, true);
	}
	public static <T extends Item> DeferredItem<T> registerItem(String name, Function<Item.Properties, T> itemConstructor, Supplier<Item.Properties> properties) {
		return registerItem(name, itemConstructor, properties, true);
	}

	private static <T extends Item> DeferredItem<T> registerItemBucket(String name, Supplier<T> item) {
		return ITEMS.register(name, item);
	}

	public static <T extends Item> DeferredItem<T> registerItem(String name, Supplier<T> item) {
		DeferredItem<T> toReturn = ITEMS.register(name, item);
		CREATIVE_TAB_ITEMS.add(toReturn);
		return toReturn;
	}

	public static <T extends Item> DeferredItem<T> registerItem(String name, Function<Item.Properties, T> itemConstructor, Item.Properties properties, boolean addToTab) {
		DeferredItem<T> toReturn = ITEMS.registerItem(name, itemConstructor, properties);
		if (addToTab) {
			CREATIVE_TAB_ITEMS.add(toReturn);
		}
		return toReturn;
	}

	public static <T extends Item> DeferredItem<T> registerItem(String name, Function<Item.Properties, T> itemConstructor, Supplier<Item.Properties> properties, boolean addToTab) {
		DeferredItem<T> toReturn = ITEMS.register(name,  () -> itemConstructor.apply(properties.get()));
		if (addToTab) {
			CREATIVE_TAB_ITEMS.add(toReturn);
		}
		return toReturn;
	}
}
