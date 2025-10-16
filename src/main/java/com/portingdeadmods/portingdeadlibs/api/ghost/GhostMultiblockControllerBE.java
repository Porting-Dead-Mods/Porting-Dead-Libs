package com.portingdeadmods.portingdeadlibs.api.ghost;

import com.portingdeadmods.portingdeadlibs.api.blockentities.ContainerBlockEntity;
import com.portingdeadmods.portingdeadlibs.utils.LazyFinal;
import com.portingdeadmods.portingdeadlibs.utils.UniqueArray;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.capabilities.BlockCapability;
import net.neoforged.neoforge.capabilities.Capabilities;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public abstract class GhostMultiblockControllerBE extends ContainerBlockEntity implements MenuProvider {
    public final LazyFinal<List<BlockPos>> partPos = LazyFinal.create();
    public final LazyFinal<List<BlockPos>> itemHandlerPartPos = LazyFinal.create();
    public final LazyFinal<List<BlockPos>> fluidHandlerPartPos = LazyFinal.create();
    public final LazyFinal<List<BlockPos>> energyHandlerPartPos = LazyFinal.create();

    public GhostMultiblockControllerBE(BlockEntityType<?> blockEntityType, BlockPos blockPos, BlockState blockState) {
        super(blockEntityType, blockPos, blockState);
    }

    public void setPartPositions(List<BlockPos> partPositions) {
        if (!this.partPos.isInitialized()) {
            this.partPos.initialize(partPositions);
        }
    }

    public void setItemHandlerPartPos(List<BlockPos> itemHandlerPartPos) {
        if (!this.itemHandlerPartPos.isInitialized()) {
            this.itemHandlerPartPos.initialize(itemHandlerPartPos);
        }
    }

    public void setFluidHandlerPartPos(List<BlockPos> fluidHandlerPartPos) {
        if (!this.fluidHandlerPartPos.isInitialized()) {
            this.fluidHandlerPartPos.initialize(fluidHandlerPartPos);
        }
    }

    public void setEnergyHandlerPartPos(List<BlockPos> energyHandlerPartPos) {
        if (!this.energyHandlerPartPos.isInitialized()) {
            this.energyHandlerPartPos.initialize(energyHandlerPartPos);
        }
    }

    public void setPartPositions(List<BlockPos> partPositions, List<BlockPos> itemHandlerPartPos, List<BlockPos> fluidHandlerPartPos, List<BlockPos> energyHandlerPartPos) {
        setPartPositions(partPositions);
        if (!this.itemHandlerPartPos.isInitialized()) {
            this.itemHandlerPartPos.initialize(itemHandlerPartPos);
        }
        if (!this.fluidHandlerPartPos.isInitialized()) {
            this.fluidHandlerPartPos.initialize(fluidHandlerPartPos);
        }
        if (!this.energyHandlerPartPos.isInitialized()) {
            this.energyHandlerPartPos.initialize(energyHandlerPartPos);
        }
    }

    @Override
    protected void saveData(CompoundTag tag, HolderLookup.Provider registries) {
        super.saveData(tag, registries);
        partPos.ifInitialized(pos -> tag.putLongArray("part_positions", pos.stream().mapToLong(BlockPos::asLong).toArray()));
        itemHandlerPartPos.ifInitialized(pos -> tag.putLongArray("item_handler_part_positions", pos.stream().mapToLong(BlockPos::asLong).toArray()));
        fluidHandlerPartPos.ifInitialized(pos -> tag.putLongArray("fluid_handler_part_positions", pos.stream().mapToLong(BlockPos::asLong).toArray()));
        energyHandlerPartPos.ifInitialized(pos -> tag.putLongArray("energy_handler_part_positions", pos.stream().mapToLong(BlockPos::asLong).toArray()));
    }

    @Override
    protected void loadData(CompoundTag tag, HolderLookup.Provider registries) {
        super.loadData(tag, registries);
        if (tag.contains("part_positions")) {
            long[] partPositions = tag.getLongArray("part_positions");
            List<BlockPos> positions = new UniqueArray<>();
            for (long posLong : partPositions) {
                positions.add(BlockPos.of(posLong));
            }
            this.setPartPositions(positions);
        }
        if (tag.contains("item_handler_part_positions")) {
            long[] partPositions = tag.getLongArray("item_handler_part_positions");
            List<BlockPos> positions = new UniqueArray<>();
            for (long posLong : partPositions) {
                positions.add(BlockPos.of(posLong));
            }
            this.setItemHandlerPartPos(positions);
        }
        if (tag.contains("fluid_handler_part_positions")) {
            long[] partPositions = tag.getLongArray("fluid_handler_part_positions");
            List<BlockPos> positions = new UniqueArray<>();
            for (long posLong : partPositions) {
                positions.add(BlockPos.of(posLong));
            }
            this.setFluidHandlerPartPos(positions);
        }
        if (tag.contains("energy_handler_part_positions")) {
            long[] partPositions = tag.getLongArray("energy_handler_part_positions");
            List<BlockPos> positions = new UniqueArray<>();
            for (long posLong : partPositions) {
                positions.add(BlockPos.of(posLong));
            }
            this.setEnergyHandlerPartPos(positions);
        }
    }

    /**
     * Override this method to control which capabilities are exposed by which parts.
     * @param capability The capability being requested.
     * @param partPos The position of the part requesting the capability.
     * @return True if the capability should be exposed, false otherwise.
     */
    public <T> boolean shouldExposeCapability(BlockCapability<T, @Nullable Direction> capability, BlockPos partPos) {
        if (capability == Capabilities.ItemHandler.BLOCK) {
            return itemHandlerPartPos.getOrDefault(List.of()).contains(partPos);
        }
        if (capability == Capabilities.FluidHandler.BLOCK) {
            return fluidHandlerPartPos.getOrDefault(List.of()).contains(partPos);
        }
        if (capability == Capabilities.EnergyStorage.BLOCK) {
            return energyHandlerPartPos.getOrDefault(List.of()).contains(partPos);
        }
        return true;
    }

    @Override
    public abstract Component getDisplayName();

    @Nullable
    @Override
    public abstract AbstractContainerMenu createMenu(int containerId, Inventory inventory, Player player);
}
