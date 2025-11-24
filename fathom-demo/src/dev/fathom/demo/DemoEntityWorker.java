package dev.fathom.demo;

import dev.takeoff.fathom.FathomProperties;
import dev.takeoff.fathom.worker.MongoFathomWorker;
import jakarta.annotation.PostConstruct;
import java.util.Set;
import java.util.stream.IntStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.mongodb.core.MongoTemplate;

final class DemoEntityWorker extends MongoFathomWorker<String, DemoEntity> {

  private static final Logger log = LoggerFactory.getLogger(DemoEntityWorker.class);

  private final DemoEntityRepository repository;

  DemoEntityWorker(
      final DemoEntityAlgorithm algorithm,
      final DemoEntityRepository repository,
      final MongoTemplate mongoTemplate,
      final FathomProperties properties) {
    super(DemoEntity.class, algorithm, mongoTemplate, properties);
    this.repository = repository;
  }

  @PostConstruct
  void init() {
    repository.insert(
        IntStream.range(1, 1000)
            .mapToObj(i -> new DemoEntity().setWeight(999L * i).markAsToProcess())
            .toList());
  }

  @Override
  protected void saveEntities(final Set<DemoEntity> demoEntities) {
    log.info("Saving {} demo entities", demoEntities.size());
    repository.saveAll(demoEntities);
  }
}
