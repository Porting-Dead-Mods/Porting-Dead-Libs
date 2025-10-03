package com.portingdeadmods.portingdeadlibs;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.portingdeadmods.portingdeadlibs.api.data.PDLDataComponents;
import com.portingdeadmods.portingdeadlibs.api.data.saved.PDLSavedData;
import com.portingdeadmods.portingdeadlibs.api.data.saved.SavedDataHolder;
import com.portingdeadmods.portingdeadlibs.networking.RedstoneSignalTypeSyncPayload;
import com.portingdeadmods.portingdeadlibs.networking.SyncSavedDataToClientPayload;
import com.portingdeadmods.portingdeadlibs.networking.SyncSavedDataToServerPayload;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;
import net.neoforged.neoforge.registries.NewRegistryEvent;
import org.slf4j.Logger;

import com.mojang.logging.LogUtils;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;

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
        modEventBus.addListener(this::registerPayloads);

        PDLDataComponents.DATA_COMPONENT_TYPES.register(modEventBus);

    }

    public static ResourceLocation rl(String path) {
        return ResourceLocation.fromNamespaceAndPath(MODID, path);
    }

    private void registerRegistries(NewRegistryEvent event) {
        event.register(PDLRegistries.MULTIBLOCK);
        event.register(PDLRegistries.TRANSLATION);
        event.register(PDLRegistries.SAVED_DATA);
    }

    private void registerPayloads(RegisterPayloadHandlersEvent event) {
        PayloadRegistrar registrar = event.registrar(MODID);
        registrar.playToServer(RedstoneSignalTypeSyncPayload.TYPE, RedstoneSignalTypeSyncPayload.STREAM_CODEC, RedstoneSignalTypeSyncPayload::handle);
        for (PDLSavedData<?> savedData : PDLRegistries.SAVED_DATA) {
            SavedDataHolder<?> holder = SavedDataHolder.fromValue(savedData);
            registrar.playToClient(SyncSavedDataToClientPayload.type(holder), SyncSavedDataToClientPayload.streamCodec(holder), SyncSavedDataToClientPayload::handle);
            registrar.playToServer(SyncSavedDataToServerPayload.type(holder), SyncSavedDataToServerPayload.streamCodec(holder), SyncSavedDataToServerPayload::handle);
        }
    }

}
