package dev.takeoff.fathom.worker;

import dev.takeoff.fathom.FathomProperties;
import dev.takeoff.fathom.alghoritm.FathomAlgorithm;
import dev.takeoff.fathom.entity.FathomEntity;
import java.time.Instant;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.scheduling.annotation.Scheduled;

public abstract class MongoFathomWorker<ID, ENTITY extends FathomEntity<ID, ENTITY>>
    implements FathomWorker<ID, ENTITY> {

  private final Class<ENTITY> entityType;
  private final FathomAlgorithm<ID, ENTITY> algorithm;
  private final MongoTemplate mongoTemplate;
  private final FathomProperties properties;

  protected MongoFathomWorker(
      final Class<ENTITY> entityType,
      final FathomAlgorithm<ID, ENTITY> algorithm,
      final MongoTemplate mongoTemplate,
      final FathomProperties properties) {
    this.entityType = entityType;
    this.algorithm = algorithm;
    this.mongoTemplate = mongoTemplate;
    this.properties = properties;
  }

  @Override
  @Scheduled(
      fixedRateString = "#{T(java.time.Duration).parse('${fathom.worker.fixed-rate}').toMillis()}")
  public void work() {
    final var query =
        new Query(
                Criteria.where("requiresProcessing")
                    .is(true)
                    .orOperator(
                        Criteria.where("lastProcessedAt")
                            .lt(Instant.now().minus(properties.processingStaleAfter()))))
            .limit(properties.processAtOnce());
    final var entities = mongoTemplate.find(query, entityType);
    final var processedEntities =
        entities.stream().map(algorithm::process).collect(Collectors.toSet());
    saveEntities(processedEntities);
  }

  protected abstract void saveEntities(final Set<ENTITY> entities);
}
