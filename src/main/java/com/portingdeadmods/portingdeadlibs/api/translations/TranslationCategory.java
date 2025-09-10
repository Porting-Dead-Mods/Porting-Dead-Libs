package com.portingdeadmods.portingdeadlibs.api.translations;

public record TranslationCategory(DeferredTranslationRegister register, String category) {
    public DeferredTranslation<TranslatableConstant> registerWithDefault(String key, String defaultTranslation) {
        DeferredTranslation<TranslatableConstant> translation = this.register.register(key, () -> new TranslatableConstant(key, category));
        register.getDefaultTranslations().put(category + "." + register.getNamespace() + "." + key, defaultTranslation);
        return translation;
    }
}
