package com.it.netty.timetask;

import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;

public class InitChildChannelhandler extends ChannelInitializer{

	@Override
	protected void initChannel(Channel ch) throws Exception {
		// TODO Auto-generated method stub
		ch.pipeline().addLast(new  TimeServerHandler());
	}

}
