package com.example.serverA;

import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.CharsetUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static io.netty.handler.codec.mqtt.MqttMessageBuilders.connect;


public class ServerAHandler extends ChannelInboundHandlerAdapter {

    static final List<Channel> channels = Collections.synchronizedList(new ArrayList<>());

    @Override
    public void channelActive(final ChannelHandlerContext ctx) {
        System.out.println("Client joined - " + ctx);
        channels.add(ctx.channel());
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        System.out.println("Message received: " + msg);
        for (Channel c : channels) {
            c.writeAndFlush("Hello " + msg + '\n');
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        System.out.println("Closing connection for client - " + ctx);
        ctx.close();
    }
}
