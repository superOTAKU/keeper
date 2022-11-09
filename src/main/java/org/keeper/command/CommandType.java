package org.keeper.command;

import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public enum CommandType {
    REQUEST(1),
    RESPONSE(2),
    ;

    private final int type;

    CommandType(int type) {
        this.type = type;
    }

    public int getType() {
        return type;
    }

    private static final Map<Integer, CommandType> map = Arrays.stream(CommandType.values())
            .collect(Collectors.toMap(CommandType::getType, Function.identity()));

    public static CommandType of(int type) {
        return map.get(type);
    }
}
