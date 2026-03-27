package com.portingdeadmods.portingdeadlibs.api.blockentities;

import net.minecraft.core.Direction;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.neoforged.neoforge.capabilities.BlockCapability;
import net.neoforged.neoforge.common.util.ValueIOSerializable;
import org.jetbrains.annotations.Nullable;

import java.util.function.BiConsumer;
import java.util.function.Consumer;

public interface ResourceHandlerBlockEntity {
    default <H> @Nullable H getHandler(Identifier id) {
        return (H) this.getHandlerHolder().getHandler(id);
    }

    default <H> @Nullable H getHandlerOnSide(Identifier id, Direction direction) {
        return this.getHandler(id);
    }

    default <H> @Nullable H getHandler(BlockCapability<H, ?> capability) {
        return (H) this.getHandlerHolder().getHandler(capability.name());
    }

    default <H> @Nullable H getHandlerOnSide(BlockCapability<H, ?> capability, Direction direction) {
        return this.getHandler(capability.name());
    }

    default <H extends ValueIOSerializable> H addHandler(Identifier id, H handler) {
        this.getHandlerHolder().addHandler(id, handler);
        return handler;
    }

    default <H> H addHandler(Identifier id, H handler, BiConsumer<Object, ValueOutput> serializeFunction, BiConsumer<Object, ValueInput> deserializeFunction) {
        this.getHandlerHolder().addHandler(id, handler);
        this.getHandlerHolder().addSerializer(id, serializeFunction, deserializeFunction);
        return handler;
    }

    default <H extends ValueIOSerializable> H addHandler(BlockCapability<? super H, ?> capability, H handler) {
       return this.addHandler(capability.name(), handler);
    }

    default <H> H addHandler(BlockCapability<H, ?> capability, H handler, BiConsumer<Object, ValueOutput> serializeFunction, BiConsumer<Object, ValueInput> deserializeFunction) {
        return this.addHandler(capability.name(), handler, serializeFunction, deserializeFunction);
    }

    default <H> H addHandlerNoSave(BlockCapability<? super H, ?> capability, H handler) {
        this.getHandlerHolder().addHandler(capability.name(), handler);
        return handler;
    }

    default <H> H addHandlerNoSave(Identifier id, H handler) {
        this.getHandlerHolder().addHandler(id, handler);
        return handler;
    }

    ResourceHandlerHolder getHandlerHolder();

}
