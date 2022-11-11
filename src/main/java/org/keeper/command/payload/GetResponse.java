package org.keeper.command.payload;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import lombok.Getter;
import lombok.Setter;
import org.keeper.object.KeeperObject;
import org.keeper.object.KeeperObjectType;
import org.keeper.object.payload.IntPayLoad;
import org.keeper.object.payload.KeeperObjectPayload;

@Getter
@Setter
public class GetResponse implements CommandPayload {
    private KeeperObject object;

    @Override
    public byte[] encode() {
        ByteBuf buf = Unpooled.buffer();
        buf.writeByte(object.getType().ordinal());
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

    @Override
    public String toString() {
        return "GetResponse{" +
                "object=" + object +
                '}';
    }
}
