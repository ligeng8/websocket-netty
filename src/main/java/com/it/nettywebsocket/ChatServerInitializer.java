package com.it.nettywebsocket;

import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.group.ChannelGroup;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.handler.stream.ChunkedWriteHandler;

/**
 * 1.��չ ChannelInitializer 2.��� ChannelHandler �� ChannelPipeline initChannel()
 * ������������������ע��� Channel ��ChannelPipeline,��װ������Ҫ�� ChannelHandler���ܽ����£� Table 11.2
 * ChannelHandlers for the WebSockets Chat server ChannelHandler ְ��
 * HttpServerCodec Decode bytes to HttpRequest, HttpContent,
 * LastHttpContent.Encode HttpRequest, HttpContent, LastHttpContent to bytes.
 * ChunkedWriteHandler Write the contents of a file. HttpObjectAggregator This
 * ChannelHandler aggregates an HttpMessage and its following HttpContents into
 * a single FullHttpRequest or FullHttpResponse (depending on whether it is
 * being used to handle requests or responses).With this installed the next
 * ChannelHandler in the pipeline will receive only full HTTP requests.
 * HttpRequestHandler Handle FullHttpRequests (those not sent to "/ws" URI).
 * WebSocketServerProtocolHandler As required by the WebSockets specification,
 * handle the WebSocket Upgrade handshake,
 * PingWebSocketFrames,PongWebSocketFrames and CloseWebSocketFrames.
 * TextWebSocketFrameHandler Handles TextWebSocketFrames and handshake
 * completion events �� WebSocketServerProtocolHandler �������й涨�� WebSocket
 * ֡���ͺ��������ֱ���������ֳɹ������ ChannelHandler ����ӵ��ܵ�������Щ������Ҫ����ȥ�����ܵ�����֮ǰ��״̬����ͼ���������
 * ChannelPipeline �ով��� ChatServerInitializer ��ʼ���� Figure 11.3 ChannelPipeline
 * before WebSockets Upgrade
 * 
 * 
 * ���������ɹ��� WebSocketServerProtocolHandler �滻HttpRequestDecoder Ϊ
 * WebSocketFrameDecoder��HttpResponseEncoder ΪWebSocketFrameEncoder��
 * Ϊ��������ܣ�WebSocket ���Ӳ���Ҫ�� ChannelHandler ���ᱻ�Ƴ������оͰ����� HttpObjectAggregator ��
 * HttpRequestHandler ��ͼ��չʾ�� ChannelPipeline �������������ɺ�������ע�� Netty Ŀǰ֧���ĸ��汾
 * WebSocket Э�飬ÿ��ͨ��������ķ�ʽʵ���ࡣѡ����ȷ�İ汾WebSocketFrameDecoder ��
 * WebSocketFrameEncoder ���Զ����еģ���ȡ�����ڿͻ��ˣ�������ָ���������֧�֣�����������У����Ǽ���ʹ�ð汾�� 13 ��
 * WebSocket Э�飬�Ӷ�ͼ����ʾ���� WebSocketFrameDecoder13 �� WebSocketFrameEncoder13����
 * 
 * Figure 11.4 ChannelPipeline after WebSockets Upgrade
 * 
 * @author ligeng3
 *
 */
public class ChatServerInitializer extends ChannelInitializer<Channel> {

	private ChannelGroup group;

	public ChatServerInitializer(ChannelGroup group) {
		super();
		this.group = group;
	}

	@Override
	protected void initChannel(Channel ch) throws Exception {
		ChannelPipeline pipeline = ch.pipeline();
		pipeline.addLast(new HttpServerCodec());
		pipeline.addLast(new HttpObjectAggregator(64 * 10247));
		pipeline.addLast(new ChunkedWriteHandler());
		pipeline.addLast(new HttpRequestHandler("/websocket"));
		pipeline.addLast(new WebSocketServerProtocolHandler("/websocket"));
		pipeline.addLast(new TextWebSocketFrameHandler(group));
	}

}
