package com.portingdeadmods.portingdeadlibs.events;


import com.portingdeadmods.portingdeadlibs.PortingDeadLibs;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.server.ServerAboutToStartEvent;

@EventBusSubscriber(modid = PortingDeadLibs.MODID, value = Dist.DEDICATED_SERVER)
public final class PDLServerEvents {
	@SubscribeEvent
	public static void onServerAboutToStart(ServerAboutToStartEvent event) {
		// Pass
	}
}

