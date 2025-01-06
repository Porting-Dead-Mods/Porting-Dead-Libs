package com.portingdeadmods.portingdeadlibs.utils;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;

public final class ComponentUtils {
    public static MutableComponent colored(String text, ChatFormatting col) {
        return Component.literal(text).withStyle(col);
    }

    public static MutableComponent countableStatShow(String name, Number val, Number max) {
        MutableComponent mc1 = colored(name, ChatFormatting.YELLOW)
                .append(Component.literal(": ").withStyle(ChatFormatting.WHITE));

        if (val.doubleValue() < max.doubleValue()) {
            mc1.append(colored(MathUtils.roundToPrecision(val.floatValue(), 2).toString(), ChatFormatting.GREEN));
        } else {
            mc1.append(colored(MathUtils.roundToPrecision(val.floatValue(), 2).toString(), ChatFormatting.RED));
        }

        return mc1;
    }

    public static MutableComponent stringStatShow(String name, String val) {
        return colored(name, ChatFormatting.YELLOW)
                .append(Component.literal(": ").withStyle(ChatFormatting.WHITE))
                .append(colored(val, ChatFormatting.GREEN));
    }

    public static MutableComponent statRange(Number val1, Number val2, Number max) {
        if (val2.floatValue() >= max.floatValue()) {
            return
            colored(val1.toString(), ChatFormatting.WHITE)
                    .append(Component.literal(" - ").withStyle(ChatFormatting.YELLOW))
                    .append(colored(max.toString(), ChatFormatting.RED));
        } else if (val1.floatValue() <= 0) {
            return
            colored("0", ChatFormatting.RED)
                    .append(Component.literal(" - ").withStyle(ChatFormatting.YELLOW))
                    .append(colored(val2.toString(), ChatFormatting.WHITE));
        } else {
            return
            colored(val1.toString(), ChatFormatting.WHITE)
                    .append(Component.literal(" - ").withStyle(ChatFormatting.YELLOW))
                    .append(colored(val2.toString(), ChatFormatting.WHITE));
        }
    }
}
