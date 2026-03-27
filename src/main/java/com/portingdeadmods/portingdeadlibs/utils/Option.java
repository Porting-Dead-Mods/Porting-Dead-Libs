package com.portingdeadmods.portingdeadlibs.utils;

public sealed interface Option<T> permits Option.Some, Option.None {
    record Some<T>(T value) implements Option<T> {
        @Override
        public boolean isPresent() {
            return true;
        }
    }

    record None<T>() implements Option<T> {
        @Override
        public boolean isPresent() {
            return false;
        }
    }

    boolean isPresent();

    static <T> Option.Some<T> some(T value) {
        return new Some<>(value);
    }

    static <T> Option.None<T> none() {
        return new None<>();
    }

}
