package com.portingdeadmods.portingdeadlibs.utils.rng;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * A list composed of {@link WeightedElement}<br>
 * Allows weighted random selection of elements
 * @param <T>
 */
public class WeightedList<T> {
    private final List<WeightedElement<T>> elements = new ArrayList<>();
    private final List<Integer> prefixSums = new ArrayList<>();
    private int totalWeight = 0;
    private final Random random = new Random();

    public void add(WeightedElement<? extends T> element) {
        elements.add(new WeightedElement<>(element.object(), element.weight()));
        totalWeight += element.weight();
        prefixSums.add(totalWeight);
    }

    public void remove(WeightedElement<? extends T> element) {
        int index = elements.indexOf(element);
        if (index == -1) {
            throw new IllegalArgumentException("Element not found in list");
        }
        elements.remove(index);
        totalWeight -= element.weight();
        prefixSums.remove(index);
    }

    public void remove(int index) {
        if (index < 0 || index >= elements.size()) {
            throw new IndexOutOfBoundsException("Index out of bounds");
        }
        WeightedElement<T> element = elements.get(index);
        elements.remove(index);
        totalWeight -= element.weight();
        prefixSums.remove(index);
    }

    public T next() {
        if (elements.isEmpty()) {
            throw new IllegalStateException("Trying to select from an empty list");
        }
        int rand = random.nextInt(totalWeight);

        int index = binarySearch(rand);
        return elements.get(index).object();
    }

    private int binarySearch(int target) {
        int low = 0, high = prefixSums.size() - 1;
        while (low < high) {
            int mid = (low + high) / 2;
            if (prefixSums.get(mid) > target) {
                high = mid;
            } else {
                low = mid + 1;
            }
        }
        return low;
    }
}
