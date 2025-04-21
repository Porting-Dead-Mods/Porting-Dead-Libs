package com.portingdeadmods.portingdeadlibs.api.translations;

import com.portingdeadmods.portingdeadlibs.PDLRegistries;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Function;
import java.util.function.Supplier;

public class DeferredTranslationRegister extends DeferredRegister<TranslatableConstant> {
    protected DeferredTranslationRegister(String namespace) {
        super(PDLRegistries.TRANSLATION_KEY, namespace);
    }

    public static DeferredTranslationRegister createTranslations(String namespace) {
        return new DeferredTranslationRegister(namespace);
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
