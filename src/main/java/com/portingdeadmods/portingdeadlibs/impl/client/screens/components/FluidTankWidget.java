package com.portingdeadmods.portingdeadlibs.impl.client.screens.components;

import com.portingdeadmods.portingdeadlibs.PortingDeadLibs;
import com.portingdeadmods.portingdeadlibs.api.blockentities.ContainerBlockEntity;
import com.portingdeadmods.portingdeadlibs.api.gui.utils.FluidTankRenderer;
import com.portingdeadmods.portingdeadlibs.utils.renderers.GuiUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;

import java.util.List;

public class FluidTankWidget extends AbstractWidget {
    private static final ResourceLocation SMALL_TANK = PortingDeadLibs.rl("small_tank");
    private static final ResourceLocation NORMAL_TANK = PortingDeadLibs.rl("normal_tank");
    private static final ResourceLocation LARGE_TANK = PortingDeadLibs.rl("large_tank");

    private final TankVariants variant;
    private final FluidTankRenderer renderer;
    private final IFluidHandler fluidHandler;

    public FluidTankWidget(int x, int y, TankVariants variant, ContainerBlockEntity entity) {
        this(x, y, variant, entity.getFluidHandler());
    }

    public FluidTankWidget(int x, int y, TankVariants variant, IFluidHandler fluidHandler) {
        super(x, y, variant.textureWidth, variant.textureHeight, CommonComponents.EMPTY);
        this.variant = variant;
        this.renderer = new FluidTankRenderer(fluidHandler.getTankCapacity(0), true, width-2, height-2);
        this.fluidHandler = fluidHandler;
    }

    public List<Component> getFluidTooltip() {
        return renderer.getTooltip(fluidHandler.getFluidInTank(0));
    }

    @Override
    protected void renderWidget(GuiGraphics guiGraphics, int i, int i1, float v) {
        GuiUtils.drawImg(guiGraphics, variant.location, getX(), getY(), width, height);
        renderer.render(guiGraphics.pose(), getX() + 1, getY() + 1, fluidHandler.getFluidInTank(0));

        if (isHovered()) {
            guiGraphics.renderComponentTooltip(Minecraft.getInstance().font, getFluidTooltip(), i, i1);
        }
    }

    @Override
    protected void updateWidgetNarration(NarrationElementOutput narrationElementOutput) {

    }

    public enum TankVariants {
        SMALL(18, 54, SMALL_TANK),
        NORMAL(36, 54, NORMAL_TANK),
        LARGE(54, 54, LARGE_TANK);

        final int textureWidth;
        final int textureHeight;
        final ResourceLocation location;

        TankVariants(int textureWidth, int textureHeight, ResourceLocation location) {
            this.textureWidth = textureWidth;
            this.textureHeight = textureHeight;
            this.location = location;
        }
    }
}