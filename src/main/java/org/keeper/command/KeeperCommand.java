package org.keeper.command;

public class KeeperCommand {
    private CommandType type;
    private OperationCode opCode;
    private byte[] payload;
    //only has value if it is a response
    private ResponseStatus status;

    public CommandType getType() {
        return type;
    }

    public void setType(CommandType type) {
        this.type = type;
    }

    public OperationCode getOpCode() {
        return opCode;
    }

    public void setOpCode(OperationCode opCode) {
        this.opCode = opCode;
    }

    public byte[] getPayload() {
        return payload;
    }

    public void setPayload(byte[] payload) {
        this.payload = payload;
    }

    public ResponseStatus getStatus() {
        return status;
    }

    public void setStatus(ResponseStatus status) {
        this.status = status;
    }

}
