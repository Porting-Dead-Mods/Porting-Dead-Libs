package com.portingdeadmods.portingdeadlibs.networking;

import com.portingdeadmods.portingdeadlibs.PortingDeadLibs;
import com.portingdeadmods.portingdeadlibs.api.client.data.PDLClientSavedData;
import com.portingdeadmods.portingdeadlibs.api.data.saved.SavedDataHolder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import org.jetbrains.annotations.NotNull;

public record SyncSavedDataToClientPayload<T>(SavedDataHolder<T> holder, T value) implements CustomPacketPayload {
    @Override
    public @NotNull Type<? extends CustomPacketPayload> type() {
        return type(holder);
    }

    public void handle(IPayloadContext context) {
        context.enqueueWork(() -> {
            holder.value().preSyncFunction().accept(context.player());
            PDLClientSavedData.CLIENT_SAVED_DATA_CACHE.put(holder.key(), value);
            holder.value().postSyncFunction().accept(context.player());
        }).exceptionally(err -> {
            PortingDeadLibs.LOGGER.error("Failed to handle SyncSavedDataPayload", err);
            return null;
        });
    }

    public static <T> Type<SyncSavedDataToClientPayload<T>> type(SavedDataHolder<T> dataHolder) {
        return new Type<>(dataHolder.key().withPrefix("sync_").withSuffix("_to_client_payload"));
    }

    private static <T> SyncSavedDataToClientPayload<T> untyped(SavedDataHolder<?> network, T value) {
        return new SyncSavedDataToClientPayload<>((SavedDataHolder<T>) network, value);
    }

    public static <T> StreamCodec<RegistryFriendlyByteBuf, SyncSavedDataToClientPayload<T>> streamCodec(SavedDataHolder<?> dataHolder) {
        return StreamCodec.composite(
                SavedDataHolder.STREAM_CODEC,
                SyncSavedDataToClientPayload::holder,
                ((SavedDataHolder<T>) dataHolder).value().streamCodec(),
                SyncSavedDataToClientPayload::value,
                SyncSavedDataToClientPayload::untyped
        );
    }




}