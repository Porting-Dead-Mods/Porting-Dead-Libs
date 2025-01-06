package com.portingdeadmods.portingdeadlibs;

import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import org.slf4j.Logger;

import com.mojang.logging.LogUtils;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import com.portingdeadmods.portingdeadlibs.utils.RegisteringUtils;

@Mod(PortingDeadLibs.MODID)
public final class PortingDeadLibs {
    public static final String MODID = "portingdeadlibs";
    public static final Logger LOGGER = LogUtils.getLogger();

    public PortingDeadLibs(IEventBus modEventBus, ModContainer modContainer) {
        modEventBus.addListener(this::registerCapabilities);
    }

    private void registerCapabilities(RegisterCapabilitiesEvent event) {
        RegisteringUtils.registerLibCapabilities(event);
    }
}
