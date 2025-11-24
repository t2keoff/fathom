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
  @Field private long weight;
  @Indexed @Field private long hotScore;

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

  public long hotScore() {
    return hotScore;
  }

  public DemoEntity hotScore(final long hotScore) {
    this.hotScore = hotScore;
    return this;
  }

  public long likes() {
    return likes;
  }

  public long comments() {
    return comments;
  }

  public long weight() {
    return weight;
  }

  public DemoEntity weight(final long weight) {
    this.weight = weight;
    return this;
  }
}
