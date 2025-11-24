# fathom

An experimental approach for social graphs and recommendation systems.

## Setup (gradle kts)

```kts
repositories {
    mavenLocal()
}

dependencies {
//    implementation("dev.takeoff.fathom:common:1.0.0")

    // only mongo is supported at the moment
    implementation("dev.takeoff.fathom:mongo:1.0.0")

    // for zookeeper worker strategy or add default in-memory strategy
    implementation("dev.takeoff.fathom:worker-zookeeper:1.0")
}
```

## Usage

Spring example config

```yaml
spring:
  data:
    mongodb:
      auto-index-creation: true
      uri: mongodb://localhost:27017/demo
fathom:
  process-at-once: 150
  processingStaleAfter: PT1M
  worker:
    fixed-rate: PT10S
```

Example algorithm

```java
final class DemoEntityAlgorithm implements FathomAlgorithm<DemoEntity> {

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

    return entity.hotScore(hotScore).markAsProcessed();
  }
}
```

Example worker

```java
final class DemoEntityWorker extends FathomWorker<DemoEntity> {

  DemoEntityWorker(
      final FathomProperties properties,
      final FathomWorkerStrategy workerStrategy,
      final DemoEntityAlgorithm algorithm,
      final DemoEntityStore entityStore) {
    super(properties, workerStrategy, algorithm, entityStore);
  }
}
```

Example store

```java
public final class DemoEntityStore extends MongoEntityStore<DemoEntity> {

  DemoEntityStore(final FathomProperties properties, final MongoTemplate mongoTemplate) {
    super(DemoEntity.class, properties, mongoTemplate);
  }

  public Stream<DemoEntity> retrieveViral(final int page) { // custom query example
    return retrieve(
        query -> query
            // you can add custom criteria here .addCriteria(Criteria.where("someField").is(someValue))
            .with(PageRequest.of(page, 50, Sort.by("viralScore").descending())));
  }
}
```

Example configuration

```java

@Configuration
@EnableMongoFathom
class DemoEntityConfiguration {

  DemoEntityConfiguration() {
  }

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
```

Example immutable entity

```java

@Document(collection = "demo_entities")
public record ImmutableDemoEntity(
    @MongoId String id,
    @Field long likes,
    @Field long comments,
    @Field long weight,
    @Indexed @Field long hotScore,
    @Indexed @Field boolean requiresProcessing,
    @Indexed @Field Instant lastProcessedAt)
    implements FathomEntity<ImmutableDemoEntity> {

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
```

Example entity

```java

@Document(collection = "demo_entities")
public class DemoEntity extends AbstractFathomEntity<DemoEntity> {

  @MongoId
  private String id;
  @Field
  private long likes;
  @Field
  private long comments;
  @Field
  private long weight;
  @Indexed
  @Field
  private long hotScore;

  public DemoEntity() {
    super();
  }

  @Override
  public String id() {
    return id;
  }

  public void id(final String id) {
    this.id = id;
  }

  public DemoEntity increaseWeight(final long delta) {
    weight += delta;
    return this;
  }

  public DemoEntity increaseLikes(final long delta) {
    this.likes += delta;
    return increaseWeight(1);
  }

  public DemoEntity increaseComments(final long delta) {
    this.comments += delta;
    return increaseWeight(5);
  }

  public DemoEntity markAsToProcess() {
    this.requiresProcessing = true;
    return this;
  }

  // ... getters and setters below
}

```

![Visitor Count](https://visitor-badge.laobi.icu/badge?page_id=t2keoff.fathom)
