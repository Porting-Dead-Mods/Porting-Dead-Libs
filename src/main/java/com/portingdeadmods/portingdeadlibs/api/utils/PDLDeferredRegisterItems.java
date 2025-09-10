package com.portingdeadmods.portingdeadlibs.api.utils;

import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;
import java.util.function.Supplier;

public class PDLDeferredRegisterItems extends DeferredRegister.Items {
    private final List<Supplier<? extends Item>> creativeTabItems;

    protected PDLDeferredRegisterItems(String namespace) {
        super(namespace);
        this.creativeTabItems = new ArrayList<>();
    }

    public static PDLDeferredRegisterItems createItemsRegister(String modid) {
        return new PDLDeferredRegisterItems(modid);
    }

    @Override
    public <I extends Item> DeferredItem<I> register(String name, Function<ResourceLocation, ? extends I> func) {
        DeferredItem<I> item = super.register(name, func);
        this.creativeTabItems.add(item);
        return item;
    }

    public <I extends Item> DeferredItem<I> registerNoCreative(String name, Function<ResourceLocation, ? extends I> func) {
        return super.register(name, func);
    }

    public <I extends Item> DeferredItem<I> registerNoCreative(String name, Supplier<? extends I> sup) {
        return this.registerNoCreative(name, key -> sup.get());
    }

    public DeferredItem<BlockItem> registerSimpleBlockItemNoCreative(String name, Supplier<? extends Block> block, Item.Properties properties) {
        return this.registerNoCreative(name, key -> new BlockItem(block.get(), properties));
    }

    public DeferredItem<BlockItem> registerSimpleBlockItemNoCreative(String name, Supplier<? extends Block> block) {
        return this.registerSimpleBlockItemNoCreative(name, block, new Item.Properties());
    }

    public DeferredItem<BlockItem> registerSimpleBlockItemNoCreative(Holder<Block> block, Item.Properties properties) {
        String var10001 = block.unwrapKey().orElseThrow().location().getPath();
        Objects.requireNonNull(block);
        return this.registerSimpleBlockItemNoCreative(var10001, block::value, properties);
    }

    public DeferredItem<BlockItem> registerSimpleBlockItemNoCreative(Holder<Block> block) {
        return this.registerSimpleBlockItemNoCreative(block, new Item.Properties());
    }

    public <I extends Item> DeferredItem<I> registerItemNoCreative(String name, Function<Item.Properties, ? extends I> func, Item.Properties props) {
        return this.registerNoCreative(name, () -> func.apply(props));
    }

    public <I extends Item> DeferredItem<I> registerItemNoCreative(String name, Function<Item.Properties, ? extends I> func) {
        return this.<I>registerItemNoCreative(name, func, new Item.Properties());
    }

    public DeferredItem<Item> registerSimpleItemNoCreative(String name, Item.Properties props) {
        return this.<Item>registerItemNoCreative(name, Item::new, props);
    }

    public DeferredItem<Item> registerSimpleItemNoCreative(String name) {
        return this.<Item>registerItemNoCreative(name, Item::new, new Item.Properties());
    }

    public List<Supplier<? extends Item>> getCreativeTabItems() {
        return creativeTabItems;
    }
}
