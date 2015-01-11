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

	public static void init(final JavaPlugin plugin) {
		IPlayerFactor.main = plugin;
		IPlayerFactor.factor = new IBagFactor(IPlayerFactor.main);
	}

	public static synchronized Inventory getB(final Player p) {
		return IPlayerFactor.bags.get(p);
	}

	public static void setB(final Player p, final Inventory i) {
		IPlayerFactor.bags.put(p, i);
	}

	public static void clearBag(final Player p) {
		IPlayerFactor.bags.get(p).clear();
	}

	public static void loadPlayer(final Player p) {
		if(!IPlayerFactor.bags.containsKey(p)) {
			IPlayerFactor.setB(p, IPlayerFactor.genBag(p));
			if(!IPlayerFactor.hasABag(p))
				try {
					new File(IPlayerFactor.bagsLoc + p.getName() + ".yml").createNewFile();
				} catch(final IOException e) {
					e.printStackTrace();
				}
		}
	}

	public static void unloadPlayer(final Player p) {
		if(IPlayerFactor.bags.containsKey(p)) {
			IPlayerFactor.factor.serial(p.getName(), IPlayerFactor.bags.get(p));
			IPlayerFactor.bags.remove(p);
		}
	}

	public static Inventory bagOf(final Player p) {
		if(IPlayerFactor.bags.containsKey(p))
			return IPlayerFactor.bags.get(p);
		return null;
	}

	protected static Inventory genBag(final Player p) {
		if(IPlayerFactor.hasABag(p))
			return IPlayerFactor.getOldBag(p);
		return Bukkit.getServer().createInventory(null, InventoryType.CHEST, "SAC A DOS");
	}

	public static final boolean hasABag(final Player who) {
		return new File(IPlayerFactor.bagsLoc + who.getName() + ".yml").exists();
	}

	protected static final Inventory getOldBag(final Player who) {
		if(IPlayerFactor.hasABag(who))
			return IPlayerFactor.factor.deserial(who.getName());
		return null;
	}
}
