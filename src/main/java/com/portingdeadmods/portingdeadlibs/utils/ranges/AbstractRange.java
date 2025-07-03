package com.portingdeadmods.portingdeadlibs.utils.ranges;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;

import java.util.Collection;
import java.util.function.BiFunction;

public abstract class AbstractRange<T extends Number> {
    private final T min;
    private final T max;
    private final Collection<T> possibleValues;

    protected AbstractRange(T minInclusive, T maxInclusive, T step) {
        this.min = minInclusive;
        this.max = maxInclusive;
        this.possibleValues = collectPossibleValues(step);
    }

    protected AbstractRange(T minInclusive, T maxInclusive) {
        this.min = minInclusive;
        this.max = maxInclusive;
        this.possibleValues = collectPossibleValues();
    }

    public T getMin() {
        return min;
    }

    public T getMax() {
        return max;
    }

    /**
     * Returns a collection of all possible values within the range dictated by the min, max, and step values.
     *
     * @return A collection of possible values.
     */
    public Collection<T> getPossibleValues() {
        return possibleValues;
    }

    /**
     * Checks if a given value fits within the range defined by min and max.
     *
     * @param value The value to check.
     * @return true if the value is within the range, false otherwise.
     */
    public boolean fits(T value) {
        return value.doubleValue() >= min.doubleValue() && value.doubleValue() <= max.doubleValue();
    }

    /**
     * Utility method to create a new range shifted by a specified offset.
     *
     * @param offset The offset to shift the range by.
     * @return A new range that is shifted by the specified offset.
     */
    public abstract AbstractRange<T> shift(T offset);

    /**
     * Collects all possible values in the range specified.
     * Default step of 1.0f is used.
     *
     * @return A collection of all possible float values in the range with a step of 1.0f.
     */
    protected abstract Collection<T> collectPossibleValues();

    /**
     * Collects all possible values in the range specified.
     *
     * @param step The step value to use for collecting possible values.
     * @return A collection of all possible float values in the range with a step of 1.0f.
     */
    protected abstract Collection<T> collectPossibleValues(T step);

    // Constructs a pair out of the range and uses that for encoding
    public static <T extends Number, SELF extends AbstractRange<T>> MapCodec<SELF> rangeMapCodec(Codec<T> codec, BiFunction<T, T, SELF> constructor) {
        return RecordCodecBuilder.mapCodec(instance -> instance.group(
                codec.fieldOf("min").forGetter(SELF::getMin),
                codec.fieldOf("max").forGetter(SELF::getMax)
        ).apply(instance, constructor));
    }

    public static <T extends Number, SELF extends AbstractRange<T>> StreamCodec<RegistryFriendlyByteBuf, SELF> rangeStreamCodec(StreamCodec<ByteBuf, T> streamCodec, BiFunction<T, T, SELF> constructor) {
        return StreamCodec.composite(
                streamCodec,
                SELF::getMin,
                streamCodec,
                SELF::getMax,
                constructor
        );
    }
}
