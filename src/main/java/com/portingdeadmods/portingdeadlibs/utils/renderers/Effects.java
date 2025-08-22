package com.portingdeadmods.portingdeadlibs.utils.renderers;

import com.mojang.blaze3d.platform.NativeImage;
import net.minecraft.util.FastColor;
import net.neoforged.neoforge.client.model.IModelBuilder;

import java.util.Random;

public class Effects {
    /**
     * Should be used whenever the result would be printed on a new NativeImage instance
     */
    public static abstract class PixelEffect {
        public abstract NativeImage apply(NativeImage input);
        public abstract String getName();
    }

    /**
     *  Should be used whenever the process is sort of simple and can be done pixel by pixel on the original image (eg. darkening)
     */
    public static abstract class SimplePixelEffect extends PixelEffect {
        @Override
        public NativeImage apply(NativeImage input) {
            NativeImage output = new NativeImage(input.getWidth(), input.getHeight(), false);
            for (int y = 0; y < input.getHeight(); y++) {
                for (int x = 0; x < input.getWidth(); x++) {
                    output.setPixelRGBA(x, y, processPixel(x, y, input.getPixelRGBA(x, y), input));
                }
            }
            return output;
        }

        protected abstract int processPixel(int x, int y, int color, NativeImage image);
    }

    public static class CustomEffect extends SimplePixelEffect {
        private final PixelManipulator manipulator;

        CustomEffect(PixelManipulator manipulator) {
            this.manipulator = manipulator;
        }

        @Override
        protected int processPixel(int x, int y, int color, NativeImage image) {
            return manipulator.manipulate(x, y, color);
        }

        @Override
        public String getName() {
            return "custom";
        }
    }

    public static class NamedEffect extends CustomEffect {
        private final String name;

        NamedEffect(String name, PixelManipulator manipulator) {
            super(manipulator);
            this.name = name;
        }

        @Override
        public String getName() {
            return name;
        }
    }

    public static class Grayscale extends SimplePixelEffect {
        @Override
        protected int processPixel(int x, int y, int color, NativeImage image) {
            int a = FastColor.ARGB32.alpha(color);
            int r = FastColor.ARGB32.red(color);
            int g = FastColor.ARGB32.green(color);
            int b = FastColor.ARGB32.blue(color);

            int gray = (int)(0.299f * r + 0.587f * g + 0.114f * b);
            return FastColor.ARGB32.color(a, gray, gray, gray);
        }

        @Override
        public String getName() {
            return "grayscale";
        }
    }

    public static class Invert extends SimplePixelEffect {
        @Override
        protected int processPixel(int x, int y, int color, NativeImage image) {
            int a = FastColor.ARGB32.alpha(color);
            int r = 255 - FastColor.ARGB32.red(color);
            int g = 255 - FastColor.ARGB32.green(color);
            int b = 255 - FastColor.ARGB32.blue(color);

            return FastColor.ARGB32.color(a, r, g, b);
        }

        @Override
        public String getName() {
            return "invert";
        }
    }

    public static class Brightness extends SimplePixelEffect {
        public final float factor;

        Brightness(float factor) {
            this.factor = factor;
        }

        @Override
        protected int processPixel(int x, int y, int color, NativeImage image) {
            int a = FastColor.ARGB32.alpha(color);
            int r = Math.min(255, (int)(FastColor.ARGB32.red(color) * factor));
            int g = Math.min(255, (int)(FastColor.ARGB32.green(color) * factor));
            int b = Math.min(255, (int)(FastColor.ARGB32.blue(color) * factor));

            return FastColor.ARGB32.color(a, r, g, b);
        }

        @Override
        public String getName() {
            return "brightness_" + factor;
        }
    }

    public static class Contrast extends SimplePixelEffect {
        public final float factor;

        Contrast(float factor) {
            this.factor = factor;
        }

        private static int adjustChannelContrast(int value, float contrast) {
            float normalized = value / 255.0f;
            float adjusted = ((normalized - 0.5f) * contrast) + 0.5f;
            return Math.max(0, Math.min(255, (int)(adjusted * 255)));
        }

        @Override
        protected int processPixel(int x, int y, int color, NativeImage image) {
            int a = FastColor.ARGB32.alpha(color);
            int r = adjustChannelContrast(FastColor.ARGB32.red(color), factor);
            int g = adjustChannelContrast(FastColor.ARGB32.green(color), factor);
            int b = adjustChannelContrast(FastColor.ARGB32.blue(color), factor);

            return FastColor.ARGB32.color(a, r, g, b);
        }

        @Override
        public String getName() {
            return "contrast_" + factor;
        }
    }

    public static class Tint extends SimplePixelEffect {
        public final int tintColor;
        public final float strength;

        Tint(int tintColor, float strength) {
            this.tintColor = tintColor;
            this.strength = strength;
        }

        @Override
        protected int processPixel(int x, int y, int color, NativeImage image) {
            int a1 = FastColor.ARGB32.alpha(color);
            int r1 = FastColor.ARGB32.red(color);
            int g1 = FastColor.ARGB32.green(color);
            int b1 = FastColor.ARGB32.blue(color);

            int a2 = FastColor.ARGB32.alpha(tintColor);
            int r2 = FastColor.ARGB32.red(tintColor);
            int g2 = FastColor.ARGB32.green(tintColor);
            int b2 = FastColor.ARGB32.blue(tintColor);

            int a = (int)(a1 * (1 - strength) + a2 * strength);
            int r = (int)(r1 * (1 - strength) + r2 * strength);
            int g = (int)(g1 * (1 - strength) + g2 * strength);
            int b = (int)(b1 * (1 - strength) + b2 * strength);

            return FastColor.ARGB32.color(a, r, g, b);
        }

        @Override
        public String getName() {
            return "tint_" + Integer.toHexString(tintColor);
        }
    }

    public static class ColorFilter extends SimplePixelEffect {
        public final float r, g, b;

        ColorFilter(float r, float g, float b) {
            this.r = r;
            this.g = g;
            this.b = b;
        }

        @Override
        protected int processPixel(int x, int y, int color, NativeImage image) {
            int fa = FastColor.ARGB32.alpha(color);
            int fr = Math.min(255, (int) (FastColor.ARGB32.red(color) * this.r));
            int fg = Math.min(255, (int) (FastColor.ARGB32.green(color) * this.g));
            int fb = Math.min(255, (int) (FastColor.ARGB32.blue(color) * this.b));

            return FastColor.ARGB32.color(fa, fr, fg, fb);
        }

        @Override
        public String getName() {
            return "colorfilter_" + r + "_" + g + "_" + b;
        }
    }

    public static class HueShift extends SimplePixelEffect {
        public final float degrees;

        HueShift(float degrees) {
            this.degrees = degrees;
        }

        @Override
        protected int processPixel(int x, int y, int color, NativeImage image) {
            int a = FastColor.ARGB32.alpha(color);
            int r = FastColor.ARGB32.red(color);
            int g = FastColor.ARGB32.green(color);
            int b = FastColor.ARGB32.blue(color);

            float[] hsb = new float[3];
            java.awt.Color.RGBtoHSB(r, g, b, hsb);
            hsb[0] = (hsb[0] + degrees / 360f) % 1.0f;
            if (hsb[0] < 0) hsb[0] += 1.0f;

            int rgb = java.awt.Color.HSBtoRGB(hsb[0], hsb[1], hsb[2]);
            return FastColor.ARGB32.color(a, (rgb >> 16) & 0xFF, (rgb >> 8) & 0xFF, rgb & 0xFF);
        }

        @Override
        public String getName() {
            return "hue_" + degrees;
        }
    }

    public static class Saturation extends SimplePixelEffect {
        public final float factor;

        Saturation(float factor) {
            this.factor = factor;
        }

        @Override
        protected int processPixel(int x, int y, int color, NativeImage image) {
            int a = FastColor.ARGB32.alpha(color);
            int r = FastColor.ARGB32.red(color);
            int g = FastColor.ARGB32.green(color);
            int b = FastColor.ARGB32.blue(color);

            float[] hsb = new float[3];
            java.awt.Color.RGBtoHSB(r, g, b, hsb);
            hsb[1] = Math.max(0, Math.min(1, hsb[1] * factor));

            int rgb = java.awt.Color.HSBtoRGB(hsb[0], hsb[1], hsb[2]);
            return FastColor.ARGB32.color(a, (rgb >> 16) & 0xFF, (rgb >> 8) & 0xFF, rgb & 0xFF);
        }

        @Override
        public String getName() {
            return "saturation_" + factor;
        }
    }

    public static class Opacity extends SimplePixelEffect {
        public final int opacity;

        Opacity(int opacity) {
            this.opacity = opacity;
        }

        @Override
        protected int processPixel(int x, int y, int color, NativeImage image) {
            return FastColor.ARGB32.color(
                    this.opacity,
                    FastColor.ARGB32.red(color),
                    FastColor.ARGB32.green(color),
                    FastColor.ARGB32.blue(color)
            );
        }

        @Override
        public String getName() {
            return "opacity_" + this.opacity;
        }
    }

    public static class Sepia extends SimplePixelEffect {
        @Override
        protected int processPixel(int x, int y, int color, NativeImage image) {
            int a = FastColor.ARGB32.alpha(color);
            int r = FastColor.ARGB32.red(color);
            int g = FastColor.ARGB32.green(color);
            int b = FastColor.ARGB32.blue(color);

            int tr = (int)(0.393f * r + 0.769f * g + 0.189f * b);
            int tg = (int)(0.349f * r + 0.686f * g + 0.168f * b);
            int tb = (int)(0.272f * r + 0.534f * g + 0.131f * b);

            return FastColor.ARGB32.color(a,
                    Math.min(255, tr),
                    Math.min(255, tg),
                    Math.min(255, tb));
        }

        @Override
        public String getName() {
            return "sepia";
        }
    }

    public static class Gamma extends SimplePixelEffect {
        public final float gamma;

        Gamma(float gamma) {
            this.gamma = gamma;
        }

        @Override
        protected int processPixel(int x, int y, int color, NativeImage image) {
            int a = FastColor.ARGB32.alpha(color);
            int r = (int)(255 * Math.pow(FastColor.ARGB32.red(color) / 255.0, 1.0 / this.gamma));
            int g = (int)(255 * Math.pow(FastColor.ARGB32.green(color) / 255.0, 1.0 / this.gamma));
            int b = (int)(255 * Math.pow(FastColor.ARGB32.blue(color) / 255.0, 1.0 / this.gamma));

            return FastColor.ARGB32.color(a,
                    Math.max(0, Math.min(255, r)),
                    Math.max(0, Math.min(255, g)),
                    Math.max(0, Math.min(255, b)));
        }

        @Override
        public String getName() {
            return "gamma_" + gamma;
        }
    }

    public static class Threshold extends SimplePixelEffect {
        public final int threshold;

        Threshold(int threshold) {
            this.threshold = threshold;
        }

        private static int getLuminance(int color) {
            int r = FastColor.ARGB32.red(color);
            int g = FastColor.ARGB32.green(color);
            int b = FastColor.ARGB32.blue(color);
            return (int)(0.299f * r + 0.587f * g + 0.114f * b);
        }

        @Override
        protected int processPixel(int x, int y, int color, NativeImage image) {
            int lum = getLuminance(color);
            int a = FastColor.ARGB32.alpha(color);
            return lum > threshold ? FastColor.ARGB32.color(a, 255, 255, 255) : FastColor.ARGB32.color(a, 0, 0, 0);
        }

        @Override
        public String getName() {
            return "threshold_" + threshold;
        }
    }

    public static class Pixelate extends SimplePixelEffect {
        public final int pixelSize;

        Pixelate(int pixelSize) {
            this.pixelSize = pixelSize;
        }

        @Override
        protected int processPixel(int x, int y, int color, NativeImage image) {
            int px = (x / pixelSize) * pixelSize;
            int py = (y / pixelSize) * pixelSize;
            return image.getPixelRGBA(
                    Math.min(px, image.getWidth() - 1),
                    Math.min(py, image.getHeight() - 1)
            );
        }

        @Override
        public String getName() {
            return "pixelate_" + pixelSize;
        }
    }

    public static class Noise extends SimplePixelEffect {
        public final float intensity;
        public final Random random = new Random();

        Noise(float intensity) {
            this.intensity = intensity;
        }

        @Override
        protected int processPixel(int x, int y, int color, NativeImage image) {
            float noise = (random.nextFloat() - 0.5f) * intensity;
            int a = FastColor.ARGB32.alpha(color);
            int r = Math.max(0, Math.min(255, (int)(FastColor.ARGB32.red(color) + noise * 255)));
            int g = Math.max(0, Math.min(255, (int)(FastColor.ARGB32.green(color) + noise * 255)));
            int b = Math.max(0, Math.min(255, (int)(FastColor.ARGB32.blue(color) + noise * 255)));
            return FastColor.ARGB32.color(a, r, g, b);
        }

        @Override
        public String getName() {
            return "noise_" + intensity;
        }
    }

    public static class EdgeDetect extends PixelEffect {
        public final int edgeColor;
        public final int threshold;

        EdgeDetect(int edgeColor, int threshold) {
            this.edgeColor = edgeColor;
            this.threshold = threshold;
        }

        private static int getLuminance(int color) {
            int r = FastColor.ARGB32.red(color);
            int g = FastColor.ARGB32.green(color);
            int b = FastColor.ARGB32.blue(color);
            return (int)(0.299f * r + 0.587f * g + 0.114f * b);
        }

        @Override
        public NativeImage apply(NativeImage input) {
            NativeImage result = new NativeImage(input.getWidth(), input.getHeight(), false);

            for (int y = 1; y < input.getHeight() - 1; y++) {
                for (int x = 1; x < input.getWidth() - 1; x++) {
                    int center = getLuminance(input.getPixelRGBA(x, y));
                    int left = getLuminance(input.getPixelRGBA(x - 1, y));
                    int right = getLuminance(input.getPixelRGBA(x + 1, y));
                    int top = getLuminance(input.getPixelRGBA(x, y - 1));
                    int bottom = getLuminance(input.getPixelRGBA(x, y + 1));

                    int edgeStrength = Math.abs(center - left) + Math.abs(center - right) +
                            Math.abs(center - top) + Math.abs(center - bottom);

                    if (edgeStrength > threshold) {
                        result.setPixelRGBA(x, y, edgeColor);
                    } else {
                        result.setPixelRGBA(x, y, input.getPixelRGBA(x, y));
                    }
                }
            }

            return result;
        }

        @Override
        public String getName() {
            return "edge_" + threshold;
        }
    }

    public static class Blur extends PixelEffect {
        public final int radius;

        Blur(int radius) {
            this.radius = radius;
        }

        @Override
        public NativeImage apply(NativeImage input) {
            NativeImage result = new NativeImage(input.getWidth(), input.getHeight(), false);

            for (int y = 0; y < input.getHeight(); y++) {
                for (int x = 0; x < input.getWidth(); x++) {
                    int rSum = 0, gSum = 0, bSum = 0, aSum = 0;
                    int count = 0;

                    for (int dy = -radius; dy <= radius; dy++) {
                        for (int dx = -radius; dx <= radius; dx++) {
                            int px = Math.max(0, Math.min(input.getWidth() - 1, x + dx));
                            int py = Math.max(0, Math.min(input.getHeight() - 1, y + dy));

                            int pixel = input.getPixelRGBA(px, py);
                            aSum += FastColor.ARGB32.alpha(pixel);
                            rSum += FastColor.ARGB32.red(pixel);
                            gSum += FastColor.ARGB32.green(pixel);
                            bSum += FastColor.ARGB32.blue(pixel);
                            count++;
                        }
                    }

                    result.setPixelRGBA(x, y, FastColor.ARGB32.color(
                            aSum / count, rSum / count, gSum / count, bSum / count));
                }
            }

            return result;
        }

        @Override
        public String getName() {
            return "blur_" + radius;
        }
    }

    public static class Mirror extends Effects.PixelEffect {
        public enum MirrorMode {
            HORIZONTAL,
            VERTICAL,
            BOTH
        }

        private final MirrorMode mode;

        Mirror(MirrorMode mode) {
            this.mode = mode;
        }

        @Override
        public NativeImage apply(NativeImage input) {
            int width = input.getWidth();
            int height = input.getHeight();
            NativeImage output = new NativeImage(width, height, false);

            for (int y = 0; y < height; y++) {
                for (int x = 0; x < width; x++) {
                    int srcX = x;
                    int srcY = y;

                    switch (mode) {
                        case HORIZONTAL -> srcX = width - 1 - x;
                        case VERTICAL -> srcY = height - 1 - y;
                        case BOTH -> {
                            srcX = width - 1 - x;
                            srcY = height - 1 - y;
                        }
                    }

                    output.setPixelRGBA(x, y, input.getPixelRGBA(srcX, srcY));
                }
            }

            return output;
        }

        @Override
        public String getName() {
            return "mirror_" + mode.name().toLowerCase();
        }
    }

    public enum ConvolutionKernel {
        BLUR(new float[][]{
                {1/9f, 1/9f, 1/9f},
                {1/9f, 1/9f, 1/9f},
                {1/9f, 1/9f, 1/9f}
        }),
        SHARPEN(new float[][]{
                {0, -1, 0},
                {-1, 5, -1},
                {0, -1, 0}
        }),
        EMBOSS(new float[][]{
                {-2, -1, 0},
                {-1, 1, 1},
                {0, 1, 2}
        });

        public final float[][] kernel;

        ConvolutionKernel(float[][] kernel) {
            this.kernel = kernel;
        }
    }

    public static class Convolution extends PixelEffect {
        public final float[][] kernel;

        Convolution(float[][] kernel) {
            this.kernel = kernel;
        }

        Convolution(ConvolutionKernel kernel) {
            this.kernel = kernel.kernel;
        }

        @Override
        public NativeImage apply(NativeImage input) {
            NativeImage result = new NativeImage(input.getWidth(), input.getHeight(), false);

            for (int y = 1; y < input.getHeight() - 1; y++) {
                for (int x = 1; x < input.getWidth() - 1; x++) {
                    float rSum = 0, gSum = 0, bSum = 0;

                    for (int ky = -1; ky <= 1; ky++) {
                        for (int kx = -1; kx <= 1; kx++) {
                            int pixel = input.getPixelRGBA(x + kx, y + ky);
                            float weight = kernel[ky + 1][kx + 1];

                            rSum += FastColor.ARGB32.red(pixel) * weight;
                            gSum += FastColor.ARGB32.green(pixel) * weight;
                            bSum += FastColor.ARGB32.blue(pixel) * weight;
                        }
                    }

                    int a = FastColor.ARGB32.alpha(input.getPixelRGBA(x, y));
                    int r = Math.max(0, Math.min(255, (int)rSum));
                    int g = Math.max(0, Math.min(255, (int)gSum));
                    int b = Math.max(0, Math.min(255, (int)bSum));

                    result.setPixelRGBA(x, y, FastColor.ARGB32.color(a, r, g, b));
                }
            }

            return result;
        }

        @Override
        public String getName() {
            StringBuilder name = new StringBuilder("convolution_");
            for (float[] row : kernel) {
                for (float value : row) {
                    name.append(value).append("_");
                }
            }
            return name.toString();
        }
    }

    public static class Sharpen extends Convolution {
        public final float strength;

        Sharpen(float strength) {
            super(new float[][]{{0, -strength, 0},
                    {-strength, 1 + 4*strength, -strength},
                    {0, -strength, 0}});
            this.strength = strength;
        }

        @Override
        public String getName() {
            return "sharpen_" + this.strength;
        }
    }

    public static class Emboss extends PixelEffect {
        @Override
        public NativeImage apply(NativeImage input) {
            return new Convolution(ConvolutionKernel.EMBOSS).apply(input);
        }

        @Override
        public String getName() {
            return "emboss";
        }
    }
}
