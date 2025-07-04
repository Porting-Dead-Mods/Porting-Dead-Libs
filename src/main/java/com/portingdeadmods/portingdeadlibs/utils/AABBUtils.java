package com.portingdeadmods.portingdeadlibs.utils;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.phys.AABB;

import java.util.stream.Stream;

public final class AABBUtils {

	/**
	 * Moves the given AABB in the specified direction by the specified distance.
	 *
	 * @param aabb The AABB to move.
	 * @param direction The {@link Direction} to move the AABB in.
	 * @param distance The distance to move the AABB.
	 * @return The moved AABB.
	 */
	public static AABB move(AABB aabb, Direction direction, double distance) {
		switch (direction) {
			case UP -> {
				return aabb.move(0, distance, 0);
			}
			case DOWN -> {
				return aabb.move(0, -distance, 0);
			}
			case NORTH -> {
				return aabb.move(0, 0, -distance);
			}
			case SOUTH -> {
				return aabb.move(0, 0, distance);
			}
			case WEST -> {
				return aabb.move(-distance, 0, 0);
			}
			case EAST -> {
				return aabb.move(distance, 0, 0);
			}
			default -> {
				return aabb;
			}
		}
	}

	/**
	 * Creates an AABB starting from a point, then inflates it by the given amounts in every direction.
	 *
	 * @param left The amount to inflate the AABB to the left.
	 * @param right The amount to inflate the AABB to the right.
	 * @param down The amount to inflate the AABB downwards.
	 * @param up The amount to inflate the AABB upwards.
	 * @param front The amount to inflate the AABB towards the front.
	 * @param back The amount to inflate the AABB towards the back.
	 * @param x The x position to create the AABB at.
	 * @param y The y position to create the AABB at.
	 * @param z The z position to create the AABB at.
	 * @return The resulting AABB
	 */
	public static AABB create(double left, double right, double down, double up, double front, double back, double x, double y, double z) {
		return new AABB(-left + x, -down + y, -front + z, right + x, up + y, back + z);
	}

	/**
	 * Creates an AABB starting from a 1x1x1 cube at the given position, then inflates it by the given amounts in every direction.
	 *
	 * @param left The amount to inflate the AABB to the left.
	 * @param right The amount to inflate the AABB to the right.
	 * @param down The amount to inflate the AABB downwards.
	 * @param up The amount to inflate the AABB upwards.
	 * @param front The amount to inflate the AABB towards the front.
	 * @param back The amount to inflate the AABB towards the back.
	 * @param pos The position to create the AABB at.
	 * @return The resulting AABB
	 */
	public static AABB create(double left, double right, double down, double up, double front, double back, BlockPos pos) {
		AABB blockPosAABB = new AABB(pos);
		return blockPosAABB
				.inflate((left + right) / 2, (down + up) / 2, (front + back) / 2)
				.move(((-left) + right) / 2, ((-down) + up) / 2, ((-front) + back) / 2);
	}

	/**
	 * Returns a UniqueArray of all full blocks occupied by the given AABB.
	 *
	 * @param aabb The AABB to get the positions from.
	 * @return A UniqueArray of BlockPos representing all full blocks within the AABB.
	 */
	public static UniqueArray<BlockPos> getAllPositionsInAABB(AABB aabb) {
		int minX = (int) Math.ceil(aabb.minX);
		int maxX = (int) Math.floor(aabb.maxX);
		int minY = (int) Math.ceil(aabb.minY);
		int maxY = (int) Math.floor(aabb.maxY);
		int minZ = (int) Math.ceil(aabb.minZ);
		int maxZ = (int) Math.floor(aabb.maxZ);

		UniqueArray<BlockPos> positions = new UniqueArray<>();
		for (int x = minX; x <= maxX; x++) {
			for (int y = minY; y <= maxY; y++) {
				for (int z = minZ; z <= maxZ; z++) {
					positions.add(new BlockPos(x, y, z));
				}
			}
		}

		return positions;
	}
}
