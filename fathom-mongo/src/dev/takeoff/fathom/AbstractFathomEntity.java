package dev.takeoff.fathom;

import java.time.Instant;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Field;

public abstract class AbstractFathomEntity<ENTITY extends AbstractFathomEntity<ENTITY>>
    implements FathomEntity<ENTITY> {

  @Indexed @Field protected boolean requiresProcessing;
  @Indexed @Field protected Instant lastProcessedAt;

  protected AbstractFathomEntity() {
    requiresProcessing = true;
    lastProcessedAt = Instant.EPOCH;
  }

  @Override
  public boolean requiresProcessing() {
    return requiresProcessing;
  }

  @Override
  public ENTITY requiresProcessing(final boolean requiresProcessing) {
    this.requiresProcessing = requiresProcessing;
    return touchForProcessing();
  }

  @Override
  public Instant lastProcessedAt() {
    return lastProcessedAt;
  }

  @Override
  public ENTITY lastProcessedAt(final Instant lastProcessedAt) {
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
