package com.bp389.cranaz.legacy;

import java.util.HashMap;

import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

/**
 * Classe permettant de gérer l'anti déco-combat
 * 
 * @author BlackPhantom
 * 
 */
public final class NQHandler {

	private static HashMap<Player, Handler> damagins = new HashMap<Player, Handler>();
	private static JavaPlugin jp;

	public static void init(final JavaPlugin plugin) {
		NQHandler.jp = plugin;
	}

	public static void handleNoDeco(final Player p) {
		if(NQHandler.damagins.containsKey(p)) {
			NQHandler.damagins.get(p).cancel();
			NQHandler.damagins.remove(p);
		}
		final Handler h = new Handler(p);
		NQHandler.damagins.put(p, h);
		h.runTaskLater(NQHandler.jp, 5L * 20L);
	}

	public static boolean isInCombat(final Player p) {
		return NQHandler.damagins.containsKey(p);
	}

	public static void forceRemove(final Player p) {
		NQHandler.damagins.get(p).cancel();
		NQHandler.damagins.remove(p);
	}

	public static class Handler extends BukkitRunnable {

		private final Player p;

		public Handler(final Player p) {
			this.p = p;
		}

		@Override
		public void run() {
			NQHandler.damagins.remove(this.p);
		}
	}
}
