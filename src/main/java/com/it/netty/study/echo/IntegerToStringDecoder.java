package com.it.netty.study.echo;

import java.util.List;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;
/**
 * 
 * @author ligeng3
 *
 */
public class IntegerToStringDecoder extends MessageToMessageDecoder<Integer> {

	@Override
	protected void decode(ChannelHandlerContext ctx, Integer msg, List<Object> out) throws Exception {
		// TODO Auto-generated method stub
		out.add(msg.toString());
	}

}
