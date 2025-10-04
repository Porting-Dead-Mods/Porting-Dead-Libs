package com.portingdeadmods.portingdeadlibs.networking;

import com.portingdeadmods.portingdeadlibs.PDLRegistries;
import com.portingdeadmods.portingdeadlibs.PortingDeadLibs;
import com.portingdeadmods.portingdeadlibs.api.data.saved.PDLSavedData;
import com.portingdeadmods.portingdeadlibs.api.data.saved.SavedDataHolder;
import com.portingdeadmods.portingdeadlibs.networking.cache.AskServerPlayers;
import com.portingdeadmods.portingdeadlibs.networking.cache.ReceiveServerPlayers;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;

@EventBusSubscriber(modid = PortingDeadLibs.MODID)
public class NetworkEvents {
	@SubscribeEvent
	public static void registerPayloads(final RegisterPayloadHandlersEvent event) {
		final PayloadRegistrar registrar = event.registrar(PortingDeadLibs.MODID);
		registrar.playToServer(
				RedstoneSignalTypeSyncPayload.TYPE,
				RedstoneSignalTypeSyncPayload.STREAM_CODEC,
				RedstoneSignalTypeSyncPayload::handle
		);

		registrar.playToServer(
				AskServerPlayers.TYPE,
				AskServerPlayers.STREAM_CODEC,
				AskServerPlayers::ping
		);
		registrar.playToClient(
				ReceiveServerPlayers.TYPE,
				ReceiveServerPlayers.STREAM_CODEC,
				ReceiveServerPlayers::pong
		);

		for (PDLSavedData<?> savedData : PDLRegistries.SAVED_DATA) {
			SavedDataHolder<?> holder = SavedDataHolder.fromValue(savedData);
			registrar.playToClient(
					SyncSavedDataToClientPayload.type(holder),
					SyncSavedDataToClientPayload.streamCodec(holder),
					SyncSavedDataToClientPayload::handle
			);
			registrar.playToServer(
					SyncSavedDataToServerPayload.type(holder),
					SyncSavedDataToServerPayload.streamCodec(holder),
					SyncSavedDataToServerPayload::handle
			);
		}
	}
}
