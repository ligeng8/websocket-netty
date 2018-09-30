package com.it.bio;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ServerNormal {

	private static Integer DEFAULT_PORT = 1234;

	private static ServerSocket serverSocket;

	private static ExecutorService executorService = Executors.newFixedThreadPool(80);
	
	
	public static void main(String[] args) {
		ServerNormal.start();
	}
	public static void start() {
		start(DEFAULT_PORT);
	}

	public synchronized static void start(int port)  {

		if (serverSocket != null) {
			return;
		}

		try {
			serverSocket = new ServerSocket(port);

			while (true) {
				Socket socket = serverSocket.accept();
				executorService.execute(new SocketHandler(socket));
			}
		} catch (Exception e) {
		} finally {
			if(serverSocket != null) {
				try {
					System.out.println("server close");
					serverSocket.close();
					serverSocket = null;
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}

	}

}
