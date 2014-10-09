package com.bp389.cranaz.thirst;

import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import com.bp389.PluginMethods;

public final class ThirstRunnable extends BukkitRunnable{
	private Player p;
	public ThirstRunnable(Player player){
		p = player;
	}
	@Override
    public strictfp void run() {
		if(!p.isOnline() || p.isOp()){
			cancel();
			return;
		}
		float ef = p.getExp();
		ef -= ThirstFactor.getPRF();
		if(ef <= 0F){
			PluginMethods.alert(p, "Vous etes mort de soif !");
			p.setHealth(0D);
			return;
		}
		else if(ef <= 0.3F){
			p.sendMessage("La soif commence à se faire sentir");
		}
		p.setExp(ef);
    }
}
