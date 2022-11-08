package org.keeper.command;

public class KeeperCommand {
    private KeeperCommandType type;
    private KeeperCommandCode code;
    private byte[] body;

    public KeeperCommandType getType() {
        return type;
    }

    public void setType(KeeperCommandType type) {
        this.type = type;
    }

    public KeeperCommandCode getCode() {
        return code;
    }

    public void setCode(KeeperCommandCode code) {
        this.code = code;
    }

    public byte[] getBody() {
        return body;
    }

    public void setBody(byte[] body) {
        this.body = body;
    }
}
