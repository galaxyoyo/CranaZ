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
	//Constantes explicitesS
	public static final int SNEAK = 6, WALK = 15, SPRINT = 30;
	@SuppressWarnings("unused")
	private JavaPlugin pl;
	private EntityLiving target;
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
		double d0 = this.f();
		List list = this.e.world.a(this.a, this.e.getBoundingBox().grow(d0, 4.0D, d0),
				Predicates.and(this.c, IEntitySelector.d));
		Collections.sort(list, this.b);
		if (list.isEmpty()) {
			return false;
		} else {
			this.target = (EntityLiving)list.get(0);
			if(this.target instanceof EntityHuman)
			{
				EntityHuman h = (EntityHuman)this.target;
				/*if(ReviveHandler.isHemoragic((CraftPlayer)h.getBukkitEntity()))
					return false;*/
				Location locH = target.getBukkitEntity().getLocation(), locZ = this.e.getBukkitEntity().getLocation();
				int distance = Double.valueOf(locH.distance(locZ)).intValue();
				int camoVal = 0;
				if(target.getEquipment(3) != null){
					if(target.getEquipment(3).getItem() == Items.CHAINMAIL_CHESTPLATE)
						camoVal += 15;
				}
				if(target.getEquipment(1) != null){
					if(target.getEquipment(1).getItem() == Items.CHAINMAIL_BOOTS)
						camoVal += 20;
				}
				if(target.getEquipment(2) != null){
					if(target.getEquipment(2).getItem() == Items.CHAINMAIL_LEGGINGS)
						camoVal += 20;
				}
				if(target.getEquipment(4) != null){
					if(target.getEquipment(4).getItem() == Items.CHAINMAIL_HELMET)
						camoVal += 5;
				}
				if(camoVal != 0){
					int i = (distance / 100) * camoVal;
					distance += i;
				}
				if(distance <= SNEAK){
					this.e.setGoalTarget(this.d, EntityTargetEvent.TargetReason.CLOSEST_PLAYER, true);
				}
				else if(!h.isSneaking() && (distance <= WALK)){
					this.e.setGoalTarget(this.d, EntityTargetEvent.TargetReason.CLOSEST_PLAYER, true);
				}
				else if(h.isSprinting() && (distance <= SPRINT)){
					this.e.setGoalTarget(this.d, EntityTargetEvent.TargetReason.CLOSEST_PLAYER, true);
				}
			}
		}
		return false;
	}
}
