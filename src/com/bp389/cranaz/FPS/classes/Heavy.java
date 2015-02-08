package com.bp389.cranaz.FPS.classes;

import org.bukkit.inventory.ItemStack;

import com.bp389.cranaz.api.EquipSlot;
import com.bp389.cranaz.items.Items;


final class Heavy extends GameClass {
	public Heavy(int team){
		this(team, Items.getCSUWeapon("BAR", 1),
				Items.getAmmoStack(Items.BAR, 10),
				Items.bandages(),
				Items.getCSUWeapon("Grenade", 5));
	}
	protected Heavy(int team, ItemStack... contents) {
	    super("Appui", new Trait[]{Trait.NO_TRAIT}, team, contents);
	    setOrderedEquip(Items.teamTShirt(team, EquipSlot.BOOTS),
	    		Items.teamTShirt(team, EquipSlot.PANTS),
	    		Items.teamTShirt(team, EquipSlot.PLATE),
	    		Items.teamTShirt(team, EquipSlot.HELMET));
    }

}
