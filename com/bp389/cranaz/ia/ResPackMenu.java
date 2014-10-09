package com.bp389.cranaz.ia;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.bp389.cranaz.Events;

/**
 * Représente le menu permettant de choisir un resource pack
 * @author BlackPhantom
 *
 */
public class ResPackMenu {
	private Inventory i;
	public static ItemStack light, heavy;
	public ResPackMenu(){
		i = Bukkit.getServer().createInventory(null, 9, "Choisir un resource pack");
		/*
		 * 
		 */
		light = new ItemStack(Material.REDSTONE);
		ItemMeta im = light.getItemMeta();
		im.setDisplayName("Resource pack léger");
		light.setItemMeta(im);
		/*
		 * 
		 */
		heavy = new ItemStack(Material.DIAMOND);
		ItemMeta im2 = heavy.getItemMeta();
		im2.setDisplayName("Resource pack ultra.");
		heavy.setItemMeta(im2);
		/*
		 * 
		 */
		i.addItem(light, heavy);
		Events.resGui = this.i;
	}
}
