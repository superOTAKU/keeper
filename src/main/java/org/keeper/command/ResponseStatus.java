package org.keeper.command;

import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

//we just indicates SUCCESS or error, details depends on payload
public enum ResponseStatus {
    SUCCESS(1),
    ERROR(0);

    private final int status;

    ResponseStatus(int status) {
        this.status = status;
    }

    public int getStatus() {
        return status;
    }

    private static final Map<Integer, ResponseStatus> map = Arrays.stream(ResponseStatus.values())
            .collect(Collectors.toMap(ResponseStatus::getStatus, Function.identity()));

    public static ResponseStatus of(int status) {
        return map.get(status);
    }

}
