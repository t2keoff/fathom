package dev.takeoff.fathom.entity;

import java.time.Instant;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Field;

public abstract class AbstractFathomEntity<ID, ENTITY extends AbstractFathomEntity<ID, ENTITY>>
    implements FathomEntity<ID, ENTITY> {

  @Indexed @Field protected Boolean requiresProcessing;
  @Indexed @Field protected Instant lastProcessedAt;

  protected AbstractFathomEntity() {
    requiresProcessing = true;
    lastProcessedAt = Instant.EPOCH;
  }

  @Override
  public Boolean getRequiresProcessing() {
    return requiresProcessing;
  }

  @Override
  public ENTITY setRequiresProcessing(final Boolean requiresProcessing) {
    this.requiresProcessing = requiresProcessing;
    return touchForProcessing();
  }

  @Override
  public Instant getLastProcessedAt() {
    return lastProcessedAt;
  }

  @Override
  public ENTITY setLastProcessedAt(final Instant lastProcessedAt) {
    this.lastProcessedAt = lastProcessedAt;
    return touchForProcessing();
  }

  @Override
  public ENTITY markAsProcessed() {
    this.requiresProcessing = false;
    this.lastProcessedAt = Instant.now();
    return touchForProcessing();
  }

  @SuppressWarnings("unchecked")
  public ENTITY touchForProcessing() {
    this.requiresProcessing = true;
    return (ENTITY) this;
  }
}
