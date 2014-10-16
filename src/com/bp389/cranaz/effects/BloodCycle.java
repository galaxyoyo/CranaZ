package com.bp389.cranaz.effects;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Hashtable;
import java.util.Random;

import org.bukkit.entity.Damageable;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

/**
 * Représente les cycles de saignement d'un joueur blessé
 * @author BlackPhantom
 *
 */
public final class BloodCycle extends BukkitRunnable{
	private Player p;
	private int cycle;
	private Random r = new Random();
	public static ArrayList<Player> ps = new ArrayList<Player>();
	public static Hashtable<Player, BloodCycle> ht = new Hashtable<Player, BloodCycle>();
	public BloodCycle(Player p){
		this.p = p;
		this.cycle = 0;
		if(!ps.contains(p)){
			ps.add(p);
			ht.put(p, this);
		}
	}
	@Override
    public void run() {
		if(((Damageable)p).getHealth() >= 10D){
			stop(p);
			return;
		}
		if(this.r.nextInt(3) == 0)
			p.sendMessage(Bleed.messages.get(r.nextInt(Bleed.messages.size())));
		for(PotionEffect pe : Arrays.asList(new PotionEffect(PotionEffectType.SLOW, 5 * 20, 0),
				new PotionEffect(PotionEffectType.SLOW_DIGGING, 5 * 20, 0),
				new PotionEffect(PotionEffectType.WEAKNESS, 5 * 20, 0)))
			pe.apply(p);
		if(cycle >= 3)
			p.damage(Integer.valueOf(cycle).doubleValue() - 3D);
		++cycle;
    }
	public static void stop(Player p){
		ps.remove(p);
		try{
			BloodCycle bc = ht.get(p);
			ht.remove(p);
			if(bc != null)
				bc.cancel();
		}catch(NullPointerException e){}
	}
}
