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
	private JavaPlugin plugin;
	public IBagFactor(JavaPlugin plugin){this.plugin = plugin;}
	public void serial(String pName, Inventory i)
	{
		FileConfiguration config = plugin.getConfig();
			config.set("bag.contents", i.getContents());
		try {
	        config.save(Bags.getBagFile(pName));
        } catch (IOException e) {
	        e.printStackTrace();}
	}
	@SuppressWarnings("unchecked")
    public Inventory deserial(String pName)
	{
		FileConfiguration config = plugin.getConfig();
		try {
	        config.load(Bags.getBagFile(pName));
	        ArrayList<ItemStack> temp = (ArrayList<ItemStack>)config.get("bag.contents");
	        Inventory inv = Bukkit.getServer().createInventory(null, InventoryType.CHEST, "SAC A DOS");
	        ItemStack[] temp2 = new ItemStack[temp.size()];
	        for(int i = 0; i < temp2.length;i++){
	        	temp2[i] = temp.get(i);
	        }
	        inv.setContents(temp2);
	        return inv;
        } catch (IOException | InvalidConfigurationException e) {e.printStackTrace();}
		return null;
	}
}
