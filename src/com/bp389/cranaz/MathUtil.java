package com.bp389.cranaz;

import java.util.ArrayList;

import org.bukkit.Location;

import net.minecraft.server.v1_8_R1.Entity;

/**
 * Classe contenant des utilitaires mathémathiques
 * 
 * @author BlackPhantom
 * 
 */
public final class MathUtil {

	public static final int NORTH = 0, SOUTH = 1, WEST = 2, EAST = 3;

	/**
	 * 
	 * @param x
	 *            Le nombre x
	 * @param y
	 *            Le nombre y
	 * @return Le multiple de y juste supérieur à x
	 */
	public static double math_supMultiplier(final double x, final double y) {
		return Math.ceil(x / y) * y;
	}

	public static boolean between(final double number, final double a, final double b) {
		return number < Math.max(a, b) && number > Math.min(a, b);
	}

	public static boolean approxEquals(final double a, final double b, final double approx) {
		return Math.max(a, b) - Math.min(a, b) <= approx;
	}

	/**
	 * 
	 * @param from
	 *            L'angle en radian
	 * @return La valeur de l'angle convertie en degrés
	 */
	public static double radianToDegrees(final double from) {
		return 180 * from / Math.PI;
	}

	public static double degreesToRadians(final double from) {
		return from * Math.PI / 180;
	}

	public static double getIsocelLength(final double hypothenuse) {
		return Math.sqrt(hypothenuse * hypothenuse / 2);
	}

	public static boolean locationEquals(final Location a, final Location b) {
		return MathUtil.approxEquals(a.getX(), b.getX(), 1.5) && MathUtil.approxEquals(a.getY(), b.getY(), 1.5)
		        && MathUtil.approxEquals(a.getZ(), b.getZ(), 1.5);
	}
	@SuppressWarnings("unchecked")
    public static ArrayList<Entity> get_NMS_optimizedEntities(Entity e, double x, double y, double z){
		return (ArrayList<Entity>)e.world.getEntities(e, e.getBoundingBox().grow(x, y, z));
	}
}
