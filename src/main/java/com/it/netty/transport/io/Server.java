package com.it.netty.transport.io;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {

	public void server(int port) throws IOException {

		ServerSocket serverSocket = new ServerSocket(port);

		while (true) {
			final Socket socket = serverSocket.accept();
			System.out.println(socket.getRemoteSocketAddress());
			new Thread() {

				@Override
				public void run() {
					OutputStream outputStream = null;
					try {
						
						InputStream inputStream = socket.getInputStream();
						InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
						BufferedReader reader = new BufferedReader(inputStreamReader);
						String str ;
						while((str = reader.readLine()) != null) {
							System.out.println(str);
						}
						outputStream = socket.getOutputStream();
//                                        OutputStreamWriter outputStreamWriter = new OutputStreamWriter(outputStream);
//                                        outputStreamWriter.w
						outputStream.write("HI hello".getBytes());
						outputStream.flush();

					} catch (IOException e) {
					} finally {
						try {
							outputStream.close();
							socket.close();
						} catch (IOException e) {
						}
					}
				}
			};

		}

	}
}
