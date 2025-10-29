package com.portingdeadmods.portingdeadlibs.api.utils;

import com.portingdeadmods.portingdeadlibs.example.ExampleContainerBlock;
import com.portingdeadmods.portingdeadlibs.example.ExampleContainerBlockEntity;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.Arrays;
import java.util.function.Function;
import java.util.function.Supplier;

public class PDLDeferredRegisterBlockEntities extends DeferredRegister<BlockEntityType<?>> {
    protected PDLDeferredRegisterBlockEntities(String namespace) {
        super(Registries.BLOCK_ENTITY_TYPE, namespace);
    }

    public static PDLDeferredRegisterBlockEntities createBlockEntities(String modid) {
        return new PDLDeferredRegisterBlockEntities(modid);
    }

    @SafeVarargs
    public final <BE extends BlockEntity> DeferredBlockEntity<BE> register(String name, BlockEntityType.BlockEntitySupplier<BE> supplier, Supplier<? extends Block>... validBlocks) {
        DeferredHolder<BlockEntityType<?>, BlockEntityType<BE>> holder = this.register(name, () -> BlockEntityType.Builder.of(supplier, Arrays.stream(validBlocks)
                        .map(Supplier::get)
                        .toArray(Block[]::new))
                .build(null));
        return new DeferredBlockEntity<>(holder.getKey());
    }
}
