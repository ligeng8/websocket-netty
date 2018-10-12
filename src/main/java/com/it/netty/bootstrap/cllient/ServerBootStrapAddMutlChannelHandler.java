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
 * 在一个引导中添加多个 ChannelHandler 在所有的例子代码中，我们在引导过程中通过 handler() 或childHandler()
 * 都只添加了一个 ChannelHandler 实例，对于简单的程序可能足够，但是对于复杂的程序则无法满足需求。例如，某个程序必须支持多个协议，如
 * HTTP、WebSocket。若在一个 ChannelHandle r中处理这些协议将导致一个庞大而复杂的 ChannelHandler。Netty
 * 通过添加多个 ChannelHandler，从而使每个 ChannelHandler 分工明确，结构清晰。
 * 
 * Netty 的一个优势是可以在 ChannelPipeline 中堆叠很多ChannelHandler
 * 并且可以最大程度的重用代码。如何添加多个ChannelHandler 呢？Netty 提供 ChannelInitializer 抽象类用来初始化
 * ChannelPipeline 中的 ChannelHandler。ChannelInitializer是一个特殊的
 * ChannelHandler，通道被注册到 EventLoop 后就会调用ChannelInitializer，并允许将 ChannelHandler
 * 添加到CHannelPipeline；完成初始化通道后，这个特殊的 ChannelHandler 初始化器会从 ChannelPipeline
 * 中自动删除。 听起来很复杂，其实很简单，看下面代码：
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
