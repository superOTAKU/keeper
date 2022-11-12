package org.keeper.cache;

import org.keeper.object.KeeperObject;

import java.util.Map;

public interface KeeperCache {

    void set(String key, KeeperObject object);

    KeeperObject get(String key);

    Map<String, KeeperObject> getAll();

    KeeperCache clone();

}
