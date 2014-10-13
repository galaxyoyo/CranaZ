package com.bp389.cranaz.ia;

import java.util.Arrays;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.bp389.cranaz.events.GEvent;

/**
 * Représente le menu permettant de partir avec Darkauron
 * @author BlackPhantom
 *
 */
public class GoMenu {
	private Inventory i;
	public static ItemStack alone, many;
	public GoMenu(){
		i = Bukkit.getServer().createInventory(null, 9, "Partir");
		/*
		 * 
		 */
		alone = new ItemStack(Material.ENDER_PEARL);
		ItemMeta im = alone.getItemMeta();
		im.setDisplayName("Partir seul.");
		im.setLore(Arrays.asList("Partez seul survivre dans un monde hostile."));
		alone.setItemMeta(im);
		/*
		 * 
		 */
		many = new ItemStack(Material.EMERALD);
		ItemMeta im2 = many.getItemMeta();
		im2.setDisplayName("Partir a plusieurs.");
		im2.setLore(Arrays.asList("Partez avec UN SEUL ami."));
		many.setItemMeta(im2);
		/*
		 * 
		 */
		i.addItem(alone, many);
		GEvent.goGui = this.i;
	}
}
