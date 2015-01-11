package com.bp389.cranaz.thirst;

import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import com.bp389.PluginMethods;

public final class ThirstRunnable extends BukkitRunnable {

	private final Player p;

	public ThirstRunnable(final Player player) {
		this.p = player;
	}

	@Override
	public strictfp void run() {
		if(!this.p.isOnline() || this.p.isOp()) {
			this.cancel();
			return;
		}
		float ef = this.p.getExp();
		ef -= ThirstFactor.getPRF();
		if(ef <= 0F) {
			PluginMethods.alert(this.p, "Vous etes mort de soif !");
			this.p.setHealth(0D);
			return;
		} else if(ef <= 0.3F)
			this.p.sendMessage("La soif commence à se faire sentir");
		this.p.setExp(ef);
	}
}
