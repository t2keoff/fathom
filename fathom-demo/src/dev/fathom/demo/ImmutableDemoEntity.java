package dev.fathom.demo;

import dev.takeoff.fathom.entity.FathomEntity;
import java.time.Instant;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.MongoId;

@Document(collection = "demo_entities")
public record ImmutableDemoEntity(
    @MongoId String id,
    @Field long likes,
    @Field long comments,
    @Field long weight,
    @Indexed @Field long hotScore,
    @Indexed @Field boolean requiresProcessing,
    @Indexed @Field Instant lastProcessedAt)
    implements FathomEntity<String, ImmutableDemoEntity> {

  public ImmutableDemoEntity increaseWeight(final long delta) {
    return new ImmutableDemoEntity(
        id, likes, comments, weight + delta, hotScore, requiresProcessing, lastProcessedAt);
  }

  public ImmutableDemoEntity increaseLikes(final long delta) {
    return new ImmutableDemoEntity(
        id, likes + delta, comments, weight + delta, hotScore, requiresProcessing, lastProcessedAt);
  }

  public ImmutableDemoEntity increaseComments(final long delta) {
    return new ImmutableDemoEntity(
        id,
        likes,
        comments + delta,
        weight + delta * 3,
        hotScore,
        requiresProcessing,
        lastProcessedAt);
  }

  public ImmutableDemoEntity hotScore(final Long hotScore) {
    return new ImmutableDemoEntity(
        id, likes, comments, weight, hotScore, requiresProcessing, lastProcessedAt);
  }

  public ImmutableDemoEntity weight(final Long weight) {
    return new ImmutableDemoEntity(
        id, likes, comments, weight, hotScore, requiresProcessing, lastProcessedAt);
  }

  @Override
  public ImmutableDemoEntity lastProcessedAt(final Instant lastProcessedAt) {
    return new ImmutableDemoEntity(
        id, likes, comments, weight, hotScore, requiresProcessing, lastProcessedAt);
  }

  @Override
  public ImmutableDemoEntity requiresProcessing(final boolean requiresProcessing) {
    return new ImmutableDemoEntity(
        id, likes, comments, weight, hotScore, requiresProcessing, lastProcessedAt);
  }

  @Override
  public ImmutableDemoEntity markAsProcessed() {
    return new ImmutableDemoEntity(id, likes, comments, weight, hotScore, false, Instant.now());
  }
}
