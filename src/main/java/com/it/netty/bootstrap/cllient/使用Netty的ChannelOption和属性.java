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
 * 比较麻烦的是创建通道后不得不手动配置每个通道，为了避免这种情况，Netty 提供了 ChannelOption
 * 来帮助引导配置。这些选项会自动应用到引导创建的所有通道，可用的各种选项可以配置底层连接的详细信息，
 * 如通道“keep-alive(保持活跃)”或“timeout(超时)”的特性。
 * 
 * Netty 应用程序通常会与组织或公司其他的软件进行集成，在某些情况下，Netty 的组件如 Channel 在 Netty 正常生命周期外使用；
 * Netty 的提供了抽象 AttributeMap 集合,这是由 Netty 的管道和引导类,和
 * AttributeKey，常见类用于插入和检索属性值。属性允许您安全的关联任何数据项与客户端和服务器的 Channel。
 * 例如,考虑一个服务器应用程序跟踪用户和 Channel 之间的关系。这可以通过存储用户 ID 作为 Channel
 * 的一个属性。类似的技术可以用来路由消息到基于用户 ID 或关闭基于用户活动的一个管道。 清单9.7展示了如何使用 ChannelOption 配置
 * Channel 和一个属性来存储一个整数值。
 * 
 * @author ligeng3
 *
 */
public class 使用Netty的ChannelOption和属性 {
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
