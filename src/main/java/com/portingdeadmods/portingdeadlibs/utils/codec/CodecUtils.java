package com.portingdeadmods.portingdeadlibs.utils.codec;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.netty.buffer.ByteBuf;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeHolder;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Function;
import java.util.function.IntFunction;
import java.util.function.Supplier;

public final class CodecUtils {
    public static final Codec<RecipeHolder<?>> RECIPE_HOLDER_CODEC = RecordCodecBuilder.create(instance -> instance.group (
            ResourceLocation.CODEC.fieldOf("id").forGetter(RecipeHolder::id),
            Recipe.CODEC.fieldOf("recipe").forGetter(RecipeHolder::value)
    ).apply(instance, RecipeHolder::new));

    public static <E> Codec<Set<E>> set(final Codec<E> elementCodec) {
        return set(elementCodec, HashSet::new, 0, Integer.MAX_VALUE);
    }

    public static <E> Codec<Set<E>> set(final Codec<E> elementCodec, Supplier<? extends Set<E>> setSupplier) {
        return set(elementCodec, setSupplier, 0, Integer.MAX_VALUE);
    }

    public static <E> Codec<Set<E>> set(final Codec<E> elementCodec, Supplier<? extends Set<E>> setSupplier, final int minSize, final int maxSize) {
        return new SetCodec<>(elementCodec, minSize, maxSize, setSupplier);
    }

    public static <E, B extends ByteBuf> StreamCodec<? extends B, Set<E>> setStreamCodec(final StreamCodec<? super B, E> elementStreamCodec) {
        return ByteBufCodecs.collection(HashSet::new, elementStreamCodec);
    }

    public static <E, B extends ByteBuf> StreamCodec<? extends B, Set<E>> setStreamCodec(final StreamCodec<? super B, E> elementStreamCodec, IntFunction<Set<E>> setFactory) {
        return ByteBufCodecs.collection(setFactory, elementStreamCodec);
    }

    public static <R> Codec<R> registryCodec(Registry<R> registry) {
        return ResourceLocation.CODEC.xmap(registry::get, registry::getKey);
    }

    public static <R> StreamCodec<ByteBuf, R> registryStreamCodec(Registry<R> registry) {
        return ResourceLocation.STREAM_CODEC.map(registry::get, registry::getKey);
    }

    public static <T extends Enum<T>> Codec<T> enumCodec(Class<T> enumClazz) {
        return Codec.INT.xmap(i -> enumClazz.getEnumConstants()[i], Enum::ordinal);
    }

    public static <T extends Enum<T>> StreamCodec<ByteBuf, T> enumStreamCodec(Class<T> enumClazz) {
        return ByteBufCodecs.INT.map(i -> enumClazz.getEnumConstants()[i], Enum::ordinal);
    }

}
