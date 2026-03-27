package com.portingdeadmods.portingdeadlibs.example;

import com.portingdeadmods.portingdeadlibs.api.blockentities.SimpleContainerBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.transfer.energy.InfiniteEnergyHandler;
import net.neoforged.neoforge.transfer.energy.SimpleEnergyHandler;
import net.neoforged.neoforge.transfer.fluid.FluidStacksResourceHandler;
import net.neoforged.neoforge.transfer.item.ItemStacksResourceHandler;

public class ExampleContainerBlockEntity extends SimpleContainerBlockEntity {
    public ExampleContainerBlockEntity(BlockPos blockPos, BlockState blockState) {
        super(ExampleRegistries.EXAMPLE_CONTAINER_BLOCK_ENTITY.get(), blockPos, blockState);

        this.addHandler(Capabilities.Item.BLOCK, new ItemStacksResourceHandler(10));
        this.addHandler(Capabilities.Energy.BLOCK, new SimpleEnergyHandler(1000));
        this.addHandlerNoSave(Capabilities.Energy.BLOCK, new InfiniteEnergyHandler());
        this.addHandler(Capabilities.Fluid.BLOCK, new FluidStacksResourceHandler(10, 1000));

    }

    private void onItemsChanged(int slot) {

    }
}
