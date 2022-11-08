package org.keeper.store;

import org.keeper.cache.KeeperCache;

/**
 * 持久层
 */
public interface KeeperStore {

    /**
     * 持久化缓存
     */
    void save(KeeperCache cache) throws Exception;

    /**
     * 加载缓存
     */
    KeeperCache load() throws Exception;

}
