package com.bp389.cranaz;

public class MathUtil {
	/**
	 * 
	 * @param x Le nombre x
	 * @param y Le nombre y
	 * @return Le multiple de y juste supérieur à x
	 */
	public static double math_supMultiplier(double x, double y){
		return Math.ceil((x / y)) * y;
	}
	public static double hypothenuse(double x, double y){
		double a = (x*x) + (y*y);
		return Math.floor(Math.sqrt(a));
	}
}
