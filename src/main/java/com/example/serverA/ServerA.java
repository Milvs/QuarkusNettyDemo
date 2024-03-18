package com.example.serverA;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.concurrent.ExecutorService;


/**
 * BOOTSTRAP SERVER A
 **/
@ApplicationScoped
public class ServerA {
    static final int PORT = Integer.parseInt(System.getProperty("port", "8081"));
    @Inject
    ExecutorService executorService;

    @PostConstruct
    public void start() {
        executorService.execute(() -> { // we use ES because otherwise the main thread will be blocked on line 50
            EventLoopGroup bossGroup = new NioEventLoopGroup(1);
            EventLoopGroup workerGroup = new NioEventLoopGroup();
            try {
                ServerBootstrap b = new ServerBootstrap();
                b.group(bossGroup, workerGroup) // Set boss & worker groups
                        .channel(NioServerSocketChannel.class)
                        .childHandler(new ChannelInitializer<SocketChannel>() {
                            @Override
                            public void initChannel(SocketChannel ch) throws Exception {
                                ChannelPipeline p = ch.pipeline();
                                p.addLast(new StringDecoder());
                                p.addLast(new StringEncoder());
                                p.addLast(new ServerAHandler());
                            }
                        });
                ChannelFuture f = b.bind(PORT).sync();
                System.out.println("Netty Server started.");
                f.channel().closeFuture().sync();
            } catch (InterruptedException e){
                System.out.println("Server A interrupted!!!");
            }
            finally {
                bossGroup.shutdownGracefully();
                workerGroup.shutdownGracefully();
            }
        });

    }
}
