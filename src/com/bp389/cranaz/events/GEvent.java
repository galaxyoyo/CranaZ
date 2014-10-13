package com.bp389.cranaz.events;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import com.bp389.cranaz.Loadable;

public abstract class GEvent {
	//TODO GLOBAL
	public static final Random rand = new Random();
	//public static ReviveHandler rh;
	protected JavaPlugin plugin;
	protected static JavaPlugin static_plugin;
	public static Inventory goGui, resGui;
	public static ArrayList<Player> playings = new ArrayList<Player>();
	public static ArrayList<String> temps = new ArrayList<String>();
	public static ArrayList<Inventory> is = new ArrayList<Inventory>();
	public static HashMap<Player, ItemStack> helmets = new HashMap<Player, ItemStack>();
	public GEvent(JavaPlugin jp){
		plugin = jp;
		static_plugin = jp;
		FileConfiguration fc = jp.getConfig();
		File cfg = new File("plugins/CranaZ/Divers/torteela.yml");
		try {
			boolean b = false;
			if(!cfg.exists()){
				cfg.createNewFile();
				fc.set("torteela.players", (List<String>)Arrays.asList("TST_null"));
				fc.save(cfg);
			}
			else
				b = true;
			if(b)
			{
				fc.load(cfg);
				temps = (ArrayList<String>)fc.getStringList("torteela.players");
			}
		} catch (Exception e) {}
	}
	public abstract Class<? extends Loadable> getRelativePlugin();
}
