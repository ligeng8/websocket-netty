package com.it.netty.websocket;

import java.util.Date;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.HttpVersion;
import io.netty.handler.codec.http.websocketx.CloseWebSocketFrame;
import io.netty.handler.codec.http.websocketx.PingWebSocketFrame;
import io.netty.handler.codec.http.websocketx.PongWebSocketFrame;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketServerHandshaker;
import io.netty.handler.codec.http.websocketx.WebSocketServerHandshakerFactory;
import io.netty.util.CharsetUtil;

/**
 * 接收、处理、响应客户端websocket请求的的核心业务处理类
 * 
 * @author ligeng3
 *
 */
public class MyWebSocketHandler extends SimpleChannelInboundHandler<Object> {

	private WebSocketServerHandshaker handshaker;
	private static final String WEB_SOCKET_URL = "ws://localhost:8888/websocket";

	@Override
	protected void messageReceived(ChannelHandlerContext ctx, Object msg) throws Exception {
		// 处理客户端向服务端发起http握手请求
		if (msg instanceof FullHttpRequest) {
			handHttpRequest(ctx, (FullHttpRequest) msg);
		} else if (msg instanceof WebSocketFrame) {// 处理websocket连接业务
			handWebsocketFrame(ctx,(WebSocketFrame) msg);
		}
	}

	/**
	 * 处理客户端与服务端之间的websocket业务
	 * 
	 * @param ctx
	 * @param frame
	 */
	public void handWebsocketFrame(ChannelHandlerContext ctx, WebSocketFrame frame) {
         //判断是否是关闭websocket的指令
		if (frame instanceof CloseWebSocketFrame) {
			handshaker.close(ctx.channel(), ((CloseWebSocketFrame) frame.retain()));
		}
		//判断是否是ping的消息
		if(frame instanceof PingWebSocketFrame) {
			ctx.channel().write(new PongWebSocketFrame(frame.content().retain()));
			return;
		}
	  //判断是否是二进制消息，如果是二进制消息，抛出异常
		if(!(frame instanceof TextWebSocketFrame)) {
			System.out.println("目前我们不支持二进制消息");
			throw new RuntimeException("【"+ this.getClass().getName()+"】 不支持消息");
		}
		
		//返回应答消息
		//获取客户端向服务端发送的消息
		String request = ((TextWebSocketFrame)frame).text();
		System.out.println("服务端接收到客户端的消息=====>>>>> "+ request);
		TextWebSocketFrame tws = new TextWebSocketFrame(new Date().toString()
				                                         +ctx.channel().id()
				                                         +" =====>>> "
				                                         + request );
        //群发消息，服务端向每个连接上来的客户端群发消息
		 NettyConfig.group.writeAndFlush(tws);
	}

	/**
	 * 处理客户端向服务端发起http握手请求的业务
	 * 
	 * @param ctx
	 * @param msg
	 */
	public void handHttpRequest(ChannelHandlerContext ctx, FullHttpRequest req) {
		if (!req.getDecoderResult().isSuccess() || !"websocket".equals(req.headers().get("Upgrade"))) {
			sendHttpResponse(ctx, req,
					new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.BAD_REQUEST));
			return;
		}

		WebSocketServerHandshakerFactory wsFactory = new WebSocketServerHandshakerFactory(WEB_SOCKET_URL, null, false);
		handshaker = wsFactory.newHandshaker(req);
		if (handshaker == null) {
			WebSocketServerHandshakerFactory.sendUnsupportedWebSocketVersionResponse(ctx.channel());
		} else {
			handshaker.handshake(ctx.channel(), req);
		}
	}

	/**
	 * 服务端向客户端响应消息
	 * 
	 * @param ctx
	 * @param req
	 * @param rep
	 */
	public void sendHttpResponse(ChannelHandlerContext ctx, FullHttpRequest req, DefaultFullHttpResponse rep) {
		// TODO Auto-generated method stub
		if (rep.getStatus().code() == 200) {
			ByteBuf buf = Unpooled.copiedBuffer(rep.getStatus().toString(), CharsetUtil.UTF_8);
			rep.content().writeBytes(buf);
			buf.release();
		}
       //服务端向客户端发送数据
		ChannelFuture flush = ctx.channel().writeAndFlush(rep);
		if (rep.getStatus().code() != 200) {
			flush.addListener(ChannelFutureListener.CLOSE);
		}
	}

    //  客户端与服务端连接的时候调用
	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		NettyConfig.group.add(ctx.channel());
		System.out.println("连接开启");
	}

    //客户端与服务端断开连接的时候调用
	@Override
	public void channelInactive(ChannelHandlerContext ctx) throws Exception {
		NettyConfig.group.remove(ctx.channel());
	}

    //工程出现异常的时候调用
	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		// TODO Auto-generated method stub
		cause.printStackTrace();
		ctx.close();
	}

    //服务端接收客户端发过来的数据结束之后调用
	@Override
	public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
		ctx.flush();
	}

}
