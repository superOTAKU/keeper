package org.keeper.server;

import org.keeper.cache.KeeperCache;
import org.keeper.server.replication.ReplicationManager;
import org.keeper.service.IService;
import org.keeper.store.FileKeeperStore;
import org.keeper.store.KeeperStore;

public class KeeperServerController implements IService {
    private KeeperServer server;
    private KeeperCache cache;
    private KeeperStore store;
    private KeeperStoreManager storeManager;
    private KeeperServerConfig config;
    private ReplicationManager replicationManager;

    public KeeperServerController(KeeperServerConfig config) {
        server = new KeeperServer(this);
        storeManager = new KeeperStoreManager(this);
        replicationManager = new ReplicationManager(this);
        this.config = config;
    }

    @Override
    public void start() throws Exception {
        store = new FileKeeperStore(config.getDbPath());
        cache = store.load();
        server.start();
        storeManager.startSchedule();
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
