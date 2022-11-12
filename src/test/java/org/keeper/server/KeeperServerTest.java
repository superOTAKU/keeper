package org.keeper.server;

import cn.hutool.core.thread.ThreadUtil;
import org.junit.Test;
import org.keeper.client.KeeperClient;
import org.keeper.client.KeeperClientConfig;
import org.keeper.command.KeeperCommand;
import org.keeper.command.OperationCode;
import org.keeper.command.payload.GetRequest;
import org.keeper.command.payload.GetResponse;
import org.keeper.command.payload.SetRequest;
import org.keeper.object.KeeperObject;
import org.keeper.object.KeeperObjectType;
import org.keeper.object.payload.IntPayLoad;
import org.keeper.util.PayloadUtil;

public class KeeperServerTest {

    @Test
    public void doTest() throws Exception {
        KeeperServerConfig serverConfig = new KeeperServerConfig();
        serverConfig.setBindAddress("0.0.0.0");
        serverConfig.setBindPort(30001);
        serverConfig.setDbPath("./target/keeper.store");
        KeeperServerController controller = new KeeperServerController(serverConfig);
        controller.start();
        KeeperClientConfig clientConfig = new KeeperClientConfig();
        clientConfig.setHost("localhost");
        clientConfig.setPort(30001);
        KeeperClient client = new KeeperClient(clientConfig);
        client.connect();
        SetRequest setRequest = new SetRequest();
        setRequest.setKey("key");
        setRequest.setObject(new KeeperObject(KeeperObjectType.INT, new IntPayLoad(100)));
        KeeperCommand response = client.sendRequest(OperationCode.SET, setRequest);
        System.out.println("response: " + response);
        GetRequest getRequest = new GetRequest();
        getRequest.setKey("key");
        KeeperCommand getResponseCommand = client.sendRequest(OperationCode.GET, getRequest);
        GetResponse getResponse = PayloadUtil.decode(getResponseCommand, GetResponse.class);
        System.out.println("get response: " + getResponse);

        ThreadUtil.sleep(60000);

    }

}
