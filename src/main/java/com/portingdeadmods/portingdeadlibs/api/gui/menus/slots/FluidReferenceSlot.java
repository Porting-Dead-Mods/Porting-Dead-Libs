package com.portingdeadmods.portingdeadlibs.api.gui.menus.slots;

import net.minecraft.client.input.MouseButtonEvent;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.transfer.ResourceHandler;
import net.neoforged.neoforge.transfer.ResourceHandlerUtil;
import net.neoforged.neoforge.transfer.access.ItemAccess;
import net.neoforged.neoforge.transfer.fluid.FluidResource;
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
	public boolean setReference(ItemStack stack) {
		// This is called for direct item stack references
		// We only care about fluid containers
		ResourceHandler<FluidResource> fluidHandler = stack.getCapability(Capabilities.Fluid.ITEM, ItemAccess.forStack(stack));

		if (fluidHandler != null) {
			FluidResource fluid = fluidHandler.getResource(0);
			if (!fluid.isEmpty()) {
				return setReferenceDirectly(fluid.toStack(fluidHandler.getAmountAsInt(0)));
			}
		}
		return false;
	}

	@Override
	protected FluidStack copyReference(FluidStack reference) {
		FluidStack copy = reference.copy();
		// Set to a standard amount for display
		copy.setAmount(1000); // 1 bucket amount for display
		return copy;
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
	public boolean handleSpecialClick(Player player, MouseButtonEvent event) {
		ItemStack heldItem = player.getInventory().getSelectedItem();

		// Handle mouse click with a fluid container
		if (!heldItem.isEmpty()) {
			ResourceHandler<FluidResource> fluidHandler = heldItem.getCapability(Capabilities.Fluid.ITEM, ItemAccess.forStack(heldItem));
			if (fluidHandler != null) {
				FluidResource fluid = fluidHandler.getResource(0);
				if (!fluid.isEmpty()) {
					return setReferenceDirectly(fluid.toStack(fluidHandler.getAmountAsInt(0)));
				}
			}
		} else if (event.isRight()) {
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