package dev.fathom.demo;

import dev.takeoff.fathom.FathomProperties;
import dev.takeoff.fathom.worker.FathomWorker;
import dev.takeoff.fathom.worker.FathomWorkerStrategy;

final class DemoEntityWorker extends FathomWorker<String, DemoEntity> {

  DemoEntityWorker(
      final FathomProperties properties,
      final FathomWorkerStrategy workerStrategy,
      final DemoEntityAlgorithm algorithm,
      final DemoEntityStore entityStore) {
    super(properties, workerStrategy, algorithm, entityStore);
  }
}
