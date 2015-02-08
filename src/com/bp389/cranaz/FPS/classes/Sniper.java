package com.bp389.cranaz.FPS.classes;

import org.bukkit.inventory.ItemStack;

import com.bp389.cranaz.api.EquipSlot;
import com.bp389.cranaz.items.Items;


final class Sniper extends GameClass {
	public Sniper(int team){
		this(team, Items.getCSUWeapon("Moisin", 1),
				Items.getAmmoStack(Items.MOSIN, 10),
				Items.getCSUWeapon("Smith", 1),
				Items.getAmmoStack(Items.SMITH, 1),
				Items.bandages());
	}
	protected Sniper(int team, ItemStack... contents) {
		super("Tireur d'élite", new Trait[]{Trait.SKYDIVER_DEPLOY}, team, contents);
		setOrderedEquip(Items.camo_boots(),
				Items.camo_pants(),
				Items.teamTShirt(team, EquipSlot.PLATE),
				Items.teamTShirt(team, EquipSlot.HELMET));
	}

}
