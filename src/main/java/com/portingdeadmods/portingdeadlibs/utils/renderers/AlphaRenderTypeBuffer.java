package com.portingdeadmods.portingdeadlibs.utils.renderers;

import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.neoforged.neoforge.client.model.pipeline.VertexConsumerWrapper;
import org.jetbrains.annotations.NotNull;

public class AlphaRenderTypeBuffer implements MultiBufferSource {
    private final MultiBufferSource inner;

    public AlphaRenderTypeBuffer(MultiBufferSource inner) {
        this.inner = inner;
    }

    @Override
    public @NotNull VertexConsumer getBuffer(@NotNull RenderType type) {
        return new AlphaWrapper(inner.getBuffer(type));
    }

    private static class AlphaWrapper extends VertexConsumerWrapper {
        public AlphaWrapper(VertexConsumer consumer) {
            super(consumer);
        }

        @Override
        public VertexConsumer setColor(int color) {
            super.setColor(color & 0x88FFFFFF);
            return this;
        }
    }
}