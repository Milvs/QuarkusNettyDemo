package com.example.client;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.quarkus.runtime.Startup;
import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.context.Dependent;
import jakarta.inject.Inject;

import java.util.concurrent.ExecutorService;


@ApplicationScoped
public class Client {

    static final String HOST = "127.0.0.1";
    static final int PORT = 8081;

    @Inject
    ExecutorService executorService;


    public void start() {
        executorService.execute(() -> {

            EventLoopGroup group = new NioEventLoopGroup();
            try {
                Bootstrap b = new Bootstrap();
                b.group(group)
                        .channel(NioSocketChannel.class)
                        .handler(new ChannelInitializer<SocketChannel>() {
                            @Override
                            public void initChannel(SocketChannel ch) throws Exception {
                                ChannelPipeline p = ch.pipeline();
                                p.addLast(new StringDecoder());
                                p.addLast(new StringEncoder());
                                p.addLast(new ClientHandler());
                            }
                        });

                // Start the client.
                ChannelFuture f = b.connect(HOST, PORT).sync();
                System.out.println("Client connected to server successfully!");
                String input = "Mila";
                Channel channel = f.sync().channel();
                channel.writeAndFlush(input);
                channel.flush();
                f.channel().closeFuture().sync();
            } catch (InterruptedException e) {
                System.out.println("Server A interrupted!!!");
            } finally {
                group.shutdownGracefully();
            }
        });
    }
}

