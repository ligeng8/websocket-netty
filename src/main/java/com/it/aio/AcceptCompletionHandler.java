package com.it.aio;

import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousServerSocketChannel;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;

public class AcceptCompletionHandler implements CompletionHandler<AsynchronousSocketChannel, AsyncTimeServer> {

	public void completed(AsynchronousSocketChannel result, AsyncTimeServer attachment) {
		// TODO Auto-generated method stub
		attachment.getAsynchronousServerSocketChannel().accept(attachment, this);
		ByteBuffer readBuffer = ByteBuffer.allocate(1024);
		result.read(readBuffer, readBuffer, new ReadCompletionHandler(result));
	}

	public void failed(Throwable exc, AsyncTimeServer attachment) {
		// TODO Auto-generated method stub
		exc.printStackTrace();
		attachment.getLatch().countDown();
	}



}
