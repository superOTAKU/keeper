package org.keeper.object;

import org.keeper.object.payload.KeeperObjectPayload;

/**
 * 一个对象
 */
public class KeeperObject {
    //对象类型
    private KeeperObjectType type;
    //数据对象
    private KeeperObjectPayload payload;
    //TODO 记录更多信息，包括过期时间，

    public KeeperObject() {}

    public KeeperObject(KeeperObjectType type, KeeperObjectPayload payload) {
        this.type = type;
        this.payload = payload;
    }

    public KeeperObjectType getType() {
        return type;
    }

    public void setType(KeeperObjectType type) {
        this.type = type;
    }

    public KeeperObjectPayload getPayload() {
        return payload;
    }

    public void setPayload(KeeperObjectPayload payload) {
        this.payload = payload;
    }

}
