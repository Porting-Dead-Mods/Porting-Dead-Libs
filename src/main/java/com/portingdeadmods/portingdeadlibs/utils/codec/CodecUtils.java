package com.portingdeadmods.portingdeadlibs.utils.codec;

import com.mojang.serialization.Codec;
import io.netty.buffer.ByteBuf;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;

import java.util.List;
import java.util.Set;

public final class CodecUtils {
    public static <E> Codec<Set<E>> set(final Codec<E> elementCodec) {
        return set(elementCodec, 0, Integer.MAX_VALUE);
    }

    public static <E> Codec<Set<E>> set(final Codec<E> elementCodec, final int minSize, final int maxSize) {
        return new SetCodec<>(elementCodec, minSize, maxSize);
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
