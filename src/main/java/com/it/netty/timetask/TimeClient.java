package com.it.netty.timetask;

import java.net.InetSocketAddress;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

public class TimeClient {

	
	public static void main(String[] args) {
		NioEventLoopGroup group = new NioEventLoopGroup();
		try {
			String address = "127.0.0.1";
			int port = 9999;
	
			Bootstrap bootstrap = new Bootstrap();
			bootstrap.group(group)
			         .channel(NioSocketChannel.class)
			         .option(ChannelOption.TCP_NODELAY, true)
			         .handler(new ChannelInitializer<SocketChannel>() {

						@Override
						protected void initChannel(SocketChannel ch) throws Exception {
							// TODO Auto-generated method stub
							ch.pipeline().addLast(null);
						}
					});
			
			ChannelFuture future = bootstrap.connect(new InetSocketAddress(address, port)).sync();
			future.channel().close().sync();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally {
			group.shutdownGracefully();
		}
		
		
		
	}
}
