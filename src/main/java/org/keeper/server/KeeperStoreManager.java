package org.keeper.server;

import cn.hutool.core.thread.NamedThreadFactory;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.keeper.cache.KeeperCache;
import java.util.concurrent.*;

/**
 * 负责定时存盘任务:
 *  1.定时将任务投递到keeperServer
 *  2.keeperServer接收到任务执行cache克隆
 *  3.将克隆后的cache封装成事件，投递回本线程，执行存盘操作
 */
@Slf4j
public class KeeperStoreManager {
    private final KeeperServerController controller;
    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1,
            new NamedThreadFactory("storeSchedule", false));
    private ScheduledFuture<?> scheduledFuture;
    private final ExecutorService taskService = Executors.newFixedThreadPool(1, new NamedThreadFactory("storeExecutor", false));

    public KeeperStoreManager(KeeperServerController controller) {
        this.controller = controller;
    }

    public void startSchedule() {
        scheduledFuture = scheduler.scheduleWithFixedDelay(this::triggerCacheClone, 5, 5, TimeUnit.SECONDS);
        log.info("start keeper store manager schedule");
    }

    public void onCacheCloned(CacheClonedEvent event) {
        taskService.execute(() -> {
            try {
                controller.getStore().save(event.getClonedCache());
                log.info("period save cache to store success");
            } catch (Exception e) {
                log.error("period save cache to store error", e);
            }
        });
    }

    public void triggerCacheClone() {
        log.info("period trigger cache clone");
        controller.getServer().executeTask(() -> {
            KeeperCache clonedCache = controller.getCache().clone();
            CacheClonedEvent event = new CacheClonedEvent();
            event.setClonedCache(clonedCache);
            onCacheCloned(event);
        });
    }

    @Setter
    @Getter
    public static class CacheClonedEvent {
        private KeeperCache clonedCache;
    }

}
