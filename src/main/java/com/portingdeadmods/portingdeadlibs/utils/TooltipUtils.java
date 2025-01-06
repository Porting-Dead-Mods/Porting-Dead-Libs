package com.portingdeadmods.portingdeadlibs.utils;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;

import java.util.List;

public final class TooltipUtils {
    public static void trans(List<Component> components, String key, ChatFormatting... formatings) {
        tt(components, Component.translatable(key), formatings);
    }

    public static void transInsert(List<Component> components, String key, String additional, ChatFormatting... formatings) {
        MutableComponent trans = apply(Component.translatable(key), formatings);
        MutableComponent lit = apply(Component.literal(additional), formatings);
        components.add(trans.append(lit));
    }

    public static void transtrans(List<Component> components, String key, String additional, ChatFormatting... formatings) {
        MutableComponent trans = apply(Component.translatable(key), formatings);
        MutableComponent extra = apply(Component.translatable(additional), formatings);
        components.add(trans.append(extra));
    }

    public static void tt(List<Component> components, MutableComponent c, ChatFormatting... formatings) {
        apply(c, formatings);
        components.add(c);
    }

    public static MutableComponent apply(MutableComponent component, ChatFormatting... formattings) {
        for (ChatFormatting formatting : formattings) {
            component = component.withStyle(formatting);
        }
        return component;
    }
}
