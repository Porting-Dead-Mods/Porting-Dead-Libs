package com.portingdeadmods.portingdeadlibs.registries;

import com.portingdeadmods.portingdeadlibs.PortingDeadLibs;
import com.portingdeadmods.portingdeadlibs.content.menus.CreativeFluidSupplierMenu;
import com.portingdeadmods.portingdeadlibs.content.menus.CreativeItemSupplierMenu;
import com.portingdeadmods.portingdeadlibs.content.menus.CreativePowerSourceMenu;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.inventory.MenuType;
import net.neoforged.neoforge.common.extensions.IMenuTypeExtension;
import net.neoforged.neoforge.network.IContainerFactory;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class PDLMenuTypes {
	public static final DeferredRegister<MenuType<?>> MENU_TYPES =
			DeferredRegister.create(BuiltInRegistries.MENU, PortingDeadLibs.MODID);

	public static final Supplier<MenuType<CreativePowerSourceMenu>> CREATIVE_POWER_SOURCE =
			registerMenuType("creative_power_source", CreativePowerSourceMenu::create);

	public static final Supplier<MenuType<CreativeItemSupplierMenu>> CREATIVE_ITEM_SUPPLIER =
			registerMenuType("creative_item_supplier", CreativeItemSupplierMenu::create);

	public static final Supplier<MenuType<CreativeFluidSupplierMenu>> CREATIVE_FLUID_SUPPLIER =
			registerMenuType("creative_fluid_supplier", CreativeFluidSupplierMenu::create);

	private static <T extends net.minecraft.world.inventory.AbstractContainerMenu> Supplier<MenuType<T>> registerMenuType(
			String name, IContainerFactory<T> factory) {
		return MENU_TYPES.register(name, () -> IMenuTypeExtension.create(factory));
	}
}