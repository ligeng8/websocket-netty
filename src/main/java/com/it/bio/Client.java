package com.it.bio;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
//https://github.com/ityouknow/spring-examples
public class Client {

	private static int DEFAULT_SERVER_PORT = 1234;

	private static String DEFAULT_SERVER_IP = "127.0.0.1";

	private static Socket socket = null;
	public static void send(String expression) {
		send(DEFAULT_SERVER_PORT, expression);
	}

	public static void send(int port, String expression) {
		System.out.println("算数表达式：" + expression);
		BufferedReader in = null;
		PrintWriter out = null;
		try {
			if(socket == null)
			socket = new Socket(DEFAULT_SERVER_IP, DEFAULT_SERVER_PORT);
			in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			out = new PrintWriter(socket.getOutputStream());
			out.println(expression+"\n");
			while (true) {
				System.out.println("____结果为：" + in.readLine());
			}
		} catch (Exception e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			if(in != null) {
				try {
					in.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
			if(out != null) {
				out.flush();
				out.close();
			}
			
			if(socket != null) {
				
				try {
					socket.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
			
			
		}
	}

}
