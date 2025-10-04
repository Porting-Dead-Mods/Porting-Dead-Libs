package com.portingdeadmods.portingdeadlibs.api.gui.menus.slots;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ClickAction;
import net.minecraft.world.inventory.ClickType;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.capability.IFluidHandlerItem;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * A slot that displays a reference FluidStack without actually consuming the fluid.
 * Perfect for filter/pattern settings with fluids.
 */
public class FluidReferenceSlot extends ReferenceSlot<FluidStack> {
	public FluidReferenceSlot(int index, int x, int y, int width, int height, @Nullable ReferenceListener<FluidStack> listener) {
		super(index, x, y, width, height, listener);
	}

	@Override
	protected FluidStack getEmptyReference() {
		return FluidStack.EMPTY;
	}

	@Override
	public boolean setReference(FluidStack stack) {
		return setReferenceDirectly(stack);
	}

	@Override
	protected FluidStack copyReference(FluidStack reference) {
		return reference.copy();
	}

	@Override
	protected boolean areReferencesEqual(FluidStack reference1, FluidStack reference2) {
		return FluidStack.isSameFluidSameComponents(reference1, reference2);
	}

	@Override
	protected boolean isReferenceEmpty(FluidStack reference) {
		return reference.isEmpty();
	}

	@Override
	public boolean handleSpecialClick(Player player, ClickType clickType, ClickAction clickAction) {
		ItemStack heldItem = player.getInventory().getSelected();

		// Handle mouse click with a fluid container
		if (!heldItem.isEmpty()) {
			IFluidHandlerItem fluidHandler = heldItem.getCapability(Capabilities.FluidHandler.ITEM);
			if (fluidHandler != null) {
				FluidStack fluid = fluidHandler.getFluidInTank(0);
				if (!fluid.isEmpty()) {
					return setReferenceDirectly(fluid);
				}
			}
		} else if (clickAction == ClickAction.SECONDARY) {
			// Clear on right-click with empty hand
			clearReference();
			return true;
		}

		return false;
	}

	/**
	 * Gets the current fluid reference
	 */
	public FluidStack getFluidStack() {
		return reference;
	}
}