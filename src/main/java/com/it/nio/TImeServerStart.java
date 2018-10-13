package com.it.nio;

public class TImeServerStart {
	public static void main(String[] args) {
         MultiexerTimeServer multiexerTimeServer = new  MultiexerTimeServer(9999);
         new Thread(multiexerTimeServer).start();
         try {
			Thread.sleep(20000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
         multiexerTimeServer.publishAll();
         System.out.println("server end ");
	}
}
