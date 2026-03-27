package com.portingdeadmods.portingdeadlibs.events;

import com.portingdeadmods.portingdeadlibs.PortingDeadLibs;
import com.portingdeadmods.portingdeadlibs.cache.AllPlayersCache;
import com.portingdeadmods.portingdeadlibs.networking.cache.ReceiveServerPlayers;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.players.NameAndId;
import net.minecraft.server.players.UserNameToIdResolver;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.event.server.ServerAboutToStartEvent;
import net.neoforged.neoforge.network.PacketDistributor;

import java.util.List;
import java.util.Optional;

@EventBusSubscriber(modid = PortingDeadLibs.MODID)
public class PDLCommonEvents {
	@SubscribeEvent
	public static void populateAllPlayersCache(ServerAboutToStartEvent event) {
		MinecraftServer server = event.getServer();
		UserNameToIdResolver nameToIdResolver = server.services().nameToIdCache();

		for (String player : server.getPlayerNames()) {
			Optional<NameAndId> _nameAndId = nameToIdResolver.get(player);
			_nameAndId.ifPresent(nameAndId -> {
				AllPlayersCache.add(nameAndId.id(), nameAndId.name());
			});
		}
	}

	@SubscribeEvent
	public static void broadcastPlayerCache(PlayerEvent.PlayerLoggedInEvent event) {
		if (event.getEntity() instanceof ServerPlayer sp) {
			AllPlayersCache.add(sp.getGameProfile().id(), sp.getGameProfile().name());
			PacketDistributor.sendToAllPlayers(new ReceiveServerPlayers(List.of(sp.getGameProfile())));
		}
	}
}
