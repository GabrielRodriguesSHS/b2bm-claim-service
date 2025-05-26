package com.shs.b2bm.claim.service.exceptions;

/** Exception thrown when there is an error deserializing a Protobuf message. */
public class ProtobufDeserializationException extends RuntimeException {

  public ProtobufDeserializationException(String message) {
    super(message);
  }

  public ProtobufDeserializationException(String message, Throwable cause) {
    super(message, cause);
  }
}
