package com.bp389.cranaz.FPS.classes;

import org.bukkit.inventory.ItemStack;

import com.bp389.cranaz.api.EquipSlot;
import com.bp389.cranaz.items.Items;


final class Healer extends GameClass {
	public Healer(int team){
		this(team, Items.setAmount(Items.antalgiques(), 2),
				Items.setAmount(Items.bandages(), 2),
				Items.big_bloodBag(),
				Items.getAmmoStack(Items.AK47, 3),
				Items.getCSUWeapon("AK-47", 1));
	}
	protected Healer(int team, ItemStack...itemStacks) {
	    super("Médecin", new Trait[]{Trait.NO_TRAIT}, team, itemStacks);
	    setOrderedEquip(Items.teamTShirt(team, EquipSlot.BOOTS),
				Items.teamTShirt(team, EquipSlot.PANTS),
				Items.hospitalShirt(),
				Items.teamTShirt(team, EquipSlot.HELMET));
    }
}
