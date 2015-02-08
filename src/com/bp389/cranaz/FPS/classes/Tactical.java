package com.bp389.cranaz.FPS.classes;

import org.bukkit.inventory.ItemStack;

import com.bp389.cranaz.api.EquipSlot;
import com.bp389.cranaz.items.Items;


final class Tactical extends GameClass {
	public Tactical(int team){
		this(team, Items.getCSUWeapon("BAR", 1),
				Items.getAmmoStack(Items.BAR, 5),
				Items.getCSUWeapon("Airstrike", 1),
				Items.getCSUWeapon("Flashbang", 2),
				Items.bandages());
	}
	protected Tactical(int team, ItemStack... contents) {
	    super("Tacticien", new Trait[]{Trait.AIRSTRIKE_CALLER}, team, contents);
	    setOrderedEquip(Items.teamTShirt(team, EquipSlot.BOOTS),
	    		Items.teamTShirt(team, EquipSlot.PANTS),
	    		Items.teamTShirt(team, EquipSlot.PLATE),
	    		Items.teamTShirt(team, EquipSlot.HELMET));
    }

}
