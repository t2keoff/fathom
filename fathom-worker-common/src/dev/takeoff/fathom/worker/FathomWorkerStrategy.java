package dev.takeoff.fathom.worker;

@FunctionalInterface
public interface FathomWorkerStrategy {

  FathomWorkerTransaction createTransaction();
}
