package com.it.netty.bootstrap.cllient;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpClientCodec;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpRequestEncoder;

/**
 * ��һ����������Ӷ�� ChannelHandler �����е����Ӵ����У�����������������ͨ�� handler() ��childHandler()
 * ��ֻ�����һ�� ChannelHandler ʵ�������ڼ򵥵ĳ�������㹻�����Ƕ��ڸ��ӵĳ������޷������������磬ĳ���������֧�ֶ��Э�飬��
 * HTTP��WebSocket������һ�� ChannelHandle r�д�����ЩЭ�齫����һ���Ӵ�����ӵ� ChannelHandler��Netty
 * ͨ����Ӷ�� ChannelHandler���Ӷ�ʹÿ�� ChannelHandler �ֹ���ȷ���ṹ������
 * 
 * Netty ��һ�������ǿ����� ChannelPipeline �жѵ��ܶ�ChannelHandler
 * ���ҿ������̶ȵ����ô��롣�����Ӷ��ChannelHandler �أ�Netty �ṩ ChannelInitializer ������������ʼ��
 * ChannelPipeline �е� ChannelHandler��ChannelInitializer��һ�������
 * ChannelHandler��ͨ����ע�ᵽ EventLoop ��ͻ����ChannelInitializer�������� ChannelHandler
 * ��ӵ�CHannelPipeline����ɳ�ʼ��ͨ������������ ChannelHandler ��ʼ������� ChannelPipeline
 * ���Զ�ɾ���� �������ܸ��ӣ���ʵ�ܼ򵥣���������룺
 * 
 * @author ligeng3
 *
 */
public class ServerBootStrapAddMutlChannelHandler {
	public static void main(String[] args) {
	 ServerBootstrap bootstrap = new ServerBootstrap();
	 bootstrap.group(new NioEventLoopGroup())
	          .channel(NioServerSocketChannel.class)
	          .childHandler(new ChannelInitializer<Channel>() {

				@Override
				protected void initChannel(Channel ch) throws Exception {
					// TODO Auto-generated method stub
					ChannelPipeline pipeline = ch.pipeline(); 
					pipeline.addLast(new HttpClientCodec())
					        .addFirst(new HttpObjectAggregator(Integer.MAX_VALUE));
				}
			});
	}
}
