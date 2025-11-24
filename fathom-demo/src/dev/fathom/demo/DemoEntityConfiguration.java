package dev.fathom.demo;

import dev.takeoff.fathom.EnableMongoFathom;
import dev.takeoff.fathom.FathomProperties;
import dev.takeoff.fathom.worker.FathomWorkerStrategy;
import dev.takeoff.fathom.worker.ZookeeperWorkerStrategy;
import org.apache.curator.framework.CuratorFramework;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.MongoTemplate;

@Configuration
@EnableMongoFathom
class DemoEntityConfiguration {

  DemoEntityConfiguration() {}

  @Bean
  FathomWorkerStrategy workerStrategy(final CuratorFramework curatorFramework) {
    return new ZookeeperWorkerStrategy(curatorFramework);
  }

  @Bean
  DemoEntityAlgorithm demoEntityAlgorithm() {
    return new DemoEntityAlgorithm();
  }

  @Bean
  DemoEntityStore demoEntityStore(
      final FathomProperties properties, final MongoTemplate mongoTemplate) {
    return new DemoEntityStore(properties, mongoTemplate);
  }

  @Bean
  DemoEntityWorker demoEntityWorker(
      final FathomProperties properties,
      final FathomWorkerStrategy workerStrategy,
      final DemoEntityAlgorithm algorithm,
      final DemoEntityStore entityStore) {
    return new DemoEntityWorker(properties, workerStrategy, algorithm, entityStore);
  }
}
