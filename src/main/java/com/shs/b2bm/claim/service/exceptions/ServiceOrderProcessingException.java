package com.shs.b2bm.claim.service.exceptions;

/** Exception thrown when there is an error processing a service order. */
public class ServiceOrderProcessingException extends RuntimeException {

  public ServiceOrderProcessingException(String message) {
    super(message);
  }

  public ServiceOrderProcessingException(String message, Throwable cause) {
    super(message, cause);
  }
}
