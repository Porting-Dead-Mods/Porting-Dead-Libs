package com.portingdeadmods.portingdeadlibs.api.blockentities;

import com.portingdeadmods.portingdeadlibs.PortingDeadLibs;
import com.portingdeadmods.portingdeadlibs.api.capabilities.DynamicFluidTank;
import com.portingdeadmods.portingdeadlibs.api.capabilities.SidedEnergyStorage;
import com.portingdeadmods.portingdeadlibs.api.capabilities.SidedFluidHandler;
import com.portingdeadmods.portingdeadlibs.api.capabilities.SidedItemHandler;
import com.portingdeadmods.portingdeadlibs.api.utils.IOAction;
import it.unimi.dsi.fastutil.Pair;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import it.unimi.dsi.fastutil.objects.ObjectList;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.Containers;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.neoforged.neoforge.capabilities.BlockCapability;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.energy.EnergyStorage;
import net.neoforged.neoforge.energy.IEnergyStorage;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;
import net.neoforged.neoforge.fluids.capability.templates.FluidTank;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.items.ItemStackHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Map;
import java.util.function.BiPredicate;
import java.util.function.Predicate;
import java.util.function.UnaryOperator;

public abstract class ContainerBlockEntity extends BlockEntity {
    private @Nullable ItemStackHandler itemHandler;
    private @Nullable DynamicFluidTank fluidTank;
    private @Nullable EnergyStorage energyStorage;

    public ContainerBlockEntity(BlockEntityType<?> blockEntityType, BlockPos blockPos, BlockState blockState) {
        super(blockEntityType, blockPos, blockState);
    }

    public void commonTick() {
    }

    public IItemHandler getItemHandler() {
        return itemHandler;
    }

    public IFluidHandler getFluidHandler() {
        return fluidTank;
    }

    public IEnergyStorage getEnergyStorage() {
        return energyStorage;
    }

    protected ItemStackHandler getItemStackHandler() {
        return itemHandler;
    }

    protected DynamicFluidTank getFluidTank() {
        return fluidTank;
    }

    protected EnergyStorage getEnergyStorageImpl() {
        return energyStorage;
    }

    @Override
    protected final void loadAdditional(@NotNull CompoundTag nbt, HolderLookup.@NotNull Provider provider) {
        super.loadAdditional(nbt, provider);
        if (this.getFluidTank() != null)
            this.getFluidTank().deserializeNBT(provider, nbt.getCompound("fluid_tank"));
        if (this.getItemStackHandler() != null)
            this.getItemStackHandler().deserializeNBT(provider, nbt.getCompound("itemhandler"));
        if (this.getEnergyStorageImpl() != null)
            this.getEnergyStorageImpl().deserializeNBT(provider, nbt.get("energy_storage"));
        loadData(nbt, provider);
    }

    @Override
    protected final void saveAdditional(@NotNull CompoundTag nbt, HolderLookup.@NotNull Provider provider) {
        super.saveAdditional(nbt, provider);
        if (getFluidTank() != null)
            nbt.put("fluid_tank", getFluidTank().serializeNBT(provider));
        if (getItemStackHandler() != null)
            nbt.put("itemhandler", getItemStackHandler().serializeNBT(provider));
        if (getEnergyStorageImpl() != null)
            nbt.put("energy_storage", getEnergyStorageImpl().serializeNBT(provider));
        saveData(nbt, provider);
    }

    protected void loadData(CompoundTag tag, HolderLookup.Provider provider) {
    }

    protected void saveData(CompoundTag tag, HolderLookup.Provider provider) {
    }

    protected final void addItemHandler(int slots) {
        addItemHandler(slots, (slot, itemStack) -> true);
    }

    protected final void addItemHandler(int slots, int slotLimit) {
        addItemHandler(slots, slotLimit, (slot, itemStack) -> true);
    }

    protected final void addItemHandler(int slots, BiPredicate<Integer, ItemStack> validation) {
        addItemHandler(slots, 64, validation);
    }

    protected final void addItemHandler(int slots, UnaryOperator<Integer> slotLimit) {
        addItemHandler(slots, slotLimit, (slot, itemStack) -> true);
    }

    protected final void addItemHandler(int slots, int slotLimit, BiPredicate<Integer, ItemStack> validation) {
        addItemHandler(slots, slot -> slotLimit, validation);
    }

    protected final void addItemHandler(int slots, UnaryOperator<Integer> slotLimit, BiPredicate<Integer, ItemStack> validation) {
        this.itemHandler = new ItemStackHandler(slots) {
            @Override
            protected void onContentsChanged(int slot) {
                update();
                onItemsChanged(slot);
                invalidateCapabilities();
            }

            @Override
            public boolean isItemValid(int slot, @NotNull ItemStack stack) {
                return validation.test(slot, stack);
            }

            @Override
            public int getSlotLimit(int slot) {
                return slotLimit.apply(slot);
            }
        };
    }

    private static int getStackLimit(IItemHandler itemHandler, int slot, ItemStack stack) {
        return Math.min(itemHandler.getSlotLimit(slot), stack.getMaxStackSize());
    }

    public ItemStack forceExtractItem(int slot, int amount, boolean simulate) {
        if (amount == 0)
            return ItemStack.EMPTY;

        ItemStack existing = getItemHandler().getStackInSlot(slot);

        if (existing.isEmpty())
            return ItemStack.EMPTY;

        int toExtract = Math.min(amount, existing.getMaxStackSize());

        if (existing.getCount() <= toExtract) {
            if (!simulate) {
                getItemStackHandler().setStackInSlot(slot, ItemStack.EMPTY);
                onItemsChanged(slot);
                return existing;
            } else {
                return existing.copy();
            }
        } else {
            if (!simulate) {
                getItemStackHandler().setStackInSlot(slot, existing.copyWithCount(existing.getCount() - toExtract));
                onItemsChanged(slot);
            }

            return existing.copyWithCount(toExtract);
        }
    }

    public ItemStack forceInsertItem(int slot, ItemStack stack, boolean simulate) {
        if (stack.isEmpty())
            return ItemStack.EMPTY;

        ItemStack existing = getItemHandler().getStackInSlot(slot);

        int limit = getStackLimit(getItemHandler(), slot, stack);

        if (!existing.isEmpty()) {
            if (!ItemStack.isSameItemSameComponents(stack, existing))
                return stack;

            limit -= existing.getCount();
        }

        if (limit <= 0)
            return stack;

        boolean reachedLimit = stack.getCount() > limit;

        if (!simulate) {
            if (existing.isEmpty()) {
                getItemStackHandler().setStackInSlot(slot, reachedLimit ? stack.copyWithCount(limit) : stack);
            } else {
                existing.grow(reachedLimit ? limit : stack.getCount());
            }
            onItemsChanged(slot);
        }

        return reachedLimit ? stack.copyWithCount(stack.getCount() - limit) : ItemStack.EMPTY;
    }

    public ItemStack forceInsertItem(List<Integer> slots, ItemStack stack, boolean simulate) {
        if (stack.isEmpty())
            return ItemStack.EMPTY;

        for (int slot : slots) {
            // Fetch slot
            ItemStack existing = getItemHandler().getStackInSlot(slot);

            // Fetch if slot limit is smaller than stack size
            int limit = getStackLimit(getItemHandler(), slot, stack);
            int remaining_space = limit;

            // If slot is not empty
            if (!existing.isEmpty()) {
                if (!ItemStack.isSameItemSameComponents(stack, existing))
                    continue;

                remaining_space = limit - existing.getCount();
            }

            boolean reachedLimit = stack.getCount() > remaining_space;

            if (!simulate) {
                if (existing.isEmpty()) {
                    getItemStackHandler().setStackInSlot(slot, reachedLimit ? stack.copyWithCount(remaining_space) : stack);
                } else {
                    existing.grow(reachedLimit ? remaining_space : stack.getCount());
                }
                onItemsChanged(slot);
            }

            if (reachedLimit) {
                stack.setCount(stack.getCount() - remaining_space);
            } else {
                return ItemStack.EMPTY;
            }
        }
        return stack;
    }


    protected final void addFluidTank(int capacityInMb) {
        addFluidTank(capacityInMb, ignored -> true);
    }

    protected final void addSecondaryFluidTank(int capacityInMb) {
        addSecondaryFluidTank(capacityInMb, ignored -> true);
    }

    protected final void addSecondaryFluidTank(int capacityInMb, Predicate<FluidStack> validation) {
        addFluidTank(capacityInMb, validation, true);
    }

    protected final void addFluidTank(int capacityInMn, Predicate<FluidStack> validation) {
        addFluidTank(capacityInMn, validation, false);
    }

    protected final void addFluidTank(int capacityInMb, Predicate<FluidStack> validation, boolean secondary) {
        DynamicFluidTank tank = new DynamicFluidTank(capacityInMb) {
            @Override
            protected void onContentsChanged() {
                update();
                onFluidChanged();
            }

            @Override
            public boolean isFluidValid(FluidStack stack) {
                return validation.test(stack);
            }
        };
        if (!secondary) {
            this.fluidTank = tank;
        }
    }

    protected final void addEnergyStorage(int energyCapacity) {
        this.energyStorage = new EnergyStorage(energyCapacity) {
            @Override
            public int receiveEnergy(int toReceive, boolean simulate) {
                int receivedEnergy = super.receiveEnergy(toReceive, simulate);
                if (receivedEnergy > 0) {
                    update();
                    setChanged();
                    ContainerBlockEntity.this.onEnergyChanged();
                }
                return receivedEnergy;
            }

            @Override
            public int extractEnergy(int toExtract, boolean simulate) {
                int extractedEnergy = super.extractEnergy(toExtract, simulate);
                if (extractedEnergy > 0) {
                    update();
                    setChanged();
                    ContainerBlockEntity.this.onEnergyChanged();
                }
                return extractedEnergy;
            }
        };
    }

    public void update() {
        setChanged();
        level.sendBlockUpdated(getBlockPos(), getBlockState(), getBlockState(), 3);
    }

    protected void onItemsChanged(int slot) {
    }

    protected void onFluidChanged() {
    }

    public void onPowerChanged() {
    }

    public void onEnergyChanged() {
    }

    public void drop() {
        ItemStack[] stacks = getItemHandlerStacks();
        if (stacks != null) {
            SimpleContainer inventory = new SimpleContainer(stacks);
            Containers.dropContents(this.level, this.worldPosition, inventory);
        }
    }

    public @Nullable ItemStack[] getItemHandlerStacks() {
        IItemHandler itemStackHandler = getItemHandler();

        if (itemStackHandler == null) return null;

        ItemStack[] itemStacks = new ItemStack[itemStackHandler.getSlots()];
        for (int i = 0; i < itemStackHandler.getSlots(); i++) {
            itemStacks[i] = itemStackHandler.getStackInSlot(i);
        }
        return itemStacks;
    }

    public List<ItemStack> getItemHandlerStacksList() {
        IItemHandler itemStackHandler = getItemHandler();

        if (itemStackHandler == null) return null;

        int slots = itemStackHandler.getSlots();
        ObjectList<ItemStack> itemStacks = new ObjectArrayList<>(slots);
        for (int i = 0; i < slots; i++) {
            ItemStack stack = itemStackHandler.getStackInSlot(i);
            if (!stack.isEmpty()) {
                itemStacks.add(stack);
            }
        }
        return itemStacks;
    }

    public <T> T getHandlerOnSide(BlockCapability<T, @Nullable Direction> capability, SidedHandlerSupplier<T> handlerSupplier, Direction direction, T baseHandler) {
        if (direction == null) {
            return baseHandler;
        }

        Map<Direction, Pair<IOAction, int[]>> ioPorts = getSidedInteractions(capability);
        if (ioPorts.containsKey(direction)) {

            if (direction == Direction.UP || direction == Direction.DOWN) {
                return handlerSupplier.get(baseHandler, ioPorts.get(direction));
            }

            if (this.getBlockState().hasProperty(BlockStateProperties.HORIZONTAL_FACING)) {
                Direction localDir = this.getBlockState().getValue(BlockStateProperties.HORIZONTAL_FACING);

                return getCapOnSide(handlerSupplier, direction, baseHandler, ioPorts, localDir);
            }

            if (getBlockState().hasProperty(BlockStateProperties.FACING)) {
                Direction localDir = this.getBlockState().getValue(BlockStateProperties.FACING);

                return getCapOnSide(handlerSupplier, direction, baseHandler, ioPorts, localDir);
            }

            PortingDeadLibs.LOGGER.warn("Sided io for non facing block");
        }

        return null;
    }

    @Nullable
    private <T> T getCapOnSide(SidedHandlerSupplier<T> handlerSupplier, Direction direction, T baseHandler, Map<Direction, Pair<IOAction, int[]>> ioPorts, Direction localDir) {
        return switch (localDir) {
            case NORTH -> handlerSupplier.get(baseHandler, ioPorts.get(direction.getOpposite()));
            case EAST -> handlerSupplier.get(baseHandler, ioPorts.get(direction.getClockWise()));
            case SOUTH -> handlerSupplier.get(baseHandler, ioPorts.get(direction));
            case WEST -> handlerSupplier.get(baseHandler, ioPorts.get(direction.getCounterClockWise()));
            default -> null;
        };
    }

    public IItemHandler getItemHandlerOnSide(Direction direction) {
        return getHandlerOnSide(
                Capabilities.ItemHandler.BLOCK,
                SidedItemHandler::new,
                direction,
                getItemHandler()
        );
    }

    public IFluidHandler getFluidHandlerOnSide(Direction direction) {
        return getHandlerOnSide(
                Capabilities.FluidHandler.BLOCK,
                SidedFluidHandler::new,
                direction,
                getFluidHandler()
        );
    }

    public IEnergyStorage getEnergyStorageOnSide(Direction direction) {
        return getHandlerOnSide(
                Capabilities.EnergyStorage.BLOCK,
                SidedEnergyStorage::new,
                direction,
                getEnergyStorage()
        );
    }

    /**
     * Get the input/output config for the blockenitity.
     * If directions are not defined in the map, they are assumed to be {@link IOAction#NONE} and do not affect any slot.
     *
     * @return Map of directions that each map to a pair that defines the IOAction as well as the tanks that are affected. Return an empty map if you do not have an itemhandler
     */
    public abstract <T> Map<Direction, Pair<IOAction, int[]>> getSidedInteractions(BlockCapability<T, @Nullable Direction> capability);

    @Nullable
    @Override
    public Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public @NotNull CompoundTag getUpdateTag(HolderLookup.Provider provider) {
        return saveWithoutMetadata(provider);
    }

    @Override
    public void onDataPacket(Connection net, ClientboundBlockEntityDataPacket pkt, HolderLookup.Provider lookupProvider) {
        super.onDataPacket(net, pkt, lookupProvider);
    }

    @FunctionalInterface
    public interface SidedHandlerSupplier<T> {
        T get(T handler, Pair<IOAction, int[]> supportedActions);
    }
}