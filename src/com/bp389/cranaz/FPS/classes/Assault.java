package com.bp389.cranaz.FPS.classes;

import org.bukkit.inventory.ItemStack;

import com.bp389.cranaz.api.EquipSlot;
import com.bp389.cranaz.items.Items;


final class Assault extends GameClass {
	public Assault(int team) {
	    this(team, Items.bandages(),
	    		Items.getCSUWeapon("AK-47", 1),
	    		Items.getAmmoStack(Items.AK47, 5),
	    		Items.getCSUWeapon("Grenade", 3),
	    		Items.getCSUWeapon("Flashbang", 3));
    }
	
	protected Assault(int team, ItemStack...itemStacks){
		super("Fusilier", new Trait[]{Trait.SKYDIVER_DEPLOY}, team, itemStacks);
		setOrderedEquip(Items.teamTShirt(team, EquipSlot.BOOTS),
				Items.teamTShirt(team, EquipSlot.PANTS),
				Items.teamTShirt(team, EquipSlot.PLATE),
				Items.camo_helmet());
	}
}
