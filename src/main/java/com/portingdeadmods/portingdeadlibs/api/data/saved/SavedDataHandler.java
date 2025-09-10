package com.portingdeadmods.portingdeadlibs.api.data.saved;

import com.portingdeadmods.portingdeadlibs.PDLRegistries;
import com.portingdeadmods.portingdeadlibs.PortingDeadLibs;
import com.portingdeadmods.portingdeadlibs.api.client.data.PDLClientSavedData;
import com.portingdeadmods.portingdeadlibs.networking.SyncSavedDataPayload;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.network.PacketDistributor;

import java.util.Map;

@EventBusSubscriber(modid = PortingDeadLibs.MODID, value = Dist.CLIENT)
public final class SavedDataHandler {
    @SubscribeEvent
    private static void onJoinWorld(PlayerEvent.PlayerLoggedInEvent event) {
        if (event.getEntity() instanceof ServerPlayer serverPlayer) {
            for (Map.Entry<ResourceKey<PDLSavedData<?>>, PDLSavedData<?>> savedData : PDLRegistries.SAVED_DATA.entrySet()) {
                PDLSavedData<?> value = savedData.getValue();
                if (value.isSynced()) {
                    value.preSyncFunction().accept(serverPlayer);
                    sendSavedDataSyncPayload(serverPlayer, savedData.getKey().location(), value);
                    value.postSyncFunction().accept(serverPlayer);
                }
            }
        }
    }

    @SubscribeEvent
    private static void onLeaveWorld(PlayerEvent.PlayerLoggedOutEvent event) {
        PDLClientSavedData.CLIENT_SAVED_DATA_CACHE.clear();
    }

    private static <T> void sendSavedDataSyncPayload(ServerPlayer serverPlayer, ResourceLocation id, PDLSavedData<?> savedData) {
        PDLSavedData<T> savedData1 = (PDLSavedData<T>) savedData;
        T data = savedData1.getData(serverPlayer.serverLevel());
        PacketDistributor.sendToPlayer(serverPlayer, new SyncSavedDataPayload<>(new SavedDataHolder<>(id, savedData1), data));
    }

}
