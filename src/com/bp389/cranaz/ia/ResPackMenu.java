package com.bp389.cranaz.ia;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.bp389.cranaz.events.GEvent;

/**
 * Représente le menu permettant de choisir un resource pack
 * 
 * @author BlackPhantom
 * 
 */
public final class ResPackMenu {

	private final Inventory i;
	public static ItemStack light, heavy;

	public ResPackMenu() {
		this.i = Bukkit.getServer().createInventory(null, 9, "Choisir un resource pack");
		/*
		 * 
		 */
		ResPackMenu.light = new ItemStack(Material.REDSTONE);
		final ItemMeta im = ResPackMenu.light.getItemMeta();
		im.setDisplayName("Resource pack léger");
		ResPackMenu.light.setItemMeta(im);
		/*
		 * 
		 */
		ResPackMenu.heavy = new ItemStack(Material.DIAMOND);
		final ItemMeta im2 = ResPackMenu.heavy.getItemMeta();
		im2.setDisplayName("Resource pack ultra.");
		ResPackMenu.heavy.setItemMeta(im2);
		/*
		 * 
		 */
		this.i.addItem(ResPackMenu.light, ResPackMenu.heavy);
		GEvent.resGui = this.i;
	}
}
