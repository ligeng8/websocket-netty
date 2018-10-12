package com.it.aio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.AsynchronousServerSocketChannel;
import java.util.concurrent.CountDownLatch;

public class AsyncTimeServer implements Runnable {

	private int port;

	private CountDownLatch latch = new CountDownLatch(1);

	private AsynchronousServerSocketChannel asynchronousServerSocketChannel;

	public AsyncTimeServer(int port) {
		super();
		this.port = port;
		try {
			asynchronousServerSocketChannel = AsynchronousServerSocketChannel.open();
			asynchronousServerSocketChannel.bind(new InetSocketAddress(port));
			System.out.println("The timeServer is starting in " + port);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	public void run() {
		// TODO Auto-generated method stub
		try {
			doAccept();
			latch.await();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void doAccept() {
		asynchronousServerSocketChannel.accept(this, new AcceptCompletionHandler());
	}

	public CountDownLatch getLatch() {
		return latch;
	}

	public void setLatch(CountDownLatch latch) {
		this.latch = latch;
	}

	public AsynchronousServerSocketChannel getAsynchronousServerSocketChannel() {
		return asynchronousServerSocketChannel;
	}

	public void setAsynchronousServerSocketChannel(AsynchronousServerSocketChannel asynchronousServerSocketChannel) {
		this.asynchronousServerSocketChannel = asynchronousServerSocketChannel;
	}

}
