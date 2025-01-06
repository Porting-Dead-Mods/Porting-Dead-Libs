package com.portingdeadmods.portingdeadlibs.api.data;

import com.portingdeadmods.portingdeadlibs.PortingDeadLibs;
import net.minecraft.core.component.DataComponentType;
import net.neoforged.neoforge.fluids.SimpleFluidContent;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;
import java.util.function.UnaryOperator;

public final class PDLDataComponents {
    public static final DeferredRegister.DataComponents DATA_COMPONENT_TYPES = DeferredRegister.createDataComponents(PortingDeadLibs.MODID);

    public static final Supplier<DataComponentType<SimpleFluidContent>> FLUID = registerDataComponentType("fluid",
            () -> builder -> builder.persistent(SimpleFluidContent.CODEC).networkSynchronized(SimpleFluidContent.STREAM_CODEC));

    public static <T> Supplier<DataComponentType<T>> registerDataComponentType(
            String name, Supplier<UnaryOperator<DataComponentType.Builder<T>>> builderOperator) {
        return DATA_COMPONENT_TYPES.register(name, () -> builderOperator.get().apply(DataComponentType.builder()).build());
    }
}
