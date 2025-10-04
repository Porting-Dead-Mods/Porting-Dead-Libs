package com.portingdeadmods.portingdeadlibs.content.blockentities;

import com.portingdeadmods.portingdeadlibs.api.blockentities.ContainerBlockEntity;
import com.portingdeadmods.portingdeadlibs.api.gui.menus.slots.ReferenceSlot;
import com.portingdeadmods.portingdeadlibs.api.utils.IOAction;
import com.portingdeadmods.portingdeadlibs.content.menus.CreativeItemSupplierMenu;
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
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.capabilities.BlockCapability;
import net.neoforged.neoforge.items.IItemHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Map;

public class CreativeItemSupplierBlockEntity extends ContainerBlockEntity implements ReferenceSlot.ReferenceListener<ItemStack>, MenuProvider {
	private ItemStack suppliedItem = ItemStack.EMPTY;
	private int stackSize = 64; // Default to full stack
	private int interval = 1;
	private int tickCounter = 0;

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

	private final CreativeItemHandler creativeItemHandler = new CreativeItemHandler();

	public CreativeItemSupplierBlockEntity(BlockPos pos, BlockState state) {
		super(PDLBlockEntityTypes.CREATIVE_ITEM_SUPPLIER.get(), pos, state);
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
		if (capability == net.neoforged.neoforge.capabilities.Capabilities.ItemHandler.BLOCK) {
			return SidedCapUtils.allExtract(0);
		}
		return Map.of();
	}

	@Override
	public IItemHandler getItemHandler() {
		return creativeItemHandler;
	}

	@Override
	protected void loadData(CompoundTag tag, HolderLookup.Provider provider) {
		if (tag.contains("SuppliedItem")) {
			suppliedItem = ItemStack.parseOptional(provider, tag.getCompound("SuppliedItem"));
		} else {
			suppliedItem = ItemStack.EMPTY;
		}

		stackSize = tag.contains("StackSize") ? Math.min(64, Math.max(1, tag.getInt("StackSize"))) : 64;
		interval = tag.contains("Interval") ? Math.max(1, tag.getInt("Interval")) : 1;
	}

	@Override
	protected void saveData(CompoundTag tag, HolderLookup.Provider provider) {
		if (!suppliedItem.isEmpty()) {
			tag.put("SuppliedItem", suppliedItem.save(provider));
		}
		tag.putInt("StackSize", stackSize);
		tag.putInt("Interval", interval);
	}

	public ItemStack getSuppliedItem() {
		return suppliedItem;
	}

	public void setSuppliedItem(ItemStack item) {
		this.suppliedItem = item.isEmpty() ? ItemStack.EMPTY : item.copy();
		setChanged();
	}

	public int getStackSize() {
		return stackSize;
	}

	public void setStackSize(int size) {
		this.stackSize = Math.min(64, Math.max(1, size));
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
	public void onReferenceChanged(ItemStack newReference) {
		setSuppliedItem(newReference);
	}

	@Override
	public Component getDisplayName() {
		return Component.literal("Creative Item Supplier");
	}

	@Override
	public @Nullable AbstractContainerMenu createMenu(int i, Inventory inventory, Player player) {
		return new CreativeItemSupplierMenu(i, inventory, this, containerData);
	}

	private class CreativeItemHandler implements IItemHandler {
		@Override
		public int getSlots() {
			return 1;
		}

		@Override
		public @NotNull ItemStack getStackInSlot(int slot) {
			if (slot == 0 && !suppliedItem.isEmpty()) {
				ItemStack copy = suppliedItem.copy();
				copy.setCount(stackSize);
				return copy;
			}
			return ItemStack.EMPTY;
		}

		@Override
		public @NotNull ItemStack insertItem(int slot, @NotNull ItemStack stack, boolean simulate) {
			return stack; // Cannot insert items
		}

		@Override
		public @NotNull ItemStack extractItem(int slot, int amount, boolean simulate) {
			if (slot == 0 && !suppliedItem.isEmpty()) {
				ItemStack extractedItem = suppliedItem.copy();
				extractedItem.setCount(Math.min(amount, Math.min(stackSize, extractedItem.getMaxStackSize())));
				return extractedItem;
			}
			return ItemStack.EMPTY;
		}

		@Override
		public int getSlotLimit(int slot) {
			return 64;
		}

		@Override
		public boolean isItemValid(int slot, @NotNull ItemStack stack) {
			return false; // Cannot insert items
		}
	}
}