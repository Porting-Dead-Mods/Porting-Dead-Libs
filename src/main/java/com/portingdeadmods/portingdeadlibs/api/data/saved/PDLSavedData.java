package com.portingdeadmods.portingdeadlibs.api.data.saved;

import com.mojang.serialization.Codec;
import com.portingdeadmods.portingdeadlibs.PDLRegistries;
import com.portingdeadmods.portingdeadlibs.api.client.data.PDLClientSavedData;
import com.portingdeadmods.portingdeadlibs.networking.SyncSavedDataToClientPayload;
import com.portingdeadmods.portingdeadlibs.networking.SyncSavedDataToServerPayload;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.ServerboundPongPacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.network.PacketDistributor;

import java.util.function.Consumer;
import java.util.function.Supplier;

// TODO: Migration codecs
public final class PDLSavedData<T> {
    private final Supplier<T> defaultValueSupplier;
    private final Codec<T> codec;
    private final StreamCodec<? super RegistryFriendlyByteBuf, T> streamCodec;
    private final Consumer<Player> preSyncFunction;
    private final Consumer<Player> postSyncFunction;
    private final boolean global;

    private PDLSavedData(Builder<T> builder) {
        this.codec = builder.codec;
        this.streamCodec = builder.streamCodec;
        this.defaultValueSupplier = builder.defaultValueSupplier;
        this.preSyncFunction = builder.preSyncFunction;
        this.postSyncFunction = builder.postSyncFunction;
        this.global = builder.global;
    }

    public Codec<T> codec() {
        return codec;
    }

    public StreamCodec<? super RegistryFriendlyByteBuf, T> streamCodec() {
        return streamCodec;
    }

    public Supplier<T> defaultValueSupplier() {
        return defaultValueSupplier;
    }

    public Consumer<Player> preSyncFunction() {
        return preSyncFunction;
    }

    public Consumer<Player> postSyncFunction() {
        return postSyncFunction;
    }

    public boolean isGlobal() {
        return global;
    }

    /**
     * @return If it is synced to the player on load
     */
    public boolean isSynced() {
        return streamCodec != null;
    }

    public void syncToPlayer(ServerPlayer player) {
        if (!isSynced()) return;

        T data = getData(player.level());

        PacketDistributor.sendToPlayer(player, new SyncSavedDataToClientPayload<>(new SavedDataHolder<>(PDLRegistries.SAVED_DATA.getKey(this), this), data));
    }

    public void sync(Level level) {
        if (!isSynced()) return;

        T data = getData(level);

        if (level.isClientSide()) {
            PacketDistributor.sendToServer(new SyncSavedDataToServerPayload<>(new SavedDataHolder<>(PDLRegistries.SAVED_DATA.getKey(this), this), data));
        } else {
            PacketDistributor.sendToAllPlayers(new SyncSavedDataToClientPayload<>(new SavedDataHolder<>(PDLRegistries.SAVED_DATA.getKey(this), this), data));
        }
    }

    public void setData(Level level, T data) {
        if (level instanceof ServerLevel serverLevel0) {
            ServerLevel serverLevel;
            if (isGlobal()) {
                serverLevel = serverLevel0.getServer().overworld();
            } else {
                serverLevel = serverLevel0;
            }
            ResourceLocation location = PDLRegistries.SAVED_DATA.getKey(this);
            if (location != null) {
                SavedDataWrapper.setData(new SavedDataHolder<>(location, this), serverLevel, data);
            }
        } else {
            ResourceLocation key = PDLRegistries.SAVED_DATA.getKey(this);
            if (key != null) {
                PDLClientSavedData.CLIENT_SAVED_DATA_CACHE.put(key, data);
            }
        }
    }

    public T getData(Level level) {
        if (level instanceof ServerLevel serverLevel0) {
            ServerLevel serverLevel;
            if (isGlobal()) {
                serverLevel = serverLevel0.getServer().overworld();
            } else {
                serverLevel = serverLevel0;
            }
            ResourceLocation location = PDLRegistries.SAVED_DATA.getKey(this);
            if (location != null) {
                return SavedDataWrapper.getData(new SavedDataHolder<>(location, this), serverLevel);
            }
        } else {
            ResourceLocation location = PDLRegistries.SAVED_DATA.getKey(this);
            if (location != null) {
                return (T) PDLClientSavedData.CLIENT_SAVED_DATA_CACHE.get(location);
            }
        }
        return null;
    }

    public static <T> Builder<T> builder(Codec<T> codec, Supplier<T> defaultValueSupplier) {
        return new Builder<>(codec, defaultValueSupplier);
    }

    public static final class Builder<T> {
        private final Supplier<T> defaultValueSupplier;
        private final Codec<T> codec;
        private Consumer<Player> preSyncFunction;
        private Consumer<Player> postSyncFunction;
        private StreamCodec<? super RegistryFriendlyByteBuf, T> streamCodec;
        private boolean global = false;

        private Builder(Codec<T> codec, Supplier<T> defaultValueSupplier) {
            this.defaultValueSupplier = defaultValueSupplier;
            this.codec = codec;
        }

        /**
         * @param streamCodec Encoder/Decoder
         * @return Should sync on player login
         */
        public Builder<T> synced(StreamCodec<? super RegistryFriendlyByteBuf, T> streamCodec) {
            this.streamCodec = streamCodec;
            return this;
        }

        /**
         * The supplied method gets ran before saved data is synced.
         * This method gets executed on both sides
         */
        public Builder<T> preSync(Consumer<Player> preSyncFunction) {
            this.preSyncFunction = preSyncFunction;
            return this;
        }

        /**
         * The supplied method gets ran after saved data was synced.
         * This method gets executed on both sides
         */
        public Builder<T> postSync(Consumer<Player> postSyncFunction) {
            this.postSyncFunction = postSyncFunction;
            return this;
        }

        /**
         * Makes the saved data global, instead of dimension based
         */
        public Builder<T> global() {
            this.global = true;
            return this;
        }

        public PDLSavedData<T> build() {
            if (this.preSyncFunction != null && this.postSyncFunction != null && this.streamCodec == null) {
                throw new RuntimeException("Cannot provide sync functions for a saved data without a stream codec");
            }

            if (this.streamCodec != null) {
                if (this.preSyncFunction == null) {
                    this.preSyncFunction = p -> {
                    };
                }

                if (this.postSyncFunction == null) {
                    this.postSyncFunction = p -> {
                    };
                }

            }

            return new PDLSavedData<>(this);
        }

    }
}