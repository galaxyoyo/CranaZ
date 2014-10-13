package com.bp389.cranaz.ia.entities;

import java.lang.reflect.Field;

import net.minecraft.server.v1_7_R3.Blocks;
import net.minecraft.server.v1_7_R3.EntityHuman;
import net.minecraft.server.v1_7_R3.EntityWolf;
import net.minecraft.server.v1_7_R3.EntityZombie;
import net.minecraft.server.v1_7_R3.EnumDifficulty;
import net.minecraft.server.v1_7_R3.GenericAttributes;
import net.minecraft.server.v1_7_R3.ItemStack;
import net.minecraft.server.v1_7_R3.Items;
import net.minecraft.server.v1_7_R3.PathfinderGoalFloat;
import net.minecraft.server.v1_7_R3.PathfinderGoalLookAtPlayer;
import net.minecraft.server.v1_7_R3.PathfinderGoalMeleeAttack;
import net.minecraft.server.v1_7_R3.PathfinderGoalMoveThroughVillage;
import net.minecraft.server.v1_7_R3.PathfinderGoalMoveTowardsRestriction;
import net.minecraft.server.v1_7_R3.PathfinderGoalNearestAttackableTarget;
import net.minecraft.server.v1_7_R3.PathfinderGoalOpenDoor;
import net.minecraft.server.v1_7_R3.PathfinderGoalRandomLookaround;
import net.minecraft.server.v1_7_R3.PathfinderGoalRandomStroll;
import net.minecraft.server.v1_7_R3.PathfinderGoalSelector;
import net.minecraft.server.v1_7_R3.World;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_7_R3.inventory.CraftItemStack;
import org.bukkit.craftbukkit.v1_7_R3.util.UnsafeList;
import org.bukkit.entity.Player;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.plugin.java.JavaPlugin;

import com.bp389.cranaz.ia.VirtualSpawner;

/**
 * Classe représentant un zombie modifié
 * @author BlackPhantom
 *
 */
public class EnhancedZombie extends EntityZombie{
	private static JavaPlugin plugin;
	//True si c'est un zombie joueur
	public boolean ipf = false;
	//Constantes explicites
	public static final double SPEED = 0.23000000417232513D * 1.6D, FOLLOW_RANGE = 60D, MOVE_SPEED = SPEED * 1.75D, ATTACK_DAMAGE = 3.0D;
	public static void initPlugin(JavaPlugin jp){plugin = jp;}
	public EnhancedZombie(World world) {
		super(world);
		try {
			Field bField = PathfinderGoalSelector.class.getDeclaredField("b");
			bField.setAccessible(true);
			Field cField = PathfinderGoalSelector.class.getDeclaredField("c");
			cField.setAccessible(true);
			bField.set(goalSelector, new UnsafeList<PathfinderGoalSelector>());
			bField.set(targetSelector, new UnsafeList<PathfinderGoalSelector>());
			cField.set(goalSelector, new UnsafeList<PathfinderGoalSelector>());
			cField.set(targetSelector, new UnsafeList<PathfinderGoalSelector>());
		} catch (Exception exc) {
			exc.printStackTrace();
		}
		this.goalSelector.a(0, new PathfinderGoalFloat(this));
		this.goalSelector.a(4, new PathfinderGoalMeleeAttack(this, EntityWolf.class, 1.0D, true));
		this.goalSelector.a(2, new PathfinderGoalMeleeAttack(this, EntityHuman.class, 1.0D, false));
		this.goalSelector.a(5, new PathfinderGoalMoveTowardsRestriction(this, 1.0D));
		this.goalSelector.a(6, new PathfinderGoalMoveThroughVillage(this, 1.0D, false));
		this.goalSelector.a(7, new PathfinderGoalRandomStroll(this, 1.0D));
		this.goalSelector.a(8, new PathfinderGoalLookAtPlayer(this, EntityHuman.class, 8.0F));
		this.goalSelector.a(8, new PathfinderGoalRandomLookaround(this));
		this.goalSelector.a(8, new PathfinderGoalOpenDoor(this, true));
		this.targetSelector.a(2, new PathfinderGoalNearestAttackableTarget(this, EntityWolf.class, 0, true));
		this.targetSelector.a(2, new EnhancedZombiePathfinderGoal(this, EntityHuman.class, 0, true, plugin));
	}
	@Override
	protected void aC() {
		super.aC();
		this.getAttributeInstance(GenericAttributes.b).setValue(FOLLOW_RANGE);
		this.getAttributeInstance(GenericAttributes.d).setValue(SPEED);
		final int tmp = this.random.nextInt(30);
		this.getAttributeInstance(GenericAttributes.a).setValue(Integer.valueOf(tmp < 20 ? 20 : tmp).doubleValue());
	}
	@Override
	protected void getRareDrop(int i) {
		switch (this.random.nextInt(5)) {
		case 0:
			this.a(Items.IRON_INGOT, 1);
			break;
		case 1:
			this.a(Items.CARROT, 1);
			break;
		case 2:
			this.a(Items.POTATO, 1);
			break;
		case 3:
			this.a(Items.REDSTONE, 1);
			break;
		case 4:
			this.a(Items.SUGAR, 1);
		}
	}
	/**
	 * 
	 * @return La position exacte bukkit
	 */
	public Location getBukkitLocation(){
		return new Location(this.world.getWorld(), locX, locY, locZ, yaw, pitch);
	}
	@Override
	public boolean canSpawn() 
	{
		VirtualSpawner vs = VirtualSpawner.getNearbySpawner(getBukkitLocation(), 45);
		final int temp = this.random.nextInt(2);
		if(vs == null && this.random.nextInt(10) == 0){
			switch(temp){
			case 0:
				setVillager(true);
			case 1:
				setVillager(false);
			}
			return super.canSpawn();
		}
		else if(vs != null && vs.isRunning() && !vs.isReloading()){
			switch(temp){
			case 0:
				setVillager(true);
			case 1:
				setVillager(false);
			}
			return this.world.difficulty != EnumDifficulty.PEACEFUL;
		}
		return false;
	}
	/*
	 * 0 = weapon
	 * 1 = Bottes
	 * 2 = Pantalon
	 * 3 = Plastron
	 * 4 = Casque
	 */
	/**
	 * Définit l'équipement du zombie par rapport à ceux d'un joueur
	 * Appelée lors du spawn d'un zombie joueur (après la mort dudit joueur)
	 * @param from Le joueur source
	 */
	public void setPlayerEquipment(final Player from){
		final PlayerInventory i = from.getInventory();
		final org.bukkit.inventory.ItemStack helmet = i.getHelmet(), chestplate = i.getChestplate(), pants = i.getLeggings(), boots = i.getBoots();
		if(helmet != null && helmet.getType() != Material.AIR){
			this.setEquipment(4, CraftItemStack.asNMSCopy(helmet));
		}
		if(chestplate != null && chestplate.getType() != Material.AIR){
			this.setEquipment(3, CraftItemStack.asNMSCopy(chestplate));
		}
		if(pants != null && pants.getType() != Material.AIR){
			this.setEquipment(2, CraftItemStack.asNMSCopy(pants));
		}
		if(boots != null && boots.getType() != Material.AIR){
			this.setEquipment(1, CraftItemStack.asNMSCopy(boots));
		}
		if(from.getItemInHand() != null && from.getItemInHand().getType() != Material.AIR){
			this.setEquipment(0, CraftItemStack.asNMSCopy(from.getItemInHand()));
		}
		this.setCustomName(from.getName());
		this.setCustomNameVisible(true);
		this.ipf = true;
	}
	@Override
	protected void bC() {
		super.bC();
		if (this.random.nextFloat() < (this.world.difficulty == EnumDifficulty.HARD ? 0.05F : 0.01F)) {
			final int i = this.random.nextInt(3);
			switch(i){
			case 0:
				this.setEquipment(0, new ItemStack(Items.STONE_SWORD));
				break;
			case 1:
				this.setEquipment(0, new ItemStack(Blocks.TORCH));
				break;
			}
		}
	}
	public void move(Location loc){
		this.move(loc, MOVE_SPEED);
	}
	public void move(Location loc, double speed){
		this.getNavigation().a(loc.getX(), loc.getY(), loc.getZ(), speed);
	}
	@Override
	public void setBaby(boolean arg0) {}
	@Override
	public boolean isBaby() {return false;}
	@Override
	public void setOnFire(int i) {}
	@Override
	public int getExpReward() {
		return 0;
	}
}
