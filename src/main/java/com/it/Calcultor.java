package com.it;

public class Calcultor {

	private static Double cal(String expression ,double sum) {
		if (expression.indexOf('+') != -1) {
			String substring = expression.substring(0, expression.indexOf('+'));
			sum = sum + Double.valueOf(substring);
			cal(expression.substring(expression.indexOf('+')+1), sum);

		} else if (expression.indexOf('-') != -1) {
			String substring = expression.substring(0, expression.indexOf('-'));
			sum = sum - Double.valueOf(substring);
			cal(expression.substring(expression.indexOf('-')+1), sum);
		} else if (expression.indexOf('*') != -1) {
			String substring = expression.substring(0, expression.indexOf('*'));
			sum = sum * Double.valueOf(substring);
			cal(expression.substring(expression.indexOf('*')+1), sum);
		} else if (expression.indexOf('/') != -1) {
			String substring = expression.substring(0, expression.indexOf('/'));
			sum = sum / Double.valueOf(substring);
			cal(expression.substring(expression.indexOf('/')+1), sum);
		}
		return sum;
	}
	
	
	public static Double cal(String expression ) {
		return cal( expression , 0) ;
	}
	
	public static void main(String[] args) {
		String  str = "1+1+1";
		Calcultor.cal(str, 0);
	}
}
