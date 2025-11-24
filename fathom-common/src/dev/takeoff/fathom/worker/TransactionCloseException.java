package dev.takeoff.fathom.worker;

import dev.takeoff.fathom.FathomException;

public final class TransactionCloseException extends FathomException {

  public TransactionCloseException(final Exception cause) {
    super("Failed to close transaction", cause);
  }
}
