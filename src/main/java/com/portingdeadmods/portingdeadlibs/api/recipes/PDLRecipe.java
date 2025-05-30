package com.portingdeadmods.portingdeadlibs.api.recipes;

import net.minecraft.core.HolderLookup;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeInput;
import org.jetbrains.annotations.NotNull;

public interface PDLRecipe<T extends RecipeInput> extends Recipe<T> {
    @Override
    default @NotNull ItemStack assemble(@NotNull T container, HolderLookup.Provider provider) {
        return getResultItem(provider).copy();
    }

    @Override
    default boolean canCraftInDimensions(int i, int i1) {
        return true;
    }
}