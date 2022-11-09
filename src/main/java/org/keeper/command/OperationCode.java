package org.keeper.command;

import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public enum OperationCode {
    SET(1),
    GET(2),
    ;

    private final int code;

    OperationCode(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }

    private static final Map<Integer, OperationCode> map = Arrays.stream(OperationCode.values())
            .collect(Collectors.toMap(OperationCode::getCode, Function.identity()));

    public static OperationCode of(int code) {
        return map.get(code);
    }
}
