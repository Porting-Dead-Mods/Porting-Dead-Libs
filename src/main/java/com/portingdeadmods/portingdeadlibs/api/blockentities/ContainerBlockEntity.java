package com.portingdeadmods.portingdeadlibs.api.blockentities;

import com.portingdeadmods.portingdeadlibs.PortingDeadLibs;
import com.portingdeadmods.portingdeadlibs.api.capabilities.SidedEnergyStorage;
import com.portingdeadmods.portingdeadlibs.api.capabilities.SidedFluidHandler;
import com.portingdeadmods.portingdeadlibs.api.capabilities.SidedItemHandler;
import com.portingdeadmods.portingdeadlibs.api.utils.IOAction;
import com.portingdeadmods.portingdeadlibs.utils.capabilities.HandlerUtils;
import it.unimi.dsi.fastutil.Pair;
import it.unimi.dsi.fastutil.ints.Int2IntFunction;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import it.unimi.dsi.fastutil.objects.ObjectList;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.Containers;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.neoforged.neoforge.capabilities.BlockCapability;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.common.util.INBTSerializable;
import net.neoforged.neoforge.energy.IEnergyStorage;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.items.IItemHandlerModifiable;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.function.*;

public abstract class ContainerBlockEntity extends BlockEntity {
    private final Map<ResourceLocation, Object> handlers;
    private final Map<ResourceLocation, Function<Object, INBTSerializable<?>>> handlerSerializers;

    public ContainerBlockEntity(BlockEntityType<?> blockEntityType, BlockPos blockPos, BlockState blockState) {
        super(blockEntityType, blockPos, blockState);
        this.handlers = new HashMap<>();
        this.handlerSerializers = new HashMap<>();
    }

    protected <C> void addHandler(ResourceLocation key, C handler) {
        this.handlers.put(key, handler);
    }

    protected <C> void addHandler(BlockCapability<C, Direction> capability, C handler) {
        this.addHandler(capability.name(), handler);
    }

    protected <C> void addHandler(BlockCapability<C, Direction> capability, C handler, Function<C, INBTSerializable<?>> serializerFactory) {
        this.addHandler(capability, handler);
        this.handlerSerializers.put(capability.name(), (Function<Object, INBTSerializable<?>>) serializerFactory);
    }

    protected <C> void addHandler(ResourceLocation key, C handler, Function<C, INBTSerializable<?>> serializerFactory) {
        this.addHandler(key, handler);
        this.handlerSerializers.put(key, (Function<Object, INBTSerializable<?>>) serializerFactory);
    }

    public <C> C getHandler(ResourceLocation key) {
        return (C) this.handlers.get(key);
    }

    public <C> C getHandler(BlockCapability<C, Direction> capability) {
        return (C) this.handlers.get(capability.name());
    }

    public IItemHandler getItemHandler() {
        return this.getHandler(Capabilities.ItemHandler.BLOCK);
    }

    public IFluidHandler getFluidHandler() {
        return this.getHandler(Capabilities.FluidHandler.BLOCK);
    }

    public IEnergyStorage getEnergyStorage() {
        return this.getHandler(Capabilities.EnergyStorage.BLOCK);
    }

    protected void addItemHandler(HandlerFactory<IItemHandler, ItemStack> factory, UnaryOperator<ItemHandlerBuilder> builder) {
        ItemHandlerBuilder builder1 = builder.apply(new ItemHandlerBuilder(factory));
        if (builder1.serializer != null) {
            this.addHandler(Capabilities.ItemHandler.BLOCK, builder1.build(), h -> builder1.serializer);
        } else {
            this.addHandler(Capabilities.ItemHandler.BLOCK, builder1.build());
        }
    }

    protected void addFluidHandler(HandlerFactory<IFluidHandler, FluidStack> factory, UnaryOperator<FluidHandlerBuilder> builder) {
        FluidHandlerBuilder builder1 = builder.apply(new FluidHandlerBuilder(factory));
        if (builder1.serializer != null) {
            this.addHandler(Capabilities.FluidHandler.BLOCK, builder1.build(), h -> builder1.serializer);
        } else {
            this.addHandler(Capabilities.FluidHandler.BLOCK, builder1.build());
        }
    }

    protected void addEnergyStorage(EnergyStorageFactory<IEnergyStorage> factory, UnaryOperator<EnergyStorageBuilder> builder) {
        EnergyStorageBuilder builder1 = builder.apply(new EnergyStorageBuilder(factory));
        if (builder1.serializer != null) {
            this.addHandler(Capabilities.EnergyStorage.BLOCK, builder1.build(), h -> builder1.serializer);
        } else {
            this.addHandler(Capabilities.EnergyStorage.BLOCK, builder1.build());
        }
    }

    public void tick() {
    }

    @Override
    protected final void loadAdditional(@NotNull CompoundTag nbt, HolderLookup.@NotNull Provider provider) {
        super.loadAdditional(nbt, provider);

        CompoundTag handlersTag = nbt.getCompound("handlers");
        for (String key : handlersTag.getAllKeys()) {
            Tag tag = handlersTag.get(key);

            ResourceLocation name = ResourceLocation.parse(key);
            Object handler = this.handlers.get(name);

            INBTSerializable<?> serializable = null;
            if (handler instanceof INBTSerializable<?> serializer) {
                serializable = serializer;
            } else {
                Function<Object, INBTSerializable<?>> value = this.handlerSerializers.get(name);
                if (value != null) {
                    serializable = value.apply(handler);
                }
            }

            if (serializable != null) {
                deserializeNbt(provider, serializable, tag);
            }

        }

        this.loadData(nbt, provider);
    }

    @Override
    protected final void saveAdditional(@NotNull CompoundTag nbt, HolderLookup.@NotNull Provider provider) {
        super.saveAdditional(nbt, provider);

        CompoundTag handlersTag = new CompoundTag();
        for (Map.Entry<ResourceLocation, Object> entry : this.handlers.entrySet()) {
            ResourceLocation name = entry.getKey();
            INBTSerializable<?> serializable = null;
            if (entry.getValue() instanceof INBTSerializable<?> serializer) {
                serializable = serializer;
            } else {
                Function<Object, INBTSerializable<?>> value = this.handlerSerializers.get(entry.getKey());
                if (value != null) {
                    serializable = value.apply(entry.getValue());
                }
            }

            if (serializable != null) {
                Tag handlerTag = serializable.serializeNBT(provider);
                handlersTag.put(name.toString(), handlerTag);
            }

        }
        nbt.put("handlers", handlersTag);

        this.saveData(nbt, provider);
    }

    private static <T extends Tag> void deserializeNbt(HolderLookup.@NotNull Provider provider, INBTSerializable<T> serializer, Tag tag) {
        serializer.deserializeNBT(provider, (T) tag);
    }

    protected void loadData(CompoundTag tag, HolderLookup.Provider provider) {
    }

    protected void saveData(CompoundTag tag, HolderLookup.Provider provider) {
    }

    private static int getStackLimit(IItemHandler itemHandler, int slot, ItemStack stack) {
        return Math.min(itemHandler.getSlotLimit(slot), stack.getMaxStackSize());
    }

    public ItemStack forceExtractItem(IItemHandlerModifiable handler, int slot, int amount, boolean simulate, Consumer<Integer> onChanged) {
        if (amount == 0)
            return ItemStack.EMPTY;

        ItemStack existing = getItemHandler().getStackInSlot(slot);

        if (existing.isEmpty())
            return ItemStack.EMPTY;

        int toExtract = Math.min(amount, existing.getMaxStackSize());

        if (existing.getCount() <= toExtract) {
            if (!simulate) {
                handler.setStackInSlot(slot, ItemStack.EMPTY);
                onChanged.accept(slot);
                return existing;
            } else {
                return existing.copy();
            }
        } else {
            if (!simulate) {
                handler.setStackInSlot(slot, existing.copyWithCount(existing.getCount() - toExtract));
                onChanged.accept(slot);
            }

            return existing.copyWithCount(toExtract);
        }
    }

    public ItemStack forceInsertItem(IItemHandlerModifiable handler, int slot, ItemStack stack, boolean simulate, Consumer<Integer> onChanged) {
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
                handler.setStackInSlot(slot, reachedLimit ? stack.copyWithCount(limit) : stack);
            } else {
                existing.grow(reachedLimit ? limit : stack.getCount());
            }
            onChanged.accept(slot);
        }

        return reachedLimit ? stack.copyWithCount(stack.getCount() - limit) : ItemStack.EMPTY;
    }

    public ItemStack forceInsertItem(IItemHandlerModifiable handler, List<Integer> slots, ItemStack stack, boolean simulate, Consumer<Integer> onChanged) {
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
                    handler.setStackInSlot(slot, reachedLimit ? stack.copyWithCount(remaining_space) : stack);
                } else {
                    existing.grow(reachedLimit ? remaining_space : stack.getCount());
                }
                onChanged.accept(slot);
            }

            if (reachedLimit) {
                stack.setCount(stack.getCount() - remaining_space);
            } else {
                return ItemStack.EMPTY;
            }
        }
        return stack;
    }

    public void updateData() {
        setChanged();
        level.sendBlockUpdated(getBlockPos(), getBlockState(), getBlockState(), 3);
    }

    public void dropItems(IItemHandler handler) {
        ItemStack[] stacks = getItemHandlerStacks(handler);
        if (stacks != null) {
            SimpleContainer inventory = new SimpleContainer(stacks);
            Containers.dropContents(this.level, this.worldPosition, inventory);
        }
    }

    public @Nullable ItemStack[] getItemHandlerStacks(IItemHandler handler) {
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
    public <T> Map<Direction, Pair<IOAction, int[]>> getSidedInteractions(BlockCapability<T, @Nullable Direction> capability) {
        return null;
    }

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

    public static class ItemHandlerBuilder extends HandlerBuilder<ItemStack, IItemHandler, ItemHandlerBuilder> {
        private final HandlerFactory<IItemHandler, ItemStack> factory;
        private int slots;
        private Int2IntFunction slotLimitFunction;

        private ItemHandlerBuilder(HandlerFactory<IItemHandler, ItemStack> factory) {
            this.factory = Objects.requireNonNull(factory, "Handler factory must not be null!");
            this.slots = 1;
            this.slotLimitFunction = $ -> Item.DEFAULT_MAX_STACK_SIZE;
        }

        public ItemHandlerBuilder slots(int slots) {
            this.slots = slots;
            return this;
        }

        public ItemHandlerBuilder slotLimit(Int2IntFunction slotLimitFunction) {
            this.slotLimitFunction = slotLimitFunction;
            return this;
        }

        @Override
        public IItemHandler build() {
            IItemHandler itemHandler = this.factory.create(this.validator, this.slotLimitFunction, this.onChange, this.slots);
            if (itemHandler instanceof INBTSerializable<?> serializable && this.serializer == null) {
                this.serializer = serializable;
            }
            return itemHandler;
        }
    }

    public static class FluidHandlerBuilder extends HandlerBuilder<FluidStack, IFluidHandler, FluidHandlerBuilder> {
        private final HandlerFactory<IFluidHandler, FluidStack> factory;
        private int slots;
        private Int2IntFunction slotLimitFunction;

        private FluidHandlerBuilder(HandlerFactory<IFluidHandler, FluidStack> factory) {
            this.factory = Objects.requireNonNull(factory, "Handler factory must not be null!");
            this.slots = 1;
            this.slotLimitFunction = $ -> Item.DEFAULT_MAX_STACK_SIZE;
        }

        public FluidHandlerBuilder slots(int slots) {
            this.slots = slots;
            return this;
        }

        public FluidHandlerBuilder slotLimit(Int2IntFunction slotLimitFunction) {
            this.slotLimitFunction = slotLimitFunction;
            return this;
        }

        @Override
        public IFluidHandler build() {
            IFluidHandler fluidHandler = this.factory.create(this.validator, this.slotLimitFunction, this.onChange, this.slots);
            if (fluidHandler instanceof INBTSerializable<?> serializable && this.serializer == null) {
                this.serializer = serializable;
            }
            return fluidHandler;
        }
    }

    public static class EnergyStorageBuilder {
        private final EnergyStorageFactory<IEnergyStorage> factory;
        protected Runnable onChange;
        private int capacity;
        private int maxReceive;
        private int maxExtract;
        protected INBTSerializable<?> serializer;

        private EnergyStorageBuilder(EnergyStorageFactory<IEnergyStorage> factory) {
            this.factory = factory;
        }

        public EnergyStorageBuilder capacity(int capacity) {
            this.capacity = capacity;
            return this;
        }

        public EnergyStorageBuilder maxTransfer(int maxTransfer) {
            this.maxReceive = maxTransfer;
            this.maxExtract = maxTransfer;
            return this;
        }

        public EnergyStorageBuilder maxReceive(int maxReceive) {
            this.maxReceive = maxReceive;
            return this;
        }

        public EnergyStorageBuilder maxExtract(int maxExtract) {
            this.maxExtract = maxExtract;
            return this;
        }

        public EnergyStorageBuilder onChange(Runnable onChange) {
            this.onChange = onChange;
            return this;
        }

        public EnergyStorageBuilder serializer(INBTSerializable<?> serializer) {
            this.serializer = serializer;
            return this;
        }

        public IEnergyStorage build() {
            IEnergyStorage energyStorage = this.factory.create(this.capacity, this.maxReceive, this.maxExtract, this.onChange);
            if (energyStorage instanceof INBTSerializable<?> serializable && this.serializer == null) {
                this.serializer = serializable;
            }
            return energyStorage;
        }

    }

    public abstract static class HandlerBuilder<T, H, SELF extends HandlerBuilder<T, H, SELF>> {
        protected BiPredicate<Integer, T> validator;
        protected Consumer<Integer> onChange;
        protected INBTSerializable<?> serializer;

        public SELF validator(BiPredicate<Integer, T> validator) {
            this.validator = validator;
            return this.self();
        }

        public SELF onChange(Consumer<Integer> onChange) {
            this.onChange = onChange;
            return this.self();
        }

        public SELF serializer(INBTSerializable<?> serializer) {
            this.serializer = serializer;
            return this.self();
        }

        public abstract H build();

        private SELF self() {
            return (SELF) this;
        }
    }

    public interface HandlerFactory<H, T> {
        H create(BiPredicate<Integer, T> validator, Int2IntFunction slotLimit, Consumer<Integer> onChanged, Integer slots);
    }

    public interface EnergyStorageFactory<H> {
        H create(int capacity, int maxReceive, int maxExtract, Runnable onChanged);
    }

}