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
	 * �洢ÿһ���ͻ��˽ӽ�����Channel
	 */
public static ChannelGroup group = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);


}
