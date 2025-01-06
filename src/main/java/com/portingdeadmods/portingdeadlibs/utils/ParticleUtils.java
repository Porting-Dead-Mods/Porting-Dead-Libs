package com.portingdeadmods.portingdeadlibs.utils;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.level.Level;

public final class ParticleUtils {

    private static final int PARTICLE_COUNT = 20; // Number of particles to spawn
    private static final double PARTICLE_RADIUS = 0.5; // Radius of the sphere around the item
    private static final int PARTICLE_DELAY = 5; // Number of ticks between particle spawns

    private static int particleTicks = 0;

    // Method for spawning particles around an item entity
    public static void spawnParticlesAroundItem(ItemEntity itemEntity, Level level, ParticleOptions particlesTypes) {
        if (particleTicks % PARTICLE_DELAY == 0) {
            for (int i = 0; i < PARTICLE_COUNT; i++) {
                double theta = level.random.nextDouble() * Math.PI * 2;
                double phi = level.random.nextDouble() * Math.PI;

                double xOffset = PARTICLE_RADIUS * Math.sin(phi) * Math.cos(theta);
                double yOffset = PARTICLE_RADIUS * Math.cos(phi);
                double zOffset = PARTICLE_RADIUS * Math.sin(phi) * Math.sin(theta);

                level.addParticle(particlesTypes,
                        itemEntity.getX() + xOffset,
                        itemEntity.getY() + yOffset,
                        itemEntity.getZ() + zOffset,
                        0, 0, 0);
            }
        }

        particleTicks++;
    }

    // Method for spawning particles around a block
    public static void spawnParticlesAroundBlock(BlockPos blockPos, Level level, ParticleOptions particlesTypes) {
        if (particleTicks % PARTICLE_DELAY == 0) {
            for (int i = 0; i < PARTICLE_COUNT; i++) {
                double theta = level.random.nextDouble() * Math.PI * 2;
                double phi = level.random.nextDouble() * Math.PI;

                double xOffset = PARTICLE_RADIUS * Math.sin(phi) * Math.cos(theta);
                double yOffset = PARTICLE_RADIUS * Math.cos(phi);
                double zOffset = PARTICLE_RADIUS * Math.sin(phi) * Math.sin(theta);

                level.addParticle(particlesTypes,
                        blockPos.getX() + 0.5 + xOffset, // Center of the block
                        blockPos.getY() + 0.5 + yOffset,
                        blockPos.getZ() + 0.5 + zOffset,
                        0, 0, 0);
            }
        }

        particleTicks++;
    }
}
