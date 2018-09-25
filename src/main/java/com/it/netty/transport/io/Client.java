package com.it.netty.transport.io;

import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.net.UnknownHostException;

public class Client {

	public static void main(String[] args) throws UnknownHostException, IOException {
		String host = null;
		int port = 0;
		Socket socket = new Socket(host, port);
		InputStream inputStream = socket.getInputStream();
	}
}
