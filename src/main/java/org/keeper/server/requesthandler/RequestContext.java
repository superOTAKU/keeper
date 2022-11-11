package org.keeper.server.requesthandler;

import io.netty.channel.ChannelHandlerContext;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.keeper.command.CommandType;
import org.keeper.command.KeeperCommand;
import org.keeper.command.OperationCode;
import org.keeper.command.ResponseStatus;
import org.keeper.command.payload.CommandPayload;
import org.keeper.server.KeeperServerController;

@Slf4j
@Setter
@Getter
public class RequestContext {
    private Integer requestId;
    private OperationCode opCode;
    private ChannelHandlerContext ctx;
    private KeeperServerController controller;

    public void replyError(CommandPayload payload) {
        reply(ResponseStatus.ERROR, payload);
    }

    public void replyError(byte[] payload) {
        reply(ResponseStatus.ERROR, payload);
    }

    public void replySuccess(CommandPayload payload) {
        reply(ResponseStatus.SUCCESS, payload);
    }

    public void replySuccess() {
        replySuccess(new byte[0]);
    }

    public void replySuccess(byte[] payload) {
        reply(ResponseStatus.SUCCESS, payload);
    }

    public void reply(ResponseStatus status, CommandPayload payload) {
        reply(status, payload.encode());
    }

    public void reply(ResponseStatus status, byte[] payload) {
        KeeperCommand responseCommand = new KeeperCommand();
        responseCommand.setId(requestId);
        responseCommand.setType(CommandType.RESPONSE);
        responseCommand.setOpCode(opCode);
        responseCommand.setStatus(status);
        responseCommand.setPayload(payload);
        ctx.writeAndFlush(responseCommand);
    }

    public KeeperServerController getController() {
        return controller;
    }

}
