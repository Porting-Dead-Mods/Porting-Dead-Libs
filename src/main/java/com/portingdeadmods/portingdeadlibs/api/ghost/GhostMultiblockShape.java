package com.portingdeadmods.portingdeadlibs.api.ghost;

import com.portingdeadmods.portingdeadlibs.api.blockentities.ContainerBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.phys.AABB;

import java.util.*;

public class GhostMultiblockShape {
    private final Set<BlockPos> partPositions; // Relative to origin (0,0,0)
    private final BlockPos controllerPosition; // Relative to origin (0,0,0)
    private final AABB relativeBounds;
    private final Set<BlockPos> itemHandlerParts;
    private final Set<BlockPos> fluidHandlerParts;
    private final Set<BlockPos> energyStorageParts;

    public enum Exposes {
        /** {@link ContainerBlockEntity#getItemHandler()} */
        ITEM_HANDLER,

        /** {@link ContainerBlockEntity#getFluidHandler()} */
        FLUID_HANDLER,

        /** {@link ContainerBlockEntity#getEnergyStorage()} */
        ENERGY_STORAGE
    }

    private GhostMultiblockShape(Set<BlockPos> partPositions, BlockPos controllerPosition, Set<BlockPos> itemHandlerParts, Set<BlockPos> fluidHandlerParts, Set<BlockPos> energyStorageParts) {
        this.partPositions = partPositions;
        this.controllerPosition = controllerPosition;
        this.itemHandlerParts = itemHandlerParts;
        this.fluidHandlerParts = fluidHandlerParts;
        this.energyStorageParts = energyStorageParts;
        this.relativeBounds = calculateBounds();
    }

    private AABB calculateBounds() {
        if (controllerPosition == null) return new AABB(0, 0, 0, 0, 0, 0);
        AABB bounds = new AABB(controllerPosition);
        for (BlockPos pos : partPositions) {
            bounds = bounds.minmax(new AABB(pos));
        }
        // Expand bounds to include full blocks
        return new AABB(bounds.minX, bounds.minY, bounds.minZ, bounds.maxX + 1, bounds.maxY + 1, bounds.maxZ + 1);
    }

    public Set<BlockPos> getPartPositions() {
        return partPositions;
    }

    public BlockPos getControllerPosition() {
        return controllerPosition;
    }

    public AABB getRelativeBounds() {
        return relativeBounds;
    }

    public Set<BlockPos> getItemHandlerParts() {
        return itemHandlerParts;
    }

    public Set<BlockPos> getFluidHandlerParts() {
        return fluidHandlerParts;
    }

    public Set<BlockPos> getEnergyStorageParts() {
        return energyStorageParts;
    }

    public GhostMultiblockShape getRotated(Direction direction) {
        Set<BlockPos> rotatedPartPositions = new HashSet<>();
        for (BlockPos pos : this.partPositions) {
            rotatedPartPositions.add(rotatePos(pos, direction));
        }
        BlockPos rotatedControllerPos = this.controllerPosition != null ? rotatePos(this.controllerPosition, direction) : null;

        Set<BlockPos> rotatedItemHandlerParts = new HashSet<>();
        for (BlockPos pos : this.itemHandlerParts) {
            rotatedItemHandlerParts.add(rotatePos(pos, direction));
        }
        Set<BlockPos> rotatedFluidHandlerParts = new HashSet<>();
        for (BlockPos pos : this.fluidHandlerParts) {
            rotatedFluidHandlerParts.add(rotatePos(pos, direction));
        }
        Set<BlockPos> rotatedEnergyHandlerParts = new HashSet<>();
        for (BlockPos pos : this.energyStorageParts) {
            rotatedEnergyHandlerParts.add(rotatePos(pos, direction));
        }

        return new GhostMultiblockShape(rotatedPartPositions, rotatedControllerPos, rotatedItemHandlerParts, rotatedFluidHandlerParts, rotatedEnergyHandlerParts);
    }

    private static BlockPos rotatePos(BlockPos pos, Direction direction) {
        return switch (direction) {
            case SOUTH -> new BlockPos(-pos.getX(), pos.getY(), -pos.getZ());
            case WEST -> new BlockPos(pos.getZ(), pos.getY(), -pos.getX());
            case EAST -> new BlockPos(-pos.getZ(), pos.getY(), pos.getX());
            case NORTH, UP, DOWN -> pos;
        };
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private final List<String[]> layers = new ArrayList<>();
        private char controllerChar = 'C';
        private BlockPos pivot = BlockPos.ZERO;
        private final Map<Character, Set<Exposes>> whereMap = new HashMap<>();

        public Builder layer(String... aisle) {
            if (aisle.length > 0) {
                int width = aisle[0].length();
                for (int i = 1; i < aisle.length; i++) {
                    if (aisle[i].length() != width) {
                        throw new IllegalArgumentException("All aisles in a layer must have the same width.");
                    }
                }
            }
            layers.add(aisle);
            return this;
        }

        public Builder controllerChar(char c) {
            this.controllerChar = c;
            return this;
        }

        public Builder pivot(int x, int y, int z) {
            this.pivot = new BlockPos(x, y, z);
            return this;
        }

        public Builder where(char c, Exposes... exposes) {
            whereMap.computeIfAbsent(c, k -> new HashSet<>()).addAll(Arrays.asList(exposes));
            return this;
        }

        public GhostMultiblockShape build() {
            Set<BlockPos> partPositions = new HashSet<>();
            BlockPos controllerPos = null;
            Set<BlockPos> itemHandlerParts = new HashSet<>();
            Set<BlockPos> fluidHandlerParts = new HashSet<>();
            Set<BlockPos> energyStorageParts = new HashSet<>();

            int y = 0;
            for (String[] layer : layers) {
                int z = 0;
                for (String aisle : layer) {
                    int x = 0;
                    for (char c : aisle.toCharArray()) {
                        if (c != ' ') {
                            BlockPos currentPos = new BlockPos(x, y, z);
                            if (c == controllerChar) {
                                if (controllerPos != null) {
                                    throw new IllegalStateException("Multiple controllers defined. Only one is allowed.");
                                }
                                controllerPos = currentPos;
                            } else {
                                partPositions.add(currentPos);
                            }

                            if (whereMap.containsKey(c)) {
                                Set<Exposes> exposes = whereMap.get(c);
                                if (exposes.contains(Exposes.ITEM_HANDLER)) {
                                    itemHandlerParts.add(currentPos);
                                }
                                if (exposes.contains(Exposes.FLUID_HANDLER)) {
                                    fluidHandlerParts.add(currentPos);
                                }
                                if (exposes.contains(Exposes.ENERGY_STORAGE)) {
                                    energyStorageParts.add(currentPos);
                                }
                            }
                        }
                        x++;
                    }
                    z++;
                }
                y++;
            }

            if (controllerPos == null) {
                throw new IllegalStateException("No controller defined in the shape.");
            }

            // Normalize positions relative to the pivot
            BlockPos finalControllerPos = controllerPos.subtract(pivot);
            Set<BlockPos> finalPartPositions = new HashSet<>();
            for (BlockPos partPos : partPositions) {
                finalPartPositions.add(partPos.subtract(pivot));
            }
            Set<BlockPos> finalItemHandlerParts = new HashSet<>();
            for (BlockPos partPos : itemHandlerParts) {
                finalItemHandlerParts.add(partPos.subtract(pivot));
            }
            Set<BlockPos> finalFluidHandlerParts = new HashSet<>();
            for (BlockPos partPos : fluidHandlerParts) {
                finalFluidHandlerParts.add(partPos.subtract(pivot));
            }
            Set<BlockPos> finalEnergyHandlerParts = new HashSet<>();
            for (BlockPos partPos : energyStorageParts) {
                finalEnergyHandlerParts.add(partPos.subtract(pivot));
            }

            return new GhostMultiblockShape(finalPartPositions, finalControllerPos, finalItemHandlerParts, finalFluidHandlerParts, finalEnergyHandlerParts);
        }
    }
}
