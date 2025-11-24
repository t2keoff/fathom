package dev.fathom.demo;

import dev.takeoff.fathom.EnableMongoFathom;
import dev.takeoff.fathom.FathomProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.MongoTemplate;

@Configuration
@EnableMongoFathom
class DemoEntityConfiguration {

  DemoEntityConfiguration() {}

  @Bean
  DemoEntityAlgorithm demoEntityAlgorithm() {
    return new DemoEntityAlgorithm();
  }

  @Bean
  DemoEntityWorker demoEntityWorker(
      final DemoEntityAlgorithm algorithm,
      final DemoEntityRepository repository,
      final MongoTemplate mongoTemplate,
      final FathomProperties properties) {
    return new DemoEntityWorker(algorithm, repository, mongoTemplate, properties);
  }
}
