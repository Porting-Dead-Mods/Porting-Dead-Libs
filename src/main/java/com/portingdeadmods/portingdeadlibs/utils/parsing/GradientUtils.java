package com.portingdeadmods.portingdeadlibs.utils.parsing;

import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.TextColor;

import java.awt.Color;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Utility class for parsing and creating gradient text effects with Minecraft formatting codes.
 * Supports RGB colors and vanilla Minecraft formatting (bold, italic, underline, strikethrough).
 */
public class GradientUtils {
    /**
     * Pattern matching color codes {@literal (&#RRGGBB)} and formatting codes (&[lonm]).
     * - Group 1: Formatting code (l, o, n, m)
     * - Group 2: Hex color (RRGGBB)
     */
    private static final Pattern FORMAT_PATTERN = Pattern.compile("&([lonmk])|&#([0-9A-Fa-f]{6})");

    /**
     * Parses a string containing color codes and formatting codes into a Minecraft Component.
     * Supports hex color codes {@literal (&#RRGGBB)} and formatting codes {@literal (&l, &o, &n, &m)}.
     *
     * <p>Example usage:</p>
     * <pre>{@code
     * String text = "&#FF0000&l&oRed Bold Italic&#00FF00Normal Green";
     * Component result = GradientUtils.parseFormatting(text);
     * }</pre>
     *
     * @param input The string to parse, containing color and formatting codes
     * @return A Component with the applied colors and formatting
     *
     */
    public static Component parseFormatting(String input) {
        MutableComponent result = Component.empty();
        Matcher matcher = FORMAT_PATTERN.matcher(input);

        boolean isBold = false;
        boolean isItalic = false;
        boolean isUnderline = false;
        boolean isStrikethrough = false;
        Color currentColor = null;
        StringBuilder currentText = new StringBuilder();
        int lastEnd = 0;

        while (matcher.find()) {
            if (matcher.start() > lastEnd) {
                String text = input.substring(lastEnd, matcher.start());
                if (!text.isEmpty()) {
                    currentText.append(text);
                }
            }

            String formatCode = matcher.group(1);
            String colorCode = matcher.group(2);

            if (formatCode != null) {
                switch (formatCode) {
                    case "l" -> isBold = true;
                    case "o" -> isItalic = true;
                    case "n" -> isUnderline = true;
                    case "m" -> isStrikethrough = true;
                    case "k" -> { /* Obfuscated - not implemented */ }
                }
            } else if (colorCode != null) {

                if (currentText.length() > 0) {
                    boolean finalIsBold = isBold;
                    boolean finalIsItalic = isItalic;
                    boolean finalIsUnderline = isUnderline;
                    boolean finalIsStrikethrough = isStrikethrough;
                    Color finalCurrentColor = currentColor;
                    result.append(Component.literal(currentText.toString())
                            .withStyle(style -> style
                                    .withBold(finalIsBold)
                                    .withItalic(finalIsItalic)
                                    .withUnderlined(finalIsUnderline)
                                    .withStrikethrough(finalIsStrikethrough)
                                    .withColor(finalCurrentColor != null ? TextColor.fromRgb(finalCurrentColor.getRGB()) : null)));
                    currentText.setLength(0);
                }
                currentColor = Color.decode("#" + colorCode);
            }

            lastEnd = matcher.end();
        }

        if (lastEnd < input.length() || currentText.length() > 0) {
            currentText.append(input.substring(lastEnd));
            boolean finalIsBold1 = isBold;
            boolean finalIsItalic1 = isItalic;
            boolean finalIsUnderline1 = isUnderline;
            boolean finalIsStrikethrough1 = isStrikethrough;
            Color finalCurrentColor1 = currentColor;
            result.append(Component.literal(currentText.toString())
                    .withStyle(style -> style
                            .withBold(finalIsBold1)
                            .withItalic(finalIsItalic1)
                            .withUnderlined(finalIsUnderline1)
                            .withStrikethrough(finalIsStrikethrough1)
                            .withColor(finalCurrentColor1 != null ? TextColor.fromRgb(finalCurrentColor1.getRGB()) : null)));
        }

        return result;
    }
}
