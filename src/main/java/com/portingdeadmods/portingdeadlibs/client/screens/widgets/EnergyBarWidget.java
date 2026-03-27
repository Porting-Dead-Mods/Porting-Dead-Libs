package com.portingdeadmods.portingdeadlibs.client.screens.widgets;

import com.portingdeadmods.portingdeadlibs.PortingDeadLibs;
import com.portingdeadmods.portingdeadlibs.api.blockentities.ResourceHandlerBlockEntity;
import com.portingdeadmods.portingdeadlibs.api.wrappers.EnergyHandlerWrapper;
import com.portingdeadmods.portingdeadlibs.impl.wrappers.NeoEnergyHandlerWrapper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.neoforged.neoforge.capabilities.Capabilities;

public class EnergyBarWidget extends AbstractWidget {
    private static final Identifier ENERGY_BAR = PortingDeadLibs.rl("energy_bar");
    private static final Identifier ENERGY_BAR_EMPTY = PortingDeadLibs.rl("energy_bar_empty");
    private static final Identifier ENERGY_BAR_BORDER = PortingDeadLibs.rl("energy_bar_border");
    private static final Identifier ENERGY_BAR_EMPTY_BORDER = PortingDeadLibs.rl("energy_bar_empty_border");

    private final boolean hasBorder;
    private final EnergyHandlerWrapper wrapper;
    private final String energyUnit;

    public EnergyBarWidget(int x, int y, EnergyHandlerWrapper wrapper, String energyUnit, boolean hasBorder) {
        super(x, y, 12, 48, CommonComponents.EMPTY);
        this.hasBorder = hasBorder;
        this.wrapper = wrapper;
        this.energyUnit = energyUnit;
    }

    public static EnergyBarWidget forgeEnergy(int x, int y, ResourceHandlerBlockEntity blockEntity, boolean hasBorder) {
        return new EnergyBarWidget(x, y, new NeoEnergyHandlerWrapper(blockEntity.getHandler(Capabilities.Energy.BLOCK)), "FE", hasBorder);
    }

    @Override
    protected void extractWidgetRenderState(GuiGraphicsExtractor guiGraphics, int mouseX, int mouseY, float v) {
        Identifier loc = hasBorder ? ENERGY_BAR_EMPTY_BORDER : ENERGY_BAR_EMPTY;
        guiGraphics.blitSprite(RenderPipelines.GUI_TEXTURED, loc, width, height, 0, 0, getX(), getY(), width, height);

        int energyStored = wrapper.getEnergyStored();
        int maxStored = wrapper.getEnergyCapacity();

        int progress = (int) (height * ((float) energyStored / maxStored));
        Identifier locFull = hasBorder ? ENERGY_BAR_BORDER : ENERGY_BAR;
        guiGraphics.blitSprite(RenderPipelines.GUI_TEXTURED, locFull, width, height, 0, height - progress, getX(), getY() + height - progress, width, progress);

        if (isHovered()) {
            guiGraphics.setTooltipForNextFrame(Minecraft.getInstance().font, Component.literal(wrapper.getEnergyStored() + "/" + wrapper.getEnergyCapacity() + energyUnit), mouseX, mouseY);
        }
    }

    @Override
    protected void updateWidgetNarration(NarrationElementOutput narrationElementOutput) {

    }

}