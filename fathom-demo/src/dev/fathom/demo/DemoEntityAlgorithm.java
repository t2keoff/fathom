package dev.fathom.demo;

import dev.takeoff.fathom.alghoritm.FathomAlgorithm;
import java.time.Instant;

final class DemoEntityAlgorithm implements FathomAlgorithm<String, DemoEntity> {

  // @Override
  // public DemoEntity process(final DemoEntity entity) {
  //     // do something with entity
  //   final long likes = entity.getLikes(), comments = entity.getComments();
  //
  //   final long hotScore = likes + comments * 10;
  //   entity.setHotScore(hotScore).markAsProcessed();
  //
  //   return entity;
  // }

  @Override
  public DemoEntity process(final DemoEntity entity) {
    final Instant now = Instant.now(), lastProcessedAt = entity.getLastProcessedAt();

    final long weight = entity.getWeight(),
        daysSinceLastProcessed =
            lastProcessedAt == null
                ? 0
                : Math.max(0, now.getEpochSecond() - lastProcessedAt.getEpochSecond()) / 86400,
        hotScore = weight / (1 + daysSinceLastProcessed);

    return entity.setHotScore(hotScore).markAsProcessed();
  }
}
