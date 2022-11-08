package org.keeper.store;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.keeper.cache.DefaultKeeperCache;
import org.keeper.cache.KeeperCache;
import org.keeper.object.KeeperObject;
import org.keeper.object.KeeperObjectType;
import org.keeper.object.payload.IntPayLoad;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class FileKeeperStoreTest {

    @Test
    public void aSaveTest() throws Exception {
        KeeperCache cache = new DefaultKeeperCache();
        cache.set("key", new KeeperObject(KeeperObjectType.INT, new IntPayLoad(99)));
        FileKeeperStore keeperStore = new FileKeeperStore("./target/keeper.store");
        keeperStore.save(cache);
    }

    @Test
    public void bLoadTest() throws Exception {
        FileKeeperStore keeperStore = new FileKeeperStore("./target/keeper.store");
        KeeperCache cache = keeperStore.load();
        System.out.println(cache.get("key"));
    }

}
