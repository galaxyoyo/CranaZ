package com.bp389.cranaz;

import org.bukkit.Location;

public final class MathUtil {
	public static final int NORTH = 0, SOUTH = 1, WEST = 2, EAST = 3;
	/**
	 * 
	 * @param x Le nombre x
	 * @param y Le nombre y
	 * @return Le multiple de y juste supérieur à x
	 */
	public static double math_supMultiplier(double x, double y){
		return Math.ceil((x / y)) * y;
	}
	public static boolean between(double number, double a, double b){
		return number < Math.max(a, b) && number > Math.min(a, b);
	}
	public static boolean approxEquals(double a, double b, double approx){
		return (Math.max(a, b) - Math.min(a, b)) <= approx;
	}
	/**
	 * 
	 * @param from L'angle en radian
	 * @return La valeur de l'angle convertie en degrés
	 */
	public static double radianToDegrees(double from){
		return 180 * from / Math.PI;
	}
	public static double degreesToRadians(double from){
		return from * Math.PI / 180;
	}
	public static double getIsocelLength(double hypothenuse){
		return Math.sqrt((hypothenuse * hypothenuse) / 2);
	}
	public static boolean locationEquals(final Location a, final Location b){
		return approxEquals(a.getX(), b.getX(), 1.5) &&
				approxEquals(a.getY(), b.getY(), 1.5) &&
				approxEquals(a.getZ(), b.getZ(), 1.5);
	}
}
