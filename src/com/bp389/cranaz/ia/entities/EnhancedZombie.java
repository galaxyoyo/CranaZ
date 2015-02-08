package com.bp389.cranaz.ia.entities;

import java.util.ConcurrentModificationException;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_8_R1.inventory.CraftItemStack;
import org.bukkit.entity.Player;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.plugin.java.JavaPlugin;

import net.minecraft.server.v1_8_R1.DifficultyDamageScaler;
import net.minecraft.server.v1_8_R1.EntityHuman;
import net.minecraft.server.v1_8_R1.EntityPigZombie;
import net.minecraft.server.v1_8_R1.EntityZombie;
import net.minecraft.server.v1_8_R1.EnumDifficulty;
import net.minecraft.server.v1_8_R1.GenericAttributes;
import net.minecraft.server.v1_8_R1.ItemStack;
import net.minecraft.server.v1_8_R1.Items;
import net.minecraft.server.v1_8_R1.PathfinderGoalHurtByTarget;
import net.minecraft.server.v1_8_R1.PathfinderGoalOpenDoor;
import net.minecraft.server.v1_8_R1.World;

import com.bp389.cranaz.Util;
import com.bp389.cranaz.ia.VirtualSpawner;
import com.bp389.cranaz.ia.ZIA;

/**
 * Classe représentant un zombie modifié
 * 
 * @author BlackPhantom
 * 
 */
public class EnhancedZombie extends EntityZombie {

	@SuppressWarnings("unused")
	private static JavaPlugin plugin;
	// True si c'est un zombie joueur
	public boolean ipf = false;
	private boolean hasMove = false;
	// Constantes explicites
	public static final double SPEED = 0.23000000417232513D * 1.6D, FOLLOW_RANGE = 60D, MOVE_SPEED = EnhancedZombie.SPEED * 1.75D, ATTACK_DAMAGE = 3.0D;

	public static void initPlugin(final JavaPlugin jp) {
		EnhancedZombie.plugin = jp;
	}

	public EnhancedZombie(final World world) {
		super(world);
	}

	@Override
	protected void n() {
		this.targetSelector.a(1, new PathfinderGoalHurtByTarget(this, true, new Class[] { EntityPigZombie.class }));
		this.targetSelector.a(2, new EnhancedZombiePathfinderGoal(this, EntityHuman.class, true));
		this.goalSelector.a(4, new PathfinderGoalOpenDoor(this, true));
	}

	@Override
	protected void aW() {
		super.aW();
		this.getAttributeInstance(GenericAttributes.b).setValue(EnhancedZombie.FOLLOW_RANGE);
		this.getAttributeInstance(GenericAttributes.d).setValue(EnhancedZombie.SPEED);
		final int tmp = this.random.nextInt(30);
		this.getAttributeInstance(GenericAttributes.maxHealth).setValue(Integer.valueOf(tmp < 20 ? 20 : tmp).doubleValue());
	}

	@Override
	protected void getRareDrop() {
		switch(this.random.nextInt(5)) {
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
	public Location getBukkitLocation() {
		return new Location(this.world.getWorld(), this.locX, this.locY, this.locZ, this.yaw, this.pitch);
	}

	@Override
	public boolean bQ() {
		final VirtualSpawner vs = VirtualSpawner.getNearbySpawner(this.getBukkitLocation(), 80);
		final Integer i = (Integer)Util.getFromYaml(ZIA.ia_config, "zombies.spawn.facteur_attenuation", Integer.valueOf(12));
		final int temp = this.random.nextInt(2);
		if(vs == null && this.random.nextInt(i.intValue()) == 0) {
			switch(temp) {
				case 0:
					this.setVillager(true);
				case 1:
					this.setVillager(false);
			}
			return super.bQ();
		} else if(vs != null && vs.isRunning()) {
			switch(temp) {
				case 0:
					this.setVillager(true);
				case 1:
					this.setVillager(false);
			}
			return this.world.getDifficulty() != EnumDifficulty.PEACEFUL && this.random.nextInt(4) < 3;
		}
		return false;
	}

	public boolean hasMove(){
		return this.hasMove;
	}


	/*
	 * 0 = weapon 1 = Bottes 2 = Pantalon 3 = Plastron 4 = Casque
	 */
	/**
	 * Définit l'équipement du zombie par rapport à ceux d'un joueur Appelée
	 * lors du spawn d'un zombie joueur (après la mort dudit joueur)
	 * 
	 * @param from
	 *            Le joueur source
	 */
	public void setPlayerEquipment(final Player from) {
		final PlayerInventory i = from.getInventory();
		final org.bukkit.inventory.ItemStack helmet = i.getHelmet(), chestplate = i.getChestplate(), pants = i.getLeggings(), boots = i.getBoots();
		if(helmet != null && helmet.getType() != Material.AIR)
			this.setEquipment(4, CraftItemStack.asNMSCopy(helmet));
		if(chestplate != null && chestplate.getType() != Material.AIR)
			this.setEquipment(3, CraftItemStack.asNMSCopy(chestplate));
		if(pants != null && pants.getType() != Material.AIR)
			this.setEquipment(2, CraftItemStack.asNMSCopy(pants));
		if(boots != null && boots.getType() != Material.AIR)
			this.setEquipment(1, CraftItemStack.asNMSCopy(boots));
		if(from.getItemInHand() != null && from.getItemInHand().getType() != Material.AIR)
			this.setEquipment(0, CraftItemStack.asNMSCopy(from.getItemInHand()));
		this.setCustomName(from.getName());
		this.setCustomNameVisible(true);
		this.ipf = true;
	}

	@Override
	protected void a(final DifficultyDamageScaler dds) {
		super.a(dds);
		if(this.random.nextFloat() < (this.world.getDifficulty() == EnumDifficulty.HARD ? 0.05F : 0.01F)) {
			final int i = this.random.nextInt(3);
			if(i == 0)
				this.setEquipment(0, new ItemStack(Items.STONE_SWORD));
			else
				this.setEquipment(0, new ItemStack(Items.WOODEN_SWORD));
		}
	}

	public void move(final Location loc) {
		this.move(loc, EnhancedZombie.MOVE_SPEED, false);
	}

	public void move(final Location loc, final double speed, final boolean flag) {
		try{
			this.getNavigation().a(loc.getX(), loc.getY(), loc.getZ(), speed);
			this.hasMove = flag;
		} catch(NullPointerException | ArrayIndexOutOfBoundsException | ConcurrentModificationException e){}
	}

	@Override
	public void setBaby(final boolean arg0) {}

	@Override
	public boolean isBaby() {
		return false;
	}

	@Override
	public void setOnFire(final int i) {}

	@Override
	public int getExpReward() {
		return 0;
	}
}
