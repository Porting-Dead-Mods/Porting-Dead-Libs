package com.portingdeadmods.portingdeadlibs.api.ghost;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.Identifier;
import net.minecraft.world.phys.AABB;
import net.neoforged.neoforge.capabilities.Capabilities;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public class GhostMultiblockShape {
    private final Set<BlockPos> partPositions; // Relative to origin (0,0,0)
    private final BlockPos controllerPosition; // Relative to origin (0,0,0)
    private final AABB relativeBounds;
    private final Map<BlockPos, List<Identifier>> handlerExposure;
    private final Map<BlockPos, GhostPartMenuFactory> partMenus;
    private final BlockPos placementOffset;

    private static final Identifier ITEM_HANDLER_KEY = Capabilities.Item.BLOCK.name();
    private static final Identifier FLUID_HANDLER_KEY = Capabilities.Fluid.BLOCK.name();
    private static final Identifier ENERGY_HANDLER_KEY = Capabilities.Energy.BLOCK.name();

    public enum Exposes {
        ITEM_HANDLER,
        FLUID_HANDLER,
        ENERGY_STORAGE
    }

    private GhostMultiblockShape(Set<BlockPos> partPositions,
                                 BlockPos controllerPosition,
                                 Map<BlockPos, List<Identifier>> handlerExposure,
                                 Map<BlockPos, GhostPartMenuFactory> partMenus,
                                 BlockPos placementOffset) {
        this.partPositions = partPositions;
        this.controllerPosition = controllerPosition;
        this.handlerExposure = handlerExposure;
        this.partMenus = partMenus;
        this.placementOffset = placementOffset;
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

    public Map<BlockPos, List<Identifier>> getHandlerExposure() {
        return handlerExposure;
    }

    public Map<BlockPos, GhostPartMenuFactory> getPartMenus() {
        return partMenus;
    }

    public BlockPos getPlacementOffset() {
        return placementOffset;
    }

    public GhostMultiblockShape getRotated(Direction direction) {
        Set<BlockPos> rotatedPartPositions = new HashSet<>();
        for (BlockPos pos : this.partPositions) {
            rotatedPartPositions.add(rotatePos(pos, direction));
        }
        BlockPos rotatedControllerPos = this.controllerPosition != null ? rotatePos(this.controllerPosition, direction) : null;
        Map<BlockPos, List<Identifier>> rotatedExposure = new HashMap<>();
        for (Map.Entry<BlockPos, List<Identifier>> entry : this.handlerExposure.entrySet()) {
            rotatedExposure.put(rotatePos(entry.getKey(), direction), entry.getValue());
        }
        Map<BlockPos, GhostPartMenuFactory> rotatedMenus = new HashMap<>();
        for (Map.Entry<BlockPos, GhostPartMenuFactory> entry : this.partMenus.entrySet()) {
            rotatedMenus.put(rotatePos(entry.getKey(), direction), entry.getValue());
        }
        BlockPos rotatedPlacementOffset = rotatePos(this.placementOffset, direction);

        return new GhostMultiblockShape(
                rotatedPartPositions,
                rotatedControllerPos,
                Collections.unmodifiableMap(rotatedExposure),
                Collections.unmodifiableMap(rotatedMenus),
                rotatedPlacementOffset
        );
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
        private final Map<Character, List<Identifier>> explicitHandlerMap = new HashMap<>();
        private final Map<Character, GhostPartMenuFactory> menuFactories = new HashMap<>();
        private final Set<Character> noMenuChars = new HashSet<>();
        private BlockPos placementOffset = BlockPos.ZERO;

	    /**
	     * The multiblock definition itself. <br> <br>
	     * A {@code layer} call defines a single multiblock layer by continuous string params of the same length <br> <br>
	     * Eg.
	     * {@code layer(
	     *  "AAAAA",
	     *  "AAAAA",
	     *  "AACAA",
	     * )}
	     */
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

	    /**
	     * Define the character for the controller position <br>
	     * At most there can be only one controller
	     * @param c Character used in the Layer
	     */
        public Builder controllerChar(char c) {
            this.controllerChar = c;
            return this;
        }

        public Builder exposeHandlers(char c, Identifier... handlers) {
            explicitHandlerMap.computeIfAbsent(c, k -> new ArrayList<>()).addAll(Arrays.asList(handlers));
            return this;
        }

	    /**
	     * Lets you open custom menus per block instead of a centralised controller BE menu
	     * @param c Character used in the Layer
	     * @param factory Lambda following {@link GhostPartMenuFactory}
	     */
        public Builder withMenu(char c, @Nullable GhostPartMenuFactory factory) {
            if (factory == null) {
                noMenuChars.add(c);
                menuFactories.remove(c);
            } else {
                menuFactories.put(c, factory);
                noMenuChars.remove(c);
            }
            return this;
        }

	    /**
	     * Taking the controller as an anchor, add an offset for placing following the idea that North is forward. <br> <br>
	     * It is generally recommended to offset the multiblock in such way that placing it is centered and all of the blocks are in the back of the place position.
	     * @param x offset
	     * @param y offset
	     * @param z offset
	     */
        public Builder onPlaceOffset(int x, int y, int z) {
            this.placementOffset = new BlockPos(x, y, z);
            return this;
        }

        public GhostMultiblockShape build() {
            Set<BlockPos> partPositions = new HashSet<>();
            BlockPos controllerPos = null;
            Map<BlockPos, List<Identifier>> handlerExposure = new HashMap<>();
            Map<BlockPos, GhostPartMenuFactory> partMenus = new HashMap<>();

			// Height
            int y = 0;
            for (String[] layer : layers) {

				// Length
                int z = 0;
                for (String aisle : layer) {

					// Width
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

                            List<Identifier> handlerKeys = new ArrayList<>();
                            if (explicitHandlerMap.containsKey(c)) {
                                handlerKeys.addAll(explicitHandlerMap.get(c));
                            }
                            if (!handlerKeys.isEmpty()) {
                                List<Identifier> deduped = List.copyOf(new LinkedHashSet<>(handlerKeys));
                                handlerExposure.put(currentPos, deduped);
                            }

                            if (noMenuChars.contains(c)) {
                                partMenus.put(currentPos, null);
                            } else if (menuFactories.containsKey(c)) {
                                partMenus.put(currentPos, menuFactories.get(c));
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


            return new GhostMultiblockShape(
                    partPositions,
                    controllerPos,
                    handlerExposure,
                    partMenus,
                    placementOffset
            );
        }
    }
}
