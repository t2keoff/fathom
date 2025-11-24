package dev.takeoff.fathom.worker;

import dev.takeoff.fathom.FathomException;

public final class TransactionCreateException extends FathomException {

  public TransactionCreateException(final Exception cause) {
    super("Failed to create transaction", cause);
  }
}
