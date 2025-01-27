package com.portingdeadmods.portingdeadlibs.utils.rng;

/**
 * A container for an object that attaches a weight to it<br>
 * Used in {@link WeightedList}
 * @param <T>
 */
public record WeightedElement<T>(T object, int weight) {
}
