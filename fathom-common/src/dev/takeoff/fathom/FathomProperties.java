package dev.takeoff.fathom;

import java.time.Duration;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "fathom")
public record FathomProperties(
    int processAtOnce, Duration processingStaleAfter, boolean useParallelStreams, int retryTimes) {}
