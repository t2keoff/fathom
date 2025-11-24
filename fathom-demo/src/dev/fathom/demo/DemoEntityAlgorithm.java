package dev.fathom.demo;

import dev.takeoff.fathom.alghoritm.FathomAlgorithm;
import java.time.Instant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

final class DemoEntityAlgorithm implements FathomAlgorithm<String, DemoEntity> {

  private static final Logger log = LoggerFactory.getLogger(DemoEntityAlgorithm.class);

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
    final Instant now = Instant.now(), lastProcessedAt = entity.lastProcessedAt();

    final long weight = entity.weight(),
        daysSinceLastProcessed =
            lastProcessedAt == null
                ? 0
                : Math.max(0, now.getEpochSecond() - lastProcessedAt.getEpochSecond()) / 86400,
        hotScore = weight / (1 + daysSinceLastProcessed);

    log.info(
        "Processing entity id={} weight={} daysSinceLastProcessed={} hotScore={}",
        entity.id(),
        weight,
        daysSinceLastProcessed,
        hotScore);

    return entity.hotScore(hotScore).markAsProcessed();
  }
}
