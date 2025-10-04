package com.portingdeadmods.portingdeadlibs;

import com.mojang.blaze3d.shaders.FogShape;
import com.mojang.blaze3d.systems.RenderSystem;
import com.portingdeadmods.portingdeadlibs.api.fluids.BaseFluidType;
import com.portingdeadmods.portingdeadlibs.content.client.screens.CreativeFluidSupplierScreen;
import com.portingdeadmods.portingdeadlibs.content.client.screens.CreativeItemSupplierScreen;
import com.portingdeadmods.portingdeadlibs.content.client.screens.CreativePowerSourceScreen;
import com.portingdeadmods.portingdeadlibs.registries.PDLMenuTypes;
import net.minecraft.client.Camera;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.FogRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.FastColor.ARGB32;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.client.event.RegisterMenuScreensEvent;
import net.neoforged.neoforge.client.extensions.common.IClientFluidTypeExtensions;
import net.neoforged.neoforge.client.extensions.common.RegisterClientExtensionsEvent;
import net.neoforged.neoforge.fluids.FluidType;
import net.neoforged.neoforge.registries.NeoForgeRegistries;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector3f;
import org.joml.Vector4i;

@Mod(
        value = PortingDeadLibsClient.MODID,
        dist = Dist.CLIENT
)
public final class PortingDeadLibsClient {
    public static final String MODID = "portingdeadlibs";

    public PortingDeadLibsClient(IEventBus modEventBus, ModContainer modContainer) {
        modEventBus.addListener(this::registerClientExtensions);
        modEventBus.addListener(this::registerMenus);
    }

    private void registerClientExtensions(RegisterClientExtensionsEvent event) {
        for(FluidType fluidType : NeoForgeRegistries.FLUID_TYPES) {
            if (fluidType instanceof final BaseFluidType baseFluidType) {
                event.registerFluidType(new IClientFluidTypeExtensions() {
                    public @NotNull ResourceLocation getStillTexture() {
                        return baseFluidType.getStillTexture();
                    }

                    public @NotNull ResourceLocation getFlowingTexture() {
                        return baseFluidType.getFlowingTexture();
                    }

                    public @Nullable ResourceLocation getOverlayTexture() {
                        return baseFluidType.getOverlayTexture();
                    }

                    public int getTintColor() {
                        Vector4i color = baseFluidType.getColor();
                        return ARGB32.color(color.w, color.x, color.y, color.z);
                    }

                    public @NotNull Vector3f modifyFogColor(Camera camera, float partialTick, ClientLevel level, int renderDistance, float darkenWorldAmount, Vector3f fluidFogColor) {
                        Vector4i color = baseFluidType.getColor();
                        return new Vector3f((float)color.x / 255.0F, (float)color.y / 255.0F, (float)color.z / 255.0F);
                    }

                    public void modifyFogRender(Camera camera, FogRenderer.FogMode mode, float renderDistance, float partialTick, float nearDistance, float farDistance, FogShape shape) {
                        RenderSystem.setShaderFogStart(1.0F);
                        RenderSystem.setShaderFogEnd(6.0F);
                    }
                }, baseFluidType);
            }
        }
    }

    private void registerMenus(RegisterMenuScreensEvent event) {
        event.register(PDLMenuTypes.CREATIVE_FLUID_SUPPLIER.get(), CreativeFluidSupplierScreen::new);
        event.register(PDLMenuTypes.CREATIVE_ITEM_SUPPLIER.get(), CreativeItemSupplierScreen::new);
        event.register(PDLMenuTypes.CREATIVE_POWER_SOURCE.get(), CreativePowerSourceScreen::new);
    }
}
