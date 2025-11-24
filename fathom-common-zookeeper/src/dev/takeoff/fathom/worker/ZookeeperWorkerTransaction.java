package dev.takeoff.fathom.worker;

import static dev.takeoff.fathom.worker.ZookeeperUtils.GLOBAL_LOCK_PATH;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.locks.InterProcessMutex;

public final class ZookeeperWorkerTransaction implements FathomWorkerTransaction {

  private final InterProcessMutex globalLock;

  ZookeeperWorkerTransaction(final CuratorFramework curatorFramework)
      throws TransactionCreateException {
    this.globalLock = new InterProcessMutex(curatorFramework, GLOBAL_LOCK_PATH);
    try {
      globalLock.acquire();
    } catch (final Exception exception) {
      throw new TransactionCreateException(exception);
    }
  }

  @Override
  public void close() throws TransactionCloseException {
    try {
      globalLock.release();
    } catch (final Exception exception) {
      throw new TransactionCloseException(exception);
    }
  }

  @Override
  public void markAsSuccessful() {}
}
