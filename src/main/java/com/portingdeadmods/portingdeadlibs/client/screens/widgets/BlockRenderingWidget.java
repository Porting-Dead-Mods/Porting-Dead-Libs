package com.portingdeadmods.portingdeadlibs.client.screens.widgets;

import com.mojang.blaze3d.platform.Window;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.portingdeadmods.portingdeadlibs.PortingDeadLibs;
import it.unimi.dsi.fastutil.ints.IntIntPair;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.BlockRenderDispatcher;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.client.model.data.ModelData;
import org.joml.Matrix4f;
import org.joml.Quaternionf;
import org.lwjgl.BufferUtils;

import java.nio.FloatBuffer;
import java.util.HashMap;
import java.util.Map;

public class BlockRenderingWidget extends AbstractWidget {
    public final Map<BlockPos, Block> blocksToRender = new HashMap<>();
    public int maxLeft;
    public int maxRight;
    public int maxForward;
    public int maxBackward;
    public int maxUp;
    public int maxDown;

    private final BlockRenderDispatcher blocks;
    private double zoom = 1;
    public double rotation = 30; // Degrees
    public double yOffset = 0; // Blocks

    public BlockRenderingWidget(int x, int y, int width, int height) {
        super(x, y, width, height, Component.empty());
        this.blocks = Minecraft.getInstance().getBlockRenderer();
    }

    public BlockRenderingWidget(int x, int y, int width, int height, Map<BlockPos, Block> blocksToRender) {
        this(x, y, width, height);

        this.blocksToRender.putAll(blocksToRender);
        for (BlockPos pos : blocksToRender.keySet()) {
            if (pos.getX() < maxLeft) maxLeft = pos.getX();
            if (pos.getX() > maxRight) maxRight = pos.getX();
            if (pos.getZ() < maxForward) maxForward = pos.getZ();
            if (pos.getZ() > maxBackward) maxBackward = pos.getZ();
            if (pos.getY() < maxDown) maxDown = pos.getY();
            if (pos.getY() > maxUp) maxUp = pos.getY();
        }
    }

    public Map<Integer, Map<BlockPos, Block>> getBlocksAsLayers() {
        Map<Integer, Map<BlockPos, Block>> layers = new HashMap<>();
        for (BlockPos pos : blocksToRender.keySet()) {
            layers.putIfAbsent(pos.getY(), new HashMap<>());
            layers.get(pos.getY()).put(pos, blocksToRender.get(pos));
        }
        return layers;
    }

    public Map<BlockPos, Block> getBlocksToRender() {
        return blocksToRender;
    }

    public void addBlockToRender(BlockPos pos, Block block) {
        this.blocksToRender.put(pos, block);
    }

    public void removeBlockToRender(BlockPos pos) {
        this.blocksToRender.remove(pos);
    }


    private void drawScaledTexture(
            GuiGraphics guiGraphics,
            ResourceLocation texture,
            float u, float v,
            int uWidth, int vHeight,
            int textureWidth, int textureHeight) {

        guiGraphics.blit(texture, this.getX(), this.getY(), this.getWidth(), this.getHeight(), u, v, uWidth, vHeight, textureWidth, textureHeight);
    }

    private void renderRecipe(GuiGraphics guiGraphics, double guiScaleFactor) {
        PoseStack mx = guiGraphics.pose();
        try {
            guiGraphics.fill(
                    this.getX(),
                    this.getY(),
                    this.getX() + this.getWidth(),
                    this.getY() + this.getHeight(),
                    0xFF404040
            );

            MultiBufferSource.BufferSource buffers = Minecraft.getInstance().renderBuffers().bufferSource();

            final double scale = Minecraft.getInstance().getWindow().getGuiScale();
            final Matrix4f matrix = mx.last().pose();
            final FloatBuffer buf = BufferUtils.createFloatBuffer(16);
            matrix.get(buf);

            int scaledX = (int)(this.getX() * scale);
            int scaledY = (int)(this.getY() * scale);
            int scaledWidth = (int)(this.getWidth() * scale);
            int scaledHeight = (int)(this.getHeight() * scale);
            final int scissorX = Math.round((float)(buf.get(12) * scale + scaledX));
            final int scissorY = Math.round((float)(Minecraft.getInstance().getWindow().getHeight() - scaledY - scaledHeight - buf.get(13) * scale));
            final int scissorW = Math.round((float)scaledWidth);
            final int scissorH = Math.round((float)scaledHeight);
            RenderSystem.enableScissor(scissorX, scissorY, scissorW, scissorH);

            mx.pushPose();

            mx.translate(
                    this.getX() + (this.getWidth() / 2.0),
                    this.getY() + (this.getHeight() / 2.0),
                    100);

            int totalLayers = this.maxUp - this.maxDown + 1;
            float avgDim = (float) Math.sqrt(totalLayers * 9);
            float previewScale = (float) ((3 + Math.exp(3 - (avgDim / 5)))) * (float) zoom;
            mx.scale(previewScale, -previewScale, previewScale);

            drawBlocks(mx, buffers);

            mx.popPose();

            buffers.endBatch();

            RenderSystem.disableScissor();
        } catch (Exception ex) {
            PortingDeadLibs.LOGGER.warn("Error rendering multiblock", ex);
        }
    }

    private void drawBlocks(PoseStack mx, MultiBufferSource.BufferSource buffers) {
        mx.mulPose(new Quaternionf().rotationXYZ(
                (float) Math.toRadians(rotation),
                (float) Math.toRadians(30),
                0
        ));

        Map<Integer, Map<BlockPos, Block>> layersMap = getBlocksAsLayers();
        IntIntPair width = IntIntPair.of(maxLeft, maxRight);

        mx.translate(
                -(width.leftInt() / 2.0) - 0.5,
                -(layersMap.keySet().size() / 2.0) - 0.5,
                -(width.rightInt() / 2.0) - 0.5
        );

        for (int y : layersMap.keySet()) {
            renderBlockLayer(mx, buffers, layersMap.get(y));
        }
    }

    private void renderBlockLayer(PoseStack mx, MultiBufferSource.BufferSource buffers, Map<BlockPos, Block> layer) {
        mx.pushPose();

        for (BlockPos pos : layer.keySet()) {
            int x = pos.getX();
            int y = pos.getY();
            int z = pos.getZ();

            mx.pushPose();

            mx.translate(
                    (x + 0.5),
                    (y + 0.5),
                    (z + 0.5)
            );

            Block block = layer.get(pos);
            if (block != null && block != Blocks.AIR) {
                renderBlock(mx, buffers, block.defaultBlockState());
            }

            mx.popPose();
        }

        mx.popPose();
    }

    private void renderBlock(PoseStack mx, MultiBufferSource.BufferSource buffers, BlockState state) {
        try {
            blocks.renderSingleBlock(state,
                    mx,
                    buffers,
                    LightTexture.FULL_BRIGHT,
                    OverlayTexture.NO_OVERLAY,
                    ModelData.EMPTY, null);
        } catch (Exception e) {
            PortingDeadLibs.LOGGER.warn("Error rendering block in preview: {}", state);
            PortingDeadLibs.LOGGER.error("Stack Trace", e);
        }
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double scrollX, double scrollY) {
        if (isHovered()) {
            zoom = Math.clamp(zoom + scrollY * 0.1, 0.1, 5);
            return true;
        }
        return false;
    }

    @Override
    public boolean mouseDragged(double mouseX, double mouseY, int button, double dragX, double dragY) {
        if (isValidClickButton(button)) {
            if (isHovered()) {
                rotation = (rotation + dragY) % 360;
                yOffset = (yOffset - dragX) % 360;
                return true;
            }
        }

        return false;
    }

    @Override
    protected void renderWidget(GuiGraphics guiGraphics, int i, int i1, float v) {
        Window mainWindow = Minecraft.getInstance().getWindow();
        double guiScaleFactor = mainWindow.getGuiScale();

        renderRecipe(guiGraphics, guiScaleFactor);

        if (isHovered()) {
            guiGraphics.renderOutline(this.getX(), this.getY(), this.getWidth(), this.getHeight(), 0x80FFFFFF);
        }
    }

    @Override
    protected void updateWidgetNarration(NarrationElementOutput narrationElementOutput) {

    }
}
