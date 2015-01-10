package com.bp389.cranaz.effects;

import java.io.File;
import java.io.IOException;

import org.bukkit.Location;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import com.bp389.PluginMethods;
import com.bp389.cranaz.Loader;
import com.bp389.cranaz.loots.LootRefactor;

public final class WeaponAim {
	/**
	 * Permet de générer du recul selon les armes
	 * @param weap Le nom de l'arme
	 * @param p Le joueur
	 */
	public static void handleAim(String weap, Player p){
		fluidlyAim(Config.getRecoil(weap), p);
	}
	private static void fluidlyAim(float f, final Player p){
		final Location l = p.getLocation();
		final float scale = f / 25;
		new BukkitRunnable(){
			float f0 = l.getPitch();
			@Override
			public void run(){
				for(int i = 0;i < 25;++i){
					if((f0 + scale) < -90F){
						l.setPitch(-90F);
						p.teleport(l);
						break;
					}
					l.setPitch(f0 - scale);
					l.setYaw(LootRefactor.random.nextInt(2) == 0 ? 
							p.getLocation().getYaw() - LootRefactor.random.nextInt(6) :
						p.getLocation().getYaw() + LootRefactor.random.nextInt(6));
					f0 = l.getPitch();
					p.teleport(l);
				}
			}
		}.runTaskAsynchronously(Loader.plugin);
	}
	public static final class Config{
		public static final File recoil_config = new File("plugins/CranaZ/Divers/recoil.yml");
		public static strictfp float getRecoil(String weap){
			FileConfiguration fc = Loader.plugin.getConfig();
			try {
	            fc.load(recoil_config);
	            Object o = fc.get("recoil.values." + weap);
	            if(o == null){
	            	throw new IOException();
	            }
	            if(o instanceof Double)
	            	return Double.valueOf((Double)o).floatValue();
	            return Float.valueOf((Float)o).floatValue();
            } catch (IOException | InvalidConfigurationException | ClassCastException e) {
            	PluginMethods.alert("Impossible d'obtenir le recul personnalisé. Valeur par défaut.");
            	float f = 0F;
        		switch(weap)
        		{
        		case "Moisin":
        			f = 25;
        			break;
        		case "AK-47":
        			f = 3;
        			break;
        		case "BAR":
        			f = 4;
        			break;
        		case "GrenadeLauncher":
        			f = 50;
        			break;
        		}
        		return f;
            }
		}
	}
}
