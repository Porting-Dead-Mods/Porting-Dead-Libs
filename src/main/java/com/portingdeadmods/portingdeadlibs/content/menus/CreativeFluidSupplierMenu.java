package com.portingdeadmods.portingdeadlibs.content.menus;

import com.portingdeadmods.portingdeadlibs.content.blockentities.CreativeFluidSupplierBlockEntity;
import com.portingdeadmods.portingdeadlibs.registries.PDLMenuTypes;
import com.portingdeadmods.portingdeadlibs.api.gui.menus.slots.FluidReferenceSlot;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.material.Fluids;
import net.neoforged.neoforge.fluids.FluidStack;
import org.jetbrains.annotations.NotNull;

public class CreativeFluidSupplierMenu extends CreativeBaseMenu<CreativeFluidSupplierBlockEntity> {
	// The amount per operation is stored in data.get(0)
	// The interval is stored in data.get(1)
	private final FluidReferenceSlot referenceSlot;

	public CreativeFluidSupplierMenu(int containerId, Inventory inv, CreativeFluidSupplierBlockEntity blockEntity, ContainerData data) {
		super(PDLMenuTypes.CREATIVE_FLUID_SUPPLIER.get(), containerId, inv, blockEntity, data);

		this.referenceSlot = new FluidReferenceSlot(0, 80, 35, 18, 18, blockEntity);
		this.referenceSlot.setReference(new FluidStack(Fluids.WATER, 1000));

		if (!blockEntity.getSuppliedFluid().isEmpty()) {
			this.referenceSlot.setReferenceDirectly(blockEntity.getSuppliedFluid());
		}

		addPlayerInventory(inv);
		addPlayerHotbar(inv);
		addFluidReferenceSlot(this.referenceSlot);
	}

	// Factory method for client-side creation
	public static CreativeFluidSupplierMenu create(int containerId, Inventory inv, FriendlyByteBuf extraData) {
		BlockEntity entity = inv.player.level().getBlockEntity(extraData.readBlockPos());
		if (entity instanceof CreativeFluidSupplierBlockEntity creativeFluidSupplier) {
			return new CreativeFluidSupplierMenu(containerId, inv, creativeFluidSupplier, creativeFluidSupplier.getContainerData());
		}
		throw new IllegalStateException("Invalid block entity!");
	}

	public int getAmountPerOperation() {
		return getOutputSize();
	}

	public void setAmountPerOperation(int amount) {
		setOutputSize(amount);
	}

	@Override
	public @NotNull ItemStack quickMoveStack(@NotNull Player player, int index) {
		// Don't allow quick move into reference slots
		return ItemStack.EMPTY;
	}

	public FluidReferenceSlot getReferenceSlot() {
		return referenceSlot;
	}
}