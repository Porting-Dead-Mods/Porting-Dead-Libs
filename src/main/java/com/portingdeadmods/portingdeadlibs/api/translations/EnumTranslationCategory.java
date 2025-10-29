package com.portingdeadmods.portingdeadlibs.api.translations;

import net.minecraft.util.StringRepresentable;

public record EnumTranslationCategory<E extends Enum<E> & StringRepresentable>(
        DeferredTranslationRegister parentRegister, String category) implements TranslationCategory<E> {
    @Override
    public String keyToString(E key) {
        return key.getSerializedName();
    }
}
