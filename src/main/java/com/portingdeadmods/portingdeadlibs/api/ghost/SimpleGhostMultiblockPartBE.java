package com.portingdeadmods.portingdeadlibs.api.ghost;

import com.portingdeadmods.portingdeadlibs.api.blockentities.multiblocks.SavesControllerPosBlockEntity;
import com.portingdeadmods.portingdeadlibs.utils.LazyFinal;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.neoforged.neoforge.capabilities.BlockCapability;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.energy.IEnergyStorage;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.transfer.ResourceHandler;
import net.neoforged.neoforge.transfer.energy.EnergyHandler;
import net.neoforged.neoforge.transfer.fluid.FluidResource;
import net.neoforged.neoforge.transfer.item.ItemResource;
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
    protected void loadAdditional(ValueInput input) {
        super.loadAdditional(input);

        Optional<Long> controllerPos = input.getLong("controller_pos");
        Optional<Long> controllerPos1 = input.getLong("ControllerPos");
        Long l = controllerPos.or(() -> controllerPos1).orElse(null);

        if (l != null) {
            this.setControllerPos(BlockPos.of(l));
        }
    }

    @Override
    protected void saveAdditional(ValueOutput output) {
        super.saveAdditional(output);

        this.controllerPos.ifInitialized(pos -> output.putLong("controller_pos", pos.asLong()));
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
					List<Identifier> caps = controllerBE.getExposedHandlers().get(this.getBlockPos());
					if (caps == null) return null;

					Optional<Identifier> optional = caps.stream().filter(a -> a.equals(capability.name())).findFirst();
					if (optional.isPresent()) {
						return controllerBE.getHandler(optional.get());
					}
				}
			}
		}
		return null;
    }

    @Nullable
    public ResourceHandler<ItemResource> getControllerItemHandler() {
        return tryAndGetCapability(Capabilities.Item.BLOCK);
    }

    @Nullable
    public ResourceHandler<FluidResource> getControllerFluidHandler() {
        return tryAndGetCapability(Capabilities.Fluid.BLOCK);
    }

    @Nullable
    public EnergyHandler getControllerEnergyStorage() {
        return tryAndGetCapability(Capabilities.Energy.BLOCK);
    }
}
