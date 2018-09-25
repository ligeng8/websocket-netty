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
		OioEventLoopGroup group = new OioEventLoopGroup();// ʹ�� OioEventLoopGroup ��������ģʽ
		ServerBootstrap b = new ServerBootstrap(); // 1.����һ�� ServerBootstrap
		b.group(group).localAddress(new InetSocketAddress(port)).childHandler(new ChannelInitializer<SocketChannel>() {// ָ��
																														// ChannelInitializer
																														// ����ÿ�����ܵ����ӵ���
			@Override
			protected void initChannel(SocketChannel ch) throws Exception {
				// TODO Auto-generated method stub
				ch.pipeline().addLast(new ChannelHandlerAdapter() {// ��ӵ� ChannelHandler �����¼�������������������Ӧ
					@Override
					public void channelActive(ChannelHandlerContext ctx) throws Exception {
						// TODO Auto-generated method stub
						ctx.writeAndFlush(buf.duplicate()).addListener(ChannelFutureListener.CLOSE);// д��Ϣ���ͻ��ˣ������
																									// ChannelFutureListener
																									// ��һ����Ϣд��͹ر�����
					}
				});
			}
		});
		ChannelFuture f = b.bind().sync();// 6.�󶨷���������������
		f.channel().closeFuture().sync();
		group.shutdownGracefully().sync();// 7.�ͷ�������Դ

	}
}
