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
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.oio.OioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;

public class NettyOioServer {
	public static void main(String[] args) throws InterruptedException {
		int port = 8682;
		final ByteBuf buf = Unpooled.unreleasableBuffer(Unpooled.copiedBuffer("Hi!\r\n", Charset.forName("UTF-8")));
		OioEventLoopGroup group = new OioEventLoopGroup();// 使用 OioEventLoopGroup 允许阻塞模式
		ServerBootstrap b = new ServerBootstrap(); // 1.创建一个 ServerBootstrap
		b.group(group).localAddress(new InetSocketAddress(port)).childHandler(new ChannelInitializer<SocketChannel>() {// 指定
																														// ChannelInitializer
																														// 将给每个接受的连接调用
			@Override
			protected void initChannel(SocketChannel ch) throws Exception {
				// TODO Auto-generated method stub
				ch.pipeline().addLast(new ChannelHandlerAdapter() {// 添加的 ChannelHandler 拦截事件，并允许他们作出反应
					@Override
					public void channelActive(ChannelHandlerContext ctx) throws Exception {
						// TODO Auto-generated method stub
						ctx.writeAndFlush(buf.duplicate()).addListener(ChannelFutureListener.CLOSE);// 写信息到客户端，并添加
																									// ChannelFutureListener
																									// 当一旦消息写入就关闭连接
					}
				});
			}
		});
		ChannelFuture f = b.bind().sync();// 6.绑定服务器来接受连接
		f.channel().closeFuture().sync();
		group.shutdownGracefully().sync();// 7.释放所有资源

	}
}
