package com.portingdeadmods.portingdeadlibs.utils.codec;

import com.google.common.collect.ImmutableSet;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.DynamicOps;
import com.mojang.serialization.Encoder;
import com.mojang.serialization.Lifecycle;

import java.util.function.UnaryOperator;

public interface SetBuilder<T> {
    DynamicOps<T> ops();

    DataResult<T> build(T prefix);

    SetBuilder<T> add(final T value);

    SetBuilder<T> add(final DataResult<T> value);

    SetBuilder<T> withErrorsFrom(final DataResult<?> result);

    SetBuilder<T> mapError(UnaryOperator<String> onError);

    default DataResult<T> build(final DataResult<T> prefix) {
        return prefix.flatMap(this::build);
    }

    default <E> SetBuilder<T> add(final E value, final Encoder<E> encoder) {
        return add(encoder.encodeStart(ops(), value));
    }

    default <E> SetBuilder<T> addAll(final Iterable<E> values, final Encoder<E> encoder) {
        values.forEach(v -> encoder.encode(v, ops(), ops().empty()));
        return this;
    }

    final class Builder<T> implements SetBuilder<T> {
        private final DynamicOps<T> ops;
        private DataResult<ImmutableSet.Builder<T>> builder = DataResult.success(ImmutableSet.builder(), Lifecycle.stable());

        public Builder(final DynamicOps<T> ops) {
            this.ops = ops;
        }

        @Override
        public DynamicOps<T> ops() {
            return ops;
        }

        @Override
        public SetBuilder<T> add(final T value) {
            builder = builder.map(b -> b.add(value));
            return this;
        }

        @Override
        public SetBuilder<T> add(final DataResult<T> value) {
            builder = builder.apply2stable(ImmutableSet.Builder::add, value);
            return this;
        }

        @Override
        public SetBuilder<T> withErrorsFrom(final DataResult<?> result) {
            builder = builder.flatMap(r -> result.map(v -> r));
            return this;
        }

        @Override
        public SetBuilder<T> mapError(final UnaryOperator<String> onError) {
            builder = builder.mapError(onError);
            return this;
        }

        @Override
        public DataResult<T> build(final T prefix) {
            final DataResult<T> result = builder.flatMap(b -> ops.mergeToList(prefix, b.build().asList()));
            builder = DataResult.success(ImmutableSet.builder(), Lifecycle.stable());
            return result;
        }
    }
}
