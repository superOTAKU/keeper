package org.keeper.server.replication;

import org.keeper.client.KeeperClient;
import org.keeper.server.KeeperServerController;

/**
 * 集群管理，例如master - slave通信
 */
public class ReplicationManager {
    private final KeeperServerController controller;
    private ReplicationRole role;
    //连接到master以完成状态同步的客户端
    private KeeperClient slaveClient;

    public ReplicationManager(KeeperServerController controller) {
        this.controller = controller;
    }

}
