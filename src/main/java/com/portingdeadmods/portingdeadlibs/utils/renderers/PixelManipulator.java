package com.portingdeadmods.portingdeadlibs.utils.renderers;

@FunctionalInterface
public interface PixelManipulator {
    int manipulate(int x, int y, int originalColor);
}