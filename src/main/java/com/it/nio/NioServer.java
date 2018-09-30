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
//		        ����ѡ����
		Selector selector = Selector.open();
//		�򿪼���ͨ��
		ServerSocketChannel serverChannel = ServerSocketChannel.open();
//		���Ϊtrue����ͨ������Ϊ����ģʽ�����Ϊfalse����ͨ��Ϊ������ģʽ
		serverChannel.configureBlocking(false);
//		�󶨶˿�
		serverChannel.socket().bind(new InetSocketAddress(port), 1024);
//		�����ͻ�����������
		serverChannel.register(selector, SelectionKey.OP_ACCEPT);
		System.out.println("���������������˿ںţ�" + port);
		while (true) {
			selector.select(1000);// selector ÿ��1s����һ��
			Set<SelectionKey> selectedKeys = selector.selectedKeys();
			Iterator<SelectionKey> iterator = selectedKeys.iterator();
			SelectionKey key = null;
			while (iterator.hasNext()) {
				key = iterator.next();
				iterator.remove();
				if (key.isValid()) {
					if (key.isAcceptable()) {
						ServerSocketChannel ssc = (ServerSocketChannel) key.channel();
						// ͨ��ServerSocketChannel��accept����SocketChannelʵ��
						// ��ɸò�����ζ�����TCP�������֣�TCP������·��ʽ����
						SocketChannel sc = ssc.accept();
						sc.configureBlocking(false);
						// ע��Ϊ��
						sc.register(selector, SelectionKey.OP_READ);
					}
					// ����Ϣ
					if (key.isReadable()) {
						SocketChannel sc = (SocketChannel) key.channel();
						// ����byteBuffer ������һ��1M�Ļ�����
						ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
						int readBytes = sc.read(byteBuffer);
						// �������ֽ���
						if (readBytes > 0) {
							// ����������ǰ��limit����Ϊposition=0 �����ں����Ի�������ȡ����
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
