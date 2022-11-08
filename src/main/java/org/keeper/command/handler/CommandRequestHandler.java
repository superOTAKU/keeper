package org.keeper.command.handler;

import org.keeper.command.KeeperCommand;

public interface CommandRequestHandler {

    void handle(CommandRequestContext context, KeeperCommand command);

}
