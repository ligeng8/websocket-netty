package com.it.bio;

import java.io.IOException;
import java.util.Random;

public class Test { // ����������
	public static void main(String[] args) throws InterruptedException {
		final char operators[] = { '+', '-', '*', '/' };
		final Random random = new Random(System.currentTimeMillis());
		// TODO Auto-generated method stub
		while (true) {
			// ��������������ʽ
			String expression = random.nextInt(10) + "" + operators[random.nextInt(4)] + (random.nextInt(10) + 1);
			Client.send(expression);
//			try {
//				Thread.currentThread().sleep(random.nextInt(1000));
//			} catch (InterruptedException e) {
//				e.printStackTrace();
//			}
		}

	}
}
