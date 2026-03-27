package com.portingdeadmods.portingdeadlibs.api.blockentities;

import net.minecraft.resources.Identifier;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.neoforged.neoforge.common.util.ValueIOSerializable;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.BiConsumer;

public class ResourceHandlerHolder {
    private final Map<Identifier, Object> handlers;
    private final Map<Identifier, BiConsumer<Object, ValueOutput>> handlerSerializeFunctions;
    private final Map<Identifier, BiConsumer<Object, ValueInput>> handlerDeserializeFunctions;

    public ResourceHandlerHolder() {
        this.handlers = new HashMap<>();
        this.handlerSerializeFunctions = new HashMap<>();
        this.handlerDeserializeFunctions = new HashMap<>();
    }

    public void addHandler(Identifier id, Object handler) {
        this.handlers.put(id, handler);
    }

    public void addSerializer(Identifier id, BiConsumer<Object, ValueOutput> serializeFunction, BiConsumer<Object, ValueInput> deserializeFunction) {
        this.handlerSerializeFunctions.put(id, serializeFunction);
        this.handlerDeserializeFunctions.put(id, deserializeFunction);
    }

    public Object getHandler(Identifier id) {
        return this.handlers.get(id);
    }

    public void serialize(ValueOutput output) {
        for (Identifier id : this.handlers.keySet()) {
            Object handler = this.getHandler(id);
            ValueOutput child = output.child(id.toString().replace(":", "_"));
            if (handler instanceof ValueIOSerializable serializable) {
                serializable.serialize(child);
            } else if (this.handlerSerializeFunctions.containsKey(id) && this.handlerDeserializeFunctions.containsKey(id)) {
                BiConsumer<Object, ValueOutput> serializeFunction = this.handlerSerializeFunctions.get(id);

                serializeFunction.accept(handler, child);
            }
        }
    }

    public void deserialize(ValueInput input) {
        for (Identifier id : this.handlers.keySet()) {
            Object handler = this.getHandler(id);
            Optional<ValueInput> _child = input.child(id.toString().replace(":", "_"));
            if (_child.isPresent()) {
                ValueInput child = _child.get();
                if (handler instanceof ValueIOSerializable serializable) {
                    serializable.deserialize(child);
                } else if (this.handlerSerializeFunctions.containsKey(id) && this.handlerDeserializeFunctions.containsKey(id)) {
                    BiConsumer<Object, ValueInput> deserializeFunction = this.handlerDeserializeFunctions.get(id);

                    deserializeFunction.accept(handler, child);
                }
            }
        }
    }

}
