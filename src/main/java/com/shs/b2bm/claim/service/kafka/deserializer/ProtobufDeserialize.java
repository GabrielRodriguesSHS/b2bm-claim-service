package com.shs.b2bm.claim.service.kafka.deserializer;

import com.google.protobuf.Message;
import org.apache.kafka.common.serialization.Deserializer;

import java.util.Map;

/** Custom deserializer for Protobuf messages. */
public class ProtobufDeserialize<T extends Message> implements Deserializer<T> {

    private final Class<T> type;

    public ProtobufDeserialize(Class<T> type) {
        this.type = type;
    }

    @Override
    public void configure(Map<String, ?> configs, boolean isKey) {
        // No-op
    }

    @Override
    public T deserialize(String topic, byte[] data) {
        if (data == null) {
            return null;
        }
        try {
            // Use reflection to call the static parseFrom(byte[]) method
            return type.cast(type.getMethod("parseFrom", byte[].class).invoke(null, new Object[]{ data }));
        } catch (Exception e) {
            throw new RuntimeException("Failed to deserialize Protobuf message", e);
        }
    }

    @Override
    public void close() {
        // No-op
    }
}
