package org.keeper.server.requesthandler;

import org.keeper.command.OperationCode;
import org.keeper.server.requesthandler.impl.GetRequestHandler;
import org.keeper.server.requesthandler.impl.SetRequestHandler;
import org.keeper.server.requesthandler.impl.SlaveOfRequestHandler;

import java.util.HashMap;
import java.util.Map;

public class RequestHandlers {

    public static final Map<OperationCode, RequestHandler> HANDLER_MAP = new HashMap<>();

    static {
        HANDLER_MAP.put(OperationCode.SET, new SetRequestHandler());
        HANDLER_MAP.put(OperationCode.GET, new GetRequestHandler());

        HANDLER_MAP.put(OperationCode.SLAVE_OF, new SlaveOfRequestHandler());
    }

}
