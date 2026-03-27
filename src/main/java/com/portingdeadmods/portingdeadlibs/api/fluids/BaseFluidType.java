package com.portingdeadmods.portingdeadlibs.api.fluids;

import net.minecraft.resources.Identifier;
import net.neoforged.neoforge.fluids.FluidType;
import org.joml.Vector4i;

public class BaseFluidType extends FluidType {
    private final Identifier stillTexture;
    private final Identifier flowingTexture;
    private final Identifier overlayTexture;
    private final Vector4i color;

    public BaseFluidType(Identifier stillTexture, Identifier flowingTexture, Identifier overlayTexture, Vector4i color, FluidType.Properties properties) {
        super(properties);
        this.stillTexture = stillTexture;
        this.flowingTexture = flowingTexture;
        this.overlayTexture = overlayTexture;
        this.color = color;
    }

    public Identifier getStillTexture() {
        return stillTexture;
    }

    public Identifier getFlowingTexture() {
        return flowingTexture;
    }

    public Identifier getOverlayTexture() {
        return overlayTexture;
    }

    public Vector4i getColor() {
        return color;
    }
}