package com.portingdeadmods.portingdeadlibs.api.ghost;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.portingdeadmods.portingdeadlibs.api.blockentities.SimpleContainerBlockEntity;
import com.portingdeadmods.portingdeadlibs.api.gui.menus.PDLAbstractContainerMenu;
import com.portingdeadmods.portingdeadlibs.utils.UniqueArray;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.*;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.neoforged.neoforge.capabilities.BlockCapability;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public abstract class GhostMultiblockControllerBE extends SimpleContainerBlockEntity implements MenuProvider {
    public final Set<BlockPos> partPositions = new UniqueArray<>();
    public final Map<BlockPos, List<Identifier>> exposedHandlers = new HashMap<>();
    public final Map<BlockPos, GhostPartMenuFactory> partMenus = new HashMap<>();

	@Nullable
    private BlockPos pendingMenuPart; // Internal

    public GhostMultiblockControllerBE(BlockEntityType<?> blockEntityType, BlockPos blockPos, BlockState blockState) {
        super(blockEntityType, blockPos, blockState);
    }

    public void setPartConfiguration(Set<BlockPos> partPositions,
                                     Map<BlockPos, List<Identifier>> handlerExposure,
                                     Map<BlockPos, GhostPartMenuFactory> menuFactories) {

		this.partPositions.clear(); // Should already be empty
        handlerExposure.forEach((pos, handlers) -> Objects.requireNonNull(handlers, "Handler list cannot be null"));
        partPositions.forEach(pos -> this.partPositions.add(pos.immutable()));

        this.exposedHandlers.clear(); // Should already be empty
        handlerExposure.forEach((pos, handlers) ->
                this.exposedHandlers.put(pos.immutable(), List.copyOf(handlers)));

        this.partMenus.clear(); // Should already be empty
        menuFactories.forEach((pos, factory) -> this.partMenus.put(pos.immutable(), factory));
        setChanged();
    }

    public Set<BlockPos> getPartPositions() {
        return Collections.unmodifiableSet(partPositions);
    }

    public Map<BlockPos, List<Identifier>> getExposedHandlers() {
        return Collections.unmodifiableMap(exposedHandlers);
    }

    public boolean exposesHandler(Identifier handlerKey, BlockPos partPos) {
        List<Identifier> handlers = exposedHandlers.get(partPos);
        if (handlers == null) {
            return true;
        }
        return handlers.contains(handlerKey);
    }

    public <T> boolean exposesHandler(BlockCapability<T, @Nullable Direction> handler, BlockPos partPos) {
        return exposesHandler(handler.name(), partPos);
    }

    public void prepareMenu(@Nullable BlockPos partPos) {
        this.pendingMenuPart = partPos;
    }

    record HandlerExposure(BlockPos pos, List<Identifier> handlers) {
        public static final Codec<HandlerExposure> CODEC = RecordCodecBuilder.create(inst -> inst.group(
                BlockPos.CODEC.fieldOf("pos").forGetter(HandlerExposure::pos),
                Identifier.CODEC.listOf().fieldOf("handlers").forGetter(HandlerExposure::handlers)
        ).apply(inst, HandlerExposure::new));
    }

    @Override
    protected void saveAdditional(ValueOutput output) {
        super.saveAdditional(output);

        if (!partPositions.isEmpty()) {
            ValueOutput.TypedOutputList<Long> partPositions = output.list("part_positions", Codec.LONG);
            this.partPositions.stream().mapToLong(BlockPos::asLong).forEach(partPositions::add);
        }

        if (!exposedHandlers.isEmpty()) {
            ValueOutput.TypedOutputList<HandlerExposure> exposures = output.list("handler_exposures", HandlerExposure.CODEC);
            for (Map.Entry<BlockPos, List<Identifier>> entry : exposedHandlers.entrySet()) {
                HandlerExposure exposure = new HandlerExposure(entry.getKey(), entry.getValue());
                exposures.add(exposure);
            }
        }

        if (!partMenus.isEmpty()) {
            ValueOutput.TypedOutputList<Long> noPartMenus = output.list("no_part_menus", Codec.LONG);
            for (Map.Entry<BlockPos, GhostPartMenuFactory> entry : partMenus.entrySet()) {
                if (entry.getValue() == null) {
                    noPartMenus.add(entry.getKey().asLong());
                }
            }
        }
    }

    @Override
    protected void loadAdditional(ValueInput input) {
        super.loadAdditional(input);

        this.partPositions.clear();
        this.exposedHandlers.clear();
        this.partMenus.clear();

        ValueInput.TypedInputList<Long> partPositions = input.listOrEmpty("part_positions", Codec.LONG);
        partPositions.stream().map(BlockPos::of).forEach(this.partPositions::add);

        ValueInput.TypedInputList<HandlerExposure> exposures = input.listOrEmpty("handler_exposure", HandlerExposure.CODEC);
        for (HandlerExposure exposure : exposures) {
            this.exposedHandlers.put(exposure.pos, exposure.handlers);
        }

        ValueInput.TypedInputList<Long> noPartMenus = input.listOrEmpty("no_part_menus", Codec.LONG);
        noPartMenus.stream().map(BlockPos::of).forEach(pos -> this.partMenus.put(pos, null));
    }

    @Override
    public abstract Component getDisplayName();

    @Nullable
    @Override
    public final AbstractContainerMenu createMenu(int containerId, Inventory inventory, Player player) {
        BlockPos target = this.pendingMenuPart;
        this.pendingMenuPart = null;

        if (target != null && partMenus.containsKey(target)) {
            GhostPartMenuFactory factory = partMenus.get(target);
            if (factory == null) {
                return null;
            }
            PDLAbstractContainerMenu<?> menu = factory.create(this, target, containerId, inventory, player);
            if (menu != null) {
                return menu;
            }
            return null;
        }

        return createControllerMenu(containerId, inventory, player);
    }

    protected abstract PDLAbstractContainerMenu<?> createControllerMenu(int containerId, Inventory inventory, Player player);
}
