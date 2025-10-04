package com.portingdeadmods.portingdeadlibs.content.blockentities;

import com.portingdeadmods.portingdeadlibs.api.blockentities.ContainerBlockEntity;
import com.portingdeadmods.portingdeadlibs.api.gui.menus.slots.FluidReferenceSlot;
import com.portingdeadmods.portingdeadlibs.api.gui.menus.slots.ReferenceSlot;
import com.portingdeadmods.portingdeadlibs.api.utils.IOAction;
import com.portingdeadmods.portingdeadlibs.content.menus.CreativeFluidSupplierMenu;
import com.portingdeadmods.portingdeadlibs.registries.PDLBlockEntityTypes;
import com.portingdeadmods.portingdeadlibs.utils.capabilities.SidedCapUtils;
import it.unimi.dsi.fastutil.Pair;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.capabilities.BlockCapability;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.openjdk.nashorn.internal.objects.annotations.Getter;

import java.util.Map;

public class CreativeFluidSupplierBlockEntity extends ContainerBlockEntity implements ReferenceSlot.ReferenceListener<FluidStack>, MenuProvider {
	private FluidStack suppliedFluid = FluidStack.EMPTY;
	private int stackSize = 1000; // Default to full stack
	private int interval = 1;
	private int tickCounter = 0;

	private final CreativeFluidHandler creativeFluidHandler = new CreativeFluidHandler();

	// Container data for GUI syncing
	private final ContainerData containerData = new ContainerData() {
		@Override
		public int get(int index) {
			return switch (index) {
				case 0 -> stackSize;
				case 1 -> interval;
				default -> 0;
			};
		}

		@Override
		public void set(int index, int value) {
			switch (index) {
				case 0 -> stackSize = value;
				case 1 -> interval = value;
			}
		}

		@Override
		public int getCount() {
			return 2;
		}
	};

	public ContainerData getContainerData() {
		return containerData;
	}

	public CreativeFluidSupplierBlockEntity(BlockPos pos, BlockState state) {
		super(PDLBlockEntityTypes.CREATIVE_FLUID_SUPPLIER.get(), pos, state);
	}

	@Override
	public void commonTick() {
		if (!level.isClientSide) {
			tickCounter++;
			if (tickCounter >= interval) {
				tickCounter = 0;
				// In a real implementation, you might push items to adjacent inventories here
				// For now, we'll just let others extract from us
			}
		}
	}

	@Override
	public <T> Map<Direction, Pair<IOAction, int[]>> getSidedInteractions(BlockCapability<T, @Nullable Direction> capability) {
		if (capability == net.neoforged.neoforge.capabilities.Capabilities.FluidHandler.BLOCK) {
			return SidedCapUtils.allExtract(0);
		}
		return Map.of();
	}

	@Override
	public IFluidHandler getFluidHandler() {
		return creativeFluidHandler;
	}

	@Override
	protected void loadData(CompoundTag tag, HolderLookup.Provider provider) {
		if (tag.contains("SuppliedFluid")) {
			suppliedFluid = FluidStack.parseOptional(provider, tag.getCompound("SuppliedFluid"));
		} else {
			suppliedFluid = FluidStack.EMPTY;
		}
	}

	@Override
	protected void saveData(CompoundTag tag, HolderLookup.Provider provider) {
		if (!suppliedFluid.isEmpty()) {
			tag.put("SuppliedFluid", suppliedFluid.save(provider));
		}
	}

	public FluidStack getSuppliedFluid() {
		return suppliedFluid;
	}

	public void setSuppliedFluid(FluidStack fluid) {
		this.suppliedFluid = fluid;
		setChanged();
	}

	@Override
	public void onReferenceChanged(FluidStack newReference) {
		setSuppliedFluid(newReference);
	}

	@Override
	public Component getDisplayName() {
		return null;
	}

	@Override
	public @Nullable AbstractContainerMenu createMenu(int i, Inventory inventory, Player player) {
		return new CreativeFluidSupplierMenu(i, inventory, this, containerData);
	}

	private class CreativeFluidHandler implements IFluidHandler {
		@Override
		public int getTanks() {
			return 1;
		}

		@Override
		public @NotNull FluidStack getFluidInTank(int tank) {
			if (tank == 0) {
				return suppliedFluid.copy();
			}
			return FluidStack.EMPTY;
		}

		@Override
		public int getTankCapacity(int tank) {
			return Integer.MAX_VALUE;
		}

		@Override
		public boolean isFluidValid(int tank, @NotNull FluidStack stack) {
			return false; // Cannot insert fluids
		}

		@Override
		public int fill(FluidStack resource, FluidAction action) {
			return 0; // Cannot fill
		}

		@Override
		public @NotNull FluidStack drain(FluidStack resource, FluidAction action) {
			if (suppliedFluid.isEmpty() || !(FluidStack.isSameFluidSameComponents(suppliedFluid, resource))) {
				return FluidStack.EMPTY;
			}

			FluidStack drained = suppliedFluid.copy();
			drained.setAmount(Math.min(resource.getAmount(), Integer.MAX_VALUE / 2));
			return drained;
		}

		@Override
		public @NotNull FluidStack drain(int maxDrain, FluidAction action) {
			if (suppliedFluid.isEmpty()) {
				return FluidStack.EMPTY;
			}

			FluidStack drained = suppliedFluid.copy();
			drained.setAmount(Math.min(maxDrain, Integer.MAX_VALUE / 2));
			return drained;
		}
	}
}