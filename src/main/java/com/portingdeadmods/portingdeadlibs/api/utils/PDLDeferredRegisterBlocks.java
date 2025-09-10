package com.portingdeadmods.portingdeadlibs.api.utils;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.function.Supplier;

public class PDLDeferredRegisterBlocks extends DeferredRegister.Blocks {
    private final DeferredRegister.Items itemsRegistry;
    private final List<Supplier<? extends BlockItem>> blockItems;

    protected PDLDeferredRegisterBlocks(String namespace, DeferredRegister.Items itemsRegistry) {
        super(namespace);
        this.itemsRegistry = itemsRegistry;
        this.blockItems = new ArrayList<>();
    }

    public static PDLDeferredRegisterBlocks createBlocks(String modid, DeferredRegister.Items itemsRegistry) {
        return new PDLDeferredRegisterBlocks(modid, itemsRegistry);
    }

    public <B extends Block> DeferredBlock<B> registerWithItem(String name, Supplier<? extends B> sup) {
        DeferredBlock<B> block = this.register(name, key -> sup.get());
        DeferredItem<BlockItem> item = this.itemsRegistry.registerSimpleBlockItem(name, block, new Item.Properties());
        this.blockItems.add(item);
        return block;
    }

    public <B extends Block, BI extends BlockItem> DeferredBlock<B> registerWithItem(String name, Supplier<? extends B> sup, Function<B, BI> biFunction) {
        DeferredBlock<B> block = this.register(name, key -> sup.get());
        DeferredItem<BI> item = this.itemsRegistry.register(name, () -> biFunction.apply(block.get()));
        this.blockItems.add(item);
        return block;
    }

    public <B extends Block> DeferredBlock<B> registerBlockWithItem(String name, Function<BlockBehaviour.Properties, ? extends B> func, BlockBehaviour.Properties props) {
        return this.registerWithItem(name, (() -> func.apply(props)));
    }

    public <B extends Block> DeferredBlock<B> registerBlockWithItem(String name, Function<BlockBehaviour.Properties, ? extends B> func) {
        return this.registerBlockWithItem(name, func, BlockBehaviour.Properties.of());
    }

    public DeferredBlock<Block> registerSimpleBlockWithItem(String name, BlockBehaviour.Properties props) {
        return this.registerBlockWithItem(name, Block::new, props);
    }

    public DeferredBlock<Block> registerSimpleBlockWithItem(String name) {
        return this.registerSimpleBlockWithItem(name, BlockBehaviour.Properties.of());
    }

    public List<Supplier<? extends BlockItem>> getBlockItems() {
        return blockItems;
    }

}
