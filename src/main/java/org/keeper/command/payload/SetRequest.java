package org.keeper.command.payload;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import org.keeper.object.KeeperObject;
import org.keeper.object.KeeperObjectType;
import org.keeper.object.payload.IntPayLoad;
import org.keeper.object.payload.KeeperObjectPayload;

import java.nio.charset.StandardCharsets;

public class SetRequest implements KeeperCommandPayload {
    private String key;
    private KeeperObject object;

    @Override
    public byte[] encode() {
        ByteBuf buf = Unpooled.buffer();
        buf.writeByte(object.getType().ordinal());
        buf.writeInt(key.length());
        buf.writeBytes(key.getBytes(StandardCharsets.UTF_8));
        switch (object.getType()) {
            case INT:
                buf.writeInt(((IntPayLoad)object.getPayload()).getValue());
                break;
            default:
                throw new IllegalStateException("not supported yet");
        }
        byte[] dst = new byte[buf.readableBytes()];
        buf.readBytes(dst);
        return dst;
    }

    @Override
    public void decode(byte[] payload) {
        ByteBuf buf = Unpooled.wrappedBuffer(payload);
        var type = KeeperObjectType.values()[buf.readByte()];
        key = buf.readCharSequence(buf.readInt(), StandardCharsets.UTF_8).toString();
        KeeperObjectPayload objectPayload;
        switch (type) {
            case INT:
                objectPayload = new IntPayLoad(buf.readInt());
                break;
            default:
                throw new IllegalStateException("not supported yet");
        }
        object = new KeeperObject(type, objectPayload);
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getKey() {
        return key;
    }

    public void setObject(KeeperObject object) {
        this.object = object;
    }

    public KeeperObject getObject() {
        return object;
    }

}
