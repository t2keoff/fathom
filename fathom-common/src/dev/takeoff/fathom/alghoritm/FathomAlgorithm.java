package dev.takeoff.fathom.alghoritm;

import dev.takeoff.fathom.entity.FathomEntity;

@FunctionalInterface
public interface FathomAlgorithm<ENTITY extends FathomEntity<ENTITY>> {

  ENTITY process(ENTITY entity);
}
