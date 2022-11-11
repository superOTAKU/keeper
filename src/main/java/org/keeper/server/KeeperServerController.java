package org.keeper.server;

import org.keeper.cache.KeeperCache;
import org.keeper.service.IService;
import org.keeper.store.FileKeeperStore;
import org.keeper.store.KeeperStore;

public class KeeperServerController implements IService {
    private KeeperServer server;
    private KeeperCache cache;
    private KeeperStore store;
    private KeeperServerConfig config;

    public KeeperServerController(KeeperServerConfig config) {
        server = new KeeperServer(this);
        this.config = config;
    }

    @Override
    public void start() throws Exception {
        store = new FileKeeperStore(config.getDbPath());
        cache = store.load();
        server.start();
        //TODO 定时存盘
    }

    @Override
    public void stop() {

    }

    public KeeperServer getServer() {
        return server;
    }

    public KeeperCache getCache() {
        return cache;
    }

    public KeeperStore getStore() {
        return store;
    }

    public KeeperServerConfig getConfig() {
        return config;
    }
}
