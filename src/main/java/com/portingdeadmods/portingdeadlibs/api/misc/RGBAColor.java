package com.portingdeadmods.portingdeadlibs.api.misc;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.util.ARGB;

public record RGBAColor(int r, int g, int b, int a) {
    public RGBAColor(int r, int g, int b) {
        this(r, g, b, 255);
    }

    public static final Codec<RGBAColor> CODEC = RecordCodecBuilder.create(inst -> inst.group(
            Codec.INT.fieldOf("r").forGetter(RGBAColor::r),
            Codec.INT.fieldOf("g").forGetter(RGBAColor::g),
            Codec.INT.fieldOf("b").forGetter(RGBAColor::b),
            Codec.INT.optionalFieldOf("a", 255).forGetter(RGBAColor::a)
    ).apply(inst, RGBAColor::new));

	public static final StreamCodec<ByteBuf, RGBAColor> STREAM_CODEC = StreamCodec.composite(
			ByteBufCodecs.INT,
			RGBAColor::r,
			ByteBufCodecs.INT,
			RGBAColor::g,
			ByteBufCodecs.INT,
			RGBAColor::b,
			ByteBufCodecs.INT,
			RGBAColor::a,
			RGBAColor::new
	);

    public int toARGB() {
        return ARGB.color(a, r, g, b);
    }
}
