package com.it.nettywebsocket;

import java.io.File;
import java.io.RandomAccessFile;
import java.net.URISyntaxException;
import java.net.URL;

import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.DefaultFileRegion;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.DefaultHttpResponse;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpHeaders;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.HttpVersion;
import io.netty.handler.codec.http.LastHttpContent;
import io.netty.handler.ssl.SslHandler;
import io.netty.handler.stream.ChunkedNioFile;

/**
 * HttpRequestHandler �������漸���£� ����� HTTP ���󱻷��͵�URI ��/ws��������� FullHttpRequest �ϵ�
 * retain()����ͨ������ fireChannelRead(msg) ת������һ�� ChannelInboundHandler��retain()
 * �ĵ����Ǳ�Ҫ�ģ���Ϊ channelRead() ��ɺ�������� FullHttpRequest �ϵ� release() ���ͷ�����Դ��
 * ����ο�������ǰ�ڵ�6���й��� SimpleChannelInboundHandler �����ۣ� ����ͻ��˷��͵� HTTP 1.1 ͷ�ǡ�Expect:
 * 100-continue�� �����͡�100 Continue������Ӧ�� �� ͷ�����ú�дһ�� HttpResponse ���ظ��ͻ��ˡ�ע�⣬�ⲻ��
 * FullHttpResponse����ֻ����Ӧ�ĵ�һ���֡����⣬��������Ҳ��ʹ�� writeAndFlush()�� ����������������ɡ�
 * ���������̼�û��Ҫ�����Ҳû��Ҫ��ѹ������ô�� index.html �����ݴ洢��һ�� DefaultFileRegion
 * ��Ϳ��Դﵽ��õ�Ч�ʡ��⽫�����㿽����ִ�д��䡣�������ԭ������Ҫ��� ChannelPipeline ���Ƿ���һ��
 * SslHandler������ǵĻ������Ǿ�ʹ�� ChunkedNioFile�� д LastHttpContent �������Ӧ�Ľ���������ֹ�� �����Ҫ��
 * keepalive ����� ChannelFutureListener �� ChannelFuture ��������д�룬���ر����ӡ�ע�⣬�������ǵ���
 * writeAndFlush() ��ˢ��������ǰд����Ϣ�� ����չʾ��Ӧ�ó���ĵ�һ���֣����������� HTTP �������Ӧ�����������ǽ�����
 * WebSocket �� frame��֡������������������Ϣ�� WebSocket frame WebSockets
 * �ڡ�֡���������������ݣ�����ÿһ����������һ����Ϣ��һ���֡�һ����������Ϣ���������˶��֡��
 * 
 * @author ligeng3
 *
 */
public class HttpRequestHandler extends SimpleChannelInboundHandler<FullHttpRequest> {

	private String wsUrl = "/websocket";
	private static File INDEX;

	public HttpRequestHandler(String wsUrl) {
		this.wsUrl = wsUrl;
	}

	static {
		URL location = HttpRequestHandler.class.getProtectionDomain().getCodeSource().getLocation();
		try {
			String path = location.toURI() + "index.html";
			path = !path.contains("file:") ? path : path.substring(5);
			INDEX = new File(path);
		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			throw new IllegalStateException("Unable to locate index.html", e);
		}
	}

	@Override
	protected void messageReceived(ChannelHandlerContext ctx, FullHttpRequest request) throws Exception {// ��չ
																											// SimpleChannelInboundHandler
																											// ���ڴ���
																											// FullHttpRequest��Ϣ
		// TODO Auto-generated method stub
		if (this.wsUrl.equalsIgnoreCase(request.getUri().substring(0, "/websocket".length()))) {
			ctx.fireChannelRead(request.retain());// ���������һ�������˵� WebSocket ������������ü�������retain�����ҽ������ݸ��� ChannelPipeline
													// �е��¸� ChannelInboundHandler
		} else {
			if (HttpHeaders.is100ContinueExpected(request)) {
				send100Continue(ctx);// 3.������� HTTP 1.1�� "100 Continue" ����
			}
			RandomAccessFile file = new RandomAccessFile(INDEX, "r");// 4.��ȡ index.html
			DefaultHttpResponse response = new DefaultHttpResponse(request.getProtocolVersion(), HttpResponseStatus.OK);
			response.headers().set(HttpHeaders.Names.CONTENT_TYPE, "text/html;charset=UTF-8");
			boolean keepAlive = HttpHeaders.isKeepAlive(request);// 5.�ж� keepalive �Ƿ�������ͷ����
			if (keepAlive) {
				response.headers().set(HttpHeaders.Names.CONTENT_LENGTH, file.length());
				response.headers().set(HttpHeaders.Names.CONNECTION, HttpHeaders.Values.KEEP_ALIVE);
			}
			ctx.write(response);// 6.д HttpResponse ���ͻ���
			if (ctx.pipeline().get(SslHandler.class) == null) {
				ctx.write(new DefaultFileRegion(file.getChannel(), 0, file.length()));// .д index.html ���ͻ��ˣ�����
																						// ChannelPipeline ���Ƿ���
																						// SslHandler ������ʹ��
																						// DefaultFileRegion ����
																						// ChunkedNioFile
			} else {
				ctx.write(new ChunkedNioFile(file.getChannel()));
			}

			ChannelFuture future = ctx.writeAndFlush(LastHttpContent.EMPTY_LAST_CONTENT);// д��ˢ�� LastHttpContent
																							// ���ͻ��ˣ������Ӧ���
			if (!keepAlive) {// ��� ����ͷ�в����� keepalive����д���ʱ���ر� Channel
				future.addListener(ChannelFutureListener.CLOSE);
			}
		}

	}

	private static void send100Continue(ChannelHandlerContext ctx) {
		FullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.CONTINUE);
		ctx.writeAndFlush(response);
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		cause.printStackTrace();
		ctx.close();
	}
}
