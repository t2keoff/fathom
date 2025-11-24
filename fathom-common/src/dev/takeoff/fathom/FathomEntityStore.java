package dev.takeoff.fathom;

import java.util.Collection;
import java.util.stream.Stream;

public interface FathomEntityStore<ENTITY extends FathomEntity<ENTITY>> {

  Stream<ENTITY> retrieveAwaitingEntities();

  void batchSaveEntities(Collection<ENTITY> entities);
}
