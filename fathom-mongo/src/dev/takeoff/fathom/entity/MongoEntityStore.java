package dev.takeoff.fathom.entity;

import dev.takeoff.fathom.FathomProperties;
import java.time.Instant;
import java.util.Collection;
import java.util.List;
import java.util.function.UnaryOperator;
import java.util.stream.Stream;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

public class MongoEntityStore<ENTITY extends FathomEntity<ENTITY>>
    implements FathomEntityStore<ENTITY> {

  private final Class<ENTITY> entityType;
  private final FathomProperties properties;
  private final MongoTemplate mongoTemplate;

  public MongoEntityStore(
      final Class<ENTITY> entityType,
      final FathomProperties properties,
      final MongoTemplate mongoTemplate) {
    this.entityType = entityType;
    this.properties = properties;
    this.mongoTemplate = mongoTemplate;
  }

  public Stream<ENTITY> retrieve(final UnaryOperator<Query> queryOperator) {
    final var query = new Query(Criteria.where("requiresProcessing").is(false));

    final List<ENTITY> entities = mongoTemplate.find(queryOperator.apply(query), entityType);
    return properties.useParallelStreams() ? entities.parallelStream() : entities.stream();
  }

  @Override
  public Stream<ENTITY> retrieveAwaitingEntities() {
    final var query =
        new Query(
                Criteria.where("requiresProcessing")
                    .is(true)
                    .orOperator(
                        Criteria.where("lastProcessedAt")
                            .lt(Instant.now().minus(properties.processingStaleAfter()))))
            .limit(properties.processAtOnce());

    final List<ENTITY> entities = mongoTemplate.find(query, entityType);
    return properties.useParallelStreams() ? entities.parallelStream() : entities.stream();
  }

  @Override
  public void batchSaveEntities(final Collection<ENTITY> entities) {
    entities.forEach(mongoTemplate::save);
  }
}
