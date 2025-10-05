package com.portingdeadmods.portingdeadlibs.api.capabilities.itemref;

import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.portingdeadmods.portingdeadlibs.PortingDeadLibs;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.NbtOps;
import net.minecraft.nbt.Tag;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.common.util.INBTSerializable;
import org.jetbrains.annotations.UnknownNullability;

import java.util.List;

public class ItemReferenceHandler implements IItemReferenceHandler, INBTSerializable<Tag> {
	private NonNullList<ItemStack> refs;
	private int size;

	private static final Codec<ItemReferenceHandler> CODEC = RecordCodecBuilder.create(instance -> instance.group(
		Codec.INT.fieldOf("size").forGetter(ItemReferenceHandler::getSize),
		ItemStack.CODEC.listOf().fieldOf("refs").forGetter(ItemReferenceHandler::getAllReferences)
	).apply(instance, ItemReferenceHandler::new));

	public ItemReferenceHandler(int size) {
		this.refs = NonNullList.withSize(size, ItemStack.EMPTY);
		this.size = size;
	}

	public ItemReferenceHandler(int size, List<ItemStack> refs) {
		this(size);
		for (int i = 0; i < Math.min(size, refs.size()); i++) {
			this.refs.set(i, refs.get(i));
		}
	}

	@Override
	public int getSize() {
		return this.size;
	}

	@Override
	public ItemStack getReferenceInSlot(int idx) {
		return this.refs.get(idx);
	}

	@Override
	public List<ItemStack> getAllReferences() {
		return this.refs;
	}

	@Override
	public void setReferenceInSlot(int idx, ItemStack ref) {
		this.validateSlotIndex(idx);
		this.refs.set(idx, ref);
		this.onContentsChanged();
	}

	@Override
	public int getSlotLimit(int idx) {
		return 0;
	}

	@Override
	public boolean isReferenceValid(int idx, ItemStack ref) {
		return true;
	}

	protected void onContentsChanged() {}

	@Override
	public @UnknownNullability Tag serializeNBT(HolderLookup.Provider provider) {
		DataResult<Tag> tagDataResult = CODEC.encodeStart(NbtOps.INSTANCE, this);
		if (tagDataResult.isSuccess()) {
			return tagDataResult.result().get();
		}
		PortingDeadLibs.LOGGER.error("Error encoding BacteriaStorage: {}", tagDataResult.error().get().message());
		return null;
	}

	@Override
	public void deserializeNBT(HolderLookup.Provider provider, Tag nbt) {
		DataResult<Pair<ItemReferenceHandler, Tag>> dataResult = CODEC.decode(NbtOps.INSTANCE, nbt);
		if (dataResult.isSuccess()) {
			ItemReferenceHandler newThis = dataResult.getOrThrow().getFirst();
			this.refs = newThis.refs;
		} else {
			PortingDeadLibs.LOGGER.error("Error decoding BacteriaStorage: {}", dataResult.error().get().message());
		}
	}
}
