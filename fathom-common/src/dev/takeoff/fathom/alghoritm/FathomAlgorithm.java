package dev.takeoff.fathom.alghoritm;

import dev.takeoff.fathom.entity.FathomEntity;

@FunctionalInterface
public interface FathomAlgorithm<ID, ENTITY extends FathomEntity<ID, ENTITY>> {

  ENTITY process(ENTITY entity);
}
