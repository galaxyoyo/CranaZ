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
				b = true;
				break;
			case IRON_AXE:
				b = true;
				break;
			case IRON_HOE:
				b = true;
				break;
			case IRON_PICKAXE:
				b = true;
				break;
			case IRON_SPADE:
				b = true;
				break;
			case IRON_SWORD:
				b = true;
				break;
			case WOOD_SPADE:
				b = true;
				break;
			case WOOD_AXE:
				b = true;
				break;
			case WOOD_HOE:
				b = true;
				break;
			case WOOD_PICKAXE:
				b = true;
				break;
			case WOOD_SWORD:
				b = true;
				break;
			case STONE_AXE:
				b = true;
				break;
			case STONE_HOE:
				b = true;
				break;
			case STONE_PICKAXE:
				b = true;
				break;
			case STONE_SPADE:
				b = true;
				break;
			case STONE_SWORD:
				b = true;
				break;
			case GOLD_AXE:
				b = true;
				break;
			case GOLD_PICKAXE:
				b = true;
				break;
			case GOLD_SPADE:
				b = true;
				break;
			case GOLD_SWORD:
				b = true;
				break;
			case GOLD_HOE:
				b = true;
				break;
			case DIAMOND_AXE:
				b = true;
				break;
			case DIAMOND_HOE:
				b = true;
				break;
			case DIAMOND_PICKAXE:
				b = true;
				break;
			case DIAMOND_SPADE:
				b = true;
				break;
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
