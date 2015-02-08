package com.bp389.cranaz.events;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Random;

import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import com.bp389.cranaz.Loadable;
import com.bp389.cranaz.Util;

public abstract class GEvent implements Listener {

	public static final Random rand = new Random();
	// public static ReviveHandler rh;
	protected JavaPlugin plugin;
	protected static JavaPlugin static_plugin;
	public static Inventory goGui, resGui;
	public static ArrayList<Player> playings = new ArrayList<Player>();
	public static ArrayList<String> temps = new ArrayList<String>();
	public static ArrayList<Inventory> is = new ArrayList<Inventory>();
	public static HashMap<Player, ItemStack> helmets = new HashMap<Player, ItemStack>();

	@SuppressWarnings("unchecked")
	public GEvent(final JavaPlugin jp) {
		this.plugin = jp;
		GEvent.static_plugin = jp;
		final File cfg = new File("plugins/CranaZ/database/torteela.yml");
		boolean b = false;
		if(!cfg.exists()) {
			Util.saveToYaml(cfg, "torteela.players", Arrays.asList("TST_null"));
		} else
			b = true;
		if(b) {
			GEvent.temps = (ArrayList<String>)Util.getFromYaml(cfg, "torteela.players");
		}
	}
	
	public static void registerAllEvents(final JavaPlugin jp){
		final PluginManager pm = jp.getServer().getPluginManager();
		pm.registerEvents(new EBags(jp), jp);
		pm.registerEvents(new EEffects(jp), jp);
		pm.registerEvents(new EIA(jp), jp);
		pm.registerEvents(new EItemsLegacy(jp), jp);
		pm.registerEvents(new ELoots(jp), jp);
		pm.registerEvents(new EThirst(jp), jp);
		pm.registerEvents(new EFPS(jp), jp);
	}

	public abstract Class<? extends Loadable> getRelativePlugin();
	
}
