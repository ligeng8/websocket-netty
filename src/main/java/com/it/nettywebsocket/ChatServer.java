package com.it.nettywebsocket;

import java.net.InetSocketAddress;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.util.concurrent.ImmediateEventExecutor;

public class ChatServer {

	private final ChannelGroup  channelGroup = new DefaultChannelGroup(ImmediateEventExecutor.INSTANCE);
	private final EventLoopGroup  group = new NioEventLoopGroup();
	
	private Channel channel;
	
	
	public ChannelFuture start(InetSocketAddress address) {
		ServerBootstrap serverBootstrap = new  ServerBootstrap();
		serverBootstrap.group(group)
		               .channel(NioServerSocketChannel.class)
		               .childHandler(new ChatServerInitializer(channelGroup));
		ChannelFuture channelFuture = serverBootstrap.bind(address);
		channelFuture.syncUninterruptibly();
		this.channel = channelFuture.channel();
		return channelFuture;
	}
	
	public void  destroy() {
		if(this.channel != null) {
			this.channel.close();
		}
		this.channelGroup.close();
		this.group.shutdownGracefully();
	}
	
	
	public static void main(String[] args) {
		int port = 8888;
		final ChatServer server = new ChatServer();
		ChannelFuture channelFuture = server.start(new InetSocketAddress(port));
		
		Runtime.getRuntime().addShutdownHook(new Thread() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				server.destroy();
			}
		});
		channelFuture.channel().closeFuture().syncUninterruptibly();
	}
}
