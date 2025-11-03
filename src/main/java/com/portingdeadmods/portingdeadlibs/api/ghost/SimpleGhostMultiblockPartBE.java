package com.portingdeadmods.portingdeadlibs.api.ghost;

import com.portingdeadmods.portingdeadlibs.api.blockentities.multiblocks.SavesControllerPosBlockEntity;
import com.portingdeadmods.portingdeadlibs.utils.LazyFinal;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.capabilities.BlockCapability;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.energy.IEnergyStorage;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;
import net.neoforged.neoforge.items.IItemHandler;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Optional;

/**
 * Declared abstract due to the BlockEntityType not being registered.
 * Also, usage of the bare SimpleGhostMultiblockPartBE is discouraged.
 */
public abstract class SimpleGhostMultiblockPartBE extends BlockEntity implements SavesControllerPosBlockEntity, GhostMultiblockPartBE {
    private final LazyFinal<BlockPos> controllerPos = new LazyFinal<>();

    public SimpleGhostMultiblockPartBE(BlockEntityType<?> type, BlockPos pos, BlockState blockState) {
        super(type, pos, blockState);
    }

    @Override
    protected void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.loadAdditional(tag, registries);
        if (tag.contains("ControllerPos")) {
            this.setControllerPos(BlockPos.of(tag.getLong("ControllerPos")));
        }
    }

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.saveAdditional(tag, registries);
        this.controllerPos.ifInitialized(pos -> tag.putLong("ControllerPos", this.controllerPos.getOrThrow().asLong()));
    }

    @Override
    public void setControllerPos(BlockPos controllerPos) {
        if (!this.controllerPos.isInitialized()) {
            this.controllerPos.initialize(controllerPos);
        }
    }

    public BlockPos getControllerPos() {
        return this.controllerPos.getOrThrow();
    }

    @Nullable
    public <T> T tryAndGetCapability(BlockCapability<T, @Nullable Direction> capability) {
        if (this.level != null) {
			if (this.controllerPos.isInitialized()) {
				BlockEntity be = this.level.getBlockEntity(controllerPos.getOrThrow());
				if (be instanceof GhostMultiblockControllerBE controllerBE) {
					List<ResourceLocation> caps = controllerBE.getExposedHandlers().get(this.getBlockPos());
					if (caps == null) return null;

					Optional<ResourceLocation> optional = caps.stream().filter(a -> a.equals(capability.name())).findFirst();
					if (optional.isPresent()) {
						return controllerBE.getHandler(optional.get());
					}
				}
			}
		}
		return null;
    }

    @Nullable
    public IItemHandler getControllerItemHandler() {
        return tryAndGetCapability(Capabilities.ItemHandler.BLOCK);
    }

    @Nullable
    public IFluidHandler getControllerFluidHandler() {
        return tryAndGetCapability(Capabilities.FluidHandler.BLOCK);
    }

    @Nullable
    public IEnergyStorage getControllerEnergyStorage() {
        return tryAndGetCapability(Capabilities.EnergyStorage.BLOCK);
    }
}
