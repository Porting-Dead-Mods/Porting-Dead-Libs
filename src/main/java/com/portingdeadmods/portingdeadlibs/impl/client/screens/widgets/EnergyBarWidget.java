package com.portingdeadmods.portingdeadlibs.impl.client.screens.widgets;

import com.portingdeadmods.portingdeadlibs.PortingDeadLibs;
import com.portingdeadmods.portingdeadlibs.api.blockentities.ContainerBlockEntity;
import com.portingdeadmods.portingdeadlibs.api.capabilities.EnergyStorageWrapper;
import com.portingdeadmods.portingdeadlibs.api.capabilities.NeoEnergyStorageWrapper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

public class EnergyBarWidget extends AbstractWidget {
    private static final ResourceLocation ENERGY_BAR = PortingDeadLibs.rl("energy_bar");
    private static final ResourceLocation ENERGY_BAR_EMPTY = PortingDeadLibs.rl("energy_bar_empty");
    private static final ResourceLocation ENERGY_BAR_BORDER = PortingDeadLibs.rl("energy_bar_border");
    private static final ResourceLocation ENERGY_BAR_EMPTY_BORDER = PortingDeadLibs.rl("energy_bar_empty_border");

    private final boolean hasBorder;
    private final EnergyStorageWrapper wrapper;
    private final String energyUnit;

    public EnergyBarWidget(int x, int y, EnergyStorageWrapper wrapper, String energyUnit, boolean hasBorder) {
        super(x, y, 12, 48, CommonComponents.EMPTY);
        this.hasBorder = hasBorder;
        this.wrapper = wrapper;
        this.energyUnit = energyUnit;
    }

    public EnergyBarWidget(int x, int y, ContainerBlockEntity blockEntity, boolean hasBorder) {
        this(x, y, new NeoEnergyStorageWrapper(blockEntity.getEnergyStorage()), "FE", hasBorder);
    }

    @Override
    protected void renderWidget(GuiGraphics guiGraphics, int mouseX, int mouseY, float delta) {
        ResourceLocation loc = hasBorder ? ENERGY_BAR_EMPTY_BORDER : ENERGY_BAR_EMPTY;
        guiGraphics.blitSprite(loc, width, height, 0, 0, getX(), getY(), width, height);

        int energyStored = wrapper.getEnergyStored();
        int maxStored = wrapper.getEnergyCapacity();

        int progress = (int) (height * ((float) energyStored / maxStored));
        ResourceLocation locFull = hasBorder ? ENERGY_BAR_BORDER : ENERGY_BAR;
        guiGraphics.blitSprite(locFull, width, height, 0, height - progress, getX(), getY() + height - progress, width, progress);

        if (isHovered()) {
            guiGraphics.renderTooltip(Minecraft.getInstance().font, Component.literal(wrapper.getEnergyStored() + "/" + wrapper.getEnergyCapacity() + energyUnit), mouseX, mouseY);
        }

    }

    @Override
    protected void updateWidgetNarration(NarrationElementOutput narrationElementOutput) {

    }

}