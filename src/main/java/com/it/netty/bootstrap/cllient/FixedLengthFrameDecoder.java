package com.it.netty.bootstrap.cllient;

import java.util.List;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

public class FixedLengthFrameDecoder extends ByteToMessageDecoder { // 1
	private final int frameLength;

	public FixedLengthFrameDecoder(int frameLength) { // 2
		if (frameLength <= 0) {
			throw new IllegalArgumentException("frameLength must be a positive integer: " + frameLength);
		}
		this.frameLength = frameLength;
	}

	@Override
	protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
		if (in.readableBytes() >= frameLength) { // 3
			ByteBuf buf = in.readBytes(frameLength);// 4
			out.add(buf); // 5
		}
	}
}