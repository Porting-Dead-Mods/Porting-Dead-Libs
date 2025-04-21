package com.portingdeadmods.portingdeadlibs.api.translations;

import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;

import java.util.Objects;

public class TranslatableConstant {
    private final String key;
    private final String category;

    public TranslatableConstant(String key, String category) {
        this.key = key;
        this.category = category;
    }

    public String getRawKey() {
        return this.key;
    }

    public String key(String modid) {
        return category + "." + modid + "." + key;
    }

    public MutableComponent component(String modid, Object... args) {
        return Component.translatable(key(modid), args);
    }

    public String category() {
        return category;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null || obj.getClass() != this.getClass()) return false;
        var that = (TranslatableConstant) obj;
        return Objects.equals(this.key, that.key) &&
                Objects.equals(this.category, that.category);
    }

    @Override
    public int hashCode() {
        return Objects.hash(key, category);
    }

}
