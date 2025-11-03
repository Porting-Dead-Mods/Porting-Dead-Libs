package com.portingdeadmods.portingdeadlibs.api.ghost;

import com.portingdeadmods.portingdeadlibs.api.blockentities.ContainerBlockEntity;
import com.portingdeadmods.portingdeadlibs.api.gui.menus.PDLAbstractContainerMenu;
import com.portingdeadmods.portingdeadlibs.utils.UniqueArray;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.*;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.capabilities.BlockCapability;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public abstract class GhostMultiblockControllerBE extends ContainerBlockEntity implements MenuProvider {
    public final Set<BlockPos> partPositions = new UniqueArray<>();
    public final Map<BlockPos, List<ResourceLocation>> exposedHandlers = new HashMap<>();
    public final Map<BlockPos, GhostPartMenuFactory> partMenus = new HashMap<>();

	@Nullable
    private BlockPos pendingMenuPart; // Internal

    public GhostMultiblockControllerBE(BlockEntityType<?> blockEntityType, BlockPos blockPos, BlockState blockState) {
        super(blockEntityType, blockPos, blockState);
    }

    public void setPartConfiguration(Set<BlockPos> partPositions,
                                     Map<BlockPos, List<ResourceLocation>> handlerExposure,
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

    public Map<BlockPos, List<ResourceLocation>> getExposedHandlers() {
        return Collections.unmodifiableMap(exposedHandlers);
    }

    public boolean exposesHandler(ResourceLocation handlerKey, BlockPos partPos) {
        List<ResourceLocation> handlers = exposedHandlers.get(partPos);
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

    @Override
    protected void saveData(CompoundTag tag, HolderLookup.Provider registries) {
        super.saveData(tag, registries);
        if (!partPositions.isEmpty()) {
            tag.putLongArray("part_positions", partPositions.stream().mapToLong(BlockPos::asLong).toArray());
        }
        if (!exposedHandlers.isEmpty()) {
            ListTag exposure = new ListTag();
            for (Map.Entry<BlockPos, List<ResourceLocation>> entry : exposedHandlers.entrySet()) {
                CompoundTag entryTag = new CompoundTag();
                entryTag.putLong("pos", entry.getKey().asLong());
                ListTag handlers = new ListTag();
                for (ResourceLocation id : entry.getValue()) {
                    handlers.add(StringTag.valueOf(id.toString()));
                }
                entryTag.put("handlers", handlers);
                exposure.add(entryTag);
            }
            tag.put("handler_exposure", exposure);
        }
        if (!partMenus.isEmpty()) {
            ListTag noMenu = new ListTag();
            for (Map.Entry<BlockPos, GhostPartMenuFactory> entry : partMenus.entrySet()) {
                if (entry.getValue() == null) {
                    noMenu.add(LongTag.valueOf(entry.getKey().asLong()));
                }
            }
            if (!noMenu.isEmpty()) {
                tag.put("no_part_menus", noMenu);
            }
        }
    }

    @Override
    protected void loadData(CompoundTag tag, HolderLookup.Provider registries) {
        super.loadData(tag, registries);
        this.partPositions.clear();
        this.exposedHandlers.clear();
        this.partMenus.clear();

        if (tag.contains("part_positions")) {
            long[] partPositionsArray = tag.getLongArray("part_positions");
            for (long posLong : partPositionsArray) {
                this.partPositions.add(BlockPos.of(posLong));
            }
        }
        if (tag.contains("handler_exposure", Tag.TAG_LIST)) {
            ListTag exposure = tag.getList("handler_exposure", Tag.TAG_COMPOUND);
            for (int i = 0; i < exposure.size(); i++) {
                CompoundTag entry = exposure.getCompound(i);
                BlockPos pos = BlockPos.of(entry.getLong("pos"));
                ListTag handlers = entry.getList("handlers", Tag.TAG_STRING);
                List<ResourceLocation> ids = new ArrayList<>(handlers.size());
                for (int j = 0; j < handlers.size(); j++) {
                    ids.add(ResourceLocation.parse(handlers.getString(j)));
                }
                this.exposedHandlers.put(pos, List.copyOf(ids));
            }
        }
        if (tag.contains("no_part_menus", Tag.TAG_LIST)) {
            ListTag noMenu = tag.getList("no_part_menus", Tag.TAG_LONG);
            for (int i = 0; i < noMenu.size(); i++) {
                BlockPos pos = BlockPos.of(((LongTag) noMenu.get(i)).getAsLong());
                this.partMenus.put(pos, null);
            }
        }
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
