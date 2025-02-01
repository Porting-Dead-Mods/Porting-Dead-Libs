package com.portingdeadmods.portingdeadlibs.utils;

import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;

public final class RegistryUtils {
    /**
     * Returns the resource key for a value on that registry.
     */
    public static <T> ResourceKey<T> resourceKey(Registry<T> registry, T value) {
        return registry.getResourceKey(value).orElseThrow();
    }

    /**
     * Returns the holder reference for a value on that registry.
     */
    public static <T> Holder.Reference<T> holder(Registry<T> registry, T value) {
        return registry.getHolderOrThrow(resourceKey(registry, value));
    }
}
