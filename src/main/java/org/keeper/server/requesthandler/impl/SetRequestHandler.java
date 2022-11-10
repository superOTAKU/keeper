package org.keeper.server.requesthandler.impl;

import org.keeper.command.KeeperCommand;
import org.keeper.command.payload.SetRequest;
import org.keeper.server.requesthandler.RequestContext;
import org.keeper.server.requesthandler.RequestHandler;
import org.keeper.util.PayloadUtil;

public class SetRequestHandler implements RequestHandler {

    @Override
    public void handle(RequestContext context, KeeperCommand requestCommand) {
        SetRequest request = PayloadUtil.decode(requestCommand, SetRequest.class);
        context.getController().getCache().set(request.getKey(), request.getObject());
        context.replySuccess();
    }

}
