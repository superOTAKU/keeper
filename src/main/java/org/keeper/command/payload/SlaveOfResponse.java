package org.keeper.command.payload;

import lombok.Getter;
import lombok.Setter;
import org.keeper.replication.SlaveOfErrorCode;

@Getter
@Setter
public class SlaveOfResponse implements CommandPayload {
    private SlaveOfErrorCode code;

    @Override
    public byte[] encode() {
        return new byte[0];
    }

    @Override
    public void decode(byte[] payload) {

    }

}
