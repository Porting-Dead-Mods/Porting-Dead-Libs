package com.portingdeadmods.portingdeadlibs.utils.capabilities;

import com.portingdeadmods.portingdeadlibs.api.capabilities.DynamicFluidTank;
import it.unimi.dsi.fastutil.ints.Int2IntFunction;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.energy.EnergyStorage;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.capability.templates.FluidTank;
import net.neoforged.neoforge.items.ItemStackHandler;

import java.util.function.BiPredicate;
import java.util.function.Consumer;

public final class HandlerUtils {
    public static ItemStackHandler newItemStackHandler(BiPredicate<Integer, ItemStack> validator, Int2IntFunction slotLimit, Consumer<Integer> onChange, int slots) {
        return new ItemStackHandler(slots) {
            @Override
            public int getSlotLimit(int slot) {
                return slotLimit.applyAsInt(slot);
            }

            @Override
            public boolean isItemValid(int slot, ItemStack stack) {
                return validator.test(slot, stack);
            }

            @Override
            protected void onContentsChanged(int slot) {
                onChange.accept(slot);
            }
        };
    }

    public static FluidTank newFluidTank(BiPredicate<Integer, FluidStack> fluidValidator, Int2IntFunction tankLimit, Consumer<Integer> onChange, int ignoredTanks) {
        return new FluidTank(tankLimit.applyAsInt(0)) {
            @Override
            public int getTankCapacity(int tank) {
                return tankLimit.applyAsInt(tank);
            }

            @Override
            public boolean isFluidValid(int tank, FluidStack stack) {
                return fluidValidator.test(tank, stack);
            }

            @Override
            protected void onContentsChanged() {
                onChange.accept(0);
            }
        };
    }

    public static DynamicFluidTank newDynamicFluidTank(BiPredicate<Integer, FluidStack> fluidValidator, Int2IntFunction tankLimit, Consumer<Integer> onChange, int ignoredTanks) {
        return new DynamicFluidTank(tankLimit.applyAsInt(0)) {
            @Override
            public boolean isFluidValid(int tank, FluidStack stack) {
                return fluidValidator.test(tank, stack);
            }

            @Override
            protected void onContentsChanged() {
                onChange.accept(0);
            }
        };
    }

    public static EnergyStorage newEnergystorage(int capacity, int maxReceive, int maxExtract, Runnable onChanged) {
        return new EnergyStorage(capacity, maxReceive, maxExtract) {
            @Override
            public int receiveEnergy(int toReceive, boolean simulate) {
                int receivedEnergy = super.receiveEnergy(toReceive, simulate);
                if (receivedEnergy > 0) {
                    onChanged.run();
                }
                return receivedEnergy;
            }

            @Override
            public int extractEnergy(int toExtract, boolean simulate) {
                int extractedEnergy = super.extractEnergy(toExtract, simulate);
                if (extractedEnergy > 0) {
                    onChanged.run();
                }
                return extractedEnergy;
            }
        };
    }

}
