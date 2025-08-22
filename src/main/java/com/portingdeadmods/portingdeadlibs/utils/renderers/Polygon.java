package com.portingdeadmods.portingdeadlibs.utils.renderers;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Renderable;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.util.FastColor;
import org.joml.Matrix4f;
import org.joml.Vector2f;

import java.util.ArrayList;
import java.util.List;

public class Polygon implements Renderable {
    private final List<Vector2f> vertices;
    private int color = 0xFFFFFFFF;
    private boolean filled = true;
    private float lineWidth = 1.0f;

    public Polygon() {
        this.vertices = new ArrayList<>();
    }

    public Polygon(List<Vector2f> vertices) {
        this.vertices = new ArrayList<>(vertices);
    }

    public static Polygon regular(int sides, float radius) {
        List<Vector2f> verts = new ArrayList<>();
        double angleStep = 2 * Math.PI / sides;

        for (int i = 0; i < sides; i++) {
            float x = (float)(radius * Math.cos(i * angleStep));
            float y = (float)(radius * Math.sin(i * angleStep));
            verts.add(new Vector2f(x, y));
        }

        return new Polygon(verts);
    }

    public static Polygon rectangle(float width, float height) {
        List<Vector2f> verts = new ArrayList<>();
        verts.add(new Vector2f(-width/2, -height/2));
        verts.add(new Vector2f(width/2, -height/2));
        verts.add(new Vector2f(width/2, height/2));
        verts.add(new Vector2f(-width/2, height/2));
        return new Polygon(verts);
    }

    public Polygon addVertex(float x, float y) {
        vertices.add(new Vector2f(x, y));
        return this;
    }

    /**
     * Radians
     */
    public Polygon rotateCW(double theta) {
        for (Vector2f vertex : vertices) {
            float x = vertex.x;
            float y = vertex.y;
            vertex.x = (float) (x * Math.cos(theta) - y * Math.sin(theta));
            vertex.y = (float) (x * Math.sin(theta) + y * Math.cos(theta));
        }

        return this;
    }

    public Polygon rotateCCW(double theta) {
        return rotateCW(-theta);
    }

    public Polygon translate(float dx, float dy) {
        for (Vector2f vertex : vertices) {
            vertex.add(dx, dy);
        }
        return this;
    }

    public Polygon translateX(float dx) {
        return translate(dx, 0);
    }

    public Polygon translateY(float dy) {
        return translate(0, dy);
    }

    public Polygon setColor(int color) {
        this.color = color;
        return this;
    }

    public Polygon setColor(int a, int r, int g, int b) {
        return this.setColor(FastColor.ARGB32.color(a, r, g, b));
    }

    public Polygon setFilled(boolean filled) {
        this.filled = filled;
        return this;
    }

    public Polygon setLineWidth(float width) {
        this.lineWidth = width;
        return this;
    }

    @Override
    public void render(GuiGraphics guiGraphics, int x, int y, float partialTick) {
        if (vertices.size() < 3) return;

        PoseStack poseStack = guiGraphics.pose();
        poseStack.pushPose();

        Matrix4f matrix = poseStack.last().pose();

        float r = FastColor.ARGB32.red(color) / 255f;
        float g = FastColor.ARGB32.green(color) / 255f;
        float b = FastColor.ARGB32.blue(color) / 255f;
        float a = FastColor.ARGB32.alpha(color) / 255f;

        if (a == 0) return;

        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        RenderSystem.disableDepthTest();
        RenderSystem.disableCull();
        RenderSystem.setShader(GameRenderer::getPositionColorShader);

        Tesselator tesselator = Tesselator.getInstance();

        if (filled) {
            renderFilledPolygon(tesselator, matrix, x, y, r, g, b, a);
        } else {
            // renderOutlinePolygon(tesselator, matrix, x, y, r, g, b, a);
        }

        RenderSystem.enableDepthTest();
        RenderSystem.enableCull();
        RenderSystem.disableBlend();
        poseStack.popPose();
    }

    private void renderFilledPolygon(Tesselator tesselator, Matrix4f matrix, int x, int y,
                                     float r, float g, float b, float a) {
        BufferBuilder buffer = tesselator.begin(VertexFormat.Mode.TRIANGLES,
                DefaultVertexFormat.POSITION_COLOR);

        if (vertices.size() >= 3) {
            Vector2f first = vertices.get(0);
            for (int i = 1; i < vertices.size() - 1; i++) {
                Vector2f second = vertices.get(i);
                Vector2f third = vertices.get(i + 1);

                buffer.addVertex(matrix, first.x + x, first.y + y, 0)
                        .setColor(r, g, b, a);
                buffer.addVertex(matrix, second.x + x, second.y + y, 0)
                        .setColor(r, g, b, a);
                buffer.addVertex(matrix, third.x + x, third.y + y, 0)
                        .setColor(r, g, b, a);
            }
        }

        BufferUploader.drawWithShader(buffer.buildOrThrow());
    }


    /* WIP, currently broken for some reason.
    private void renderOutlinePolygon(Tesselator tesselator, Matrix4f matrix, int x, int y,
                                      float r, float g, float b, float a) {
        RenderSystem.lineWidth(lineWidth);

        BufferBuilder buffer = tesselator.begin(VertexFormat.Mode.LINE_STRIP,
                DefaultVertexFormat.POSITION_COLOR);

        for (Vector2f vertex : vertices) {
            buffer.addVertex(matrix, vertex.x + x, vertex.y + y, 0)
                    .setColor(r, g, b, a);
        }

        if (!vertices.isEmpty()) {
            Vector2f first = vertices.get(0);
            buffer.addVertex(matrix, first.x, first.y, 0)
                    .setColor(r, g, b, a);
        }

        BufferUploader.drawWithShader(buffer.buildOrThrow());
        RenderSystem.lineWidth(1.0f);
    }
    */

    public static class Builder {
        private final Polygon polygon = new Polygon();

        public Builder vertex(float x, float y) {
            polygon.addVertex(x, y);
            return this;
        }

        public Builder color(int color) {
            polygon.setColor(color);
            return this;
        }

        public Builder color(int a, int r, int g, int b) {
            polygon.setColor(a, r, g, b);
            return this;
        }

        public Builder filled(boolean filled) {
            polygon.setFilled(filled);
            return this;
        }

        public Builder lineWidth(float width) {
            polygon.setLineWidth(width);
            return this;
        }

        /**
         * Radians
         */
        public Builder rotateCW(double theta) {
            polygon.rotateCW(theta);
            return this;
        }

        public Builder rotateCCW(double theta) {
            polygon.rotateCCW(theta);
            return this;
        }

        public Builder translate(float x, float y) {
            polygon.translate(x, y);
            return this;
        }

        public Builder translateX(float x) {
            polygon.translateX(x);
            return this;
        }

        public Builder translateY(float y) {
            polygon.translateY(y);
            return this;
        }

        public Polygon build() {
            return polygon;
        }
    }
}