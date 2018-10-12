package com.it.bio;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import com.it.Calcultor;

public class SocketHandler implements Runnable {

	private Socket socket;

	public SocketHandler(Socket socket) {
		super();
		this.socket = socket;
	}

	public void run() {
		// TODO Auto-generated method stub
		BufferedReader in = null;
		PrintWriter out = null;
		try {
			in = new BufferedReader(new InputStreamReader(this.socket.getInputStream()));
			out = new PrintWriter(this.socket.getOutputStream());
			String message;
			String result;

			while (true) {
				message = in.readLine();
				if (message == null) {
					continue;
				}
				System.out.println("server receiver message :" + message);
				try {
					result = Calcultor.cal(message).toString();
				} catch (Exception e) {
					result = "º∆À„¥ÌŒÛ" + e.getMessage();
				}
				out.println(result);
			}

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {

			if (in != null) {
				try {
					in.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

			if (out != null) {
				out.flush();
				out.close();
			}

			if (socket != null) {
				try {
					this.socket.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

		}

	}

}
