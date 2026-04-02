package com.portingdeadmods.portingdeadlibs.api.multiblocks;

import com.portingdeadmods.portingdeadlibs.api.misc.HorizontalDirection;
import net.minecraft.nbt.CompoundTag;

import java.util.Optional;

public record MultiblockData(boolean valid, HorizontalDirection direction, MultiblockLayer[] layers) {
    public static final MultiblockData EMPTY = new MultiblockData(false, HorizontalDirection.NORTH, new MultiblockLayer[0]);

    public CompoundTag serializeNBT() {
        CompoundTag tag = new CompoundTag();
        tag.putInt("layersLength", layers.length);
        CompoundTag listTag = new CompoundTag();
        for (int i = 0, expandedLayersLength = layers.length; i < expandedLayersLength; i++) {
            MultiblockLayer layer = layers[i];
            listTag.put(String.valueOf(i), layer.save());
        }
        tag.put("layersList", listTag);
        tag.putInt("direction", this.direction.ordinal());
        tag.putBoolean("valid", this.valid);
        return tag;
    }

    public static MultiblockData deserializeNBT(CompoundTag nbt) {
        int layersLength = nbt.getIntOr("layersLength", 0);
        CompoundTag listTag = nbt.getCompoundOrEmpty("layersList");
        MultiblockLayer[] layers = new MultiblockLayer[layersLength];
        for (int i = 0; i < layers.length; i++) {
            Optional<CompoundTag> compound = listTag.getCompound(String.valueOf(i));
            if (compound.isPresent()) {
                layers[i] = MultiblockLayer.load(compound.get());
            }
        }
        HorizontalDirection direction = HorizontalDirection.values()[nbt.getIntOr("direction", 0)];
        boolean valid = nbt.getBooleanOr("valid", false);
        return new MultiblockData(valid, direction, layers);
    }
}