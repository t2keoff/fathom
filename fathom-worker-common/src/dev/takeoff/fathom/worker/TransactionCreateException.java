package dev.takeoff.fathom.worker;

public final class TransactionCreateException extends IllegalStateException {

  public TransactionCreateException(final Exception cause) {
    super("Failed to create transaction", cause);
  }
}
