package com.portingdeadmods.portingdeadlibs.example;

import com.portingdeadmods.portingdeadlibs.PortingDeadLibs;
import com.portingdeadmods.portingdeadlibs.api.utils.DeferredBlockEntity;
import com.portingdeadmods.portingdeadlibs.api.utils.PDLDeferredRegisterBlockEntities;
import com.portingdeadmods.portingdeadlibs.api.utils.PDLDeferredRegisterBlocks;
import com.portingdeadmods.portingdeadlibs.api.utils.PDLDeferredRegisterItems;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class ExampleRegistries {
    public static final PDLDeferredRegisterItems ITEMS = PDLDeferredRegisterItems.createItemsRegister(PortingDeadLibs.MODID);
    public static final PDLDeferredRegisterBlocks BLOCKS = PDLDeferredRegisterBlocks.createBlocksRegister(PortingDeadLibs.MODID, ITEMS);
    public static final PDLDeferredRegisterBlockEntities BLOCK_ENTITIES = PDLDeferredRegisterBlockEntities.createBlockEntities(PortingDeadLibs.MODID);

    public static final DeferredBlock<ExampleContainerBlock> EXAMPLE_CONTAINER_BLOCK = BLOCKS.registerBlockWithItem("example_container_block", ExampleContainerBlock::new);
    public static final DeferredBlockEntity<ExampleContainerBlockEntity> EXAMPLE_CONTAINER_BLOCK_ENTITY = BLOCK_ENTITIES.register("example_container_be", ExampleContainerBlockEntity::new, EXAMPLE_CONTAINER_BLOCK);

}
