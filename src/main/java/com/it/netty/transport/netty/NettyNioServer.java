package com.it.netty.transport.netty;

import java.net.InetSocketAddress;
import java.nio.charset.Charset;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

public class NettyNioServer {
	public static void main(String[] args) throws InterruptedException {
		int port = 0;
		final ByteBuf buf = Unpooled
				.unreleasableBuffer(Unpooled.copiedBuffer("hello ! \r\n", Charset.forName("UTF-8")));
		NioEventLoopGroup bossGroup = new NioEventLoopGroup(1);
		NioEventLoopGroup workerGroup = new NioEventLoopGroup();

		try {
			ServerBootstrap bootstrap = new ServerBootstrap();
			bootstrap.group(bossGroup, workerGroup).channel(NioServerSocketChannel.class)
					.localAddress(new InetSocketAddress(port)).childHandler(new ChannelInitializer<SocketChannel>() {

						@Override
						protected void initChannel(SocketChannel ch) throws Exception {
							// TODO Auto-generated method stub
							ch.pipeline().addLast(new ChannelHandlerAdapter() {
								@Override
								public void channelActive(ChannelHandlerContext ctx) throws Exception {
									// TODO Auto-generated method stub
									ctx.writeAndFlush(buf.duplicate()).addListener(ChannelFutureListener.CLOSE);
								}
							});
						}
					});
			ChannelFuture future = bootstrap.bind().sync();
			future.channel().closeFuture().sync();
		} finally {
			bossGroup.shutdownGracefully().sync();
			workerGroup.shutdownGracefully().sync();
			
		}
	}
}
