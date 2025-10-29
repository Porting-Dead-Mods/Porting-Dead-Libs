package com.portingdeadmods.portingdeadlibs.example;

import com.mojang.serialization.MapCodec;
import com.portingdeadmods.portingdeadlibs.api.blockentities.ContainerBlockEntity;
import com.portingdeadmods.portingdeadlibs.api.blocks.ContainerBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.BlockHitResult;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;
import org.jetbrains.annotations.NotNull;

public class ExampleContainerBlock extends ContainerBlock {
    public ExampleContainerBlock(Properties properties) {
        super(properties);
    }

    @Override
    public boolean tickingEnabled() {
        return false;
    }

    @Override
    public BlockEntityType<? extends ContainerBlockEntity> getBlockEntityType() {
        return ExampleRegistries.EXAMPLE_CONTAINER_BLOCK_ENTITY.get();
    }

    @Override
    protected @NotNull MapCodec<? extends BaseEntityBlock> codec() {
        return simpleCodec(ExampleContainerBlock::new);
    }

    @Override
    protected ItemInteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult) {
        if (level.getBlockEntity(pos) instanceof ExampleContainerBlockEntity be) {
            be.getItemHandler().insertItem(0, new ItemStack(Items.DIAMOND, 10), false);
            be.getFluidHandler().fill(new FluidStack(Fluids.WATER, 10), IFluidHandler.FluidAction.EXECUTE);
            return ItemInteractionResult.SUCCESS;
        }
        return ItemInteractionResult.FAIL;
    }

}
