package com.portingdeadmods.portingdeadlibs.api.data.saved;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.portingdeadmods.portingdeadlibs.PDLRegistries;
import com.portingdeadmods.portingdeadlibs.PortingDeadLibs;
import com.portingdeadmods.portingdeadlibs.utils.codec.CodecUtils;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;

public record SavedDataHolder<T>(ResourceLocation key, PDLSavedData<T> value) {
    public static final Codec<SavedDataHolder<?>> CODEC = RecordCodecBuilder.create(inst -> inst.group(
            ResourceLocation.CODEC.fieldOf("key").forGetter(SavedDataHolder::key),
            CodecUtils.registryCodec(PDLRegistries.SAVED_DATA).fieldOf("value").forGetter(SavedDataHolder::value)
    ).apply(inst, SavedDataHolder::new));
    public static final StreamCodec<RegistryFriendlyByteBuf, SavedDataHolder<?>> STREAM_CODEC = StreamCodec.composite(
            ResourceLocation.STREAM_CODEC,
            SavedDataHolder::key,
            CodecUtils.registryStreamCodec(PDLRegistries.SAVED_DATA),
            SavedDataHolder::value,
            SavedDataHolder::new
    );

    public static <T> SavedDataHolder<T> fromValue(PDLSavedData<T> value) {
        return new SavedDataHolder<>(PDLRegistries.SAVED_DATA.getKey(value), value);
    }

}