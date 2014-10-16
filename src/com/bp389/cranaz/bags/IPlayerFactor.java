package com.bp389.cranaz.bags;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.plugin.java.JavaPlugin;

public final class IPlayerFactor {
	private static volatile HashMap<Player, Inventory> bags = new HashMap<Player, Inventory>();
	public static final String bagsLoc = "plugins/CranaZ/CranaZBags/bags/";
	protected static JavaPlugin main;
	public static IBagFactor factor;
	public static void init(JavaPlugin plugin){main = plugin;factor = new IBagFactor(main);}
	public static synchronized Inventory getB(Player p){
		return bags.get(p);
	}
	public static void setB(Player p, Inventory i){
		bags.put(p, i);
	}
	public static void clearBag(Player p){
		bags.get(p).clear();
	}
	public static void loadPlayer(Player p){
		if(!bags.containsKey(p)){
			setB(p, genBag(p));
			if(!hasABag(p)){
				try {
	                new File(bagsLoc + p.getName() + ".yml").createNewFile();
                } catch (IOException e) {
	                e.printStackTrace();}
			}
		}
	}
	public static void unloadPlayer(Player p){
		if(bags.containsKey(p)){
			factor.serial(p.getName(), bags.get(p));
			bags.remove(p);
		}
	}
	public static Inventory bagOf(Player p){
		if(bags.containsKey(p)){
			return bags.get(p);
		}
		return null;
	}
	protected static Inventory genBag(Player p)
	{
		if(hasABag(p)){
			return getOldBag(p);
		}
		return Bukkit.getServer().createInventory(null, InventoryType.CHEST, "SAC A DOS");
	}
	public static final boolean hasABag(Player who){
		return new File(bagsLoc + who.getName() + ".yml").exists();
	}
	protected static final Inventory getOldBag(Player who)
	{
		if(hasABag(who)){
			return factor.deserial(who.getName());
		}
		return null;
	}
}
