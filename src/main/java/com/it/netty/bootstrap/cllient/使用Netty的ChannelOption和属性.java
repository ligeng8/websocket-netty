package com.it.netty.bootstrap.cllient;

import java.net.InetSocketAddress;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelOption;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.util.AttributeKey;

/**
 * �Ƚ��鷳���Ǵ���ͨ���󲻵ò��ֶ�����ÿ��ͨ����Ϊ�˱������������Netty �ṩ�� ChannelOption
 * �������������á���Щѡ����Զ�Ӧ�õ���������������ͨ�������õĸ���ѡ��������õײ����ӵ���ϸ��Ϣ��
 * ��ͨ����keep-alive(���ֻ�Ծ)����timeout(��ʱ)�������ԡ�
 * 
 * Netty Ӧ�ó���ͨ��������֯��˾������������м��ɣ���ĳЩ����£�Netty ������� Channel �� Netty ��������������ʹ�ã�
 * Netty ���ṩ�˳��� AttributeMap ����,������ Netty �Ĺܵ���������,��
 * AttributeKey�����������ڲ���ͼ�������ֵ��������������ȫ�Ĺ����κ���������ͻ��˺ͷ������� Channel��
 * ����,����һ��������Ӧ�ó�������û��� Channel ֮��Ĺ�ϵ�������ͨ���洢�û� ID ��Ϊ Channel
 * ��һ�����ԡ����Ƶļ�����������·����Ϣ�������û� ID ��رջ����û����һ���ܵ��� �嵥9.7չʾ�����ʹ�� ChannelOption ����
 * Channel ��һ���������洢һ������ֵ��
 * 
 * @author ligeng3
 *
 */
public class ʹ��Netty��ChannelOption������ {
	public static void main(String[] args) {
		final AttributeKey<Integer> attributeKey = AttributeKey.valueOf("ID");
		Bootstrap bootstrap = new Bootstrap();
		bootstrap.group(new NioEventLoopGroup())
		         .channel(NioSocketChannel.class)
		         .handler(new SimpleChannelInboundHandler<ByteBuf>() {

		        	 
		        	  @Override
		        	public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
		        		// TODO Auto-generated method stub
		        		  Integer integer = ctx.channel().attr(attributeKey).get();
		        		super.channelRegistered(ctx);
		        	}
		        	 
					@Override
					protected void messageReceived(ChannelHandlerContext ctx, ByteBuf msg) throws Exception {
						// TODO Auto-generated method stub
						System.out.println("message received");
					}
				});
		
		
		bootstrap.option(ChannelOption.SO_KEEPALIVE, true).option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 5000);
		ChannelFuture channelFuture = bootstrap.connect(new InetSocketAddress("www.baidu.com", 80));
		channelFuture.syncUninterruptibly();
	}
}
