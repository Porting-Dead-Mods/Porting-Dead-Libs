package com.portingdeadmods.portingdeadlibs.utils;

import com.portingdeadmods.portingdeadlibs.utils.ranges.FloatRange;
import com.portingdeadmods.portingdeadlibs.utils.ranges.IntRange;

public final class RNGUtils {
    public static float random() {
        return (float) Math.random();
    }

    public static int uniformRandInt(IntRange range) {
        return uniformRandInt(range.getMin(), range.getMax());
    }

    public static int uniformRandInt(int min, int max) {
        return (int) (Math.random() * (max - min + 1) + min);
    }

    public static int uniformRandInt(int max) {
        return uniformRandInt(0, max);
    }

    public static float uniformRandFloat(FloatRange range) {
        return uniformRandFloat(range.getMin(), range.getMax());
    }

    public static float uniformRandFloat(float min, float max) {
        return (float) Math.random() * (max - min) + min;
    }

    public static float uniformRandFloat(float max) {
        return uniformRandFloat(0, max);
    }

    public static int intInRangeOf(int val, int range) {
        return (int) (uniformRandInt(-val, val) * random() + range);
    }

    public static int intInRangeOf(int range) {
        return intInRangeOf(0, range);
    }

    public static float floatInRangeOf(float val, float range) {
        return uniformRandFloat(-val, val) * random() + range;
    }

    public static float floatInRangeOf(float range) {
        return floatInRangeOf(0, range);
    }

    /*
    * -1 -> always in the negative range
    * 1 -> always in the positive range
    * 0 -> neutral
     */
    public static float biasedInRangeOf(float val, float range, float bias) {
        float absBias = Math.abs(bias);

        if (bias < 0) {
            return uniformRandFloat(-range, range * (1 - absBias)) + val;
        } else if (bias > 0) {
            return uniformRandFloat(-range * (1 - absBias), range) + val;
        }
        return uniformRandFloat(-range, range) + val;
    }

    public static float biasedInRangeOf(float range, float bias) {
        return biasedInRangeOf(0, range, bias);
    }

    public static float biasedInRange(float rangeStart, float rangeEnd, float bias) {
        float absBias = Math.abs(bias);
        float middlePoint = (rangeStart + rangeEnd) / 2;

        float left = middlePoint - rangeStart;
        float right = rangeEnd - middlePoint;

        if (bias < 0) {
            return uniformRandFloat(left, right * (1 - absBias)) + middlePoint;
        } else if (bias > 0) {
            return uniformRandFloat(left * (1 - absBias), right) + middlePoint;
        }
        return uniformRandFloat(left, right) + middlePoint;
    }
}
