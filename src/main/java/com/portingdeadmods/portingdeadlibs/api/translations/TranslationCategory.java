package com.portingdeadmods.portingdeadlibs.api.translations;

public interface TranslationCategory<K> {
    String category();

    DeferredTranslationRegister parentRegister();

    String keyToString(K key);

    default DeferredTranslation<TranslatableConstant> registerWithDefault(K key, String defaultTranslation) {
        DeferredTranslation<TranslatableConstant> translation = this.parentRegister().register(this.keyToString(key), () -> new TranslatableConstant(this.keyToString(key), this.category()));
        parentRegister().getDefaultTranslations().put(this.category() + "." + this.parentRegister().getNamespace() + "." + key, defaultTranslation);
        return translation;
    }
}
