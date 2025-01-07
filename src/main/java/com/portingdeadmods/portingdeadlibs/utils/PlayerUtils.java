package com.portingdeadmods.portingdeadlibs.utils;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.world.entity.player.Player;

public final class PlayerUtils {
    public static void openScreen(Player player, Screen screen) {
        if (player.level().isClientSide) {
            Minecraft.getInstance().setScreen(screen);
        }
    }
}
