package com.portingdeadmods.portingdeadlibs.api.utils;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.util.FastColor;

public record RGBAColor(int r, int g, int b, int a) {
    public RGBAColor(int r, int g, int b) {
        this(r, g, b, 255);
    }

    private static final Codec<RGBAColor> RAW_CODEC = RecordCodecBuilder.create(inst -> inst.group(
            Codec.INT.fieldOf("r").forGetter(RGBAColor::r),
            Codec.INT.fieldOf("g").forGetter(RGBAColor::g),
            Codec.INT.fieldOf("b").forGetter(RGBAColor::b),
            Codec.INT.optionalFieldOf("a", 255).forGetter(RGBAColor::a)
    ).apply(inst, RGBAColor::new));

    public int toARGB() {
        return FastColor.ARGB32.color(a, r, g, b);
    }
}
