package dev.takeoff.fathom;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.scheduling.annotation.EnableScheduling;

@Retention(RUNTIME)
@Target(TYPE)
@EnableScheduling
@EnableConfigurationProperties(FathomProperties.class)
public @interface EnableMongoFathom {}
