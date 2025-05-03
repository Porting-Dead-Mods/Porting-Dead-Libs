package com.portingdeadmods.portingdeadlibs.api.client.data;

import net.minecraft.resources.ResourceLocation;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class PDLClientSavedData {
    public static final Map<ResourceLocation, Object> CLIENT_SAVED_DATA_CACHE = new ConcurrentHashMap<>();
}
