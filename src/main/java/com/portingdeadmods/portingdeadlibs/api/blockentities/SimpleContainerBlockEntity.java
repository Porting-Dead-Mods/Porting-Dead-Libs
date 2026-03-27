package com.portingdeadmods.portingdeadlibs.api.blockentities;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;

public class SimpleContainerBlockEntity extends PDLBlockEntity implements ResourceHandlerBlockEntity {
    private final ResourceHandlerHolder handlerHolder;

    public SimpleContainerBlockEntity(BlockEntityType<?> type, BlockPos worldPosition, BlockState blockState) {
        super(type, worldPosition, blockState);
        this.handlerHolder = new ResourceHandlerHolder();
    }

    @Override
    public ResourceHandlerHolder getHandlerHolder() {
        return this.handlerHolder;
    }

    @Override
    protected void saveAdditional(ValueOutput output) {
        super.saveAdditional(output);

        this.handlerHolder.serialize(output);

    }

    @Override
    protected void loadAdditional(ValueInput input) {
        super.loadAdditional(input);

        this.handlerHolder.deserialize(input);
    }

}
