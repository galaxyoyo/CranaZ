package com.bp389.cranaz.bags;

import java.io.IOException;
import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

public final class IBagFactor {

	private final JavaPlugin plugin;

	public IBagFactor(final JavaPlugin plugin) {
		this.plugin = plugin;
	}

	public void serial(final String pName, final Inventory i) {
		final FileConfiguration config = this.plugin.getConfig();
		config.set("bag.contents", i.getContents());
		try {
			config.save(Bags.getBagFile(pName));
		} catch(final IOException e) {
			e.printStackTrace();
		}
	}

	@SuppressWarnings("unchecked")
	public Inventory deserial(final String pName) {
		final FileConfiguration config = this.plugin.getConfig();
		try {
			config.load(Bags.getBagFile(pName));
			final ArrayList<ItemStack> temp = (ArrayList<ItemStack>) config.get("bag.contents");
			final Inventory inv = Bukkit.getServer().createInventory(null, InventoryType.CHEST, "SAC A DOS");
			final ItemStack[] temp2 = new ItemStack[temp.size()];
			for(int i = 0; i < temp2.length; i++)
				temp2[i] = temp.get(i);
			inv.setContents(temp2);
			return inv;
		} catch(IOException | InvalidConfigurationException e) {
			e.printStackTrace();
		}
		return null;
	}
}
