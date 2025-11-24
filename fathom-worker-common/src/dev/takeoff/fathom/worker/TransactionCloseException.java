package dev.takeoff.fathom.worker;

public final class TransactionCloseException extends RuntimeException {

  public TransactionCloseException(final Exception cause) {
    super("Failed to close transaction", cause);
  }
}
