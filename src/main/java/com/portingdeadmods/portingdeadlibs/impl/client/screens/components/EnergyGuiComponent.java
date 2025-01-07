package com.portingdeadmods.portingdeadlibs.impl.client.screens.components;

import com.portingdeadmods.portingdeadlibs.PortingDeadLibs;
import com.portingdeadmods.portingdeadlibs.api.blockentities.ContainerBlockEntity;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.energy.IEnergyStorage;
import org.jetbrains.annotations.NotNull;
import org.joml.Vector2i;

import java.util.List;

public class EnergyGuiComponent extends com.portingdeadmods.portingdeadlibs.api.client.screens.components.TooltipGuiComponent {
    private static final ResourceLocation ENERGY_BAR = PortingDeadLibs.rl("energy_bar");
    private static final ResourceLocation ENERGY_BAR_EMPTY = PortingDeadLibs.rl("energy_bar_empty");
    private static final ResourceLocation ENERGY_BAR_BORDER = PortingDeadLibs.rl("energy_bar_border");
    private static final ResourceLocation ENERGY_BAR_EMPTY_BORDER = PortingDeadLibs.rl("energy_bar_empty_border");

    private final boolean hasBorder;

    public EnergyGuiComponent(@NotNull Vector2i position, boolean hasBorder) {
        super(position);
        this.hasBorder = hasBorder;
    }

    @Override
    public List<Component> getTooltip(int mouseX, int mouseY) {
        ContainerBlockEntity blockEntity = screen.getMenu().getBlockEntity();
        IEnergyStorage energyStorage = blockEntity.getEnergyStorage();
        if (energyStorage != null) {
            return List.of(
                    Component.literal(energyStorage.getEnergyStored() + "/" + energyStorage.getMaxEnergyStored())
            );
        } else {
            return List.of();
        }
    }

    @Override
    public int textureWidth() {
        return 12;
    }

    @Override
    public int textureHeight() {
        return 48;
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float delta) {
        super.render(guiGraphics, mouseX, mouseY, delta);
        ResourceLocation loc = hasBorder ? ENERGY_BAR_EMPTY_BORDER : ENERGY_BAR_EMPTY;
        int height = textureHeight();
        int width = textureWidth();
        guiGraphics.blitSprite(loc, width, height, 0, 0, position.x, position.y, width, height);

        ContainerBlockEntity blockEntity = this.screen.getMenu().getBlockEntity();
        IEnergyStorage energyStorage = blockEntity.getEnergyStorage();
        if (energyStorage != null) {
            int energyStored = energyStorage.getEnergyStored();
            int maxStored = energyStorage.getMaxEnergyStored();

            int progress = (int) (height * ((float) energyStored / maxStored));
            ResourceLocation locFull = hasBorder ? ENERGY_BAR_BORDER : ENERGY_BAR;
            guiGraphics.blitSprite(locFull, width, height, 0, height - progress, position.x, position.y + height - progress, width, progress);
        }
    }

}