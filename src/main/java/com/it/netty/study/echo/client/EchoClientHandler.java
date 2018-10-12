package com.it.netty.study.echo.client;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.CharsetUtil;

@Sharable                                //1
public class EchoClientHandler extends
        SimpleChannelInboundHandler<ByteBuf> {

    @Override//�����������ӽ��������
    public void channelActive(ChannelHandlerContext ctx) {
        ctx.writeAndFlush(Unpooled.copiedBuffer("Netty rocks!", //2
        CharsetUtil.UTF_8));
    }

    @Override//���յ����������ݺ����
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
    	// TODO Auto-generated method stub
    	System.out.println("Client received: " + msg.toString());    //3
    	super.channelRead(ctx, msg);
    }
    @Override//�����쳣ʱ����
    public void exceptionCaught(ChannelHandlerContext ctx,
        Throwable cause) {                    //4
        cause.printStackTrace();
        ctx.close();
    }

	@Override
	protected void messageReceived(ChannelHandlerContext ctx, ByteBuf msg) throws Exception {
		// TODO Auto-generated method stub
        System.out.println("Client received: " + msg.toString(CharsetUtil.UTF_8));    //3
	}
}