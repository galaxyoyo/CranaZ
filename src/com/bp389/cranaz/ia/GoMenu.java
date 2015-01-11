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
 * 
 * @author BlackPhantom
 * 
 */
public final class GoMenu {

	private final Inventory i;
	public static ItemStack alone, many;

	public GoMenu() {
		this.i = Bukkit.getServer().createInventory(null, 9, "Partir");
		/*
		 * 
		 */
		GoMenu.alone = new ItemStack(Material.ENDER_PEARL);
		final ItemMeta im = GoMenu.alone.getItemMeta();
		im.setDisplayName("Partir seul.");
		im.setLore(Arrays.asList("Partez seul survivre dans un monde hostile."));
		GoMenu.alone.setItemMeta(im);
		/*
		 * 
		 */
		GoMenu.many = new ItemStack(Material.EMERALD);
		final ItemMeta im2 = GoMenu.many.getItemMeta();
		im2.setDisplayName("Partir a plusieurs.");
		im2.setLore(Arrays.asList("Partez avec UN SEUL ami."));
		GoMenu.many.setItemMeta(im2);
		/*
		 * 
		 */
		this.i.addItem(GoMenu.alone, GoMenu.many);
		GEvent.goGui = this.i;
	}
}
