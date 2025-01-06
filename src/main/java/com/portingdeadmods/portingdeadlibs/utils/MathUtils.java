package com.portingdeadmods.portingdeadlibs.utils;

import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;

public final class MathUtils {

    public static Vec3 rotatePitch(Vec3 vec, float pitch){
        float fc = Mth.cos(pitch);
        float fs = Mth.sin(pitch);
        return new Vec3(vec.x, vec.y * fc - vec.z * fs, vec.z * fc + vec.y * fs);
    }

    public static Vec3 rotateYaw(Vec3 vec, float yaw) {
        float fc = Mth.cos(yaw);
        float fs = Mth.sin(yaw);
        return new Vec3(vec.x * fc + vec.z * fs, vec.y, vec.z * fc - vec.x * fs);
    }

    public static Vec3 rotateRoll(Vec3 vec, float roll) {
        float fc = Mth.cos(roll);
        float fs = Mth.sin(roll);
        return new Vec3(vec.x * fc + vec.y * fs, vec.y * fc - vec.x * fs, vec.z);
    }

    public static double map(double val, double inMin, double inMax, double desMin, double desMax) {
        return desMin + (desMax - desMin) * (val - inMin) / (inMax - inMin);
    }

    public static Vec3 rotateYawDegrees(Vec3 direction, double angleDegrees) {
        double radians = Math.toRadians(angleDegrees);
        double cos = Math.cos(radians);
        double sin = Math.sin(radians);

        double newX = direction.x * cos - direction.z * sin;
        double newZ = direction.x * sin + direction.z * cos;

        return new Vec3(newX, direction.y, newZ);
    }

    public static Float roundToPrecision(float value, int precision) {
        return (float) Math.round(value * Math.pow(10, precision)) / (float) Math.pow(10, precision);
    }
}
// Wow this class is full of maths, I guess the person who did it, is just good at maths :shrug: