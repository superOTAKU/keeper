package org.keeper.server.requesthandler.impl;

import org.keeper.command.KeeperCommand;
import org.keeper.command.payload.SlaveOfRequest;
import org.keeper.command.payload.SlaveOfResponse;
import org.keeper.replication.SlaveOfErrorCode;
import org.keeper.server.replication.ReplicationManager;
import org.keeper.server.replication.ReplicationRole;
import org.keeper.server.requesthandler.RequestContext;
import org.keeper.server.requesthandler.RequestHandler;
import org.keeper.util.PayloadUtil;

public class SlaveOfRequestHandler implements RequestHandler {

    @Override
    public void handle(RequestContext context, KeeperCommand requestCommand) {
        ReplicationManager replicationManager = context.getController().getReplicationManager();
        ReplicationRole role = replicationManager.getRole();
        SlaveOfResponse response = new SlaveOfResponse();
        //前置状态检查
        if (role == ReplicationRole.SLAVE) {
            response.setCode(SlaveOfErrorCode.ALREADY_SLAVE);
            context.replyError(response);
            return;
        } else if (role == ReplicationRole.MASTER) {
            response.setCode(SlaveOfErrorCode.IS_MASTER);
            context.replyError(response);
            return;
        }
        SlaveOfRequest request = PayloadUtil.decode(requestCommand, SlaveOfRequest.class);
        //通过复制管理器，成为该节点的slave
        replicationManager.slaveOf(request.getMasterHost(), request.getPort());
        response.setCode(SlaveOfErrorCode.SUCCESS);
        context.replySuccess(response);
    }

}
