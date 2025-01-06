package com.portingdeadmods.portingdeadlibs.utils.codec;

import com.mojang.serialization.Codec;
import io.netty.buffer.ByteBuf;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;

import java.util.List;
import java.util.Set;

public final class CodecUtils {
    public static final Codec<Item> ITEM_CODEC = ResourceLocation.CODEC.xmap(BuiltInRegistries.ITEM::get, BuiltInRegistries.ITEM::getKey);
    public static final StreamCodec<ByteBuf, Item> ITEM_STREAM_CODEC = ResourceLocation.STREAM_CODEC.map(BuiltInRegistries.ITEM::get, BuiltInRegistries.ITEM::getKey);

    public static StreamCodec<ByteBuf, List<Float>> FLOAT_LIST_STREAM_CODEC = new StreamCodec<ByteBuf, List<Float>>() {
        @Override
        public List<Float> decode(ByteBuf buffer) {
            List<Float> floats = List.of();
            for (int i = 0; i < buffer.readableBytes(); i += 4) {
                floats.add(buffer.readFloat());
            }
            return floats;
        }

        @Override
        public void encode(ByteBuf buffer, List<Float> value) {
            for (Float f : value) {
                buffer.writeFloat(f);
            }
        }
    };

    public static StreamCodec<ByteBuf, List<Integer>> INT_LIST_STREAM_CODEC = new StreamCodec<ByteBuf, List<Integer>>() {
        @Override
        public List<Integer> decode(ByteBuf buffer) {
            List<Integer> integers = List.of();
            for (int i = 0; i < buffer.readableBytes(); i += 4) {
                integers.add(buffer.readInt());
            }
            return integers;
        }

        @Override
        public void encode(ByteBuf buffer, List<Integer> value) {
            for (Integer i : value) {
                buffer.writeFloat(i);
            }
        }
    };

    public static <E> Codec<Set<E>> set(final Codec<E> elementCodec) {
        return set(elementCodec, 0, Integer.MAX_VALUE);
    }

    public static <E> Codec<Set<E>> set(final Codec<E> elementCodec, final int minSize, final int maxSize) {
        return new SetCodec<>(elementCodec, minSize, maxSize);
    }

}
