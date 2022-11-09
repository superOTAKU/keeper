package org.keeper.server.requesthandler;

import org.keeper.command.KeeperCommand;

public interface RequestHandler {

    void handle(RequestContext context, KeeperCommand requestCommand);

}
