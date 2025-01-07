package com.portingdeadmods.portingdeadlibs.impl.client.screens.components;

import com.portingdeadmods.portingdeadlibs.PortingDeadLibs;
import com.portingdeadmods.portingdeadlibs.api.blockentities.ContainerBlockEntity;
import com.portingdeadmods.portingdeadlibs.api.client.screens.components.TooltipGuiComponent;
import com.portingdeadmods.portingdeadlibs.api.gui.utils.FluidTankRenderer;
import com.portingdeadmods.portingdeadlibs.utils.renderers.GuiUtils;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;
import org.joml.Vector2i;

import java.util.List;

public class FluidTankGuiComponent extends TooltipGuiComponent {
    private static final ResourceLocation SMALL_TANK = PortingDeadLibs.rl("small_tank");
    private static final ResourceLocation NORMAL_TANK = PortingDeadLibs.rl("normal_tank");
    private static final ResourceLocation LARGE_TANK = PortingDeadLibs.rl("large_tank");

    private final TankVariants variant;
    private FluidTankRenderer renderer;

    public FluidTankGuiComponent(@NotNull Vector2i position, TankVariants variant) {
        super(position);
        this.variant = variant;
    }

    @Override
    protected void onInit() {
        super.onInit();
        ContainerBlockEntity blockEntity = this.screen.getMenu().getBlockEntity();
        // Need to initialize it here, since in the constructor we can't access the screen yet
        this.renderer = new FluidTankRenderer(blockEntity.getFluidHandler().getTankCapacity(0), true, textureWidth()-2, textureHeight()-2);
    }

    @Override
    public List<Component> getTooltip(int mouseX, int mouseY) {
        ContainerBlockEntity blockEntity = this.screen.getMenu().getBlockEntity();
        return renderer.getTooltip(blockEntity.getFluidHandler().getFluidInTank(0));
    }

    @Override
    public int textureWidth() {
        return variant.textureWidth;
    }

    @Override
    public int textureHeight() {
        return variant.textureHeight;
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float delta) {
        super.render(guiGraphics, mouseX, mouseY, delta);
        ContainerBlockEntity blockEntity = this.screen.getMenu().getBlockEntity();
        renderer.render(guiGraphics.pose(), position.x + 1, position.y + 1, blockEntity.getFluidHandler().getFluidInTank(0));
    }

    @Override
    public void renderInBackground(GuiGraphics guiGraphics, int mouseX, int mouseY, float delta) {
        super.renderInBackground(guiGraphics, mouseX, mouseY, delta);
        GuiUtils.drawImg(guiGraphics, variant.location, position.x, position.y, textureWidth(), textureHeight());
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