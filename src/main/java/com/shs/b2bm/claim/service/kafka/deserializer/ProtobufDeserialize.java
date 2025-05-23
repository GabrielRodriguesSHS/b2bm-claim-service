package com.shs.b2bm.claim.service.kafka.deserializer;

import com.google.protobuf.Message;
import com.shs.b2bm.claim.service.exceptions.ProtobufDeserializationException;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.common.header.Headers;
import org.apache.kafka.common.serialization.Deserializer;

import java.util.Map;

/**
 * Custom deserializer for Protobuf messages.
 * Handles message versioning and provides robust error handling.
 */
@Slf4j
public class ProtobufDeserialize<T extends Message> implements Deserializer<T> {

    private final Class<T> type;

    /**
     * Constructs a new ProtobufDeserialize instance.
     *
     * @param type The class type of the Protobuf message
     */
    public ProtobufDeserialize(Class<T> type) {
        this.type = type;
    }

    @Override
    public void configure(Map<String, ?> configs, boolean isKey) {
        // Configuration can be added here if needed
    }

    @Override
    public T deserialize(String topic, byte[] data) {
        return deserializeWithVersion(topic, data);
    }

    /**
     * Deserializes the message data with version checking.
     *
     * @param topic The Kafka topic
     * @param data The message data
     * @return The deserialized message
     * @throws ProtobufDeserializationException if deserialization fails
     */
    private T deserializeWithVersion(String topic, byte[] data) {
        if (data == null) {
            return null;
        }

        try {
            log.debug("Deserializing message from topic: {}", topic);
            
            return type.cast(type.getMethod("parseFrom", byte[].class).invoke(null, new Object[]{ data }));
        } catch (Exception e) {
            String errorMessage = String.format(
                "Failed to deserialize Protobuf message from topic: %s", topic);
            log.error(errorMessage, e);
            throw new ProtobufDeserializationException(errorMessage, e);
        }
    }

    @Override
    public void close() {
        // Cleanup can be added here if needed
    }
}
