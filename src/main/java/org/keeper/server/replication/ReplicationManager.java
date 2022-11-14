package org.keeper.server.replication;

import org.keeper.client.KeeperClient;
import org.keeper.server.KeeperServerController;

import java.util.HashMap;
import java.util.Map;

/**
 * 集群管理，例如master - slave通信
 */
public class ReplicationManager {
    private final KeeperServerController controller;
    private ReplicationRole role;
    //连接到master以完成状态同步的客户端
    private KeeperClient masterClient;
    private final Map<String, SlaveInfo> slaves = new HashMap<>();

    public ReplicationManager(KeeperServerController controller) {
        this.controller = controller;
    }

    public ReplicationRole getRole() {
        return role;
    }

    public void slaveOf(String masterHost, int port) {

    }

}
