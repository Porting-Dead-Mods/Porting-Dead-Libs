package com.portingdeadmods.portingdeadlibs.api.gui.menus.slots;

import com.portingdeadmods.portingdeadlibs.api.gui.menus.slots.AbstractSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ClickAction;
import net.minecraft.world.inventory.ClickType;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * A generic base reference slot that doesn't actually consume items when set.
 * Used for pattern/filter/setting slots.
 * @param <T> The type of reference (ItemStack or FluidStack)
 */
public abstract class ReferenceSlot<T> extends AbstractSlot {
	protected T reference;
	protected final ReferenceListener<T> listener;
	private final int width;
	private final int height;

	/**
	 * Listener interface for reference changes
	 */
	public interface ReferenceListener<T> {
		void onReferenceChanged(T newReference);
	}

	public ReferenceSlot(int index, int x, int y, int width, int height, @Nullable ReferenceListener<T> listener) {
		super(index, x, y);
		this.listener = listener;
		this.reference = getEmptyReference();
		this.width = width;
		this.height = height;
	}

	/**
	 * Returns an empty reference of type T
	 */
	protected abstract T getEmptyReference();

	/**
	 * Called when a player clicks on this slot with an item
	 * @param stack The stack the player is holding
	 * @return true if the reference was set, false otherwise
	 */
	public abstract boolean setReference(T stack);

	/**
	 * Sets the reference directly
	 * @param reference The reference to set
	 * @return true if the reference was set, false otherwise
	 */
	public boolean setReferenceDirectly(T reference) {
		if (reference == null || isReferenceEmpty(reference)) {
			clearReference();
			return true;
		}

		T copyOfReference = copyReference(reference);

		// Only notify if the reference actually changed
		if (!areReferencesEqual(copyOfReference, this.reference)) {
			this.reference = copyOfReference;
			if (listener != null) {
				listener.onReferenceChanged(copyOfReference);
			}
			return true;
		}
		return false;
	}

	/**
	 * Creates a copy of the reference
	 */
	protected abstract T copyReference(T reference);

	/**
	 * Checks if two references are equal
	 */
	protected abstract boolean areReferencesEqual(T reference1, T reference2);

	/**
	 * Checks if a reference is empty
	 */
	protected abstract boolean isReferenceEmpty(T reference);

	/**
	 * Clears the current reference
	 */
	public void clearReference() {
		if (!isReferenceEmpty(reference)) {
			reference = getEmptyReference();
			if (listener != null) {
				listener.onReferenceChanged(reference);
			}
		}
	}

	/**
	 * Gets the current reference
	 */
	public T getReference() {
		return reference;
	}

	/**
	 * Override this to handle clicking with a fluid container
	 * @param player The player clicking
	 * @param clickType The type of click
	 * @param clickAction The click action
	 * @return true if the click was handled
	 */
	public boolean handleSpecialClick(Player player, ClickType clickType, ClickAction clickAction) {
		return false;
	}
}