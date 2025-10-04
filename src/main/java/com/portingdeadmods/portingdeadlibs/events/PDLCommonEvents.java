package com.portingdeadmods.portingdeadlibs.events;

import com.portingdeadmods.portingdeadlibs.PortingDeadLibs;
import com.portingdeadmods.portingdeadlibs.cache.AllPlayersCache;
import com.portingdeadmods.portingdeadlibs.networking.cache.ReceiveServerPlayers;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.players.GameProfileCache;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.event.server.ServerAboutToStartEvent;
import net.neoforged.neoforge.network.PacketDistributor;

import java.util.List;

@EventBusSubscriber(modid = PortingDeadLibs.MODID)
public class PDLCommonEvents {
	@SubscribeEvent
	public static void populateAllPlayersCache(ServerAboutToStartEvent event) {
		MinecraftServer server = event.getServer();
		GameProfileCache cache = server.getProfileCache();
		if (cache != null) {
			for (GameProfileCache.GameProfileInfo profile : cache.profilesByName.values()) {
				AllPlayersCache.add(profile.getProfile().getId(), profile.getProfile().getName());
			}
		}
	}

	@SubscribeEvent
	public static void broadcastPlayerCache(PlayerEvent.PlayerLoggedInEvent event) {
		if (event.getEntity() instanceof ServerPlayer sp) {
			AllPlayersCache.add(sp.getGameProfile().getId(), sp.getGameProfile().getName());
			PacketDistributor.sendToAllPlayers(new ReceiveServerPlayers(List.of(sp.getGameProfile())));
		}
	}
}
