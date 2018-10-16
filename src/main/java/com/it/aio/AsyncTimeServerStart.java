package com.it.aio;

public class AsyncTimeServerStart {
	public static void main(String[] args) {
       AsyncTimeServer asyncTimeServer = new AsyncTimeServer(9999);
       new Thread(asyncTimeServer).start();
	}
}
