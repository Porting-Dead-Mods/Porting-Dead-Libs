package com.portingdeadmods.portingdeadlibs.api.data.transfer;

import net.minecraft.core.NonNullList;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.transfer.fluid.FluidResource;
import net.neoforged.neoforge.transfer.item.ItemResource;
import net.neoforged.neoforge.transfer.item.ItemStacksResourceHandler;

import java.util.function.BiConsumer;
import java.util.function.BiPredicate;
import java.util.function.Consumer;

public class PDLItemStacksHandler extends ItemStacksResourceHandler {
    private BiConsumer<Integer, ItemStack> onChangeFunction;
    private BiPredicate<Integer, ItemResource> validator = (_, _) -> true;

    public PDLItemStacksHandler(int size) {
        super(size);
    }

    public PDLItemStacksHandler(NonNullList<ItemStack> stacks) {
        super(stacks);
    }

    public void setOnChangeFunction(BiConsumer<Integer, ItemStack> onChangeFunction) {
        this.onChangeFunction = onChangeFunction;
    }

    public void setValidator(BiPredicate<Integer, ItemResource> validator) {
        this.validator = validator;
    }

    @Override
    public boolean isValid(int index, ItemResource resource) {
        return validator.test(index, resource);
    }

    @Override
    protected void onContentsChanged(int index, ItemStack previousContents) {
        if (onChangeFunction != null) {
            onChangeFunction.accept(index, previousContents);
        }
    }

}
