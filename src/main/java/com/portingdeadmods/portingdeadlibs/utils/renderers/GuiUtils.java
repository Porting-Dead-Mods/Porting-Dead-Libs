package com.portingdeadmods.portingdeadlibs.utils.renderers;

import com.mojang.blaze3d.platform.NativeImage;
import com.mojang.blaze3d.shaders.BlendMode;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.Resource;
import net.minecraft.util.FastColor;
import org.joml.Matrix4f;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL14;

import java.io.IOException;
import java.util.*;

public final class GuiUtils {
    private static final Map<ResourceLocation, DynamicTexture> DYNAMIC_TEXTURE_CACHE = new HashMap<>();
    private static final TextureManager TEXTURE_MANAGER = Minecraft.getInstance().getTextureManager();

    public static void drawImg(GuiGraphics guiGraphics, ResourceLocation texturePath, int x, int y, int width, int height) {
        guiGraphics.blit(texturePath, x, y, 0, 0, 0, width, height, width, height);
    }

    public static void drawWithZ(GuiGraphics guiGraphics, ResourceLocation texturePath, int x, int y, int z, int width, int height) {
        RenderSystem.setShaderTexture(0, texturePath);
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        PoseStack poseStack = guiGraphics.pose();
        poseStack.pushPose();
        poseStack.translate(0, 0, z);
        Matrix4f matrix4f = poseStack.last().pose();
        BufferBuilder bufferbuilder = Tesselator.getInstance().begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX);
        bufferbuilder.addVertex(matrix4f, (float) x, (float) y, (float) 0).setUv(((float) 0 + 0.0F) / (float) width, ((float) 0 + 0.0F) / (float) height);
        bufferbuilder.addVertex(matrix4f, (float) x, (float) (y + height), (float) 0).setUv(((float) 0 + 0.0F) / (float) width, ((float) 0 + (float) height) / (float) height);
        bufferbuilder.addVertex(matrix4f, (float) (x + width), (float) (y + height), (float) 0).setUv(((float) 0 + (float) width) / (float) width, ((float) 0 + (float) height) / (float) height);
        bufferbuilder.addVertex(matrix4f, (float) (x + width), (float) y, (float) 0).setUv(((float) 0 + (float) width) / (float) width, ((float) 0 + 0.0F) / (float) height);
        BufferUploader.drawWithShader(bufferbuilder.buildOrThrow());
        poseStack.popPose();
    }

    public static void renderRect(GuiGraphics guiGraphics, int x, int y, int width, int height, int lineWidth, int lineColor, int fillColor) {
        guiGraphics.fill(x, y, x + width, y + height, lineColor);
        guiGraphics.fill(x + lineWidth, y + lineWidth, x + width - lineWidth, y + height - lineWidth, fillColor);
    }

    /**
     * Allows multiple compound effects
     */
    public static class ShaderChain {
        private final List<Effects.PixelEffect> effects = new ArrayList<>();
        private boolean cacheResult = false;
        private String cacheKey = null;

        private ShaderChain() {}

        public static ShaderChain create() {
            return new ShaderChain();
        }

        /**
         * Add a custom pixel manipulation effect
         */
        public ShaderChain then(PixelManipulator manipulator) {
            effects.add(new Effects.CustomEffect(manipulator));
            return this;
        }

        /**
         * Add a named effect for better debugging/caching
         */
        public ShaderChain then(String name, PixelManipulator manipulator) {
            effects.add(new Effects.NamedEffect(name, manipulator));
            return this;
        }

        // Convenience methods for common effects

        public ShaderChain grayscale() {
            effects.add(new Effects.Grayscale());
            return this;
        }

        public ShaderChain invert() {
            effects.add(new Effects.Invert());
            return this;
        }

        public ShaderChain brightness(float factor) {
            effects.add(new Effects.Brightness(factor));
            return this;
        }

        public ShaderChain contrast(float factor) {
            effects.add(new Effects.Contrast(factor));
            return this;
        }

        public ShaderChain tint(int color, float strength) {
            effects.add(new Effects.Tint(color, strength));
            return this;
        }

        public ShaderChain colorFilter(float r, float g, float b) {
            effects.add(new Effects.ColorFilter(r, g, b));
            return this;
        }

        public ShaderChain hueShift(float degrees) {
            effects.add(new Effects.HueShift(degrees));
            return this;
        }

        public ShaderChain saturation(float factor) {
            effects.add(new Effects.Saturation(factor));
            return this;
        }

        public ShaderChain sepia() {
            effects.add(new Effects.Sepia());
            return this;
        }

        public ShaderChain gamma(float gamma) {
            effects.add(new Effects.Gamma(gamma));
            return this;
        }

        public ShaderChain threshold(int threshold) {
            effects.add(new Effects.Threshold(threshold));
            return this;
        }

        public ShaderChain pixelate(int pixelSize) {
            effects.add(new Effects.Pixelate(pixelSize));
            return this;
        }

        public ShaderChain noise(float intensity) {
            effects.add(new Effects.Noise(intensity));
            return this;
        }

        public ShaderChain edgeDetect(int edgeColor, int threshold) {
            effects.add(new Effects.EdgeDetect(edgeColor, threshold));
            return this;
        }

        public ShaderChain blur(int radius) {
            effects.add(new Effects.Blur(radius));
            return this;
        }

        public ShaderChain sharpen(float strength) {
            effects.add(new Effects.Sharpen(strength));
            return this;
        }

        public ShaderChain emboss() {
            effects.add(new Effects.Emboss());
            return this;
        }

        public ShaderChain mirrorHorizontal() {
            effects.add(new Effects.Mirror(Effects.Mirror.MirrorMode.HORIZONTAL));
            return this;
        }

        public ShaderChain mirrorVertical() {
            effects.add(new Effects.Mirror(Effects.Mirror.MirrorMode.VERTICAL));
            return this;
        }

        public ShaderChain mirrorBoth() {
            effects.add(new Effects.Mirror(Effects.Mirror.MirrorMode.BOTH));
            return this;
        }

        public ShaderChain mirror(Effects.Mirror.MirrorMode mode) {
            effects.add(new Effects.Mirror(mode));
            return this;
        }

        /**
         * Enable caching for this chain
         */
        public ShaderChain cache(String key) {
            this.cacheResult = true;
            this.cacheKey = key;
            return this;
        }

        /**
         * Apply the chain to an image
         */
        public NativeImage apply(NativeImage input) {
            NativeImage current = copyImage(input);

            for (Effects.PixelEffect effect : effects) {
                NativeImage next = effect.apply(current);
                if (next != current) {
                    current.close();
                    current = next;
                }
            }

            return current;
        }

        /**
         * Apply and draw the result
         */
        public void drawTo(GuiGraphics guiGraphics, ResourceLocation texturePath,
                           int x, int y, int width, int height) {
            try {
                String cacheId = cacheKey != null ? cacheKey : generateCacheKey(texturePath);

                if (cacheResult && DYNAMIC_TEXTURE_CACHE.containsKey(ResourceLocation.parse(cacheId))) {
                    drawImg(guiGraphics, ResourceLocation.parse(cacheId), x, y, width, height);
                    return;
                }

                NativeImage original = loadImage(texturePath);
                NativeImage result = apply(original);
                original.close();

                ResourceLocation dynamicTexture = createOrUpdateDynamicTexture(ResourceLocation.parse(cacheId), result);
                drawImg(guiGraphics, dynamicTexture, x, y, width, height);
            } catch (IOException e) {
                drawImg(guiGraphics, texturePath, x, y, width, height);
            }
        }

        /**
         * Apply with blend mode
         */
        public void drawTo(GuiGraphics guiGraphics, ResourceLocation texturePath,
                           int x, int y, int width, int height, BlendMode blendMode) {
            try {
                String cacheId = cacheKey != null ? cacheKey : generateCacheKey(texturePath);

                if (cacheResult && DYNAMIC_TEXTURE_CACHE.containsKey(ResourceLocation.parse(cacheId))) {
                    drawBlending(guiGraphics, ResourceLocation.parse(cacheId), x, y, width, height, blendMode);
                    return;
                }

                NativeImage original = loadImage(texturePath);
                NativeImage result = apply(original);
                original.close();

                ResourceLocation dynamicTexture = createOrUpdateDynamicTexture(ResourceLocation.parse(cacheId), result);
                drawBlending(guiGraphics, dynamicTexture, x, y, width, height, blendMode);
            } catch (IOException e) {
                drawBlending(guiGraphics, texturePath, x, y, width, height, blendMode);
            }
        }

        private String generateCacheKey(ResourceLocation base) {
            StringBuilder sb = new StringBuilder(base.toString());
            for (Effects.PixelEffect effect : effects) {
                sb.append("_").append(effect.getName());
            }
            return sb.toString();
        }
    }

    private static NativeImage loadImage(ResourceLocation texturePath) throws IOException {
        Minecraft mc = Minecraft.getInstance();
        Optional<Resource> resource = mc.getResourceManager().getResource(texturePath);
        if (resource.isPresent()) {
            try (var stream = resource.get().open()) {
                return NativeImage.read(stream);
            }
        }
        throw new IOException("Could not load texture: " + texturePath);
    }

    private static NativeImage copyImage(NativeImage original) {
        NativeImage copy = new NativeImage(original.getWidth(), original.getHeight(), false);
        copy.copyFrom(original);
        return copy;
    }

    private static NativeImage loadAndManipulateImage(ResourceLocation texturePath, PixelManipulator manipulator) throws IOException {
        NativeImage original = loadImage(texturePath);
        NativeImage manipulated = new NativeImage(original.getWidth(), original.getHeight(), false);

        for (int y = 0; y < original.getHeight(); y++) {
            for (int x = 0; x < original.getWidth(); x++) {
                int originalColor = original.getPixelRGBA(x, y);
                int manipulatedColor = manipulator.manipulate(x, y, originalColor);
                manipulated.setPixelRGBA(x, y, manipulatedColor);
            }
        }

        original.close();
        return manipulated;
    }

    private static ResourceLocation createOrUpdateDynamicTexture(ResourceLocation basePath, NativeImage image) {
        String dynamicPath = basePath.toString() + "_dynamic";
        ResourceLocation dynamicLocation = ResourceLocation.parse(dynamicPath);

        if (DYNAMIC_TEXTURE_CACHE.containsKey(dynamicLocation)) {
            DynamicTexture oldTexture = DYNAMIC_TEXTURE_CACHE.get(dynamicLocation);
            oldTexture.close();
        }

        DynamicTexture dynamicTexture = new DynamicTexture(image);
        DYNAMIC_TEXTURE_CACHE.put(dynamicLocation, dynamicTexture);
        TEXTURE_MANAGER.register(dynamicLocation, dynamicTexture);

        return dynamicLocation;
    }

    /**
     * Should be called when appropriate (e.g., screen close)
     */
    public static void cleanupDynamicTextures() {
        DYNAMIC_TEXTURE_CACHE.forEach((location, texture) -> {
            texture.close();
            TEXTURE_MANAGER.release(location);
        });
        DYNAMIC_TEXTURE_CACHE.clear();
    }

    public enum BlendMode {
        MULTIPLY,
        ADD,
        DARKEN,
        LIGHTEN,
        DIFFERENCE
    }

    /**
     * Blit to the screen with a specific blend mode.
     */
    public static void drawBlending(GuiGraphics guiGraphics, ResourceLocation texturePath,
                                         int x, int y, int width, int height, BlendMode blendMode) {
        RenderSystem.enableBlend();

        switch (blendMode) {
            case MULTIPLY -> {
                RenderSystem.blendFunc(GL11.GL_DST_COLOR, GL11.GL_ONE_MINUS_SRC_ALPHA);
            }
            case ADD -> {
                RenderSystem.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE);
            }
            case DARKEN -> {
                RenderSystem.blendEquation(GL14.GL_MIN);
                RenderSystem.blendFunc(GL11.GL_ONE, GL11.GL_ONE);
            }
            case LIGHTEN -> {
                RenderSystem.blendEquation(GL14.GL_MAX);
                RenderSystem.blendFunc(GL11.GL_ONE, GL11.GL_ONE);
            }
            case DIFFERENCE -> {
                RenderSystem.blendEquation(GL14.GL_FUNC_REVERSE_SUBTRACT);
                RenderSystem.blendFunc(GL11.GL_ONE, GL11.GL_ONE);
            }
            default -> {
                RenderSystem.defaultBlendFunc();
            }
        }

        drawImg(guiGraphics, texturePath, x, y, width, height);

        RenderSystem.blendEquation(GL14.GL_FUNC_ADD);
        RenderSystem.defaultBlendFunc();
        RenderSystem.disableBlend();
    }
}
