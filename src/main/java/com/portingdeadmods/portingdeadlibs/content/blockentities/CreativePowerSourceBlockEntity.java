package com.portingdeadmods.portingdeadlibs.content.blockentities;

import com.portingdeadmods.portingdeadlibs.api.blockentities.ContainerBlockEntity;
import com.portingdeadmods.portingdeadlibs.api.utils.IOAction;
import com.portingdeadmods.portingdeadlibs.content.menus.CreativePowerSourceMenu;
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
import net.minecraft.world.inventory.SimpleContainerData;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.capabilities.BlockCapability;
import net.neoforged.neoforge.energy.IEnergyStorage;
import org.jetbrains.annotations.Nullable;

import java.util.Map;

public class CreativePowerSourceBlockEntity extends ContainerBlockEntity implements MenuProvider {
	// Default to 1,000,000 energy per tick
	private int energyPerTick = 1_000_000;
	// Default interval is 1 tick
	private int interval = 1;
	private int tickCounter = 0;

	// Container data for GUI syncing
	private final ContainerData containerData = new ContainerData() {
		@Override
		public int get(int index) {
			return switch (index) {
				case 0 -> energyPerTick;
				case 1 -> interval;
				default -> 0;
			};
		}

		@Override
		public void set(int index, int value) {
			switch (index) {
				case 0 -> energyPerTick = value;
				case 1 -> interval = value;
			}
		}

		@Override
		public int getCount() {
			return 2;
		}
	};

	// A creative power source that implements IEnergyStorage but generates infinite energy
	private final CreativeEnergyStorage creativeEnergyStorage = new CreativeEnergyStorage();

	public CreativePowerSourceBlockEntity(BlockPos pos, BlockState state) {
		super(PDLBlockEntityTypes.CREATIVE_POWER_SOURCE.get(), pos, state);
	}

	@Override
	public void commonTick() {
		if (!level.isClientSide) {
			tickCounter++;
			if (tickCounter >= interval) {
				tickCounter = 0;
				// In a real implementation, you might push energy to adjacent blocks here
				// For now, we'll just let others extract from us
			}
		}
	}

	@Override
	public <T> Map<Direction, Pair<IOAction, int[]>> getSidedInteractions(BlockCapability<T, @Nullable Direction> capability) {
		if (capability == net.neoforged.neoforge.capabilities.Capabilities.EnergyStorage.BLOCK) {
			return SidedCapUtils.allExtract(0);
		}
		return Map.of();
	}

	@Override
	public IEnergyStorage getEnergyStorage() {
		return creativeEnergyStorage;
	}

	@Override
	protected void loadData(CompoundTag tag, HolderLookup.Provider provider) {
		energyPerTick = tag.getInt("EnergyPerTick");
		interval = Math.max(1, tag.getInt("Interval"));
	}

	@Override
	protected void saveData(CompoundTag tag, HolderLookup.Provider provider) {
		tag.putInt("EnergyPerTick", energyPerTick);
		tag.putInt("Interval", interval);
	}

	public int getEnergyPerTick() {
		return energyPerTick;
	}

	public void setEnergyPerTick(int energyPerTick) {
		this.energyPerTick = Math.max(1, energyPerTick);
		setChanged();
	}

	public int getInterval() {
		return interval;
	}

	public void setInterval(int interval) {
		this.interval = Math.max(1, interval);
		tickCounter = 0; // Reset counter when interval changes
		setChanged();
	}

	public ContainerData getContainerData() {
		return containerData;
	}

	@Override
	public Component getDisplayName() {
		return Component.literal("Creative Power Source");
	}

	@Override
	public @Nullable AbstractContainerMenu createMenu(int i, Inventory inventory, Player player) {
		return new CreativePowerSourceMenu(i, inventory, this, new SimpleContainerData(2));
	}

	// Custom energy storage for creative mode that provides unlimited energy
	private class CreativeEnergyStorage implements IEnergyStorage {
		@Override
		public int receiveEnergy(int maxReceive, boolean simulate) {
			return 0; // Cannot receive energy, only provide it
		}

		@Override
		public int extractEnergy(int maxExtract, boolean simulate) {
			return Math.min(maxExtract, energyPerTick); // Provide energy up to the configured amount per operation
		}

		@Override
		public int getEnergyStored() {
			return Integer.MAX_VALUE; // Always reports max energy
		}

		@Override
		public int getMaxEnergyStored() {
			return Integer.MAX_VALUE; // Unlimited max energy
		}

		@Override
		public boolean canExtract() {
			return true; // Can always extract
		}

		@Override
		public boolean canReceive() {
			return false; // Cannot receive energy
		}
	}
}