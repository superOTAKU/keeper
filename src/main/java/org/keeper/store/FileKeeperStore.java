package org.keeper.store;

import cn.hutool.core.io.IoUtil;
import lombok.extern.slf4j.Slf4j;
import org.keeper.cache.DefaultKeeperCache;
import org.keeper.cache.KeeperCache;
import org.keeper.object.KeeperObject;
import org.keeper.object.KeeperObjectType;
import org.keeper.object.payload.IntPayLoad;
import org.keeper.object.payload.KeeperObjectPayload;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

/**
 * 协议格式：
 *  magic keeper
 *  1 byte indicate value type
 *  4 bytes key length
 *  key string utf-8
 *  value content based on payload type
 *  one byte FF to indicates EOF
 */
@Slf4j
public class FileKeeperStore implements KeeperStore {
    private static final String MAGIC = "keeper";
    private static final int EOF = 0xFF;

    private final String path;

    public FileKeeperStore(String path) throws IOException {
        this.path = path;
        //try to create this file
        try (var ignored = new FileOutputStream(path, true)) {
            //ignored
        }
    }

    @Override
    public void save(KeeperCache cache) throws Exception {
        DataOutputStream out = null;
        try {
            out = new DataOutputStream(new BufferedOutputStream(new FileOutputStream(path)));
            //a magic
            out.write(MAGIC.getBytes(StandardCharsets.UTF_8));
            for (var entry : cache.getAll().entrySet()) {
                String key = entry.getKey();
                KeeperObject value = entry.getValue();
                out.write(value.getType().ordinal());
                out.writeInt(key.length());
                out.write(key.getBytes(StandardCharsets.UTF_8));
                switch (value.getType()) {
                    case INT:
                        IntPayLoad payLoad = (IntPayLoad)value.getPayload();
                        out.writeInt(payLoad.getValue());
                        break;
                    default:
                        throw new IllegalStateException("not supported yet");
                }
            }
            out.write(EOF);
        } finally {
            IoUtil.close(out);
        }
    }

    @Override
    public KeeperCache load() throws Exception {
        KeeperCache cache = new DefaultKeeperCache();
        DataInputStream in = null;
        try {
            in = new DataInputStream(new BufferedInputStream(new FileInputStream(path)));
            String magic = new String(in.readNBytes(6), StandardCharsets.UTF_8);
            if (magic.isEmpty()) {
                //空文件
                return cache;
            }
            if (!Objects.equals(MAGIC, magic)) {
                throw new RuntimeException("illegal magic: " + magic);
            }
            int typeOrFlag;
            while ((typeOrFlag = in.read()) >= 0) {
                //meets end flag
                if (typeOrFlag == EOF) {
                    break;
                }
                //dedicate type
                KeeperObjectType type = KeeperObjectType.values()[typeOrFlag];
                //read key
                int keyLen = in.readInt();
                String key = new String(in.readNBytes(keyLen), StandardCharsets.UTF_8);
                KeeperObjectPayload payload;
                switch (type) {
                    case INT:
                        payload = new IntPayLoad(in.readInt());
                        break;
                    default:
                        throw new IllegalStateException("not supported yet");
                }
                cache.set(key, new KeeperObject(type, payload));
            }
            return cache;
        } finally {
            IoUtil.close(in);
        }
    }

}
