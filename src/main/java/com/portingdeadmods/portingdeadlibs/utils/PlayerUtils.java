package com.portingdeadmods.portingdeadlibs.utils;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

import java.util.UUID;

public final class PlayerUtils {
    @Deprecated
    public static void openScreen(Player player, Screen screen) {
        if (player.level().isClientSide) {
            Minecraft.getInstance().setScreen(screen);
        }
    }

    public static final UUID EmptyUUID = UUID.fromString("00000000-0000-0000-0000-000000000000");
    public static Player getPlayerFromString(Level level, String name) {
        return level.getServer().getPlayerList().getPlayerByName(name);
    }

    public static UUID getPlayerUUIDFromName(Level level, String name) {
        return getPlayerFromString(level, name).getUUID();
    }

    public static String getPlayerNameFromUUID(Level level, UUID uuid) {
        return level.getPlayerByUUID(uuid).getName().getString();
    }

}
