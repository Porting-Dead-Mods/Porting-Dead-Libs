package com.portingdeadmods.portingdeadlibs.utils.metrics;

import com.portingdeadmods.portingdeadlibs.PortingDeadLibs;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.commands.Commands;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RenderGuiEvent;
import net.neoforged.neoforge.event.RegisterCommandsEvent;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;
import net.neoforged.neoforge.event.tick.ServerTickEvent;

import java.util.*;

public class Profiler {
    private static final Map<String, ProfilerInstance> INSTANCES = new HashMap<>();
    private static boolean enabled = false;
    private static boolean stressTest = false;
    private static final ProfilerInstance stressProfiler = create("StressTest", ChatFormatting.RED);

    public static ProfilerInstance create(String name, ChatFormatting color) {
        ProfilerInstance instance = new ProfilerInstance(name, color);
        INSTANCES.put(name, instance);
        return instance;
    }

    public static class ProfilerInstance {
        private final String name;
        private final ChatFormatting color;
        private long startTime;
        private final List<Long> measurements = new ArrayList<>();
        private double currentAvg = 0;

        private ProfilerInstance(String name, ChatFormatting color) {
            this.name = name;
            this.color = color;
        }

        public void start() {
            if (enabled) startTime = System.nanoTime();
        }

        public void end() {
            if (enabled && startTime != 0) {
                measurements.add(System.nanoTime() - startTime);
                if (measurements.size() > 100) measurements.remove(0);
                currentAvg = measurements.stream().mapToLong(l -> l).average().orElse(0) / 1_000_000.0;
            }
        }

        private String getStats() {
            return String.format("%s: %.2f ms", name, currentAvg);
        }
    }

    @EventBusSubscriber(modid = PortingDeadLibs.MODID)
    public static class ProfilerCommands {
        @SubscribeEvent
        public static void register(RegisterCommandsEvent event) {
            event.getDispatcher().register(Commands.literal("pdl")
                    .then(Commands.literal("profiler")
                            .then(Commands.literal("toggle")
                                    .executes(context -> {
                                        enabled = !enabled;
                                        return 1;
                                    }))));
        }
    }

    @EventBusSubscriber(modid = PortingDeadLibs.MODID, value = Dist.CLIENT)
    public static class ClientEvents {
        @SubscribeEvent
        public static void onRender(RenderGuiEvent.Post event) {
            if (!enabled) return;

            GuiGraphics graphics = event.getGuiGraphics();
            int y = 5;

            for (ProfilerInstance instance : INSTANCES.values()) {
                String text = instance.getStats();
                graphics.drawString(Minecraft.getInstance().font, text, 5, y, instance.color.getColor());
                y += 10;
            }
        }
    }
}