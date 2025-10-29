package com.portingdeadmods.portingdeadlibs.api.translations;

public record DefaultTranslationCategory(DeferredTranslationRegister parentRegister, String category) implements TranslationCategory<String> {
    @Override
    public String keyToString(String key) {
        return key;
    }
}
