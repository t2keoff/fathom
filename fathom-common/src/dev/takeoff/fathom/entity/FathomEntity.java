package dev.takeoff.fathom.entity;

import java.time.Instant;

public interface FathomEntity<ID, ENTITY extends FathomEntity<ID, ENTITY>> {

  ID getId();

  Instant getLastProcessedAt();

  ENTITY setLastProcessedAt(Instant lastProcessedAt);

  Boolean getRequiresProcessing();

  ENTITY setRequiresProcessing(Boolean requiresProcessing);

  ENTITY markAsProcessed();
}
