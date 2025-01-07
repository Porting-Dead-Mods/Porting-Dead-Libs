package com.portingdeadmods.portingdeadlibs;

import net.minecraft.resources.ResourceLocation;
import org.slf4j.Logger;

import com.mojang.logging.LogUtils;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;

@Mod(PortingDeadLibs.MODID)
public final class PortingDeadLibs {
    public static final String MODID = "portingdeadlibs";
    public static final Logger LOGGER = LogUtils.getLogger();

    public PortingDeadLibs(IEventBus modEventBus, ModContainer modContainer) {
    }

    public static ResourceLocation rl(String path) {
        return ResourceLocation.fromNamespaceAndPath(MODID, path);
    }
}
