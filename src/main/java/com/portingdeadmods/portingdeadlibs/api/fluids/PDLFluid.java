package com.portingdeadmods.portingdeadlibs.api.fluids;

import net.minecraft.world.item.BucketItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.neoforged.neoforge.fluids.BaseFlowingFluid;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.FluidType;
import net.neoforged.neoforge.registries.DeferredItem;
import org.joml.Vector4i;

import java.util.function.Supplier;

public abstract class PDLFluid {
    public Supplier<BaseFlowingFluid.Source> stillFluid;
    public Supplier<BaseFlowingFluid.Flowing> flowingFluid;

    public Supplier<FluidType> fluidType;

    public Supplier<LiquidBlock> block;
    public Supplier<BucketItem> bucket;
    public DeferredItem<BucketItem> deferredBucket;

    protected BaseFlowingFluid.Properties properties;

    protected final String name;

    public PDLFluid(String name) {
        this.stillFluid = () -> new BaseFlowingFluid.Source(fluidProperties());
        this.flowingFluid = () -> new BaseFlowingFluid.Flowing(fluidProperties());
        this.name = name;
    }

    public BlockBehaviour.Properties blockProperties() {
        return BlockBehaviour.Properties.of();
    }

    public BaseFlowingFluid.Properties fluidProperties() {
        return new BaseFlowingFluid.Properties(fluidType, stillFluid, flowingFluid);
    }

    public BaseFlowingFluid.Source getStillFluid() {
        return stillFluid.get();
    }

    public BaseFlowingFluid.Flowing getFlowingFluid() {
        return flowingFluid.get();
    }

    public Supplier<FluidType> getFluidType() {
        return fluidType;
    }

    public Item getBucket() {
        return getDeferredBucket().get();
    }

    public DeferredItem<BucketItem> getDeferredBucket() {
        return deferredBucket;
    }

    public String getName() {
        return name;
    }

    public Supplier<FluidType> registerFluidType(FluidType.Properties properties, Vector4i color, FluidTemplate template) {
        return () -> new BaseFluidType(template.getStillTexture(), template.getFlowingTexture(), template.getOverlayTexture(), color, properties);
    }

    public FluidStack toStack() {
        return toStack(1000);
    }

    public FluidStack toStack(int amount) {
        return new FluidStack(this.stillFluid.get(), amount);
    }
}

