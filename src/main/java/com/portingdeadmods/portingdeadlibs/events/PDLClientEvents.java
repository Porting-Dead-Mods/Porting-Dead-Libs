package com.portingdeadmods.portingdeadlibs.events;

import com.portingdeadmods.portingdeadlibs.PortingDeadLibs;
import com.portingdeadmods.portingdeadlibs.networking.cache.AskServerPlayers;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ClientPlayerNetworkEvent;
import net.neoforged.neoforge.network.PacketDistributor;

@EventBusSubscriber(modid = PortingDeadLibs.MODID)
public class PDLClientEvents {
	@SubscribeEvent
	public static void onClientPlayerLoggingIn(ClientPlayerNetworkEvent.LoggingIn event) {
		PacketDistributor.sendToServer(AskServerPlayers.UNIT);
	}
}
