package com.bp389.cranaz.ia.entities;

import java.util.Collections;
import java.util.List;

import net.minecraft.server.v1_8_R1.EntityCreature;
import net.minecraft.server.v1_8_R1.EntityHuman;
import net.minecraft.server.v1_8_R1.EntityLiving;
import net.minecraft.server.v1_8_R1.IEntitySelector;
import net.minecraft.server.v1_8_R1.Items;
import net.minecraft.server.v1_8_R1.PathfinderGoalNearestAttackableTarget;

import org.bukkit.Location;
import org.bukkit.event.entity.EntityTargetEvent;
import org.bukkit.plugin.java.JavaPlugin;

import com.google.common.base.Predicate;
import com.google.common.base.Predicates;

/**
 * Pathfinder définissant les cibles humaines du zombie
 * @author BlackPhantom
 *
 */
public class EnhancedZombiePathfinderGoal extends PathfinderGoalNearestAttackableTarget {
	//Constantes explicites
	public static final int SNEAK = 6, WALK = 15, SPRINT = 25;
	@SuppressWarnings("unused")
	private JavaPlugin pl;
	@SuppressWarnings("rawtypes")
	public EnhancedZombiePathfinderGoal(EntityCreature entitycreature, Class oclass, boolean flag)
	{
		this(entitycreature, oclass, flag, false);
	}

	@SuppressWarnings("rawtypes")
	public EnhancedZombiePathfinderGoal(EntityCreature entitycreature, Class oclass, boolean flag, boolean flag1)
	{
		this(entitycreature, oclass, 10, flag, flag1, (Predicate)null);
	}

	@SuppressWarnings("rawtypes")
	public EnhancedZombiePathfinderGoal(EntityCreature entitycreature, Class oclass, int i, boolean flag, boolean flag1, Predicate predicate)
	{
		super(entitycreature, oclass, i, flag, flag1, predicate);
	}
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public boolean a() {
		final double d0 = this.f();
		List list = this.e.world.a(this.a, this.e.getBoundingBox().grow(d0, 4.0D, d0),
				Predicates.and(this.c, IEntitySelector.d));
		Collections.sort(list, this.b);
		if (list.isEmpty()) {
			return false;
		} else {
			this.d = (EntityLiving)list.get(0);
			if(this.d instanceof EntityHuman)
			{
				EntityHuman h = (EntityHuman)this.d;
				if(!this.e.hasLineOfSight(h))
					return false;
				/*if(ReviveHandler.isHemoragic((CraftPlayer)h.getBukkitEntity()))
					return false;*/
				Location locH = d.getBukkitEntity().getLocation(), locZ = this.e.getBukkitEntity().getLocation();
				int distance = Double.valueOf(locH.distance(locZ)).intValue();
				int camoVal = 0;
				if(d.getEquipment(3) != null){
					if(d.getEquipment(3).getItem() == Items.CHAINMAIL_CHESTPLATE)
						camoVal += 15;
				}
				if(d.getEquipment(1) != null){
					if(d.getEquipment(1).getItem() == Items.CHAINMAIL_BOOTS)
						camoVal += 20;
				}
				if(d.getEquipment(2) != null){
					if(d.getEquipment(2).getItem() == Items.CHAINMAIL_LEGGINGS)
						camoVal += 20;
				}
				if(d.getEquipment(4) != null){
					if(d.getEquipment(4).getItem() == Items.CHAINMAIL_HELMET)
						camoVal += 5;
				}
				if(camoVal != 0){
					int i = (distance / 100) * camoVal;
					distance += i;
				}
				if(distance <= SNEAK){
					target();
				}
				else if(!h.isSneaking() && (distance <= WALK)){
					target();
				}
				else if(h.isSprinting() && (distance <= SPRINT)){
					target();
				}
			}
		}
		return false;
	}
	public void target(){
		this.e.setGoalTarget(this.d, EntityTargetEvent.TargetReason.CLOSEST_PLAYER, true);
	}
}
