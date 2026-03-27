package com.portingdeadmods.portingdeadlibs.networking.cache;

import com.mojang.authlib.GameProfile;
import com.portingdeadmods.portingdeadlibs.PortingDeadLibs;
import com.portingdeadmods.portingdeadlibs.utils.UniqueArray;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.players.UserNameToIdResolver;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class AskServerPlayers implements CustomPacketPayload {
    public static final AskServerPlayers UNIT = new AskServerPlayers();

    private AskServerPlayers() {
    }

    ;

    public static final Type<AskServerPlayers> TYPE = new Type<>(PortingDeadLibs.rl("ask_server_players"));
    public static final StreamCodec<RegistryFriendlyByteBuf, AskServerPlayers> STREAM_CODEC = StreamCodec.unit(UNIT);

    @Override
    public @NotNull Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static void ping(AskServerPlayers payload, IPayloadContext context) {
        context.enqueueWork(() -> {
            if (context.player() instanceof ServerPlayer sp) {
                List<GameProfile> profiles = new UniqueArray<>();
                MinecraftServer server = sp.level().getServer();
                UserNameToIdResolver nameToIdResolver = server.services().nameToIdCache();
                for (String name : server.getPlayerNames()) {
                    nameToIdResolver.get(name).ifPresent(p -> profiles.add(new GameProfile(p.id(), p.name())));
                }
                PacketDistributor.sendToPlayer(sp, new ReceiveServerPlayers(profiles));
            }
        }).exceptionally(e -> {
            context.disconnect(Component.literal("Failed to handle AskServerPlayers" + e.getMessage()));
            return null;
        });

    }
}