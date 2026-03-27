package com.portingdeadmods.portingdeadlibs.example;

import com.mojang.serialization.MapCodec;
import com.portingdeadmods.portingdeadlibs.api.blockentities.PDLBlockEntity;
import com.portingdeadmods.portingdeadlibs.api.blocks.PDLEntityBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.BlockHitResult;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.transfer.fluid.FluidResource;
import net.neoforged.neoforge.transfer.item.ItemResource;
import net.neoforged.neoforge.transfer.transaction.Transaction;
import org.jetbrains.annotations.NotNull;

public class ExampleContainerBlock extends PDLEntityBlock {
    public ExampleContainerBlock(Properties properties) {
        super(properties);
    }

    @Override
    public boolean tickingEnabled() {
        return false;
    }

    @Override
    protected RotationType getRotationType() {
        return RotationType.NONE;
    }

    @Override
    public BlockEntityType<? extends PDLBlockEntity> getBlockEntityType() {
        return ExampleRegistries.EXAMPLE_CONTAINER_BLOCK_ENTITY.get();
    }

    @Override
    protected InteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult) {
        if (level.getBlockEntity(pos) instanceof ExampleContainerBlockEntity be) {
            try (Transaction tx = Transaction.openRoot()) {
                be.getHandler(Capabilities.Item.BLOCK).insert(ItemResource.of(Items.DIAMOND), 10, tx);
                be.getHandler(Capabilities.Fluid.BLOCK).insert(FluidResource.of(Fluids.WATER), 10, tx);
                tx.commit();
            }

            return InteractionResult.SUCCESS;
        }
        return InteractionResult.FAIL;
    }

}
