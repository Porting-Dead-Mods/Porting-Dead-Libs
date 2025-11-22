package com.portingdeadmods.portingdeadlibs;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mojang.logging.LogUtils;
import com.portingdeadmods.portingdeadlibs.api.config.PDLConfigHelper;
import com.portingdeadmods.portingdeadlibs.api.data.PDLDataComponents;
import com.portingdeadmods.portingdeadlibs.example.ExampleConfig;
import com.portingdeadmods.portingdeadlibs.example.ExampleRegistries;
import net.minecraft.SharedConstants;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.neoforge.registries.NewRegistryEvent;
import org.slf4j.Logger;

@Mod(PortingDeadLibs.MODID)
public final class PortingDeadLibs {
    public static final String MODID = "portingdeadlibs";
    public static final Logger LOGGER = LogUtils.getLogger();
    public static final Gson GSON = new GsonBuilder()
            .setPrettyPrinting()
            .disableHtmlEscaping()
            .create();

    public PortingDeadLibs(IEventBus modEventBus, ModContainer modContainer) {
        modEventBus.addListener(this::registerRegistries);

        PDLDataComponents.DATA_COMPONENT_TYPES.register(modEventBus);

        if (SharedConstants.IS_RUNNING_IN_IDE) {
            ExampleRegistries.ITEMS.register(modEventBus);
            ExampleRegistries.BLOCKS.register(modEventBus);
            ExampleRegistries.BLOCK_ENTITIES.register(modEventBus);

            PDLConfigHelper.registerConfig(ExampleConfig.class, ModConfig.Type.COMMON, modContainer);
        }

    }

    public static ResourceLocation rl(String path) {
        return ResourceLocation.fromNamespaceAndPath(MODID, path);
    }

    private void registerRegistries(NewRegistryEvent event) {
        event.register(PDLRegistries.MULTIBLOCK);
        event.register(PDLRegistries.TRANSLATION);
        event.register(PDLRegistries.SAVED_DATA);
    }
}
