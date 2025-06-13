package com.shs.b2bm.claim.service.kafka.deserializer;

import com.google.protobuf.Message;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.common.errors.SerializationException;
import org.apache.kafka.common.serialization.Deserializer;

import java.util.Map;

/**
 * Custom deserializer for Protobuf messages. Handles message versioning and provides robust error
 * handling.
 */
@Slf4j
public class ProtobufDeserializer<T extends Message> implements Deserializer<T> {

    private final Class<T> type;

    /**
     * Constructs a new ProtobufDeserialize instance.
     *
     * @param type The class type of the Protobuf message
     */
    public ProtobufDeserializer(Class<T> type) {
        this.type = type;
    }

    @Override
    public void configure(Map<String, ?> configs, boolean isKey) {
        // Configuration can be added here if needed
    }

    @Override
    public T deserialize(String topic, byte[] data) {
        if (data == null) {
            return null;
        }

        try {
            log.debug("Deserializing message from topic: {}", topic);

            return type.cast(type.getMethod("parseFrom", byte[].class).invoke(null, new Object[]{data}));
        } catch (Exception e) {
            String errorMessage =
                    String.format("Failed to deserialize Protobuf message from topic: %s", topic);
            log.error(errorMessage, e);
            throw new SerializationException("Error deserializing Protobuf message", e);
        }
    }

    @Override
    public void close() {
        // Cleanup can be added here if needed
    }
}
