package com.portingdeadmods.portingdeadlibs.api.data.transfer;

import net.minecraft.core.NonNullList;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.transfer.fluid.FluidResource;
import net.neoforged.neoforge.transfer.fluid.FluidStacksResourceHandler;

import java.util.function.BiConsumer;
import java.util.function.BiPredicate;

public class PDLFluidStacksHandler extends FluidStacksResourceHandler {
    private BiConsumer<Integer, FluidStack> onChangeFunction;
    private BiPredicate<Integer, FluidResource> validator = (_, _) -> true;

    public PDLFluidStacksHandler(NonNullList<FluidStack> stacks, int capacity) {
        super(stacks, capacity);
    }

    public PDLFluidStacksHandler(int size, int capacity) {
        super(size, capacity);
    }

    public void setOnChangeFunction(BiConsumer<Integer, FluidStack> onChangeFunction) {
        this.onChangeFunction = onChangeFunction;
    }

    public void setValidator(BiPredicate<Integer, FluidResource> validator) {
        this.validator = validator;
    }

    @Override
    public boolean isValid(int index, FluidResource resource) {
        return validator.test(index, resource);
    }

    @Override
    protected void onContentsChanged(int index, FluidStack previousContents) {
        if (onChangeFunction != null) {
            onChangeFunction.accept(index, previousContents);
        }
    }
}
