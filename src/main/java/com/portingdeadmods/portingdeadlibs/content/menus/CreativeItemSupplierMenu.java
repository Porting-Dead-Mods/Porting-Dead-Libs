package com.portingdeadmods.portingdeadlibs.content.menus;

import com.portingdeadmods.portingdeadlibs.content.blockentities.CreativeFluidSupplierBlockEntity;
import com.portingdeadmods.portingdeadlibs.content.blockentities.CreativeItemSupplierBlockEntity;
import com.portingdeadmods.portingdeadlibs.registries.PDLMenuTypes;
import com.portingdeadmods.portingdeadlibs.api.gui.menus.slots.ItemReferenceSlot;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ClickAction;
import net.minecraft.world.inventory.ClickType;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.SimpleContainerData;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.entity.BlockEntity;
import org.jetbrains.annotations.NotNull;

public class CreativeItemSupplierMenu extends CreativeBaseMenu<CreativeItemSupplierBlockEntity> {
	// The stack size is stored in data.get(0)
	// The interval is stored in data.get(1)
	private final ItemReferenceSlot referenceSlot;

	public CreativeItemSupplierMenu(int containerId, Inventory inv, CreativeItemSupplierBlockEntity blockEntity, ContainerData data) {
		super(PDLMenuTypes.CREATIVE_ITEM_SUPPLIER.get(), containerId, inv, blockEntity, data);

		this.referenceSlot = new ItemReferenceSlot(0, 80, 35, 18, 18, blockEntity);
		this.referenceSlot.setReference(Items.STONE.getDefaultInstance());

		if (!blockEntity.getSuppliedItem().isEmpty()) {
			this.referenceSlot.setReferenceDirectly(blockEntity.getSuppliedItem());
		}

		addPlayerInventory(inv);
		addPlayerHotbar(inv);
		addItemReferenceSlot(this.referenceSlot);
	}

	// Factory method for client-side creation
	public static CreativeItemSupplierMenu create(int containerId, Inventory inv, FriendlyByteBuf extraData) {
		BlockEntity entity = inv.player.level().getBlockEntity(extraData.readBlockPos());
		if (entity instanceof CreativeItemSupplierBlockEntity creativeItemSupplierBlockEntity) {
			return new CreativeItemSupplierMenu(containerId, inv, creativeItemSupplierBlockEntity, creativeItemSupplierBlockEntity.getContainerData());
		}
		throw new IllegalStateException("Invalid block entity!");
	}

	@Override
	public @NotNull ItemStack quickMoveStack(@NotNull Player player, int index) {
		// Don't allow quick move into reference slots
		return ItemStack.EMPTY;
	}

	public ItemReferenceSlot getReferenceSlot() {
		return referenceSlot;
	}
}