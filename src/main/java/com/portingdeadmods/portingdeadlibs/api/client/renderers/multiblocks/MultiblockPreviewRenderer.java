package com.portingdeadmods.portingdeadlibs.api.client.renderers.multiblocks;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.portingdeadmods.portingdeadlibs.api.multiblocks.Multiblock;
import com.portingdeadmods.portingdeadlibs.api.multiblocks.MultiblockDefinition;
import com.portingdeadmods.portingdeadlibs.api.multiblocks.MultiblockLayer;
import com.portingdeadmods.portingdeadlibs.api.utils.HorizontalDirection;
import com.portingdeadmods.portingdeadlibs.utils.MultiblockHelper;
import com.portingdeadmods.portingdeadlibs.utils.renderers.AlphaRenderTypeBuffer;
import com.portingdeadmods.portingdeadlibs.utils.renderers.PDLRenderTypes;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.BlockRenderDispatcher;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Vec3i;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LightLayer;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.client.model.data.ModelData;
import net.neoforged.neoforge.client.model.pipeline.VertexConsumerWrapper;
import org.joml.Matrix4f;

public final class MultiblockPreviewRenderer {
    public static void renderPreview(Multiblock multiblock, BlockPos controllerPos, Level level, HorizontalDirection direction, PoseStack poseStack, MultiBufferSource multiBufferSource, Vec3 cameraPos) {
        double camX = cameraPos.x;
        double camY = cameraPos.y;
        double camZ = cameraPos.z;

        Minecraft mc = Minecraft.getInstance();
        BlockRenderDispatcher blockRenderer = mc.getBlockRenderer();

        Vec3i relativeControllerPos = MultiblockHelper.getRelativeControllerPos(multiblock);
        BlockPos firstPos = MultiblockHelper.getFirstBlockPos(direction, controllerPos, relativeControllerPos);
        MultiblockLayer[] layout = multiblock.getLayout();
        MultiblockDefinition def = multiblock.getDefinition();

        int y = 0;
        for (MultiblockLayer layer : layout) {
            for (int i = 0; i < layer.range().getMax(); i++) {
                // initialize/reset x and z coords for indexing
                int x = 0;
                int z = 0;

                int width = multiblock.getWidths().get(y).leftInt();

                // Iterate over blocks in a layer (X, Z)
                for (int blockIndex : layer.layer()) {
                    // Define position-related variables
                    BlockPos curPos = MultiblockHelper.getCurPos(firstPos, new Vec3i(x, y, z), HorizontalDirection.NORTH);

                    Block block = def.getDefaultBlock(blockIndex);
                    if (block != null) {
                        BlockState blockState = level.getBlockState(curPos);
                        if (!def.getPredicate(blockIndex).test(blockState)) {
                            if (blockState.isEmpty()) {
                                if (i < layer.range().getMin()) {
                                    MultiblockPreviewRenderer.renderSmallBlock(poseStack, curPos, camX, camY, camZ, blockRenderer, multiBufferSource, mc.level, block);
                                } else {
                                    MultiblockPreviewRenderer.renderSmallOptionalBlock(poseStack, curPos, camX, camY, camZ, blockRenderer, multiBufferSource, mc.level, block);
                                }
                            } else {
                                MultiblockPreviewRenderer.renderErrorBlock(poseStack, curPos, camX, camY, camZ, multiBufferSource, mc.level);
                            }
                        }
                    }
                    // Increase x and z coordinates
                    // start new x if we are done with row and increase z as another row is done
                    if (x + 1 < width) {
                        x++;
                    } else {
                        x = 0;
                        z++;
                    }
                }

                y++;


            }
        }
    }

    private static void renderSmallOptionalBlock(PoseStack poseStack, BlockPos blockPos, double camX, double camY, double camZ, BlockRenderDispatcher blockRenderer, MultiBufferSource bufferSource, Level level, Block block) {
        poseStack.pushPose();
        {
            poseStack.translate(blockPos.getX() - camX, blockPos.getY() - camY, blockPos.getZ() - camZ);
            poseStack.scale(0.5f, 0.5f, 0.5f);
            poseStack.translate(0.5, 0.5, 0.5);
            blockRenderer.renderSingleBlock(block.defaultBlockState(), poseStack, new AlphaRenderTypeBuffer(bufferSource), getLightLevel(level, blockPos), OverlayTexture.NO_OVERLAY, ModelData.EMPTY, RenderType.translucent());
        }
        poseStack.popPose();
    }

    private static void renderSmallBlock(PoseStack poseStack, BlockPos blockPos, double camX, double camY, double camZ, BlockRenderDispatcher blockRenderer, MultiBufferSource bufferSource, Level level, Block block) {
        poseStack.pushPose();
        {
            poseStack.translate(blockPos.getX() - camX, blockPos.getY() - camY, blockPos.getZ() - camZ);
            poseStack.scale(0.5f, 0.5f, 0.5f);
            poseStack.translate(0.5, 0.5, 0.5);
            blockRenderer.renderSingleBlock(block.defaultBlockState(), poseStack, bufferSource, getLightLevel(level, blockPos), OverlayTexture.NO_OVERLAY, ModelData.EMPTY, RenderType.solid());
        }
        poseStack.popPose();
    }

    private static void renderErrorBlock(PoseStack poseStack, BlockPos blockPos, double camX, double camY, double camZ, MultiBufferSource bufferSource, Level level) {
        poseStack.pushPose();
        {
            poseStack.translate(blockPos.getX() - camX, blockPos.getY() - camY, blockPos.getZ() - camZ);
            int r = 255;
            int g = 0;
            int b = 0;
            int a = 100;
            VertexConsumer consumer = bufferSource.getBuffer(PDLRenderTypes.SIMPLE_SOLID);
            Matrix4f matrix = poseStack.last().pose();

            renderCube(consumer, matrix, r, g, b, a);
        }
        poseStack.popPose();
    }

    private static void renderCube(VertexConsumer consumer, Matrix4f matrix, int r, int g, int b, int a) {
        // Top side
        consumer.addVertex(matrix, -0.1f, 1.1f, -0.1f).setColor(r, g, b, a).setNormal(0, 1, 0);
        consumer.addVertex(matrix, -0.1f, 1.1f, 1.1f).setColor(r, g, b, a).setNormal(0, 1, 0);
        consumer.addVertex(matrix, 1.1f, 1.1f, 1.1f).setColor(r, g, b, a).setNormal(0, 1, 0);
        consumer.addVertex(matrix, 1.1f, 1.1f, -0.1f).setColor(r, g, b, a).setNormal(0, 1, 0);

        // Bottom side
        consumer.addVertex(matrix, -0.1f, -0.1f, -0.1f).setColor(r, g, b, a).setNormal(0, -1, 0);
        consumer.addVertex(matrix, 1.1f, -0.1f, -0.1f).setColor(r, g, b, a).setNormal(0, -1, 0);
        consumer.addVertex(matrix, 1.1f, -0.1f, 1.1f).setColor(r, g, b, a).setNormal(0, -1, 0);
        consumer.addVertex(matrix, -0.1f, -0.1f, 1.1f).setColor(r, g, b, a).setNormal(0, -1, 0);

        // Front side
        consumer.addVertex(matrix, -0.1f, -0.1f, 1.1f).setColor(r, g, b, a).setNormal(0, 0, 1);
        consumer.addVertex(matrix, 1.1f, -0.1f, 1.1f).setColor(r, g, b, a).setNormal(0, 0, 1);
        consumer.addVertex(matrix, 1.1f, 1.1f, 1.1f).setColor(r, g, b, a).setNormal(0, 0, 1);
        consumer.addVertex(matrix, -0.1f, 1.1f, 1.1f).setColor(r, g, b, a).setNormal(0, 0, 1);

        // Back side
        consumer.addVertex(matrix, -0.1f, -0.1f, -0.1f).setColor(r, g, b, a).setNormal(0, 0, -1);
        consumer.addVertex(matrix, -0.1f, 1.1f, -0.1f).setColor(r, g, b, a).setNormal(0, 0, -1);
        consumer.addVertex(matrix, 1.1f, 1.1f, -0.1f).setColor(r, g, b, a).setNormal(0, 0, -1);
        consumer.addVertex(matrix, 1.1f, -0.1f, -0.1f).setColor(r, g, b, a).setNormal(0, 0, -1);

        // Left side
        consumer.addVertex(matrix, -0.1f, -0.1f, -0.1f).setColor(r, g, b, a).setNormal(-1, 0, 0);
        consumer.addVertex(matrix, -0.1f, -0.1f, 1.1f).setColor(r, g, b, a).setNormal(-1, 0, 0);
        consumer.addVertex(matrix, -0.1f, 1.1f, 1.1f).setColor(r, g, b, a).setNormal(-1, 0, 0);
        consumer.addVertex(matrix, -0.1f, 1.1f, -0.1f).setColor(r, g, b, a).setNormal(-1, 0, 0);

        // Right side
        consumer.addVertex(matrix, 1.1f, -0.1f, -0.1f).setColor(r, g, b, a).setNormal(1, 0, 0);
        consumer.addVertex(matrix, 1.1f, 1.1f, -0.1f).setColor(r, g, b, a).setNormal(1, 0, 0);
        consumer.addVertex(matrix, 1.1f, 1.1f, 1.1f).setColor(r, g, b, a).setNormal(1, 0, 0);
        consumer.addVertex(matrix, 1.1f, -0.1f, 1.1f).setColor(r, g, b, a).setNormal(1, 0, 0);
    }

    private static int getLightLevel(Level level, BlockPos pos) {
        int bLight = level.getBrightness(LightLayer.BLOCK, pos);
        int sLight = level.getBrightness(LightLayer.SKY, pos);
        return LightTexture.pack(bLight, sLight);
    }
}
