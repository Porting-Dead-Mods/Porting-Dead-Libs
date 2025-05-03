package com.portingdeadmods.portingdeadlibs.utils.codec;

import com.mojang.datafixers.util.Pair;
import com.mojang.datafixers.util.Unit;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.DynamicOps;
import com.mojang.serialization.Lifecycle;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Stream;

public record SetCodec<E>(Codec<E> elementCodec, int minSize, int maxSize, Supplier<? extends Set<E>> setSupplier) implements Codec<Set<E>> {
    private <R> DataResult<R> createTooShortError(final int size) {
        return DataResult.error(() -> "Set is too short: " + size + ", expected range [" + minSize + "-" + maxSize + "]");
    }

    private <R> DataResult<R> createTooLongError(final int size) {
        return DataResult.error(() -> "Set is too long: " + size + ", expected range [" + minSize + "-" + maxSize + "]");
    }

    @Override
    public <T> DataResult<T> encode(Set<E> input, DynamicOps<T> ops, T prefix) {
        if (input.size() < minSize) {
            return createTooShortError(input.size());
        }
        if (input.size() > maxSize) {
            return createTooLongError(input.size());
        }
        final SetBuilder<T> builder = new SetBuilder.Builder<>(ops);
        for (final E element : input) {
            builder.add(elementCodec.encodeStart(ops, element));
        }
        return builder.build(prefix);
    }

    @Override
    public <T> DataResult<Pair<Set<E>, T>> decode(DynamicOps<T> ops, T input) {
        return ops.getList(input).setLifecycle(Lifecycle.stable()).flatMap(stream -> {
            final DecoderState<T> decoder = new DecoderState<>(ops, setSupplier);
            stream.accept(decoder::accept);
            return decoder.build();
        });
    }

    @Override
    public String toString() {
        return "SetCodec[" + elementCodec + "]";
    }

    private class DecoderState<T> {
        private static final DataResult<Unit> INITIAL_RESULT = DataResult.success(Unit.INSTANCE, Lifecycle.stable());

        private final DynamicOps<T> ops;
        private final Set<E> elements;
        private final Stream.Builder<T> failed = Stream.builder();
        private DataResult<Unit> result = INITIAL_RESULT;
        private int totalCount;

        private DecoderState(final DynamicOps<T> ops, Supplier<? extends Set<E>> setSupplier) {
            this.ops = ops;
            this.elements = setSupplier.get();
        }

        public void accept(final T value) {
            totalCount++;
            if (elements.size() >= maxSize) {
                failed.add(value);
                return;
            }
            final DataResult<Pair<E, T>> elementResult = elementCodec.decode(ops, value);
            elementResult.error().ifPresent(error -> failed.add(value));
            elementResult.resultOrPartial().ifPresent(pair -> elements.add(pair.getFirst()));
            result = result.apply2stable((result, element) -> result, elementResult);
        }

        public DataResult<Pair<Set<E>, T>> build() {
            if (elements.size() < minSize) {
                return createTooShortError(elements.size());
            }
            final T errors = ops.createList(failed.build());
            final Pair<Set<E>, T> pair = Pair.of(elements, errors);
            if (totalCount > maxSize) {
                result = createTooLongError(totalCount);
            }
            return result.map(ignored -> pair).setPartial(pair);
        }
    }
}
