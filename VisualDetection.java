package com.bp389.cranaz.ia;

import net.minecraft.server.v1_8_R1.Entity;
import net.minecraft.server.v1_8_R1.EntityLiving;

import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_8_R1.entity.CraftEntity;
import org.bukkit.craftbukkit.v1_8_R1.entity.CraftLivingEntity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.util.Vector;

import com.bp389.cranaz.MathUtil;

public final class VisualDetection {
	/*
	 * FOV = Field Of View, le champ de vision des entités, en degrés
	 * DISTANCE = La distance maximale de vue des entités, en distances
	 */
	public static final int FOV = 90, DISTANCE = 30;
	/**
	 * 
	 * @param from L'entité source, sous forme Bukkit
	 * @param target L'entité à vérifier (cible), sous forme Bukkit
	 * @return True si l'entité cible est dans le champ de vision, false sinon
	 * @see #canSee(LivingEntity, org.bukkit.entity.Entity, int)
	 */
	public static boolean canSee(LivingEntity from, org.bukkit.entity.Entity target){
		return canSee(from, target, FOV, DISTANCE);
	}
	/**
	 * 
	 * @param from L'entité source, sous forme NMS
	 * @param target L'entité à vérifier (cible), sous forme NMS
	 * @return True si l'entité cible est dans le champ de vision, false sinon
	 * @see #canSee(LivingEntity, org.bukkit.entity.Entity, int)
	 */
	public static boolean canSee(EntityLiving from, Entity target){
		return canSee(from, target, FOV, DISTANCE);
	}
	/**
	 * 
	 * @param from L'entité source sous forme Bukkit
	 * @param target L'entité à vérifier (cible), sous forme Bukkit
	 * @param fov Le champ de vision, en degrés
	 * @param distance La distance en blocs
	 * @return True si l'entité cible est dans le champ de vision, false sinon
	 */
	public static boolean canSee(LivingEntity from, org.bukkit.entity.Entity target, int fov, int distance){
		return canSee(((CraftLivingEntity)from).getHandle(), ((CraftEntity)target).getHandle(), fov, distance);
	}
	/**
	 * 
	 * @param from L'entité source sous forme NMS
	 * @param target L'entité à vérifier (cible), sous forme NMS
	 * @param fov Le champ de vision, en degrés
	 * @param distance La distance en blocs
	 * @return True si l'entité cible est dans le champ de vision, false sinon
	 */
	public static boolean canSee(EntityLiving from, Entity target, int fov, int distance){
		Location l = from.getBukkitEntity().getLocation(), l0 = target.getBukkitEntity().getLocation(), l1;
		double dist = l.distance(l0);
		Vector v = l.getDirection();
		v.multiply(dist);
		if(!MathUtil.locationEquals(l.add(v), l0))
			return false;
		
		return true;
	}
}
