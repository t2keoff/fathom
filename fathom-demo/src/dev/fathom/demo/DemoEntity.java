package dev.fathom.demo;

import dev.takeoff.fathom.entity.AbstractFathomEntity;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.MongoId;

@Document(collection = "demo_entities")
public class DemoEntity extends AbstractFathomEntity<String, DemoEntity> {

  @MongoId private String id;
  @Field private long likes;
  @Field private long comments;
  @Field private Long weight;
  @Indexed @Field private Long hotScore;

  public DemoEntity() {
    super();
    weight = 0L;
    hotScore = weight;
  }

  @Override
  public String getId() {
    return id;
  }

  public DemoEntity setId(final String id) {
    this.id = id;
    return this;
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

  public DemoEntity markAsToProcess() {
    this.requiresProcessing = true;
    return this;
  }

  public Long getHotScore() {
    return hotScore;
  }

  public DemoEntity setHotScore(final Long hotScore) {
    this.hotScore = hotScore;
    return this;
  }

  public long getLikes() {
    return likes;
  }

  public long getComments() {
    return comments;
  }

  public Long getWeight() {
    return weight;
  }

  public DemoEntity setWeight(final Long weight) {
    this.weight = weight;
    return this;
  }
}
