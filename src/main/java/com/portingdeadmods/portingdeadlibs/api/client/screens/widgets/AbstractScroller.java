package com.portingdeadmods.portingdeadlibs.api.client.screens.widgets;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

public abstract class AbstractScroller extends AbstractWidget {
    private final Screen parentScreen;
    private final ResourceLocation sprite;
    private final int trackLength;
    private final AbstractScroller.Mode mode;
    private final int padding;

    private int offsetY;
    private int offsetX;

    private float scrollPercentage = 0.0f;
    private int scrollOffset = 0;

    private boolean mbDown = false;

    /**
     * @param parentScreen The parent screen of the scroller
     * @param x X position of the Top-Left corner of the scroller texture
     * @param y Y position of the Top-Left corner of the scroller texture
     * @param width Width of the scroller texture
     * @param height Height of the scroller texture
     * @param trackLength The track length of the scroller
     * @param mode Orientation of the scroller
     * @param sprite The texture of the scroller - It's recommended the texture to have an odd size (centerable to 1 px. Width for Horizontal - Horizontal for Vertical)
     */
    public AbstractScroller(Screen parentScreen, int x, int y, int width, int height, int trackLength, AbstractScroller.Mode mode, ResourceLocation sprite) {
        super(x, y, width, height, Component.empty());

        this.parentScreen = parentScreen;

        this.trackLength = trackLength;
        this.sprite = sprite;
        this.mode = mode;

        if (this.mode == Mode.VERTICAL) {
            this.padding = height / 2 + 1;
        } else {
            this.padding = width / 2 + 1;
        }
    }

    public void renderWidget(GuiGraphics guiGraphics) {
        this.renderWidget(guiGraphics, 0, 0, 0);
    }

    @Override
    public void renderWidget(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        this.offsetX = this.parentScreen.getRectangle().left() + this.getX();
        this.offsetY = this.parentScreen.getRectangle().top() + this.getY();

        if (this.mode == Mode.VERTICAL) {
            guiGraphics.blitSprite(this.sprite, this.offsetX, this.offsetY + (int) (this.scrollPercentage * this.trackLength), this.width, this.height);
        } else {
            guiGraphics.blitSprite(this.sprite, this.offsetX + (int) (this.scrollPercentage * this.trackLength), this.offsetY, this.width, this.height);
        }

        if (this.mbDown)
            this.updatePos(mouseX, mouseY);
    }

    @Override
    protected void updateWidgetNarration(NarrationElementOutput narrationElementOutput) { }

    public void updatePos(double mouseX, double mouseY) {
        if (!mbDown) return;

        if (this.mode == Mode.VERTICAL) {
            if (mouseY >= this.offsetY + this.padding && mouseY <= this.offsetY + this.padding + this.trackLength) {
                float relativeY = (float) (mouseY - this.offsetY - this.padding);
                this.scrollPercentage = relativeY / (float) this.trackLength;
            } else if (mouseY < this.offsetY + this.padding) {
                this.scrollPercentage = 0.0f;
            } else if (mouseY > this.offsetY + this.padding + this.trackLength) {
                this.scrollPercentage = 1.0f;
            }
        } else {
            if (mouseX >= this.offsetX + this.padding && mouseX <= this.offsetX + this.padding + this.trackLength) {
                float relativeX = (float) (mouseX - this.offsetX - this.padding);
                this.scrollPercentage = relativeX / (float) this.trackLength;
            } else if (mouseX < this.offsetX + this.padding) {
                this.scrollPercentage = 0.0f;
            } else if (mouseX > this.offsetX + this.padding + this.trackLength) {
                this.scrollPercentage = 1.0f;
            }
        }

        this.scrollOffset = (int) (this.getScrollableLength() * this.scrollPercentage);
        this.onScroll();
    }

    @Override
    public boolean clicked(double mouseX, double mouseY) {
        boolean flag;
        if (this.mode == AbstractScroller.Mode.VERTICAL) {
            flag = (
                    this.active &&
                            this.visible &&
                            mouseX >= this.offsetX &&
                            mouseY >= this.offsetY &&
                            mouseX < this.offsetX + this.width &&
                            mouseY < this.offsetY + this.trackLength + 2 * this.padding
            );
        } else {
            flag = (
                    this.active &&
                            this.visible &&
                            mouseX >= this.offsetX &&
                            mouseY >= this.offsetY &&
                            mouseX < this.offsetX + this.trackLength + 2 * this.padding &&
                            mouseY < this.offsetY + this.height
            );
        }

		flag = (flag && (this.getContentLength() >= this.getVisibleContentLength()));
        if (flag) mbDown = true;
        updatePos(mouseX, mouseY);
        return flag;
    }

    @Override
    public void onRelease(double mouseX, double mouseY) {
        mbDown = false;
    }

    public int getScrollOffset() {
        return this.scrollOffset;
    }

    /**
     * @return The total length of content that could be seen
     */
    public abstract int getContentLength();

    /**
     * @return The total length of the content that is visible at a moment in time
     */
    public abstract int getVisibleContentLength();

    /**
     * Called when the scroller is scrolled
     */
    public abstract void onScroll();

    public int getScrollableLength() {
        return this.getContentLength() - this.getVisibleContentLength();
    }

    public enum Mode {
        VERTICAL,
        HORIZONTAL
    }
}