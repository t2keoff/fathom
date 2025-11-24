package dev.fathom.demo;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DemoEntityRepository extends MongoRepository<DemoEntity, String> {}
