package com.portingdeadmods.portingdeadlibs.utils;

import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.neoforged.neoforge.common.util.INBTSerializable;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;
import net.neoforged.neoforge.fluids.capability.templates.FluidTank;
import org.jetbrains.annotations.UnknownNullability;

import java.util.function.Function;

public class SerializerUtils {
    public static <H> INBTSerializable<Tag> empty(H ignoredHandler) {
        return new INBTSerializable<>() {
            @Override
            public @UnknownNullability Tag serializeNBT(HolderLookup.Provider provider) {
                return new CompoundTag();
            }

            @Override
            public void deserializeNBT(HolderLookup.Provider provider, Tag tag) {
            }
        };
    }

    public static INBTSerializable<CompoundTag> fluidTank(IFluidHandler tank) {
        return new FluidTankSerializer((FluidTank) tank);
    }

    private record FluidTankSerializer(FluidTank tank) implements INBTSerializable<CompoundTag> {
        @Override
        public @UnknownNullability CompoundTag serializeNBT(HolderLookup.Provider provider) {
            return tank.writeToNBT(provider, new CompoundTag());
        }

        @Override
        public void deserializeNBT(HolderLookup.Provider provider, CompoundTag tag) {
            tank.setFluid(tank.readFromNBT(provider, tag).getFluid());
        }
    }
}
