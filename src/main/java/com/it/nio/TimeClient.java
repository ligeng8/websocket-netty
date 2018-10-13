package com.it.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Random;
import java.util.Set;

import io.netty.util.CharsetUtil;

public class TimeClient implements Runnable {

	private String ip;

	private int port;

	private Selector selector;

	private SocketChannel socketChannel;

	private volatile boolean stop = false;

	public TimeClient(String ip, int port) {
		super();
		this.ip = ip == null ? "127.0.0.1" : ip;
		this.port = port;
		try {
			selector = Selector.open();
			socketChannel = SocketChannel.open();
			socketChannel.configureBlocking(false);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void run() {
		// TODO Auto-generated method stub
		try {
			doConnect();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.exit(1);
		}
		while (!stop) {
			try {
				selector.select(1000);
				Set<SelectionKey> selectedKeys = selector.selectedKeys();
				Iterator<SelectionKey> iterator = selectedKeys.iterator();
				SelectionKey key = null;
				while (iterator.hasNext()) {
					key = iterator.next();
					iterator.remove();
					try {
						handleInput(key);
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						if (key != null) {
							key.cancel();
							if (key.channel() != null) {
								key.channel().close();
							}
						}
					}
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				System.exit(1);
			} finally {

			}
		}
		
		try {
			if (selector != null)
				selector.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void handleInput(SelectionKey key) throws IOException {
		if (key.isValid()) {
			SocketChannel sc = (SocketChannel) key.channel();
			if (key.isConnectable()) {
				if (sc.finishConnect()) {
					sc.register(selector, SelectionKey.OP_READ);
					dowrite( sc);
				} else {
					System.exit(1); // 连接失败，进程退出
				}
			} else if (key.isReadable()) {
				ByteBuffer readbuffer = ByteBuffer.allocate(1024);
				int readBytes = sc.read(readbuffer);
				if (readBytes > 0) {
					readbuffer.flip();
					byte[] bytes = new byte[readbuffer.remaining()];
					readbuffer.get(bytes);
					String body = new String(bytes, CharsetUtil.UTF_8);
					System.out.println("Now is :" + body);
//					this.stop = true;
					if(body.equals("sur success")) {
						return;
					}
					sub(sc, "sur="+new Random().nextInt()+"");
				} else if (readBytes < 0) {
					key.cancel();
					selector.close();// 对端链路关闭
				} else {
					;// 读到空字节 ，忽略
				}
			}

		}
	}

	public void doConnect() throws Exception {
		if (socketChannel.connect(new InetSocketAddress(ip, port))) {
			socketChannel.register(selector, SelectionKey.OP_READ);
			dowrite(socketChannel);
		} else {
			socketChannel.register(selector, SelectionKey.OP_CONNECT);
		}
	}

	
	
	public void sub(SocketChannel sc,String subr) throws IOException {
		byte[] bytes = subr.getBytes();
		ByteBuffer writeBuffer = ByteBuffer.allocate(bytes.length);
		writeBuffer.put(bytes);
		writeBuffer.flip();
		sc.write(writeBuffer);
		if (!writeBuffer.hasRemaining()) {
			System.out.println("send order to server success");
		}
	}
	public void dowrite(SocketChannel sc) throws IOException {
		byte[] bytes = "QUERY TIME ORDER".getBytes();
		ByteBuffer writeBuffer = ByteBuffer.allocate(bytes.length);
		writeBuffer.put(bytes);
		writeBuffer.flip();
		sc.write(writeBuffer);
		if (!writeBuffer.hasRemaining()) {
			System.out.println("send order to server success");
		}
	}

}
