package com.it.nettywebsocket;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;

/**
 * 1.��չ SimpleChannelInboundHandler ���ڴ��� TextWebSocketFrame ��Ϣ
 * 2.��дuserEventTriggered() �����������Զ����¼� 3.������յ��¼��������ֳɹ�,�ʹ� ChannelPipeline
 * ��ɾ��HttpRequestHandler ����Ϊ������������� HTTP ��Ϣ�� 4.дһ����Ϣ�����е������� WebSocket
 * �ͻ��ˣ�֪ͨ���ǽ�����һ���µ� Channel ���� 5.��������ӵ� WebSocket Channel �� ChannelGroup
 * �У������������յ����е���Ϣ
 * 
 * 6.�����յ�����Ϣ����ͨ�� writeAndFlush() ���ݸ��������ӵĿͻ��ˡ� ������ʾ�� TextWebSocketFrameHandler
 * �����˼����£� ��WebSocket ���¿ͻ����ѳɹ�������ɣ�ͨ��д����Ϣ�� ChannelGroup �е� Channel
 * ��֪ͨ�������ӵĿͻ��ˣ�Ȼ������� Channel �� ChannelGroup ������յ� TextWebSocketFrame������ retain()
 * ��������д��ˢ�µ� ChannelGroup��ʹ�������ӵ� WebSocket Channel ���ܽ��յ���������ǰһ����retain()
 * �Ǳ���ģ���Ϊ�� channelRead0��������ʱ��TextWebSocketFrame
 * �����ü������ݼ����������в��������첽�ģ�writeAndFlush() ���ܻ����Ժ���ɣ����ǲ�ϣ����������Ч�����á� ���� Netty
 * �����ڲ�����������󲿷ֹ��ܣ�Ψһʣ�µ���Ҫ����ȥ���ľ���Ϊÿһ���´����� Channel ��ʼ�� ChannelPipeline
 * ��Ҫ��������������Ҫһ��ChannelInitializer
 * 
 * @author ligeng3
 *
 */
public class TextWebSocketFrameHandler extends SimpleChannelInboundHandler<TextWebSocketFrame> {
	private final ChannelGroup group;

	public TextWebSocketFrameHandler(ChannelGroup group) {
		this.group = group;
	}

	@Override
	public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception { // 2
		if (evt == WebSocketServerProtocolHandler.ServerHandshakeStateEvent.HANDSHAKE_COMPLETE) {

			ctx.pipeline().remove(HttpRequestHandler.class); // 3

			group.writeAndFlush(new TextWebSocketFrame("Client " + ctx.channel() + " joined"));// 4

			group.add(ctx.channel()); // 5
		} else {
			super.userEventTriggered(ctx, evt);
		}
	}

	@Override
	protected void messageReceived(ChannelHandlerContext ctx, TextWebSocketFrame msg) throws Exception {
		// TODO Auto-generated method stub
		group.writeAndFlush(msg.retain()); // 6
	}

}
