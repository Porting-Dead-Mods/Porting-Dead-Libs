package com.portingdeadmods.portingdeadlibs.api.client.screens.widgets;

import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.input.MouseButtonEvent;
import net.minecraft.network.chat.Component;
import org.jspecify.annotations.NonNull;

public abstract class AbstractDraggableWidget extends AbstractWidget {
    private boolean isHovered;
    private boolean updateIsHovered = true;

    public AbstractDraggableWidget(int x, int y, int width, int height, Component message) {
        super(x, y, width, height, message);
    }

    @Override
    protected void extractWidgetRenderState(GuiGraphicsExtractor guiGraphics, int i, int i1, float v) {
        if (this.updateIsHovered) {
            this.isHovered = this.isRectHovered(guiGraphics, i, i1, this.getWidth(), 12);
        }
    }

    @Override
    protected void updateWidgetNarration(NarrationElementOutput narrationElementOutput) {
    }

    @Override
    protected void onDrag(MouseButtonEvent event, double dx, double dy) {
        super.onDrag(event, dx, dy);

        this.updateIsHovered = false;

        if (this.isHovered) {
            this.setPosition(getX() + (int) dx, getY() + (int) dy);
            this.onMoved();
        }
    }

    protected void onMoved() {
    }

    @Override
    public void onRelease(@NonNull MouseButtonEvent event) {
        super.onRelease(event);

        this.updateIsHovered = true;
        this.isHovered = false;
    }

    public boolean isLazyHovered() {
        return this.isHovered;
    }

    protected boolean isRectHovered(GuiGraphicsExtractor guiGraphics, int mouseX, int mouseY, int width, int height) {
        return guiGraphics.containsPointInScissor(mouseX, mouseY)
                && mouseX >= this.getX()
                && mouseY >= this.getY()
                && mouseX < this.getX() + width
                && mouseY < this.getY() + height;
    }

}