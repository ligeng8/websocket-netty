package com.it.netty.transport.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

public class PlainNioServer {

	public void serve(int port) throws IOException {
		ServerSocketChannel serverChannel = ServerSocketChannel.open();
		serverChannel.configureBlocking(false);
		ServerSocket socket = serverChannel.socket();
		InetSocketAddress address = new InetSocketAddress(port);
		socket.bind(address);//1 �󶨷��������ƶ��˿�
		Selector selector = Selector.open();//2 �� selector ���� channel
		serverChannel.register(selector, SelectionKey.OP_ACCEPT);//3 ע�� ServerSocket �� ServerSocket ����ָ������ר������� ���ӡ�
	     final ByteBuffer msg = ByteBuffer.wrap("Hi!\r\n".getBytes());
	      while(true) {
	    	  
	    	  try {
				selector.select();//4 �ȴ��µ��¼��������⽫������ֱ��һ���¼��Ǵ��롣
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				break;
			}
	    	  Set<SelectionKey> readykeys = selector.selectedKeys();//5 ���յ��������¼��� ��ȡ SelectionKey ʵ����
	    	  Iterator<SelectionKey> iterator = readykeys.iterator();
	    	  while(iterator.hasNext()) {
	    		  SelectionKey key = iterator.next();
	    		  iterator.remove();
	    		  try {
					if(key.isAcceptable()) {//6 �����¼���һ���µ�����׼���ý��ܡ�
						  ServerSocketChannel server = (ServerSocketChannel)key.channel();
						  SocketChannel client = server.accept();
						  client.configureBlocking(false);
						  client.register(selector, SelectionKey.OP_WRITE|SelectionKey.OP_READ,msg.duplicate());//7 .���ܿͻ��ˣ����� selector ����ע�ᡣ
						  System.out.println("Accepted connection from "+ client);
					  }
					  if(key.isWritable()) {//8 ��� socket �Ƿ�׼����д���ݡ�
						  SocketChannel client =(SocketChannel) key.channel();
						  ByteBuffer buffer = (ByteBuffer)key.attachment();
						  while(buffer.hasRemaining()) {
							  if(client.write(buffer) == 0) {//9������д�뵽�����ӵĿͻ��ˡ�������籥�ͣ������ǿ�д�ģ���ô���ѭ����д�����ݣ�ֱ���û������ǿյġ�
								  break;
							  }
						  }
						  client.close();//10 �ر����ӡ�
					  }
				} catch (Exception e) {
					try {
						key.cancel();
						key.channel().close();
					} catch (Exception e1) {}
				}
	    	  }
	    	  
	      }
	}
}
