package dev.takeoff.fathom.worker;

public interface FathomWorkerTransaction extends AutoCloseable {

  @Override
  void close() throws TransactionCloseException;

  void markAsSuccessful();
}
