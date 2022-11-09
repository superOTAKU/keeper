package org.keeper.util;

import cn.hutool.core.util.ClassUtil;
import cn.hutool.core.util.ReflectUtil;
import org.keeper.command.KeeperCommand;
import org.keeper.command.payload.CommandPayload;

public class PayloadUtil {

    public static <T extends CommandPayload> T decode(KeeperCommand command, Class<T> type) {
        T instance = ReflectUtil.newInstance(type);
        instance.decode(command.getPayload());
        return instance;
    }

}
