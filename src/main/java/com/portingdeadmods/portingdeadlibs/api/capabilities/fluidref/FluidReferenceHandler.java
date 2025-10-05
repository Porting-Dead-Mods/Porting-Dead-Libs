package com.portingdeadmods.portingdeadlibs.api.capabilities.fluidref;

import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.portingdeadmods.portingdeadlibs.PortingDeadLibs;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.NbtOps;
import net.minecraft.nbt.Tag;
import net.neoforged.neoforge.common.util.INBTSerializable;
import net.neoforged.neoforge.fluids.FluidStack;
import org.jetbrains.annotations.UnknownNullability;

import java.util.List;

public class FluidReferenceHandler implements IFluidReferenceHandler, INBTSerializable<Tag> {
	private NonNullList<FluidStack> refs;
	private int size;

	private static final Codec<FluidReferenceHandler> CODEC = RecordCodecBuilder.create(instance -> instance.group(
			Codec.INT.fieldOf("size").forGetter(FluidReferenceHandler::getSize),
			FluidStack.CODEC.listOf().fieldOf("refs").forGetter(FluidReferenceHandler::getAllReferences)
	).apply(instance, FluidReferenceHandler::new));

	public FluidReferenceHandler(int size) {
		this.refs = NonNullList.withSize(size, FluidStack.EMPTY);
		this.size = size;
	}

	public FluidReferenceHandler(int size, List<FluidStack> refs) {
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
	public FluidStack getReferenceInSlot(int idx) {
		return this.refs.get(idx);
	}

	@Override
	public List<FluidStack> getAllReferences() {
		return this.refs;
	}

	@Override
	public void setReferenceInSlot(int idx, FluidStack ref) {
		this.validateSlotIndex(idx);
		this.refs.set(idx, ref);
		this.onContentsChanged();
	}

	@Override
	public int getSlotLimit(int idx) {
		return 1;
	}

	@Override
	public boolean isReferenceValid(int idx, FluidStack ref) {
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
		DataResult<Pair<FluidReferenceHandler, Tag>> dataResult = CODEC.decode(NbtOps.INSTANCE, nbt);
		if (dataResult.isSuccess()) {
			FluidReferenceHandler newThis = dataResult.getOrThrow().getFirst();
			this.refs = newThis.refs;
		} else {
			PortingDeadLibs.LOGGER.error("Error decoding BacteriaStorage: {}", dataResult.error().get().message());
		}
	}
}
