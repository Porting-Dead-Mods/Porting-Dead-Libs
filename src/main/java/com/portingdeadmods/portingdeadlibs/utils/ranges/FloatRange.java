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

    protected FloatRange(Float minInclusive, Float maxInclusive) {
        super(minInclusive, maxInclusive);
    }

    @Override
    protected Collection<Float> collectPossibleValues() {
        FloatList values = new FloatArrayList((int) Math.abs(getMax() - getMin()) + 1);
        for (float i = getMin(); i < getMax(); i++) {
            values.add(i);
        }
        return ImmutableList.copyOf(values);
    }

    public static FloatRange of(float min, float max) {
        return new FloatRange(min, max);
    }
}