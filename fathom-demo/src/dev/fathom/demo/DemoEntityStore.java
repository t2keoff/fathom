package dev.fathom.demo;

import dev.takeoff.fathom.FathomProperties;
import dev.takeoff.fathom.MongoEntityStore;
import java.util.stream.Stream;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;

public final class DemoEntityStore extends MongoEntityStore<DemoEntity> {

  DemoEntityStore(final FathomProperties properties, final MongoTemplate mongoTemplate) {
    super(DemoEntity.class, properties, mongoTemplate);
  }

  public Stream<DemoEntity> retrieveViral(final int page) {
    return retrieve(
        query -> query.with(PageRequest.of(page, 50, Sort.by("viralScore").descending())));
  }
}
