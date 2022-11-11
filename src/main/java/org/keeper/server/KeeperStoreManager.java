package org.keeper.server;

import cn.hutool.core.thread.NamedThreadFactory;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.keeper.cache.KeeperCache;
import org.keeper.object.KeeperObject;

import java.util.LinkedHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

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
    private final ExecutorService taskService = Executors.newFixedThreadPool(1, new NamedThreadFactory("storeExecutor", false));

    public KeeperStoreManager(KeeperServerController controller) {
        this.controller = controller;
    }

    public void startSchedule() {
        scheduler.scheduleWithFixedDelay(() -> {}, 5, 5, TimeUnit.SECONDS);
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

    @Setter
    @Getter
    public static class CacheClonedEvent {
        private KeeperCache clonedCache;
    }

}
