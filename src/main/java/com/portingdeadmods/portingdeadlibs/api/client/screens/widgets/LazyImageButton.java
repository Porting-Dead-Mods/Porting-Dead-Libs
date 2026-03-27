package com.portingdeadmods.portingdeadlibs.api.client.screens.widgets;

import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.AbstractButton;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.input.InputWithModifiers;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;

import java.util.function.Consumer;

public class LazyImageButton extends AbstractButton {
    private final Identifier sprite;
    private final int spriteWidth;
    private final int spriteHeight;
    private final Consumer<LazyImageButton> onPressFunction;

    public LazyImageButton(Identifier sprite, int spriteWidth, int spriteHeight, int x, int y, int width, int height, Consumer<LazyImageButton> onPressFunction) {
        super(x, y, width, height, Component.empty());
        this.sprite = sprite;
        this.spriteWidth = spriteWidth;
        this.spriteHeight = spriteHeight;
        this.onPressFunction = onPressFunction;
    }

    @Override
    protected void extractContents(GuiGraphicsExtractor guiGraphics, int i, int i1, float v) {
        this.extractDefaultSprite(guiGraphics);

        int paddingX = (this.getWidth() - this.spriteWidth) / 2;
        int paddingY = (this.getHeight() - this.spriteHeight) / 2;

        guiGraphics.blitSprite(RenderPipelines.GUI_TEXTURED, this.sprite, getX() + paddingX, getY() + paddingY, this.spriteWidth, this.spriteHeight);

    }

    @Override
    public void onPress(InputWithModifiers inputWithModifiers) {
        onPressFunction.accept(this);
    }

    @Override
    protected void updateWidgetNarration(NarrationElementOutput narrationElementOutput) {
    }
}