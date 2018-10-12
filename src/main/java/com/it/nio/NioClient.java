package com.it.nio;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectableChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

import io.netty.util.CharsetUtil;

public class NioClient {
	public static void main(String[] args) throws IOException {
		// 创建选择器
		Selector selector = Selector.open();
		// 打开监听通道
		SocketChannel socketChannel = SocketChannel.open();
		// 如果为true，则此通道为阻塞模式，如果为false，则此通道为异步模式
		socketChannel.configureBlocking(false);
		boolean connect = socketChannel.connect(new InetSocketAddress("127.0.0.1", 8888));
		if (connect) {
			socketChannel.register(selector, SelectionKey.OP_CONNECT);
		}
		socketChannel.register(selector, SelectionKey.OP_READ);
		//将消息编码为字节数组		
		byte[] bytes1 = "wolaile ".getBytes();		
		//根据数组容量创建ByteBuffer		
		ByteBuffer writeBuffer = ByteBuffer.allocate(bytes1.length);		
		//将字节数组复制到缓冲区		
		writeBuffer.put(bytes1);		
		//flip操作		
		writeBuffer.flip();		
		//发送缓冲区的字节数组	
		socketChannel.write(writeBuffer);
		while (true) {
			// 无论是读事件写事件发生，selector每隔1s被唤醒一次
			selector.select(1000);
			Set<SelectionKey> selectedKeys = selector.selectedKeys();
			Iterator<SelectionKey> iterator = selectedKeys.iterator();
			SelectionKey key = null;
			while (iterator.hasNext()) {
				key = iterator.next();
				iterator.remove();
				if (key.isValid()) {
					SocketChannel sc = (SocketChannel) key.channel();
					if (key.isConnectable()) {
						if (!sc.finishConnect()) {
							System.exit(1);
						}
					}
					if (key.isReadable()) {
						ByteBuffer buffer = ByteBuffer.allocate(1024);
						int readBytes = sc.read(buffer);
						if (readBytes > 0) {
							buffer.flip();
							byte[] bytes = new byte[buffer.remaining()];
							buffer.get(bytes);
							String result = new String(bytes,CharsetUtil.UTF_8);
							System.out.println("客户端收到消息：" + result);
						}
					}
					

				}
			}
		}
	}
}
