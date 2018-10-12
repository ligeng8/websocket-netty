package com.it.nio;

public class TImeServerStart {
	public static void main(String[] args) {
         MultiexerTimeServer multiexerTimeServer = new  MultiexerTimeServer(9999);
         new Thread(multiexerTimeServer).start();
         
	}
}
