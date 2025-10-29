package com.portingdeadmods.portingdeadlibs.api.recipes;

import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import net.minecraft.core.NonNullList;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.tags.TagKey;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;

import java.util.List;

public record IngredientWithCount(Ingredient ingredient, int count) {
    public static final IngredientWithCount EMPTY = new IngredientWithCount(Ingredient.EMPTY, -1);
    public static final Codec<IngredientWithCount> CODEC = Codec.pair(
            ExtraCodecs.POSITIVE_INT.optionalFieldOf("amount", 1).codec(),
            Ingredient.CODEC
    ).xmap(pair -> new IngredientWithCount(pair.getSecond(), pair.getFirst()),
            iwc -> new Pair<>(iwc.count, iwc.ingredient));
    public static final StreamCodec<RegistryFriendlyByteBuf, IngredientWithCount> STREAM_CODEC = StreamCodec.composite(
            Ingredient.CONTENTS_STREAM_CODEC,
            IngredientWithCount::ingredient,
            ByteBufCodecs.INT,
            IngredientWithCount::count,
            IngredientWithCount::new
    );
    public static final StreamCodec<RegistryFriendlyByteBuf, List<IngredientWithCount>> STREAM_LIST_CODEC = STREAM_CODEC.apply(ByteBufCodecs.collection(NonNullList::createWithCapacity));

    public IngredientWithCount(Ingredient ingredient) {
        this(ingredient, 1);
    }

    public boolean test(ItemStack itemStack) {
        return ingredient.test(itemStack) && itemStack.getCount() >= count;
    }

    public Ingredient toIngredientSaveCount() {
        Ingredient ingredient = this.ingredient();
        for (ItemStack itemStack : ingredient.getItems()) {
            itemStack.setCount(this.count());
        }
        return ingredient;
    }

    public static IngredientWithCount of(ItemStack itemStack) {
        return new IngredientWithCount(Ingredient.of(itemStack), itemStack.getCount());
    }

    public static IngredientWithCount of(TagKey<Item> itemTagKey) {
        return new IngredientWithCount(Ingredient.of(itemTagKey), 1);
    }

    public static IngredientWithCount of(TagKey<Item> itemTagKey, int count) {
        return new IngredientWithCount(Ingredient.of(itemTagKey), count);
    }

    public static IngredientWithCount of(ItemLike itemLike) {
        return new IngredientWithCount(Ingredient.of(itemLike), 1);
    }

    public static IngredientWithCount of(ItemLike itemLike, int count) {
        return new IngredientWithCount(Ingredient.of(itemLike), count);
    }
}