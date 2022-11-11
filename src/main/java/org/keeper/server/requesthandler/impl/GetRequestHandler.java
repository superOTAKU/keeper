package org.keeper.server.requesthandler.impl;

import org.keeper.command.KeeperCommand;
import org.keeper.command.payload.GetRequest;
import org.keeper.command.payload.GetResponse;
import org.keeper.object.KeeperObject;
import org.keeper.server.requesthandler.RequestContext;
import org.keeper.server.requesthandler.RequestHandler;
import org.keeper.util.PayloadUtil;

public class GetRequestHandler implements RequestHandler {

    @Override
    public void handle(RequestContext context, KeeperCommand requestCommand) {
        GetRequest request = PayloadUtil.decode(requestCommand, GetRequest.class);
        KeeperObject object = context.getController().getCache().get(request.getKey());
        GetResponse response = new GetResponse();
        response.setObject(object);
        context.replySuccess(response);
    }

}
