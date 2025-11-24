package dev.takeoff.fathom.worker;

import dev.takeoff.fathom.entity.FathomEntity;

public interface FathomWorker<ID, ENTITY extends FathomEntity<ID, ENTITY>> {

  void work();
}
