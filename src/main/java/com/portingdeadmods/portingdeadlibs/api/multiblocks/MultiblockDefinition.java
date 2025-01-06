package com.portingdeadmods.portingdeadlibs.api.multiblocks;

import it.unimi.dsi.fastutil.Pair;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Predicate;

public record MultiblockDefinition(Map<Integer, Pair<Predicate<BlockState>, Block>> def) {
    public MultiblockDefinition() {
        this(new HashMap<>());
    }

    public void put(int key, Predicate<BlockState> predicate, Block defaultValue) {
        this.def.put(key, Pair.of(predicate, defaultValue));
    }

    public void put(int key, Block defaultValue) {
        put(key, state -> state.is(defaultValue), defaultValue);
    }

    public Predicate<BlockState> getPredicate(int key) {
        return this.def.get(key).first();
    }

    public Block getDefaultBlock(int key) {
        return this.def.get(key).second();
    }
}