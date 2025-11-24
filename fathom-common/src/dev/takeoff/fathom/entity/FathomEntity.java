package dev.takeoff.fathom.entity;

import java.time.Instant;

public interface FathomEntity<ID, ENTITY extends FathomEntity<ID, ENTITY>> {

  ID id();

  Instant lastProcessedAt();

  ENTITY lastProcessedAt(Instant lastProcessedAt);

  boolean requiresProcessing();

  ENTITY requiresProcessing(boolean requiresProcessing);

  ENTITY markAsProcessed();
}
