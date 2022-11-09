package org.keeper.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import lombok.extern.slf4j.Slf4j;
import org.keeper.command.KeeperCommand;
import org.keeper.net.netty.KeeperCommandCodec;
import org.keeper.server.requesthandler.RequestContext;
import org.keeper.server.requesthandler.RequestHandler;
import org.keeper.server.requesthandler.RequestHandlers;
import org.keeper.service.IService;

@Slf4j
public class KeeperServer implements IService {
    private final KeeperServerController controller;
    private NioEventLoopGroup boss;
    private NioEventLoopGroup worker;
    private Channel channel;


    public KeeperServer(KeeperServerController controller) {
        this.controller = controller;
    }

    @Override
    public void start() {
        boss = new NioEventLoopGroup(1);
        worker = new NioEventLoopGroup(1);
        ServerBootstrap bootstrap = new ServerBootstrap();
        bootstrap.group(boss, worker)
                .channel(NioServerSocketChannel.class)
                .option(ChannelOption.SO_REUSEADDR, true)
                .option(ChannelOption.SO_BACKLOG, 1024)
                .childOption(ChannelOption.TCP_NODELAY, true)
                .childOption(ChannelOption.SO_KEEPALIVE, true)
                .childHandler(new ChannelInitializer<NioSocketChannel>() {
                    @Override
                    protected void initChannel(NioSocketChannel ch) throws Exception {
                        ChannelPipeline p = ch.pipeline();
                        p.addLast(new LengthFieldBasedFrameDecoder(65535, 0, 4));
                        p.addLast(new KeeperCommandCodec(true));
                        p.addLast(new SimpleChannelInboundHandler<KeeperCommand>() {
                            @Override
                            protected void channelRead0(ChannelHandlerContext ctx, KeeperCommand requestCommand) throws Exception {
                                RequestHandler requestHandler = RequestHandlers.HANDLER_MAP.get(requestCommand.getOpCode());
                                if (requestHandler == null) {
                                    log.error("client {} send invalid opCode: {}", ctx.channel().remoteAddress(), requestCommand.getOpCode());
                                    ctx.close();
                                    return;
                                }
                                RequestContext requestContext = new RequestContext();
                                requestContext.setOpCode(requestCommand.getOpCode());
                                requestContext.setCtx(ctx);
                                try {
                                    requestHandler.handle(requestContext, requestCommand);
                                } catch (Exception e) {
                                    log.error("client {} handle opCode {} error", ctx.channel().remoteAddress(), requestCommand.getOpCode(), e);
                                }
                            }
                        });
                    }
                });
    }

    @Override
    public void stop() {

    }

}
