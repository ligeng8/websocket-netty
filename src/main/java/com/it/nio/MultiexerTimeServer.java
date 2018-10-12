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
import java.util.Date;
import java.util.Iterator;
import java.util.Set;

import io.netty.util.CharsetUtil;

public class MultiexerTimeServer implements Runnable {

	private Selector selector;

	private ServerSocketChannel servChannel;

	private volatile boolean stop = false;

	public void setStop(boolean stop) {
		this.stop = stop;
	}

	public MultiexerTimeServer(int port) {
		try {
			selector = Selector.open();
			servChannel = ServerSocketChannel.open();
			servChannel.configureBlocking(false);
			servChannel.socket().bind(new InetSocketAddress(port));
			servChannel.register(selector, SelectionKey.OP_ACCEPT);
			System.out.println("timeServer start in port: " + port);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void run() {
		// TODO Auto-generated method stub
		while (!stop) {
			try {
				selector.select(1000);// 1s一次
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
			}
			
		}

		if (selector != null) {
			try {
				selector.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	public void handleInput(SelectionKey key) throws IOException {
		if(key.isValid()) {
			if(key.isAcceptable()) {
				// accept a new connection
				ServerSocketChannel ssc = (ServerSocketChannel) key.channel();
				SocketChannel sc = ssc.accept();
				sc.configureBlocking(false);
				sc.register(selector, SelectionKey.OP_READ);
			}
			if(key.isReadable()) {
				SocketChannel sc = (SocketChannel) key.channel();
				 ByteBuffer readBuffer = ByteBuffer.allocate(1024);
				 int read = sc.read(readBuffer);
				 if(read > 0) {//正常读到数据.对字节码编码
					 readBuffer.flip();
					 byte[] bytes =	 new byte[readBuffer.remaining()];
					 readBuffer.get(bytes);
					 String body = new String(bytes,CharsetUtil.UTF_8);
					 System.out.println("time server recevice order :"+ body);
					 String currentTime = "QUERY TIME ORDER".equalsIgnoreCase(body)?new Date().toLocaleString() : "BAD ORDER";
					 dowrite(sc, currentTime);
						servChannel.register(selector, SelectionKey.OP_ACCEPT);
				 }else if(read == -1) { //返回值为-1 链路已经关闭，需要关闭SocketChannel，释放资源
					 key.cancel();
					 sc.close();
				 }else {//返回值等于0 没有读到字节，属于正常现象，忽略
					 
				 }
			}
		}
	}
   public void dowrite(SocketChannel sc,String response) throws IOException {
	   if(response != null && response.trim().length() >0) {
		   byte[] bytes = response.getBytes();
		   ByteBuffer writeBuffer = ByteBuffer.allocate(bytes.length);
		   writeBuffer.put(bytes);
		   writeBuffer.flip();
		   sc.write(writeBuffer);
		   if (!writeBuffer.hasRemaining()) {
				System.out.println("send order to client success");
			}
	   }
   }
   
   
   
   
   
   
   
   
   
   
}
