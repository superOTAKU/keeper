package org.keeper.command.payload;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import lombok.Getter;
import lombok.Setter;

import java.nio.charset.StandardCharsets;

@Getter
@Setter
public class SlaveOfRequest implements CommandPayload {
    private String masterHost;
    private int port;

    @Override
    public byte[] encode() {
        ByteBuf buf = Unpooled.buffer();
        byte[] hostBytes = masterHost.getBytes(StandardCharsets.UTF_8);
        buf.writeInt(hostBytes.length);
        buf.writeBytes(hostBytes);
        buf.writeInt(port);
        byte[] dst = new byte[buf.readableBytes()];
        buf.readBytes(dst);
        return dst;
    }

    @Override
    public void decode(byte[] payload) {
        ByteBuf buf = Unpooled.wrappedBuffer(payload);
        masterHost = buf.readCharSequence(buf.readInt(), StandardCharsets.UTF_8).toString();
        port = buf.readInt();
    }

}
