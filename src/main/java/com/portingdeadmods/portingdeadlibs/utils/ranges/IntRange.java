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
import java.util.List;

public class IntRange extends AbstractRange<Integer> {
    public static final MapCodec<IntRange> MAP_CODEC = rangeMapCodec(Codec.INT, IntRange::new);
    public static final StreamCodec<RegistryFriendlyByteBuf, IntRange> STREAM_CODEC = rangeStreamCodec(ByteBufCodecs.INT, IntRange::new);

    protected IntRange(Integer minInclusive, Integer maxInclusive, Integer step) {
        super(minInclusive, maxInclusive, step);
    }

    protected IntRange(Integer minInclusive, Integer maxInclusive) {
        super(minInclusive, maxInclusive);
    }

    @Override
    protected Collection<Integer> collectPossibleValues() {
        IntList values = new IntArrayList(Math.abs(getMax() - getMin()) + 1);
        for (int i = getMin(); i < getMax(); i++) {
            values.add(i);
        }
        return ImmutableList.copyOf(values);
    }

    @Override
    protected Collection<Integer> collectPossibleValues(Integer step) {
        IntList values = new IntArrayList();
        for (int i = getMin(); i < getMax(); i += step) {
            values.add(i);
        }
        return ImmutableList.copyOf(values);
    }

    public IntRange shift(Integer offset) {
        return new IntRange(getMin() + offset, getMax() + offset);
    }

    /**
     * Creates a new {@link IntRange} with the specified minimum and maximum values.
     *
     * @param min The minimum value (inclusive).
     * @param max The maximum value (inclusive).
     * @return A new IntRange instance with the specified values
     */
    public static IntRange of(int min, int max) {
        return new IntRange(min, max);
    }

    /**
     * Creates a new {@link IntRange} with the specified minimum, maximum and step values.
     *
     * @param min The minimum value (inclusive).
     * @param max The maximum value (inclusive).
     * @param step The step value to use when generating the range.
     * @return A new IntRange instance with the specified values
     */
    public static IntRange of(int min, int max, int step) {
        if (step <= 0) {
            throw new IllegalArgumentException("Step must be greater than 0");
        }

        return new IntRange(min, max, step);
    }
}