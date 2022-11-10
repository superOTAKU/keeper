package org.keeper.server;

import cn.hutool.core.thread.ThreadUtil;
import org.junit.Test;

public class KeeperServerTest {

    @Test
    public void doTest() throws Exception {
        KeeperServerConfig serverConfig = new KeeperServerConfig();
        serverConfig.setBindAddress("0.0.0.0");
        serverConfig.setBindPort(30001);
        serverConfig.setDbPath("./target/keeper.store");
        KeeperServerController controller = new KeeperServerController(serverConfig);
        controller.start();
        ThreadUtil.sleep(10000);
    }

}
