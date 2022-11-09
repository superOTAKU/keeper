package org.keeper.net.netty;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageCodec;
import org.keeper.command.CommandType;
import org.keeper.command.KeeperCommand;
import org.keeper.command.OperationCode;
import org.keeper.command.ResponseStatus;

import java.util.List;
import java.util.Optional;

public class KeeperCommandCodec extends ByteToMessageCodec<KeeperCommand> {
    private static final byte[] EMPTY_BYTES = new byte[0];

    private final boolean serverSide;

    public KeeperCommandCodec(boolean serverSide) {
        this.serverSide = serverSide;
    }

    @Override
    protected void encode(ChannelHandlerContext channelHandlerContext, KeeperCommand command, ByteBuf byteBuf) throws Exception {
        if (serverSide && command.getType() != CommandType.RESPONSE || !serverSide && command.getType() != CommandType.REQUEST) {
            throw new IllegalStateException();
        }
        byte[] payload = Optional.ofNullable(command.getPayload()).orElse(EMPTY_BYTES);
        byteBuf.writeInt((command.getType() == CommandType.REQUEST ? 4 : 5) + payload.length);
        byteBuf.writeByte(command.getType().getType());
        byteBuf.writeInt(command.getOpCode().getCode());
        if (command.getType() == CommandType.RESPONSE) {
            byteBuf.writeByte(command.getStatus().getStatus());
        }
        byteBuf.writeBytes(payload);
    }

    @Override
    protected void decode(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf, List<Object> list) throws Exception {
        KeeperCommand command = new KeeperCommand();
        byteBuf.readInt();
        command.setType(CommandType.of(byteBuf.readByte()));
        command.setOpCode(OperationCode.of(byteBuf.readInt()));
        if (command.getType() == CommandType.RESPONSE) {
            command.setStatus(ResponseStatus.of(byteBuf.readByte()));
        }
        byte[] payload = new byte[byteBuf.readableBytes()];
        byteBuf.readBytes(payload);
        command.setPayload(payload);
        if (serverSide && command.getType() != CommandType.REQUEST || !serverSide && command.getType() != CommandType.RESPONSE) {
            throw new IllegalStateException();
        }
        list.add(command);
    }

}
