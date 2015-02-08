package com.bp389.cranaz.FPS.classes;

import org.bukkit.inventory.ItemStack;

import com.bp389.cranaz.api.EquipSlot;
import com.bp389.cranaz.items.Items;


final class Rusher extends GameClass {
	public Rusher(int team){
		this(team, Items.getCSUWeapon("AK-47", 1),
				Items.getAmmoStack(Items.AK47, 3),
				Items.amphetamines(),
				Items.machette(),
				Items.getCSUWeapon("Flashbang", 1));
	}
	protected Rusher(int team, ItemStack... contents) {
		super("Fonceur", new Trait[]{Trait.NO_TRAIT}, team, contents);
		 setOrderedEquip(Items.teamTShirt(team, EquipSlot.BOOTS),
		    		Items.teamTShirt(team, EquipSlot.PANTS),
		    		Items.teamTShirt(team, EquipSlot.PLATE),
		    		Items.teamTShirt(team, EquipSlot.HELMET));
	}

}
