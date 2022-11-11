package org.keeper.command.payload;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import lombok.Getter;
import lombok.Setter;

import java.nio.charset.StandardCharsets;

@Getter
@Setter
public class GetRequest implements CommandPayload {
    private String key;

    @Override
    public byte[] encode() {
        ByteBuf buf = Unpooled.buffer();
        byte[] keyBytes = key.getBytes(StandardCharsets.UTF_8);
        buf.writeInt(keyBytes.length);
        buf.writeBytes(keyBytes);
        byte[] dst = new byte[buf.readableBytes()];
        buf.readBytes(dst);
        return dst;
    }

    @Override
    public void decode(byte[] payload) {
        ByteBuf buf = Unpooled.wrappedBuffer(payload);
        int len = buf.readInt();
        key = buf.readCharSequence(len, StandardCharsets.UTF_8).toString();
    }
}
