package com.portingdeadmods.portingdeadlibs.content.blocks;

import com.mojang.serialization.MapCodec;
import com.portingdeadmods.portingdeadlibs.api.blockentities.ContainerBlockEntity;
import com.portingdeadmods.portingdeadlibs.api.blocks.ContainerBlock;
import com.portingdeadmods.portingdeadlibs.content.blockentities.CreativeItemSupplierBlockEntity;
import com.portingdeadmods.portingdeadlibs.content.blockentities.CreativePowerSourceBlockEntity;
import com.portingdeadmods.portingdeadlibs.content.client.screens.CreativePowerSourceScreen;
import com.portingdeadmods.portingdeadlibs.registries.PDLBlockEntityTypes;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.energy.IEnergyStorage;
import org.jetbrains.annotations.Nullable;

public class CreativePowerSourceBlock extends ContainerBlock {
	public CreativePowerSourceBlock(Properties properties) {
		super(properties);
	}

	@Override
	protected MapCodec<? extends BaseEntityBlock> codec() {
		return simpleCodec(CreativePowerSourceBlock::new);
	}

	@Override
	public boolean tickingEnabled() {
		return true;
	}

	@Override
	public BlockEntityType<? extends ContainerBlockEntity> getBlockEntityType() {
		return PDLBlockEntityTypes.CREATIVE_POWER_SOURCE.get();
	}
}
