package com.bp389.cranaz.ia.entities;

import java.util.Collections;
import java.util.List;

import net.minecraft.server.v1_7_R3.DistanceComparator;
import net.minecraft.server.v1_7_R3.EntityCreature;
import net.minecraft.server.v1_7_R3.EntityHuman;
import net.minecraft.server.v1_7_R3.EntityLiving;
import net.minecraft.server.v1_7_R3.IEntitySelector;
import net.minecraft.server.v1_7_R3.Items;
import net.minecraft.server.v1_7_R3.PathfinderGoalNearestAttackableTarget;

import org.bukkit.Location;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * Pathfinder définissant les cibles humaines du zombie
 * @author BlackPhantom
 *
 */
public class EnhancedZombiePathfinderGoal extends PathfinderGoalNearestAttackableTarget {
	//Constantes explicitesS
	public static final int SNEAK = 6, WALK = 15, SPRINT = 30;
	private Class<EntityHuman> a;
	@SuppressWarnings("unused")
	private JavaPlugin pl;
	private DistanceComparator e;
	private int b;
	private EntityLiving target;
	private IEntitySelector f;
	public EnhancedZombiePathfinderGoal(EntityCreature arg0, Class<EntityHuman> arg1, int arg2, boolean arg3, JavaPlugin jp) {
		super(arg0, arg1, arg2, arg3);
		this.a = arg1;
		this.b = arg2;
		this.e = new DistanceComparator(arg0);
		this.pl = jp;
		f = null;
	}
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public boolean a() {
		if ((this.b > 0) && (this.c.aH().nextInt(this.b) != 0)) {
			return false;
		}
		double d0 = this.f();
		List list = this.c.world.a(this.a, this.c.boundingBox.grow(d0, 4.0D, d0), this.f);

		Collections.sort(list, this.e);
		if (list.isEmpty()) {
			return false;
		} else {
			this.target = (EntityLiving)list.get(0);
			if(this.target instanceof EntityHuman)
			{
				EntityHuman h = (EntityHuman)this.target;
				/*if(ReviveHandler.isHemoragic((CraftPlayer)h.getBukkitEntity()))
					return false;*/
				Location locH = target.getBukkitEntity().getLocation(), locZ = this.c.getBukkitEntity().getLocation();
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
					this.c.setGoalTarget(h);
				}
				else if(!h.isSneaking() && (distance <= WALK)){
					this.c.setGoalTarget(h);

				}
				else if(h.isSprinting() && (distance <= SPRINT)){
					this.c.setGoalTarget(h);
				}
			}
		}
		return false;
	}
}
