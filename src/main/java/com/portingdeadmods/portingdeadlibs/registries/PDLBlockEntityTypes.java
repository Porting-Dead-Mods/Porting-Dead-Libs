package com.portingdeadmods.portingdeadlibs.registries;

import com.portingdeadmods.portingdeadlibs.PortingDeadLibs;
import com.portingdeadmods.portingdeadlibs.content.blockentities.CreativeFluidSupplierBlockEntity;
import com.portingdeadmods.portingdeadlibs.content.blockentities.CreativeItemSupplierBlockEntity;
import com.portingdeadmods.portingdeadlibs.content.blockentities.CreativePowerSourceBlockEntity;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class PDLBlockEntityTypes {
	public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES = DeferredRegister.create(BuiltInRegistries.BLOCK_ENTITY_TYPE, PortingDeadLibs.MODID);

	public static final Supplier<BlockEntityType<CreativePowerSourceBlockEntity>> CREATIVE_POWER_SOURCE = BLOCK_ENTITIES.register("creative_power_source",
			() -> BlockEntityType.Builder.of(CreativePowerSourceBlockEntity::new,
					PDLBlocks.CREATIVE_POWER_SUPPLY.get()).build(null));

	public static final Supplier<BlockEntityType<CreativeItemSupplierBlockEntity>> CREATIVE_ITEM_SUPPLIER = BLOCK_ENTITIES.register("creative_item_supplier",
			() -> BlockEntityType.Builder.of(CreativeItemSupplierBlockEntity::new,
					PDLBlocks.CREATIVE_ITEM_SUPPLY.get()).build(null));

	public static final Supplier<BlockEntityType<CreativeFluidSupplierBlockEntity>> CREATIVE_FLUID_SUPPLIER = BLOCK_ENTITIES.register("creative_fluid_supplier",
			() -> BlockEntityType.Builder.of(CreativeFluidSupplierBlockEntity::new,
					PDLBlocks.CREATIVE_FLUID_SUPPLY.get()).build(null));
}
