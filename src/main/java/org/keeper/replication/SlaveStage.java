package org.keeper.replication;

public enum SlaveStage {
    //等待全量复制
    PENDING_FULL_COPY,
    //增量复制中
    DELTA_COPYING
}
