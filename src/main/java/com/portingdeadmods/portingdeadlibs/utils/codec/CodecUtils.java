package com.portingdeadmods.portingdeadlibs.utils.codec;

import com.mojang.datafixers.types.Func;
import com.mojang.datafixers.util.Function7;
import com.mojang.datafixers.util.Function8;
import com.mojang.datafixers.util.Function9;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.netty.buffer.ByteBuf;
import net.minecraft.core.Registry;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.neoforged.neoforge.common.util.NeoForgeExtraCodecs;
import net.neoforged.neoforge.network.codec.NeoForgeStreamCodecs;
import org.jetbrains.annotations.NotNull;

import java.util.HashSet;
import java.util.Set;
import java.util.function.Function;
import java.util.function.IntFunction;
import java.util.function.Supplier;

public final class CodecUtils {
    public static final Codec<RecipeHolder<?>> RECIPE_HOLDER_CODEC = RecordCodecBuilder.create(instance -> instance.group (
            ResourceLocation.CODEC.fieldOf("id").forGetter(RecipeHolder::id),
            Recipe.CODEC.fieldOf("recipe").forGetter(RecipeHolder::value)
    ).apply(instance, RecipeHolder::new));

    public static <E> Codec<Set<E>> set(final Codec<E> elementCodec) {
        return set(elementCodec, HashSet::new, 0, Integer.MAX_VALUE);
    }

    public static <E> Codec<Set<E>> set(final Codec<E> elementCodec, Supplier<? extends Set<E>> setSupplier) {
        return set(elementCodec, setSupplier, 0, Integer.MAX_VALUE);
    }

    public static <E> Codec<Set<E>> set(final Codec<E> elementCodec, Supplier<? extends Set<E>> setSupplier, final int minSize, final int maxSize) {
        return new SetCodec<>(elementCodec, minSize, maxSize, setSupplier);
    }

    public static <E, B extends ByteBuf> StreamCodec<? extends B, Set<E>> setStreamCodec(final StreamCodec<? super B, E> elementStreamCodec) {
        return ByteBufCodecs.collection(HashSet::new, elementStreamCodec);
    }

    public static <E, B extends ByteBuf> StreamCodec<? extends B, Set<E>> setStreamCodec(final StreamCodec<? super B, E> elementStreamCodec, IntFunction<Set<E>> setFactory) {
        return ByteBufCodecs.collection(setFactory, elementStreamCodec);
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

    public static <B, C, T1, T2, T3, T4, T5, T6, T7, T8> StreamCodec<B, C> streamCodecComposite(final StreamCodec<? super B, T1> codec1, final Function<C, T1> getter1, final StreamCodec<? super B, T2> codec2, final Function<C, T2> getter2, final StreamCodec<? super B, T3> codec3, final Function<C, T3> getter3, final StreamCodec<? super B, T4> codec4, final Function<C, T4> getter4, final StreamCodec<? super B, T5> codec5, final Function<C, T5> getter5, final StreamCodec<? super B, T6> codec6, final Function<C, T6> getter6, final StreamCodec<? super B, T7> codec7, final Function<C, T7> getter7, final StreamCodec<? super B, T8> codec8, final Function<C, T8> getter8, final Function8<T1, T2, T3, T4, T5, T6, T7, T8, C> factory) {
        return new StreamCodec<>() {
            public @NotNull C decode(@NotNull B byteBuf) {
                T1 t1 = codec1.decode(byteBuf);
                T2 t2 = codec2.decode(byteBuf);
                T3 t3 = codec3.decode(byteBuf);
                T4 t4 = codec4.decode(byteBuf);
                T5 t5 = codec5.decode(byteBuf);
                T6 t6 = codec6.decode(byteBuf);
                T7 t7 = codec7.decode(byteBuf);
                T8 t8 = codec8.decode(byteBuf);
                return factory.apply(t1, t2, t3, t4, t5, t6, t7, t8);
            }

            public void encode(@NotNull B byteBuf, @NotNull C content) {
                codec1.encode(byteBuf, getter1.apply(content));
                codec2.encode(byteBuf, getter2.apply(content));
                codec3.encode(byteBuf, getter3.apply(content));
                codec4.encode(byteBuf, getter4.apply(content));
                codec5.encode(byteBuf, getter5.apply(content));
                codec6.encode(byteBuf, getter6.apply(content));
                codec7.encode(byteBuf, getter7.apply(content));
                codec8.encode(byteBuf, getter8.apply(content));
            }
        };
    }

    public static <B, C, T1, T2, T3, T4, T5, T6, T7, T8, T9> StreamCodec<B, C> streamCodecComposite(final StreamCodec<? super B, T1> codec1, final Function<C, T1> getter1, final StreamCodec<? super B, T2> codec2, final Function<C, T2> getter2, final StreamCodec<? super B, T3> codec3, final Function<C, T3> getter3, final StreamCodec<? super B, T4> codec4, final Function<C, T4> getter4, final StreamCodec<? super B, T5> codec5, final Function<C, T5> getter5, final StreamCodec<? super B, T6> codec6, final Function<C, T6> getter6, final StreamCodec<? super B, T7> codec7, final Function<C, T7> getter7, final StreamCodec<? super B, T8> codec8, final Function<C, T8> getter8, final StreamCodec<? super B, T9> codec9, final Function<C, T9> getter9, final Function9<T1, T2, T3, T4, T5, T6, T7, T8, T9, C> factory) {
        return new StreamCodec<>() {
            public @NotNull C decode(@NotNull B byteBuf) {
                T1 t1 = codec1.decode(byteBuf);
                T2 t2 = codec2.decode(byteBuf);
                T3 t3 = codec3.decode(byteBuf);
                T4 t4 = codec4.decode(byteBuf);
                T5 t5 = codec5.decode(byteBuf);
                T6 t6 = codec6.decode(byteBuf);
                T7 t7 = codec7.decode(byteBuf);
                T8 t8 = codec8.decode(byteBuf);
                T9 t9 = codec9.decode(byteBuf);
                return factory.apply(t1, t2, t3, t4, t5, t6, t7, t8, t9);
            }

            public void encode(@NotNull B byteBuf, @NotNull C content) {
                codec1.encode(byteBuf, getter1.apply(content));
                codec2.encode(byteBuf, getter2.apply(content));
                codec3.encode(byteBuf, getter3.apply(content));
                codec4.encode(byteBuf, getter4.apply(content));
                codec5.encode(byteBuf, getter5.apply(content));
                codec6.encode(byteBuf, getter6.apply(content));
                codec7.encode(byteBuf, getter7.apply(content));
                codec8.encode(byteBuf, getter8.apply(content));
                codec9.encode(byteBuf, getter9.apply(content));
            }
        };
    }

}
