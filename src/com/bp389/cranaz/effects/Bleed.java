package com.bp389.cranaz.effects;

import java.util.Arrays;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.entity.Damageable;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

/**
 * Classe statique contenant les méthodes relatives aux effets
 * @author BlackPhantom
 *
 */
public class Bleed {
	public static final PotionEffect heavy = new PotionEffect(PotionEffectType.BLINDNESS, 500 * 20, 1);
	public static final PotionEffect hurt = new PotionEffect(PotionEffectType.WEAKNESS, 500 * 20, 1);
	public static final PotionEffect grave = new PotionEffect(PotionEffectType.SLOW, 500 * 20, 1);
	public static final PotionEffect light = new PotionEffect(PotionEffectType.CONFUSION, 500 * 20, 1);
	public static final PotionEffect instinct = new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 15 * 20, 1);
	public static final PotionEffect instinct2 = new PotionEffect(PotionEffectType.REGENERATION, 8 * 20, 1);
	public static final List<String> messages = Arrays.asList("§r§cAh ! Putain ! Je saigne !§r",
			"§r§cEt merde je me suis ouvert !§r",
			"§r§cOuh putain je me sens mal...§r",
			"§r§cOuaaah, j'ai la tete qui tourne...§r");
	private static JavaPlugin jp;
	public static void init(JavaPlugin plugin){jp = plugin;}
	public static final void amphet(Player p){
		p.addPotionEffect(new PotionEffect(PotionEffectType.CONFUSION, 10 * 20, 1));
		p.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 240 * 20, 1));
		p.addPotionEffect(new PotionEffect(PotionEffectType.HUNGER, 120 * 20, 0));
		p.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 240 * 20, 1));
		p.addPotionEffect(new PotionEffect(PotionEffectType.HEALTH_BOOST, 180 * 20, 1));
		p.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 120 * 20, 0));
	}
	public static final void mush(Player p){
		p.addPotionEffect(new PotionEffect(PotionEffectType.CONFUSION, 45 * 20, 3));
		p.addPotionEffect(new PotionEffect(PotionEffectType.HUNGER, 60 * 20, 0));
	}
	public static void bleedEffect(LivingEntity le){
		final Location l = le.getEyeLocation();
		l.setY(l.getY() - 1D);
		le.getWorld().playEffect(l, Effect.STEP_SOUND, 152, 50);
	}
	public static void bandages(final Player p){
		removeNegatives(p);
		if(BloodCycle.ht.containsKey(p)){
			BloodCycle.stop(p);
		}
		p.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 45 * 20, 0));
	}
	public static PotionEffectType[] negatives(){
		return (PotionEffectType[])Arrays.asList(PotionEffectType.BLINDNESS,
				PotionEffectType.CONFUSION,
				PotionEffectType.HARM,
				PotionEffectType.HUNGER,
				PotionEffectType.POISON,
				PotionEffectType.SLOW,
				PotionEffectType.SLOW_DIGGING,
				PotionEffectType.WEAKNESS).toArray();
	}
	public static void removeNegatives(final Player p){
		for(PotionEffectType pet : negatives().clone()){
			if(p.hasPotionEffect(pet)){
				p.removePotionEffect(pet);
			}
		}
	}
	public static final void removeEffect(Player p, PotionEffect pe){
		if(hasEffect(p, pe))
			p.removePotionEffect(pe.getType());
	}
	public static final boolean hasEffect(Player p, PotionEffect pe){
		return p.hasPotionEffect(pe.getType());
	}
	public static final void neurtoxicPoison(final Player target){
		Bukkit.getScheduler().runTaskLater(jp, new BukkitRunnable(){
			@Override
            public void run() {
	            if(target.isOnline()){
	            	target.addPotionEffect(new PotionEffect(PotionEffectType.CONFUSION, 80 * 20, 4));
		            target.addPotionEffect(new PotionEffect(PotionEffectType.HUNGER, 120 * 20, 1));
		            target.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 80 * 20, 0));
		            target.addPotionEffect(new PotionEffect(PotionEffectType.WEAKNESS, 120 * 20, 1));
		            target.addPotionEffect(new PotionEffect(PotionEffectType.SLOW_DIGGING, 120 * 20, 1));
	            }
            }
		}, 120L * 20L);
	}
	public static final void blood(int lvl, final Player target){
		if(lvl == -1)
			return;
		if(lvl == 0){
			target.addPotionEffect(new PotionEffect(PotionEffectType.HEALTH_BOOST, 90 * 20, 0));
			Damageable d = (Damageable)target;
			target.setHealth((d.getHealth() + 10D) >= 24D ? ((Damageable)target).getMaxHealth() : (d.getHealth() + 10D));
		}
		else{
			target.addPotionEffect(new PotionEffect(PotionEffectType.HEALTH_BOOST, 90 * 20, 1));
			target.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 60 * 20, 0));
			Damageable d = (Damageable)target;
			target.setHealth(d.getMaxHealth());
		}
	}
	public static final void arterialPoison(final Player target){
		Bukkit.getScheduler().runTaskLater(jp, new BukkitRunnable(){
			@Override
            public void run() {
	            if(target.isOnline()){
	            	target.addPotionEffect(new PotionEffect(PotionEffectType.CONFUSION, 80 * 20, 4));
		            target.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 80 * 20, 1));
		            target.addPotionEffect(new PotionEffect(PotionEffectType.SLOW_DIGGING, 120 * 20, 1));
		            target.addPotionEffect(new PotionEffect(PotionEffectType.POISON, 50 * 20, 0));
	            }
            }
		}, 45L * 20L);
	}
}
