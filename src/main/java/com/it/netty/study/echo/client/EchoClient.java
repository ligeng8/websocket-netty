package com.it.netty.study.echo.client;

import java.net.InetSocketAddress;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

public class EchoClient {

	private String host;
	private int port;

	public EchoClient(String host, int port) {
		this.host = host;
		this.port = port;
	}
	
	public static void main(String[] args) throws Exception {
		String host = "127.0.0.1";
		int port = 8085;
		EchoClient echoClient = new EchoClient(host, port);
		echoClient.start();
	}
	
	public void start() throws Exception{
		NioEventLoopGroup group = new NioEventLoopGroup();
		 
		try {
			Bootstrap b = new  Bootstrap();//1.���� Bootstrap
			b.group(group)//ָ�� EventLoopGroup ������ͻ����¼�����������ʹ�� NIO ���䣬�����õ��� NioEventLoopGroup ��ʵ��
			 .channel(NioSocketChannel.class)//3.ʹ�õ� channel ������һ������ NIO ����
			 .remoteAddress(new InetSocketAddress(host, port))	//4.���÷������� InetSocketAddress
			 .handler(new ChannelInitializer<SocketChannel>() {//5.������һ�����Ӻ�һ���µ�ͨ��ʱ��������ӵ� EchoClientHandler ʵ�� �� channel pipeline
				  protected void initChannel(SocketChannel ch) throws Exception {
					ch.pipeline().addLast(new EchoClientHandler());
				  };
			});
			ChannelFuture f = b.connect().sync();//6.���ӵ�Զ��;�ȴ��������
			f.channel().closeFuture().sync();//7.����ֱ�� Channel �ر�
		} finally {
			group.shutdownGracefully().sync();//���� shutdownGracefully() ���ر��̳߳غ��ͷ�������Դ
		}
	}
	
	
	
	
	
	
	
}
