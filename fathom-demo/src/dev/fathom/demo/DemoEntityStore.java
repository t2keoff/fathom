package dev.fathom.demo;

import dev.takeoff.fathom.FathomProperties;
import dev.takeoff.fathom.entity.MongoEntityStore;
import org.springframework.data.mongodb.core.MongoTemplate;

final class DemoEntityStore extends MongoEntityStore<String, DemoEntity> {

  DemoEntityStore(final FathomProperties properties, final MongoTemplate mongoTemplate) {
    super(DemoEntity.class, properties, mongoTemplate);
  }
}
