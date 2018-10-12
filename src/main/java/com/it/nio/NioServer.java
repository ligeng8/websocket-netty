package com.it.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectableChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.util.Iterator;
import java.util.Set;

import com.it.Calcultor;

import io.netty.buffer.ByteBuf;
import io.netty.util.CharsetUtil;

public class NioServer {
	public static void main(String[] args) throws IOException {
		int port = 8888;
//		        创建选择器
		Selector selector = Selector.open();
//		打开监听通道
		ServerSocketChannel serverChannel = ServerSocketChannel.open();
//		如果为true，则通道被置为阻塞模式，如果为false，此通道为非阻塞模式
		serverChannel.configureBlocking(false);
//		绑定端口
		serverChannel.socket().bind(new InetSocketAddress(port), 1024);
//		监听客户端连接请求
		serverChannel.register(selector, SelectionKey.OP_ACCEPT);
		System.out.println("服务器已启动，端口号：" + port);
		while (true) {
			selector.select(1000);// selector 每隔1s唤醒一次
			Set<SelectionKey> selectedKeys = selector.selectedKeys();
			Iterator<SelectionKey> iterator = selectedKeys.iterator();
			SelectionKey key = null;
			while (iterator.hasNext()) {
				key = iterator.next();
				iterator.remove();
				if (key.isValid()) {
					if (key.isAcceptable()) {
						ServerSocketChannel ssc = (ServerSocketChannel) key.channel();
						// 通过ServerSocketChannel的accept创建SocketChannel实例
						// 完成该操作意味着完成TCP三次握手，TCP物理链路正式建立
						SocketChannel sc = ssc.accept();
						sc.configureBlocking(false);
						// 注册为读
						sc.register(selector, SelectionKey.OP_READ);
					}
					// 读消息
					if (key.isReadable()) {
						SocketChannel sc = (SocketChannel) key.channel();
						// 创建byteBuffer 并开辟一个1M的缓冲区
						ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
						int readBytes = sc.read(byteBuffer);
						// 读到的字节数
						if (readBytes > 0) {
							// 将缓冲区当前的limit设置为position=0 ，用于后续对缓冲区读取操作
							byteBuffer.flip();
							byte[] bytes = new byte[byteBuffer.remaining()];
							byteBuffer.get(bytes);
							String message = new String(bytes, CharsetUtil.UTF_8);
							System.out.println("server receiver : "+message);
							String result = Calcultor.cal(message).toString();
							
							ByteBuffer byteBuffer2 = ByteBuffer.allocate(result.length());
							byteBuffer2.put(result.getBytes());
							byteBuffer2.flip();
							sc.write(byteBuffer2);
						}
					}
				}
			}
		}

	}
}
