package com.bp389.cranaz.api;

import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_8_R1.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_8_R1.inventory.CraftItemStack;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityTargetEvent.TargetReason;
import org.bukkit.inventory.ItemStack;

import net.minecraft.server.v1_8_R1.EntityLiving;
import net.minecraft.server.v1_8_R1.GenericAttributes;
import net.minecraft.server.v1_8_R1.IAttribute;

import com.bp389.cranaz.ia.entities.EnhancedZombie;


public final class EZombie_API {
	private EnhancedZombie ez;
	public EZombie_API(EnhancedZombie ez){
		this.ez = ez;
	}

	/**
	 * Fait bouger le zombie avec une vitesse de base
	 * @param location La position cible
	 * @see #move(Location, double)
	 */
	public void move(Location location){
		ez.move(location);
	}
	/**
	 * Fait bouger le zombie avec sa vitesse de base * <b>speedMultiplier</b>
	 * @param location La position cible
	 * @param speedMultiplier Le multiplieur de sa vitesse de mouvement de base
	 */
	public void move(Location location, double speedMultiplier){
		ez.move(location, EnhancedZombie.MOVE_SPEED * speedMultiplier, false);
	}
	/**
	 * 
	 * @return La cible du zombie
	 */
	public EntityLiving getTarget(){
		return ez.getGoalTarget();
	}
	/**
	 * Donne comme cible <b>target</b> au zombie
	 * @param target Le joueur cible
	 */
	public void targetPlayer(final Player target){
		ez.setGoalTarget(((CraftPlayer)target).getHandle(), TargetReason.CUSTOM, true);
	}
	/**
	 * 
	 * @return La position exacte du zombie
	 */
	public Location getLocation(){
		return ez.getBukkitLocation();
	}
	/**
	 * 
	 * @param slot Le slot d'ou obtenir l'equipement
	 * @return L'equipement
	 */
	public ItemStack getEquipment(EquipSlot slot){
		return CraftItemStack.asBukkitCopy(ez.getEquipment(slot.getID()));
	}
	/**
	 * Definit un des equipements du zombie
	 * @param slot Le slot
	 * @param item L'equipement
	 */
	public void setEquipment(EquipSlot slot, ItemStack item){
		ez.setEquipment(slot.getID(), CraftItemStack.asNMSCopy(item));
	}
	/**
	 * Definit un des attributs du zombie
	 * @param attribute L'attribut
	 * @param value La valeur
	 */
	public void setAttribute(ZombieAttribute attribute, double value){
		ez.getAttributeInstance(attribute.ga).setValue(value);
	}
	/**
	 * 
	 * @param attribute L'attribut a obtenir
	 * @return La valeur de cet attribut
	 */
	public double getAttributeValue(ZombieAttribute attribute){
		return ez.getAttributeInstance(attribute.ga).getValue();
	}
	/**
	 * 
	 * @return La forme interne d'un zombie ameliore, plus de possibilites, plus de complexite
	 */
	public EnhancedZombie getEnhancedZombie(){
		return ez;
	}
	
	
	public enum ZombieAttribute{
		SPEED(GenericAttributes.d),
		FOLLOW_RANGE(GenericAttributes.b),
		HEALTH(GenericAttributes.maxHealth),
		DAMAGE(GenericAttributes.e);
		private IAttribute ga;
		ZombieAttribute(IAttribute ga){
			this.ga = ga;
		}
		public IAttribute getAttribute(){
			return ga;
		}
	}
}
