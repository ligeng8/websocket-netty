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
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import io.netty.util.CharsetUtil;

public class MultiexerTimeServer implements Runnable {

	private Selector selector;

	private ServerSocketChannel servChannel;

	private volatile boolean stop = false;
	
	private ConcurrentHashMap<String, SocketChannel> all = new ConcurrentHashMap<>();

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
					 
					 if("QUERY TIME ORDER".equalsIgnoreCase(body)) {
						 String currentTime = new Date().toLocaleString() ;
						 dowrite(sc, currentTime);
					 }else {
						 all.put(body, sc);
						 dowrite(sc, "sur success");
					 }
//						servChannel.register(selector, SelectionKey.OP_ACCEPT);
				 }else if(read == -1) { //返回值为-1 链路已经关闭，需要关闭SocketChannel，释放资源
					 key.cancel();
					 sc.close();
				 }else {//返回值等于0 没有读到字节，属于正常现象，忽略
					 
				 }
			}
		}
	}
	
	
	public void publishAll() {
		Set<Entry<String, SocketChannel>>  entrys = this.all.entrySet();
		for (Entry<String, SocketChannel> entry : entrys) {
			String key = entry.getKey();
			SocketChannel value = entry.getValue(); 
			String response = key + value;
			try {
				dowrite(value, response);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
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
