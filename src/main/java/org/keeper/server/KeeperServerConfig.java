package org.keeper.server;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class KeeperServerConfig {
    private String bindAddress;
    private int bindPort;
    private String dbPath;
}
