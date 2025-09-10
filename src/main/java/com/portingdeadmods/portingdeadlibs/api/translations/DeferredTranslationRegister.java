package com.portingdeadmods.portingdeadlibs.api.translations;

import com.portingdeadmods.portingdeadlibs.PDLRegistries;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Supplier;

public class DeferredTranslationRegister extends DeferredRegister<TranslatableConstant> {
    private final Map<String, String> defaultTranslations;

    protected DeferredTranslationRegister(String modid) {
        super(PDLRegistries.TRANSLATION_KEY, modid);
        this.defaultTranslations = new HashMap<>();
    }

    public static DeferredTranslationRegister createTranslations(String modid) {
        return new DeferredTranslationRegister(modid);
    }

    public TranslationCategory createCategory(String category) {
        return new TranslationCategory(this, category);
    }

    public Map<String, String> getDefaultTranslations() {
        return defaultTranslations;
    }

    @Override
    public <I extends TranslatableConstant> DeferredTranslation<I> register(String name, Supplier<? extends I> sup) {
        return (DeferredTranslation<I>) super.register(name, sup);
    }

    @Override
    public <I extends TranslatableConstant> DeferredTranslation<I> register(String name, Function<ResourceLocation, ? extends I> func) {
        return (DeferredTranslation<I>) super.register(name, func);
    }

    @Override
    protected <I extends TranslatableConstant> DeferredTranslation<I> createHolder(ResourceKey<? extends Registry<TranslatableConstant>> registryKey, ResourceLocation key) {
        return DeferredTranslation.createTranslation(ResourceKey.create(registryKey, key));
    }
}
