package com.portingdeadmods.portingdeadlibs.api.ghost;

import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public abstract class GhostControllerItem extends BlockItem {
    public GhostControllerItem(Block controllerBlock, Properties properties) {
        super(controllerBlock, properties);
    }

    /**
     * @return The block to use for the multiblock parts.
     */
    @NotNull
    protected abstract Block getPartBlock();

    /**
     * Defines the base shape of the multiblock in its default orientation (facing NORTH).
     * @return The base GhostMultiblockShape.
     */
    @NotNull
    protected abstract GhostMultiblockShape getBaseShape();

    /**
     * Gets the shape rotated according to the player's facing direction.
     * @param context The placement context.
     * @return The rotated GhostMultiblockShape.
     */
    @NotNull
    protected GhostMultiblockShape getShape(@NotNull BlockPlaceContext context) {
        return getBaseShape().getRotated(context.getHorizontalDirection().getOpposite());
    }

    /**
     * Calculates the origin of the multiblock structure in the world.
     * By default, the controller is placed at the clicked position.
     * @param context The placement context.
     * @return The world position of the structure's origin (0,0,0 in relative space).
     */
    @NotNull
    protected BlockPos getOrigin(@NotNull BlockPlaceContext context) {
        GhostMultiblockShape shape = getShape(context);
        return context.getClickedPos().offset(shape.getPlacementOffset()).subtract(shape.getControllerPosition());
    }

    /**
     * Calculates the world-space AABB for the multiblock.
     * @param context The placement context.
     * @return The AABB of the multiblock in the world.
     */
    @NotNull
    protected AABB getMultiblockBounds(@NotNull BlockPlaceContext context) {
        GhostMultiblockShape shape = getShape(context);
        BlockPos origin = context.getClickedPos().offset(shape.getPlacementOffset()).subtract(shape.getControllerPosition());
        return shape.getRelativeBounds().move(origin);
    }

    @Override
    public boolean canPlace(@NotNull BlockPlaceContext context, @NotNull BlockState state) {
        Level level = context.getLevel();
        GhostMultiblockShape shape = getShape(context);
        BlockPos controllerWorldPos = context.getClickedPos().offset(shape.getPlacementOffset());
        BlockPos origin = controllerWorldPos.subtract(shape.getControllerPosition());

        Set<BlockPos> allRelativePositions = new HashSet<>(shape.getPartPositions());
        allRelativePositions.add(shape.getControllerPosition());

        for (BlockPos relativePos : allRelativePositions) {
            BlockPos worldPos = origin.offset(relativePos);
            if (!level.getBlockState(worldPos).canBeReplaced()) {
                return false;
            }
        }

        return super.canPlace(context, state);
    }

    @Override
    protected boolean placeBlock(@NotNull BlockPlaceContext context, @NotNull BlockState state) {
        Level level = context.getLevel();
        if (level.isClientSide()) {
            return true;
        }

        GhostMultiblockShape shape = getShape(context);
        BlockPos controllerWorldPos = context.getClickedPos().offset(shape.getPlacementOffset());
        BlockPos origin = controllerWorldPos.subtract(shape.getControllerPosition());

        Set<BlockPos> allRelativePositions = new HashSet<>(shape.getPartPositions());
        allRelativePositions.add(shape.getControllerPosition());

        // Place all the blocks
        for (BlockPos relativePos : allRelativePositions) {
            BlockPos worldPos = origin.offset(relativePos);
            BlockState blockToPlace;
            if (relativePos.equals(shape.getControllerPosition())) {
                blockToPlace = getBlock().defaultBlockState();
            } else {
                blockToPlace = getPartBlock().defaultBlockState();
            }
            level.setBlockAndUpdate(worldPos, blockToPlace);
        }

        // Link parts to the controller
        for (BlockPos relativePartPos : shape.getPartPositions()) {
            BlockPos worldPartPos = origin.offset(relativePartPos);
            if (level.getBlockEntity(worldPartPos) instanceof SimpleGhostMultiblockPartBE partBE) {
                partBE.setControllerPos(controllerWorldPos);
            }
        }

        // Link controller to parts
        if (level.getBlockEntity(controllerWorldPos) instanceof GhostMultiblockControllerBE controllerBE) {
            Set<BlockPos> partWorldPositions = shape.getPartPositions().stream()
                .map(origin::offset)
                .collect(Collectors.toSet());
            Map<BlockPos, List<ResourceLocation>> handlerExposure = new HashMap<>();
            shape.getHandlerExposure().forEach((relativePos, handlers) ->
                    handlerExposure.put(origin.offset(relativePos), handlers));
            Map<BlockPos, GhostPartMenuFactory> partMenus = new HashMap<>();
            shape.getPartMenus().forEach((relativePos, factory) ->
                    partMenus.put(origin.offset(relativePos), factory));
            controllerBE.setPartConfiguration(partWorldPositions, handlerExposure, partMenus);
        }

        List<BlockPos> allWorldPositions = allRelativePositions.stream().map(origin::offset).collect(Collectors.toList());
        afterPlacement(level, controllerWorldPos, allWorldPositions, context.getPlayer());

        return true;
    }

    /**
     * Called after the multiblock has been placed.
     * Override this to add custom data to the block entities, for eg. Researchd: Owner UUIDs.
     *
     * @param level The level.
     * @param controllerPos The position of the controller.
     * @param allPos A list of all block positions in the multiblock.
     * @param player The player who placed the multiblock.
     */
    protected void afterPlacement(@NotNull Level level, @NotNull BlockPos controllerPos, @NotNull List<BlockPos> allPos, @Nullable Player player) {
        // Pass
    }
}
