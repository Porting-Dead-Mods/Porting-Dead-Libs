package com.portingdeadmods.portingdeadlibs.api.utils;

import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.registries.DeferredHolder;

public class DeferredBlockEntity<BE extends BlockEntity> extends DeferredHolder<BlockEntityType<?>, BlockEntityType<BE>> {
    protected DeferredBlockEntity(ResourceKey<BlockEntityType<?>> key) {
        super(key);
    }

    public BE create(BlockPos pos, BlockState state) {
        return this.get().create(pos, state);
    }
}
