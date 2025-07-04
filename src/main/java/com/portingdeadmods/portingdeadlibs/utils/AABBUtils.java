package com.portingdeadmods.portingdeadlibs.utils;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.phys.AABB;

public final class AABBUtils {
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

	public static AABB create(double left, double right, double down, double up, double front, double back, double x, double y, double z) {
		return new AABB(left + x, down + y, front + z, right + x, up + y, back + z);
	}

	public static AABB create(double left, double right, double down, double up, double front, double back, BlockPos pos) {
		return create(left, right, down, up, front, back, pos.getX(), pos.getY(), pos.getZ());
	}
}
