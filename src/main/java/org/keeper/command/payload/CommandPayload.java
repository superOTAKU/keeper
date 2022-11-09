package org.keeper.command.payload;

/**
 * 命令数据对象
 */
public interface CommandPayload {

    byte[] encode();

    /**
     * 从二进制解码，先创建对象，然后调用此方法填充属性
     */
    void decode(byte[] payload);
}
