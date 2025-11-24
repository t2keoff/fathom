package dev.takeoff.fathom;

@FunctionalInterface
public interface FathomAlgorithm<ENTITY extends FathomEntity<ENTITY>> {

  ENTITY process(ENTITY entity);
}
