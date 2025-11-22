package com.portingdeadmods.portingdeadlibs.example;

import com.mojang.serialization.Codec;
import com.portingdeadmods.portingdeadlibs.api.blockentities.ContainerBlockEntity;
import com.portingdeadmods.portingdeadlibs.api.utils.IOAction;
import com.portingdeadmods.portingdeadlibs.utils.SerializerUtils;
import com.portingdeadmods.portingdeadlibs.utils.capabilities.HandlerUtils;
import it.unimi.dsi.fastutil.Pair;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.capabilities.BlockCapability;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.common.Tags;
import net.neoforged.neoforge.common.util.INBTSerializable;
import net.neoforged.neoforge.energy.EmptyEnergyStorage;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;
import net.neoforged.neoforge.fluids.capability.templates.FluidTank;
import net.neoforged.neoforge.items.ItemStackHandler;
import org.jetbrains.annotations.Nullable;

import java.util.Map;
import java.util.function.Function;

public class ExampleContainerBlockEntity extends ContainerBlockEntity {
    public ExampleContainerBlockEntity(BlockPos blockPos, BlockState blockState) {
        super(ExampleRegistries.EXAMPLE_CONTAINER_BLOCK_ENTITY.get(), blockPos, blockState);

        ItemStackHandler itemStackHandler = this.addItemHandler(HandlerUtils::newItemStackHandler, builder -> builder
                .slotLimit(slot -> 64)
                .slots(27)
                .validator((slot, item) -> item.is(Items.DIAMOND))
                .onChange(this::onItemsChanged));
        this.addFluidHandler(HandlerUtils::newFluidTank,builder -> builder
                .slotLimit(slot -> 64)
                .slots(27)
                .validator((slot, fluid) -> fluid.is(Tags.Fluids.WATER))
                .onChange(this::onItemsChanged));
        this.addEnergyStorage(HandlerUtils::newEnergystorage,builder -> builder
                .capacity(1000)
                .onChange(() -> {}));
        this.addHandler(Capabilities.EnergyStorage.BLOCK, EmptyEnergyStorage.INSTANCE, SerializerUtils::empty);
        this.addHandler(Capabilities.FluidHandler.BLOCK, new FluidTank(1024), SerializerUtils::fluidTank);
    }

    private void onItemsChanged(int slot) {

    }
}
