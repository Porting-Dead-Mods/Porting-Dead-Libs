package com.portingdeadmods.portingdeadlibs.api.gui.menus.slots;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ClickAction;
import net.minecraft.world.inventory.ClickType;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * A slot that displays a reference ItemStack without actually consuming it.
 * Perfect for filter/pattern settings.
 */
public class ItemReferenceSlot extends ReferenceSlot<ItemStack> {
	public ItemReferenceSlot(int index, int x, int y, int width, int height, @Nullable ReferenceListener<ItemStack> listener) {
		super(index, x, y, width, height, listener);
	}

	@Override
	protected ItemStack getEmptyReference() {
		return ItemStack.EMPTY;
	}

	@Override
	public boolean setReference(ItemStack stack) {
		if (stack.isEmpty()) {
			clearReference();
			return true;
		}

		// Copy the stack with count 1 to use as reference
		ItemStack reference = stack.copy();
		reference.setCount(1);

		return setReferenceDirectly(reference);
	}

	@Override
	protected ItemStack copyReference(ItemStack reference) {
		return reference.copy();
	}

	@Override
	protected boolean areReferencesEqual(ItemStack reference1, ItemStack reference2) {
		return ItemStack.isSameItemSameComponents(reference1, reference2);
	}

	@Override
	protected boolean isReferenceEmpty(ItemStack reference) {
		return reference.isEmpty();
	}

	@Override
	public boolean handleSpecialClick(Player player, ClickType clickType, ClickAction clickAction) {
		if (clickAction == ClickAction.SECONDARY && player.getInventory().getSelected().isEmpty()) {
			// Clear on right-click with empty hand
			clearReference();
			return true;
		}
		return false;
	}
}