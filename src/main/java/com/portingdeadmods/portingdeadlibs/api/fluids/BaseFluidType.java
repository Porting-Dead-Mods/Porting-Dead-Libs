package com.portingdeadmods.portingdeadlibs.api.fluids;

import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.fluids.FluidType;
import org.joml.Vector4i;

public class BaseFluidType extends FluidType {
    private final ResourceLocation stillTexture;
    private final ResourceLocation flowingTexture;
    private final ResourceLocation overlayTexture;
    private final Vector4i color;

    public BaseFluidType(ResourceLocation stillTexture, ResourceLocation flowingTexture, ResourceLocation overlayTexture, Vector4i color, FluidType.Properties properties) {
        super(properties);
        this.stillTexture = stillTexture;
        this.flowingTexture = flowingTexture;
        this.overlayTexture = overlayTexture;
        this.color = color;
    }

    public ResourceLocation getStillTexture() {
        return stillTexture;
    }

    public ResourceLocation getFlowingTexture() {
        return flowingTexture;
    }

    public ResourceLocation getOverlayTexture() {
        return overlayTexture;
    }

    public Vector4i getColor() {
        return color;
    }
}