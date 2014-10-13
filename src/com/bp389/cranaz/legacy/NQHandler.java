package com.bp389.cranaz.legacy;

import java.util.HashMap;

import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

/**
 * Classe permettant de gérer l'anti déco-combat
 * @author BlackPhantom
 *
 */
public class NQHandler {
	private static HashMap<Player, Handler> damagins = new HashMap<Player, Handler>();
	private static JavaPlugin jp;
	public static void init(JavaPlugin plugin){
		jp = plugin;
	}
	public static void handleNoDeco(Player p){
		if(damagins.containsKey(p)){
			damagins.get(p).cancel();
			damagins.remove(p);
		}
		Handler h = new Handler(p);
		damagins.put(p, h);
		h.runTaskLater(jp, 5L * 20L);
	}
	public static boolean isInCombat(Player p){
		return damagins.containsKey(p);
	}
	public static void forceRemove(Player p){
		damagins.get(p).cancel();
		damagins.remove(p);
	}
	public static class Handler extends BukkitRunnable
	{
		private Player p;
		public Handler(Player p){
			this.p = p;
		}
		@Override
        public void run() {
			damagins.remove(p);
        }
	}
}
