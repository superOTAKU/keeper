package org.keeper.client;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import lombok.extern.slf4j.Slf4j;
import org.keeper.command.CommandType;
import org.keeper.command.KeeperCommand;
import org.keeper.command.OperationCode;
import org.keeper.command.payload.CommandPayload;
import org.keeper.net.netty.KeeperCommandCodec;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.TimeUnit;

@Slf4j
public class KeeperClient {
    private final KeeperClientConfig config;
    private final NioEventLoopGroup group = new NioEventLoopGroup(1);
    private final ConcurrentMap<Integer, CompletableFuture<KeeperCommand>> responseFutures = new ConcurrentHashMap<>();
    private volatile Channel channel;

    public KeeperClient(KeeperClientConfig config) {
        this.config = config;
    }

    public synchronized void connect() {
        if (channel != null && channel.isActive()) {
            channel.close();
        }
        Bootstrap bootstrap = new Bootstrap();
        ChannelFuture future = bootstrap.group(group)
                .channel(NioSocketChannel.class)
                .option(ChannelOption.SO_KEEPALIVE, true)
                .option(ChannelOption.TCP_NODELAY, true)
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 30000) //客户端连接服务器超时时间
                .handler(new ChannelInitializer<NioSocketChannel>() {
                    @Override
                    protected void initChannel(NioSocketChannel ch) throws Exception {
                        ChannelPipeline p = ch.pipeline();
                        p.addLast(new LengthFieldBasedFrameDecoder(65535, 0, 4));
                        p.addLast(new KeeperCommandCodec(false));
                        p.addLast(new SimpleChannelInboundHandler<KeeperCommand>() {
                            @Override
                            protected void channelRead0(ChannelHandlerContext ctx, KeeperCommand responseCommand) throws Exception {
                                CompletableFuture<KeeperCommand> future = responseFutures.remove(responseCommand.getId());
                                if (future == null) {
                                    log.warn("future not found for response id: {}", responseCommand.getId());
                                    return;
                                }
                                future.complete(responseCommand);
                            }

                            @Override
                            public void channelInactive(ChannelHandlerContext ctx) {
                                log.warn("connection inactive, try reconnect...");
                                connect();
                            }
                        });
                    }
                }).connect(config.getHost(), config.getPort()).syncUninterruptibly();
        if (!future.isSuccess()) {
            throw new RuntimeException("connect server fail!");
        }
        channel = future.channel();
    }

    public KeeperCommand sendRequest(OperationCode code, CommandPayload payload) {
        KeeperCommand request = new KeeperCommand();
        request.setType(CommandType.REQUEST);
        request.setOpCode(code);
        request.setPayload(payload.encode());
        request.allocateId();
        var responseFuture = new CompletableFuture<KeeperCommand>();
        channel.writeAndFlush(request).addListener(f -> {
           if (!f.isSuccess()) {
               CompletableFuture<KeeperCommand> rf = responseFutures.remove(request.getId());
               if (rf != null) {
                   rf.completeExceptionally(f.cause());
               }
           }
        });
        try {
            return responseFuture.get(30000L, TimeUnit.MILLISECONDS);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
