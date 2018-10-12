package com.it.netty.study.echo;

import java.util.List;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ReplayingDecoder;

public class ToIntegerDecoder2 extends ReplayingDecoder<Void> {   //1

    @Override
    public void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out)
            throws Exception {
        out.add(in.readInt());  //2
    }
}