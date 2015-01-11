package com.bp389.cranaz.events;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerExpChangeEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import com.bp389.PluginMethods;
import com.bp389.cranaz.Loadable;
import com.bp389.cranaz.thirst.ThirstFactor;
import com.bp389.cranaz.thirst.ThirstRunnable;

public final class EThirst extends GEvent implements Listener {

	public EThirst(final JavaPlugin jp) {
		super(jp);
	}

	@Override
	public Class<? extends Loadable> getRelativePlugin() {
		return null;
	}

	/*
	 * 
	 */

	@EventHandler
	public void playerJoin(final PlayerJoinEvent e) {
		if(!e.getPlayer().hasPlayedBefore())
			e.getPlayer().setExp(0.99F);
		if(!e.getPlayer().hasPermission("cranaz.thirst.no") && !e.getPlayer().isOp())
			new ThirstRunnable(e.getPlayer()).runTaskTimer(this.plugin, 100, ThirstFactor.getDelay() * 20);
	}

	@EventHandler
	public void playerDrink(final PlayerItemConsumeEvent e) {
		if(e.getItem().getType() == Material.POTION) {
			PluginMethods.gsay(e.getPlayer(), "Vous avez bu et vous sentez mieux.");
			e.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 60 * 20, 0));
			e.getPlayer().setExp(0.99F);
		}
	}

	@EventHandler
	public void expPicked(final PlayerExpChangeEvent e) {
		e.setAmount(0);
	}

	@EventHandler
	public void playerDie(final PlayerDeathEvent e) {
		new FillExp(e.getEntity()).runTaskAsynchronously(this.plugin);
	}

	class FillExp extends BukkitRunnable {

		private final Player p;

		public FillExp(final Player p) {
			this.p = p;
		}

		@Override
		public void run() {
			while(this.p.isDead());
			new BukkitRunnable() {

				@Override
				public void run() {
					FillExp.this.p.setExp(0.99F);
				}
			}.runTask(EThirst.this.plugin);
		}
	}
}
