package com.it.netty.timetask;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;

public class TimeServer {

	
	
	public static void main(String[] args) {
		int port = 9999;
		NioEventLoopGroup bossGroup = new NioEventLoopGroup();
		NioEventLoopGroup workGroup = new NioEventLoopGroup();
		try {
			ServerBootstrap bootstrap = new ServerBootstrap();
			bootstrap.group(bossGroup, workGroup)
			         .channel(NioServerSocketChannel.class)
			         .childHandler(new InitChildChannelhandler());
			//绑定端口，同步等待成功
			ChannelFuture channelFuture = bootstrap.bind(port).sync();
			
			Thread.sleep(15000);
			
			TimeServerHandler.pulishAll();//群发订阅消息
			//等待服务监听端口关闭
			channelFuture.channel().closeFuture().sync();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally {
			//优雅的突出释放线程组资源
			bossGroup.shutdownGracefully();
			workGroup.shutdownGracefully();
		}
		
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
