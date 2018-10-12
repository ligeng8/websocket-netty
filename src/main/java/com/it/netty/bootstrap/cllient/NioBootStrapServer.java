package com.it.netty.bootstrap.cllient;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;

public class NioBootStrapServer {
	public static void main(String[] args) {
		NioEventLoopGroup nioEventLoopGroup = new NioEventLoopGroup();
		ServerBootstrap serverBootstrap = new ServerBootstrap();
		serverBootstrap.group(nioEventLoopGroup)
		               .channel(NioServerSocketChannel.class)
		               .childHandler(new  SimpleChannelInboundHandler<ByteBuf>() {

						@Override
						protected void messageReceived(ChannelHandlerContext ctx, ByteBuf msg) throws Exception {
							// TODO Auto-generated method stub
							System.out.println("message received");
							msg.clear();
						}
					});
		ChannelFuture channelFuture = serverBootstrap.bind(8080);
		channelFuture.addListener(new  ChannelFutureListener() {
			
			public void operationComplete(ChannelFuture future) throws Exception {
				// TODO Auto-generated method stub
				if(future.isSuccess()) {
					System.out.println("server bound");
				}else {
					System.out.println("server bound attempt failed ");
					future.cause().printStackTrace();
				}
			}
		});
	}
}
