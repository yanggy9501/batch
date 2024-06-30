package com.freeing.batch.jdbc.exception;

public class ConvertorException extends RuntimeException {

  public ConvertorException() {
    super();
  }

  public ConvertorException(String message) {
    super(message);
  }

  public ConvertorException(String message, Throwable cause) {
    super(message, cause);
  }

  public ConvertorException(Throwable cause) {
    super(cause);
  }

}
