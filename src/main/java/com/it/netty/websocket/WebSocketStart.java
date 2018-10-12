package com.it.netty.websocket;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.EventLoop;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;

/**
 * ��������
 * 
 * @author ligeng3
 *
 */
public class WebSocketStart {
	public static void main(String[] args) {
		EventLoopGroup bossGroup = new NioEventLoopGroup();
		EventLoopGroup workGroup = new NioEventLoopGroup();

		try {
              ServerBootstrap b  = new ServerBootstrap();
              b.group(bossGroup, workGroup);
              b.channel(NioServerSocketChannel.class);
              b.childHandler(new WebSocketChannelHandler());
              System.out.println("����˿����ȴ��ͻ�������");
              Channel channel = b.bind(8888).sync().channel();
              channel.closeFuture().sync();
		} catch (Exception e) {
			// TODO: handle exception
		} finally {
			bossGroup.shutdownGracefully();
			workGroup.shutdownGracefully();
		}
	}
}
