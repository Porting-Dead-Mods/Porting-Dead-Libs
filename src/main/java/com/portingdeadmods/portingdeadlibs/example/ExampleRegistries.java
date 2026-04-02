package com.portingdeadmods.portingdeadlibs.example;

import com.portingdeadmods.portingdeadlibs.PortingDeadLibs;
import com.portingdeadmods.portingdeadlibs.api.misc.PDLDeferredRegisterBlocks;
import com.portingdeadmods.portingdeadlibs.api.misc.PDLDeferredRegisterItems;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ExampleRegistries {
    public static final PDLDeferredRegisterItems ITEMS = PDLDeferredRegisterItems.createItemsRegister(PortingDeadLibs.MODID);
    public static final PDLDeferredRegisterBlocks BLOCKS = PDLDeferredRegisterBlocks.createBlocksRegister(PortingDeadLibs.MODID, ITEMS);
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES = DeferredRegister.create(Registries.BLOCK_ENTITY_TYPE, PortingDeadLibs.MODID);

    public static final DeferredBlock<ExampleContainerBlock> EXAMPLE_CONTAINER_BLOCK = BLOCKS.registerBlockWithItem("example_container_block", ExampleContainerBlock::new);
    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<ExampleContainerBlockEntity>> EXAMPLE_CONTAINER_BLOCK_ENTITY = BLOCK_ENTITIES.register("example_container_be", () -> new BlockEntityType<>(ExampleContainerBlockEntity::new, EXAMPLE_CONTAINER_BLOCK.get()));

}
