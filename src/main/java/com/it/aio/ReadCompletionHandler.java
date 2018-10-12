package com.it.aio;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;
import java.util.Date;

import com.sun.xml.internal.ws.util.StringUtils;

import io.netty.util.CharsetUtil;
import io.netty.util.internal.StringUtil;

public class ReadCompletionHandler implements CompletionHandler<Integer, ByteBuffer> {

	private AsynchronousSocketChannel asc;

	public ReadCompletionHandler(AsynchronousSocketChannel asc) {
		super();
		this.asc = asc;
	}

	public void completed(Integer result, ByteBuffer attachment) {
		// TODO Auto-generated method stub
		attachment.flip();
		byte[] bytes = new byte[attachment.remaining()];
		attachment.get(bytes);
		String body = new String(bytes,CharsetUtil.UTF_8);
		System.out.println("The TimeServer receive order : "+body);
		String currentTime = body.equals("QUERY TIME ORDER")? new Date().toLocaleString() : "BAD ORDER";
		doWrite( body);
	}
	
	public void doWrite(String message) {
		if(message != null && !"".equals(message.trim())) {
			byte[] bytes = message.getBytes();
			ByteBuffer writeBuffer = ByteBuffer.allocate(bytes.length);
			writeBuffer.put(bytes);
			writeBuffer.flip();
			this.asc.write(writeBuffer, writeBuffer, new CompletionHandler<Integer, ByteBuffer>() {

				public void completed(Integer result, ByteBuffer attachment) {
					// TODO Auto-generated method stub
					if(attachment.hasRemaining()) {
						asc.write(attachment,attachment,this);
					}
				}

				public void failed(Throwable exc, ByteBuffer attachment) {
					// TODO Auto-generated method stub
					try {
						asc.close();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			});
			
		}
	}

	public void failed(Throwable exc, ByteBuffer attachment) {
		// TODO Auto-generated method stub
		try {
			asc.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
