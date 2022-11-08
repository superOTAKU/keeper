package org.keeper.cache;

import org.keeper.object.KeeperObject;

import java.util.LinkedHashMap;
import java.util.Map;

public class DefaultKeeperCache implements KeeperCache {
    private final LinkedHashMap<String, KeeperObject> cache = new LinkedHashMap<>();

    @Override
    public void set(String key, KeeperObject object) {
        cache.put(key, object);
    }

    @Override
    public KeeperObject get(String key) {
        return cache.get(key);
    }

    @Override
    public Map<String, KeeperObject> getAll() {
        return cache;
    }
}
