package com.portingdeadmods.portingdeadlibs;

import com.portingdeadmods.portingdeadlibs.api.multiblocks.Multiblock;
import com.portingdeadmods.portingdeadlibs.api.translations.TranslatableConstant;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.neoforged.neoforge.registries.RegistryBuilder;

public class PDLRegistries {
    public static final ResourceKey<Registry<Multiblock>> MULTIBLOCK_KEY = ResourceKey.createRegistryKey(PortingDeadLibs.rl("multiblock"));
    public static final Registry<Multiblock> MULTIBLOCK = new RegistryBuilder<>(MULTIBLOCK_KEY).create();

    public static final ResourceKey<Registry<TranslatableConstant>> TRANSLATION_KEY = ResourceKey.createRegistryKey(PortingDeadLibs.rl("translation"));
    public static final Registry<TranslatableConstant> TRANSLATION = new RegistryBuilder<>(TRANSLATION_KEY).create();
}
