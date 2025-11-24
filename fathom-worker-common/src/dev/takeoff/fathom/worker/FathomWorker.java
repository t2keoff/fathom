package dev.takeoff.fathom.worker;

import dev.takeoff.fathom.FathomProperties;
import dev.takeoff.fathom.alghoritm.FathomAlgorithm;
import dev.takeoff.fathom.entity.FathomEntity;
import dev.takeoff.fathom.entity.FathomEntityStore;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;

public abstract class FathomWorker<ENTITY extends FathomEntity<ENTITY>> {

  private static final Logger log = LoggerFactory.getLogger(FathomWorker.class);

  private final FathomProperties properties;
  private final FathomWorkerStrategy workerStrategy;
  private final FathomAlgorithm<ENTITY> algorithm;
  private final FathomEntityStore<ENTITY> entityStore;

  protected FathomWorker(
      final FathomProperties properties,
      final FathomWorkerStrategy workerStrategy,
      final FathomAlgorithm<ENTITY> algorithm,
      final FathomEntityStore<ENTITY> entityStore) {
    this.algorithm = algorithm;
    this.properties = properties;
    this.entityStore = entityStore;
    this.workerStrategy = workerStrategy;
  }

  @Scheduled(
      fixedRateString = "#{T(java.time.Duration).parse('${fathom.worker.fixed-rate}').toMillis()}")
  void scheduledRun() {
    run(0);
  }

  private void run(final int times) {
    try (final FathomWorkerTransaction transaction = workerStrategy.createTransaction()) {

      final var processedEntities =
          entityStore
              .retrieveAwaitingEntities()
              .map(algorithm::process)
              .collect(Collectors.toSet());

      entityStore.batchSaveEntities(processedEntities);

      transaction.markAsSuccessful();
    } catch (final TransactionCreateException exception) {
      retry(times, exception);
    }
  }

  private void retry(final int times, final Exception exception) {
    final int retryTimes = properties.retryTimes();
    if (retryTimes == 0) {
      log.error("Exception during transaction creation, not retrying", exception);
      return;
    }

    if (times < retryTimes) {
      log.warn(
          "Exception during transaction creation, retrying {}/{}",
          times + 1,
          retryTimes,
          exception);
      run(times + 1);
      return;
    }

    log.error(
        "Exception during transaction creation, reached max retries {}/{}",
        times,
        retryTimes,
        exception);
  }
}
