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
 * 
 * @author BlackPhantom
 * 
 */
public final class BloodCycle extends BukkitRunnable {

	private final Player p;
	private int cycle;
	private final Random r = new Random();
	public static ArrayList<Player> ps = new ArrayList<Player>();
	public static Hashtable<Player, BloodCycle> ht = new Hashtable<Player, BloodCycle>();

	public BloodCycle(final Player p) {
		this.p = p;
		this.cycle = 0;
		if(!BloodCycle.ps.contains(p)) {
			BloodCycle.ps.add(p);
			BloodCycle.ht.put(p, this);
		}
	}

	@Override
	public void run() {
		if(((Damageable) this.p).getHealth() >= 10D) {
			BloodCycle.stop(this.p);
			return;
		}
		if(this.r.nextInt(3) == 0)
			this.p.sendMessage(Bleed.messages.get(this.r.nextInt(Bleed.messages.size())));
		for(final PotionEffect pe : Arrays.asList(new PotionEffect(PotionEffectType.SLOW, 5 * 20, 0),
		        new PotionEffect(PotionEffectType.SLOW_DIGGING, 5 * 20, 0), new PotionEffect(PotionEffectType.WEAKNESS, 5 * 20, 0)))
			pe.apply(this.p);
		if(this.cycle >= 3)
			this.p.damage(Integer.valueOf(this.cycle).doubleValue() - 3D);
		++this.cycle;
	}

	public static void stop(final Player p) {
		BloodCycle.ps.remove(p);
		try {
			final BloodCycle bc = BloodCycle.ht.get(p);
			BloodCycle.ht.remove(p);
			if(bc != null)
				bc.cancel();
		} catch(final NullPointerException e) {}
	}
}
