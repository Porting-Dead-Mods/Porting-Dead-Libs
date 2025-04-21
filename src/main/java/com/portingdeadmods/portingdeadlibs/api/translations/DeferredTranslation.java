package com.portingdeadmods.portingdeadlibs.api.translations;

import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceKey;
import net.neoforged.neoforge.registries.DeferredHolder;

public class DeferredTranslation<T extends TranslatableConstant> extends DeferredHolder<TranslatableConstant, T> {
    protected DeferredTranslation(ResourceKey<TranslatableConstant> key) {
        super(key);
    }

    public String key() {
        return get().category() + "." + this.getId().getNamespace() + "." + get().getRawKey();
    }

    public MutableComponent component(Object... args) {
        return Component.translatable(key(), args);
    }

    public String category() {
        return get().category();
    }

    public static <T extends TranslatableConstant> DeferredTranslation<T> createTranslation(ResourceKey<TranslatableConstant> key) {
        return new DeferredTranslation<>(key);
    }
}
