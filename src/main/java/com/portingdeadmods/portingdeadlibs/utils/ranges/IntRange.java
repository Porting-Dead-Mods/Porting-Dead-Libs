package com.portingdeadmods.portingdeadlibs.utils.ranges;

import com.google.common.collect.ImmutableList;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import it.unimi.dsi.fastutil.ints.IntArrayList;
import it.unimi.dsi.fastutil.ints.IntList;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;

import java.util.Collection;

public class IntRange extends AbstractRange<Integer> {
    public static final MapCodec<IntRange> MAP_CODEC = rangeMapCodec(Codec.INT, IntRange::new);
    public static final StreamCodec<RegistryFriendlyByteBuf, IntRange> STREAM_CODEC = rangeStreamCodec(ByteBufCodecs.INT, IntRange::new);

    protected IntRange(Integer min, Integer max) {
        super(min, max);
    }

    @Override
    protected Collection<Integer> collectPossibleValues() {
        IntList values = new IntArrayList(Math.abs(getMax() - getMin()) + 1);
        for (int i = getMin(); i < getMax(); i++) {
            values.add(i);
        }
        return ImmutableList.copyOf(values);
    }

    public static IntRange of(int min, int max) {
        return new IntRange(min, max);
    }
}