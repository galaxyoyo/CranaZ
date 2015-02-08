package com.bp389.cranaz.bags;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import com.bp389.cranaz.Util;

public final class IBagFactor {

	@SuppressWarnings("unused")
	private final JavaPlugin plugin;

	public IBagFactor(final JavaPlugin plugin) {
		this.plugin = plugin;
	}

	public void serial(final String pName, final Inventory i) {
		Util.saveToYaml(Bags.getBagFile(pName), "bag.contents", i.getContents());
	}

	@SuppressWarnings("unchecked")
	public Inventory deserial(final String pName) {
		final ArrayList<ItemStack> temp = (ArrayList<ItemStack>)Util.getFromYaml(Bags.getBagFile(pName), "bag.contents");
		if(temp == null)
			return null;
		final Inventory inv = Bukkit.getServer().createInventory(null, InventoryType.CHEST, "SAC A DOS");
		final ItemStack[] temp2 = new ItemStack[temp.size()];
		for(int i = 0; i < temp2.length; i++)
			temp2[i] = temp.get(i);
		inv.setContents(temp2);
		return inv;
	}
}
