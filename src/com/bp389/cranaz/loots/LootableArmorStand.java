package com.bp389.cranaz.loots;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_8_R1.CraftServer;
import org.bukkit.craftbukkit.v1_8_R1.CraftWorld;
import org.bukkit.craftbukkit.v1_8_R1.entity.CraftArmorStand;
import org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.EulerAngle;

import net.minecraft.server.v1_8_R1.EntityArmorStand;

import com.bp389.cranaz.Util.MathUtil;


public final class LootableArmorStand extends CraftArmorStand {
	public LootableArmorStand(CraftServer server, EntityArmorStand eas, ItemStack icon) {
		super(server, eas);
		this.setBasePlate(false);
		this.setArms(true);
		this.setGravity(false);
		this.setLoot(icon);
		this.setVisible(false);
	}
	public static EntityArmorStand genLootable(Location loc){
		final EntityArmorStand eas = new EntityArmorStand(((CraftWorld)loc.getWorld()).getHandle(), loc.getX() + 0.75D, loc.getY() + 0.2D, loc.getZ() + 0.6D);
		return eas;
	}
	public void spawnThis(){
		((CraftWorld)this.getWorld()).getHandle().addEntity(getHandle(), SpawnReason.CUSTOM);
	}
	public void die(){
		getHandle().die();
	}
	public void setLoot(final ItemStack is){
		boolean b = false;
		switch(is.getType()){
			case BOW:
			case IRON_AXE:
			case IRON_HOE:
			case IRON_PICKAXE:
			case IRON_SPADE:
			case IRON_SWORD:
			case WOOD_SPADE:
			case WOOD_AXE:
			case WOOD_HOE:
			case WOOD_PICKAXE:
			case WOOD_SWORD:
			case STONE_AXE:
			case STONE_HOE:
			case STONE_PICKAXE:
			case STONE_SPADE:
			case STONE_SWORD:
			case GOLD_AXE:
			case GOLD_PICKAXE:
			case GOLD_SPADE:
			case GOLD_SWORD:
			case GOLD_HOE:
			case DIAMOND_AXE:
			case DIAMOND_HOE:
			case DIAMOND_PICKAXE:
			case DIAMOND_SPADE:
			case DIAMOND_SWORD:
				b = true;
				break;
		}
		if(b && is.getType() != Material.BOW){
			this.setRightArmPose(new EulerAngle(0D, 0D, MathUtil.degreesToRadians(90D)));
			getHandle().setPosition(getHandle().locX + 0.9D, getHandle().locY - 0.6D, getHandle().locZ - 0.15D);
		}
		else if(b && is.getType() == Material.BOW){
			this.setRightArmPose(new EulerAngle(0D, MathUtil.degreesToRadians(10D), MathUtil.degreesToRadians(90D)));
			getHandle().setPosition(getHandle().locX + 0.9D, getHandle().locY - 0.5D, getHandle().locZ - 0.15D);
		}
		else
			this.setRightArmPose(EulerAngle.ZERO);
		this.setItemInHand(is);
	}
}
