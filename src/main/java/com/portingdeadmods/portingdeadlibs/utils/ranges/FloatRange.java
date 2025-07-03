package com.portingdeadmods.portingdeadlibs.utils.ranges;

import com.google.common.collect.ImmutableList;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import it.unimi.dsi.fastutil.floats.FloatArrayList;
import it.unimi.dsi.fastutil.floats.FloatList;
import it.unimi.dsi.fastutil.ints.IntArrayList;
import it.unimi.dsi.fastutil.ints.IntList;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;

import java.util.Collection;
import java.util.List;

public class FloatRange extends AbstractRange<Float>{
    public static final MapCodec<FloatRange> MAP_CODEC = rangeMapCodec(Codec.FLOAT, FloatRange::new);
    public static final StreamCodec<RegistryFriendlyByteBuf, FloatRange> STREAM_CODEC = rangeStreamCodec(ByteBufCodecs.FLOAT, FloatRange::new);

    protected FloatRange(Float minInclusive, Float maxInclusive, Float step) {
        super(minInclusive, maxInclusive, step);
    }

    protected FloatRange(Float minInclusive, Float maxInclusive) {
        super(minInclusive, maxInclusive);
    }

    @Override
    protected Collection<Float> collectPossibleValues(Float step) {
        FloatList values = new FloatArrayList((int) Math.abs(getMax() - getMin()) + 1);
        for (float i = getMin(); i < getMax(); i += step) {
            values.add(i);
        }
        return ImmutableList.copyOf(values);
    }

    @Override
    protected Collection<Float> collectPossibleValues() {
        return collectPossibleValues(1f);
    }

    @Override
    public FloatRange shift(Float offset) {
        return new FloatRange(getMin() + offset, getMax() + offset);
    }

    /**
     * Creates a new {@link FloatRange} with the specified minimum and maximum values.
     *
     * @param min The minimum value (inclusive).
     * @param max The maximum value (inclusive).
     * @return A new FloatRange instance with the specified values
     */
    public static FloatRange of(float min, float max) {
        return new FloatRange(min, max);
    }

    /**
     * Creates a new {@link FloatRange} with the specified minimum and maximum values.
     *
     * @param min The minimum value (inclusive).
     * @param max The maximum value (inclusive).
     * @param step The step value to use when generating the range.
     * @return A new FloatRange instance with the specified values
     */
    public static FloatRange of(float min, float max, float step) {
        if (step <= 0) {
            throw new IllegalArgumentException("Step must be greater than 0");
        }

        return new FloatRange(min, max, step);
    }
}