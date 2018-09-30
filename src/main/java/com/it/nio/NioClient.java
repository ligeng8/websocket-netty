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
		// ����ѡ����
		Selector selector = Selector.open();
		// �򿪼���ͨ��
		SocketChannel socketChannel = SocketChannel.open();
		// ���Ϊtrue�����ͨ��Ϊ����ģʽ�����Ϊfalse�����ͨ��Ϊ�첽ģʽ
		socketChannel.configureBlocking(false);
		boolean connect = socketChannel.connect(new InetSocketAddress("127.0.0.1", 8888));
		if (connect) {
			socketChannel.register(selector, SelectionKey.OP_CONNECT);
		}
		socketChannel.register(selector, SelectionKey.OP_READ);
		//����Ϣ����Ϊ�ֽ�����		
		byte[] bytes1 = "wolaile ".getBytes();		
		//����������������ByteBuffer		
		ByteBuffer writeBuffer = ByteBuffer.allocate(bytes1.length);		
		//���ֽ����鸴�Ƶ�������		
		writeBuffer.put(bytes1);		
		//flip����		
		writeBuffer.flip();		
		//���ͻ��������ֽ�����	
		socketChannel.write(writeBuffer);
		while (true) {
			// �����Ƕ��¼�д�¼�������selectorÿ��1s������һ��
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
							System.out.println("�ͻ����յ���Ϣ��" + result);
						}
					}
					

				}
			}
		}
	}
}
