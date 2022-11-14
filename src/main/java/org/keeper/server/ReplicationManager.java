package org.keeper.server;

/**
 * 集群管理，例如master - slave通信
 */
public class ReplicationManager {
    private final KeeperServerController controller;

    public ReplicationManager(KeeperServerController controller) {
        this.controller = controller;
    }

}
