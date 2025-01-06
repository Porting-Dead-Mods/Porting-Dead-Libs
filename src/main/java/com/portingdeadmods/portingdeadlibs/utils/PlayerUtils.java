package com.portingdeadmods.portingdeadlibs.utils;

import com.portingdeadmods.nautec.client.screen.AugmentationStationScreen;
import com.portingdeadmods.nautec.content.blockentities.multiblock.controller.AugmentationStationBlockEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;

public final class PlayerUtils {
    public static void openAugmentationStationScreen(Player player, AugmentationStationBlockEntity be, Component title) {
        openScreen(player, new AugmentationStationScreen(be, player, title));
    }

    public static void openScreen(Player player, Screen screen) {
        if (player.level().isClientSide) {
            Minecraft.getInstance().setScreen(screen);
        }
    }
}
