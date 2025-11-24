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

Example entity

```java

@Document(collection = "demo_entities")
public class DemoEntity extends AbstractFathomEntity<String, DemoEntity> {

  @MongoId
  private String id;
  @Field
  private long likes;
  @Field
  private long comments;
  @Field
  private Long weight;
  @Indexed
  @Field
  private final Long hotScore;

  public DemoEntity() {
    super();
    weight = 0L;
    hotScore = weight;
  }

  @Override
  public String getId() {
    return id;
  }

  public DemoEntity increaseWeight(final long delta) {
    if (weight == null) {
      weight = 0L;
    }
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

  // ... getters and setters below
}

```

Example algorithm

```java
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
```

Example worker

```java
final class DemoEntityWorker extends MongoFathomWorker<String, DemoEntity> {

  private final DemoEntityRepository repository;

  DemoEntityWorker(
      final DemoEntityAlgorithm algorithm,
      final DemoEntityRepository repository,
      final MongoTemplate mongoTemplate,
      final FathomProperties properties) {
    super(DemoEntity.class, algorithm, mongoTemplate, properties);
    this.repository = repository;
  }

  @Override
  protected void saveEntities(final Set<DemoEntity> demoEntities) {
    repository.saveAll(demoEntities);
  }
}
```