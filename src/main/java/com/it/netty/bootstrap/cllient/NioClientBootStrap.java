package com.it.netty.bootstrap.cllient;

import java.net.InetSocketAddress;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.util.CharsetUtil;

public class NioClientBootStrap {
	public static void main(String[] args) {

		NioEventLoopGroup loopGroup = new NioEventLoopGroup();
		Bootstrap bootstrap = new Bootstrap();

		bootstrap.group(loopGroup).channel(NioSocketChannel.class).handler(new SimpleChannelInboundHandler<ByteBuf>() {

			@Override
			protected void messageReceived(ChannelHandlerContext ctx, ByteBuf msg) throws Exception {
				// TODO Auto-generated method stub
				System.out.println(ctx);
				System.out.println(msg.toString(CharsetUtil.UTF_8));

			}

			@Override
			public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
				// TODO Auto-generated method stub
				super.channelRead(ctx, msg);
				System.out.println(msg);
			}
		});
		ChannelFuture future = bootstrap.connect(new InetSocketAddress("www.baidu.com", 80));
		future.addListener(new ChannelFutureListener() {

			public void operationComplete(ChannelFuture future) throws Exception {
				// TODO Auto-generated method stub
				if (future.isSuccess()) {
					System.out.println("connection success");
				} else {
					System.out.println("connection false");
					future.cause().printStackTrace();
				}
			}
		});

	}
}
