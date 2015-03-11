package com.bp389.cranaz.ia.entities;

import java.util.Collections;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.event.entity.EntityTargetEvent;
import org.bukkit.plugin.java.JavaPlugin;

import net.minecraft.server.v1_8_R1.EntityCreature;
import net.minecraft.server.v1_8_R1.EntityHuman;
import net.minecraft.server.v1_8_R1.EntityLiving;
import net.minecraft.server.v1_8_R1.IEntitySelector;
import net.minecraft.server.v1_8_R1.Items;
import net.minecraft.server.v1_8_R1.PathfinderGoalNearestAttackableTarget;

import com.bp389.cranaz.Util;
import com.bp389.cranaz.ia.ZIA;
import com.google.common.base.Predicate;
import com.google.common.base.Predicates;

/**
 * Pathfinder d�finissant les cibles humaines du zombie
 * 
 * @author BlackPhantom
 * 
 */
public class EnhancedZombiePathfinderGoal extends PathfinderGoalNearestAttackableTarget {

	@SuppressWarnings("unused")
	private JavaPlugin pl;
	private static boolean init = false, vision;
	private static int SNEAK, WALK, SPRINT;

	@SuppressWarnings("rawtypes")
	public EnhancedZombiePathfinderGoal(final EntityCreature entitycreature, final Class oclass, final boolean flag) {
		this(entitycreature, oclass, flag, false);
	}

	@SuppressWarnings("rawtypes")
	public EnhancedZombiePathfinderGoal(final EntityCreature entitycreature, final Class oclass, final boolean flag, final boolean flag1) {
		this(entitycreature, oclass, 10, flag, flag1, (Predicate) null);
	}

	@SuppressWarnings("rawtypes")
	public EnhancedZombiePathfinderGoal(final EntityCreature entitycreature, final Class oclass, final int i, final boolean flag, final boolean flag1,
	        final Predicate predicate) {
		super(entitycreature, oclass, i, flag, flag1, predicate);
		if(!init){
			init = true;
			try{
				SNEAK = (int)Util.getFromYaml(ZIA.ia_config, "zombies.detection.sneak", 6);
				WALK = (int)Util.getFromYaml(ZIA.ia_config, "zombies.detection.marche", 15);
				SPRINT = (int)Util.getFromYaml(ZIA.ia_config, "zombies.detection.sprint", 25);
				vision = (boolean)Util.getFromYaml(ZIA.ia_config, "zombies.detection.vision_necessaire", true);
			}catch(ClassCastException e){
				SNEAK = 6;
				WALK = 15;
				SPRINT = 25;
				vision = true;
			}
		}
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public boolean a() {
		final double d0 = this.f();
		final List list = this.e.world.a(this.a, this.e.getBoundingBox().grow(d0, 4.0D, d0), Predicates.and(this.c, IEntitySelector.d));
		Collections.sort(list, this.b);
		if(list.isEmpty())
			return false;
		else {
			this.d = (EntityLiving) list.get(0);
			if(this.d instanceof EntityHuman) {
				final EntityHuman h = (EntityHuman) this.d;
				if(!this.e.hasLineOfSight(h) && vision)
					return false;
				/*
				 * if(ReviveHandler.isHemoragic((CraftPlayer)h.getBukkitEntity())
				 * ) return false;
				 */
				final Location locH = this.d.getBukkitEntity().getLocation(), locZ = this.e.getBukkitEntity().getLocation();
				int distance = Double.valueOf(locH.distance(locZ)).intValue();
				int camoVal = 0;
				if(this.d.getEquipment(3) != null)
					if(this.d.getEquipment(3).getItem() == Items.CHAINMAIL_CHESTPLATE)
						camoVal += 15;
				if(this.d.getEquipment(1) != null)
					if(this.d.getEquipment(1).getItem() == Items.CHAINMAIL_BOOTS)
						camoVal += 25;
				if(this.d.getEquipment(2) != null)
					if(this.d.getEquipment(2).getItem() == Items.CHAINMAIL_LEGGINGS)
						camoVal += 25;
				if(this.d.getEquipment(4) != null)
					if(this.d.getEquipment(4).getItem() == Items.CHAINMAIL_HELMET)
						camoVal += 10;
				if(camoVal != 0) {
					final int i = distance / 100 * camoVal;
					distance += i;
				}
				if(distance <= SNEAK)
					this.target();
				else if(!h.isSneaking() && distance <= WALK)
					this.target();
				else if(h.isSprinting() && distance <= SPRINT)
					this.target();
			}
		}
		return false;
	}

	public void target() {
		this.e.setGoalTarget(this.d, EntityTargetEvent.TargetReason.CLOSEST_PLAYER, true);
	}
}