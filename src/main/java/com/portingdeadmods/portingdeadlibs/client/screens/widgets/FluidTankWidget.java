//package com.portingdeadmods.portingdeadlibs.client.screens.widgets;
//
//import com.portingdeadmods.portingdeadlibs.PortingDeadLibs;
//import com.portingdeadmods.portingdeadlibs.api.blockentities.ResourceHandlerBlockEntity;
//import com.portingdeadmods.portingdeadlibs.api.gui.utils.FluidTankRenderer;
//import com.portingdeadmods.portingdeadlibs.api.wrappers.FluidHandlerWrapper;
//import com.portingdeadmods.portingdeadlibs.impl.wrappers.FluidHandlerWrapperImpl;
//import net.minecraft.client.Minecraft;
//import net.minecraft.client.gui.GuiGraphicsExtractor;
//import net.minecraft.client.gui.components.AbstractWidget;
//import net.minecraft.client.gui.narration.NarrationElementOutput;
//import net.minecraft.client.renderer.RenderPipelines;
//import net.minecraft.network.chat.CommonComponents;
//import net.minecraft.network.chat.Component;
//import net.minecraft.resources.Identifier;
//import net.minecraft.util.FormattedCharSequence;
//import net.neoforged.neoforge.capabilities.Capabilities;
//import net.neoforged.neoforge.transfer.ResourceHandler;
//import net.neoforged.neoforge.transfer.fluid.FluidResource;
//
//import java.util.List;
//
//public class FluidTankWidget extends AbstractWidget {
//    private static final Identifier SMALL_TANK = PortingDeadLibs.rl("small_tank");
//    private static final Identifier NORMAL_TANK = PortingDeadLibs.rl("normal_tank");
//    private static final Identifier LARGE_TANK = PortingDeadLibs.rl("large_tank");
//
//    private final TankVariants variant;
//    private final FluidTankRenderer renderer;
//
//    public FluidTankWidget(int x, int y, TankVariants variant, ResourceHandlerBlockEntity entity) {
//        this(x, y, variant, new FluidHandlerWrapperImpl(entity.getHandler(Capabilities.Fluid.BLOCK)));
//    }
//
//    public FluidTankWidget(int x, int y, TankVariants variant, FluidHandlerWrapper fluidHandlerWrapper) {
//        super(x, y, variant.textureWidth, variant.textureHeight, CommonComponents.EMPTY);
//        this.variant = variant;
//        this.renderer = new FluidTankRenderer(fluidHandlerWrapper, true, width - 2, height - 2);
//    }
//
//    public List<Component> getFluidTooltip() {
//        return renderer.getTooltip(fluidHandler.getFluidInTank(0));
//    }
//
//    @Override
//    protected void extractWidgetRenderState(GuiGraphicsExtractor guiGraphics, int i, int i1, float v) {
//        guiGraphics.blitSprite(RenderPipelines.GUI_TEXTURED, variant.location, getX(), getY(), width, height);
//        renderer.render(guiGraphics, getX() + 1, getY() + 1);
//
//        if (isHovered()) {
//            List<? extends FormattedCharSequence> fluidTooltip = getFluidTooltip();
//            guiGraphics.setTooltipForNextFrame(Minecraft.getInstance().font, fluidTooltip, i, i1);
//        }
//    }
//
//    @Override
//    protected void updateWidgetNarration(NarrationElementOutput narrationElementOutput) {
//
//    }
//
//    public enum TankVariants {
//        SMALL(18, 54, SMALL_TANK),
//        NORMAL(36, 54, NORMAL_TANK),
//        LARGE(54, 54, LARGE_TANK);
//
//        final int textureWidth;
//        final int textureHeight;
//        final Identifier location;
//
//        TankVariants(int textureWidth, int textureHeight, Identifier location) {
//            this.textureWidth = textureWidth;
//            this.textureHeight = textureHeight;
//            this.location = location;
//        }
//    }
//}