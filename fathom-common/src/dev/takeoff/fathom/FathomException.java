package dev.takeoff.fathom;

public abstract class FathomException extends RuntimeException {

  protected FathomException(final String message) {
    super(message);
  }

  protected FathomException(final String message, final Throwable cause) {
    super(message, cause);
  }
}
