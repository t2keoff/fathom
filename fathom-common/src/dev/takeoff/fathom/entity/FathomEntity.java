package dev.takeoff.fathom.entity;

import java.time.Instant;

public interface FathomEntity<ENTITY extends FathomEntity<ENTITY>> {

  Instant lastProcessedAt();

  ENTITY lastProcessedAt(Instant lastProcessedAt);

  boolean requiresProcessing();

  ENTITY requiresProcessing(boolean requiresProcessing);

  ENTITY markAsProcessed();
}
