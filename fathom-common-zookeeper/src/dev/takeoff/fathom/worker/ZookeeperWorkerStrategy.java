package dev.takeoff.fathom.worker;

import static dev.takeoff.fathom.worker.ZookeeperUtils.GLOBAL_LOCK_PATH;

import jakarta.annotation.PostConstruct;
import org.apache.curator.framework.CuratorFramework;

public final class ZookeeperWorkerStrategy implements FathomWorkerStrategy {

  private final CuratorFramework curatorFramework;

  public ZookeeperWorkerStrategy(final CuratorFramework curatorFramework) {
    this.curatorFramework = curatorFramework;
  }

  @PostConstruct
  void createLockPathIfAbsent() throws Exception {
    if (curatorFramework.checkExists().forPath(GLOBAL_LOCK_PATH) == null) {
      curatorFramework.create().creatingParentsIfNeeded().forPath(GLOBAL_LOCK_PATH);
    }
  }

  @Override
  public FathomWorkerTransaction createTransaction() throws TransactionCreateException {
    return new ZookeeperWorkerTransaction(curatorFramework);
  }
}
