package com.it.nio;

public class TimeClientStart {

	public static void main(String[] args) {
		TimeClient timeClient = new TimeClient("127.0.0.1", 9999);
		
		new Thread(timeClient).start();
		
	}
}
