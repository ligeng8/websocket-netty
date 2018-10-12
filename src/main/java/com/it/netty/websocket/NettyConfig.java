package com.it.netty.websocket;

import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.util.concurrent.GlobalEventExecutor;
/**
 * 
 * @author ligeng3
 *
 */
public class NettyConfig {
	/**
	 * 存储每一个客户端接进来的Channel
	 */
public static ChannelGroup group = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);


}
