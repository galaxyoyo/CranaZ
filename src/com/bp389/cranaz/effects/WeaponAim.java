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

public final class WeaponAim {

	protected static final float HORIZONTAL_LEFT = 0F, HORIZONTAL_RIGHT = 1F;

	/**
	 * Permet de générer du recul selon les armes
	 * 
	 * @param weap
	 *            Le nom de l'arme
	 * @param p
	 *            Le joueur
	 */
	public static void handleAim(final String weap, final Player p) {
		final float[] ft = Config.getHorizontalRecoil(weap);
		boolean b = p.getAllowFlight();
		p.setAllowFlight(true);
		WeaponAim.fluidlyAim(Config.getVerticalRecoil(weap), p, ft[0], ft[1]);
		p.setAllowFlight(b);	
	}

	private static void fluidlyAim(final float vertical, final Player p, final float horizontal, final float hz_direction) {
		final Location l = p.getLocation();
		final float scale = vertical / 25, scale2 = horizontal / 25;
		new BukkitRunnable() {

			float f0 = l.getPitch(), f1 = l.getYaw();

			@Override
			public void run() {
				for(int i = 0; i < 25; ++i) {
					if(this.f0 - scale < -90F) {
						l.setPitch(-90F);
						p.teleport(l);
						if(hz_direction == WeaponAim.HORIZONTAL_LEFT) {
							if(this.f1 - scale2 < -180) {
								l.setYaw(180F);
								p.teleport(l);
								this.f1 = l.getYaw();
								break;
							}
							l.setYaw(this.f1 - scale2);
							this.f1 = l.getYaw();
							p.teleport(l);
							break;
						}
						if(this.f1 + scale2 > 180) {
							l.setYaw(-180F);
							p.teleport(l);
							this.f1 = l.getYaw();
							break;
						}
						l.setYaw(this.f1 + scale2);
						this.f1 = l.getYaw();
						p.teleport(l);
						break;
					}
					l.setPitch(this.f0 - scale);
					this.f0 = l.getPitch();
					if(hz_direction == WeaponAim.HORIZONTAL_LEFT) {
						if(this.f1 - scale2 < -180) {
							l.setYaw(180F);
							p.teleport(l);
							this.f1 = l.getYaw();
							continue;
						}
						l.setYaw(this.f1 - scale2);
						this.f1 = l.getYaw();
						p.teleport(l);
						continue;
					}
					if(this.f1 + scale2 > 180) {
						l.setYaw(-180F);
						p.teleport(l);
						this.f1 = l.getYaw();
						continue;
					}
					l.setYaw(this.f1 + scale2);
					this.f1 = l.getYaw();
					p.teleport(l);
				}
			}
		}.runTaskAsynchronously(Loader.plugin);
	}

	public static final class Config {

		public static final File recoil_config = new File("plugins/CranaZ/Divers/recoil.yml");

		public static strictfp float[] getHorizontalRecoil(final String weap) {
			final FileConfiguration fc = Loader.plugin.getConfig();
			try {
				fc.load(Config.recoil_config);
				final Object o = fc.get("recoil.values." + weap + ".horizontal.value");
				final String s = fc.getString("recoil.values." + weap + ".horizontal.direction", "RIGHT").toUpperCase();
				if(o == null)
					throw new IOException();
				if(o instanceof Double)
					return new float[] { Double.valueOf((Double) o).floatValue(), s.equals("LEFT") ? 0F : 1F };
				return new float[] { Float.valueOf((Float) o).floatValue(), s.equals("LEFT") ? 0F : 1F };
			} catch(IOException | InvalidConfigurationException | ClassCastException e) {
				PluginMethods.alert("Impossible d'obtenir le recul personnalisé. Valeur par défaut.");
				float f = 0F;
				switch(weap.toLowerCase()) {
					case "moisin":
						f = 5F;
						break;
					case "ak-47":
						f = 1F;
						break;
					case "bar":
						f = 1.5F;
						break;
					case "grenadelauncher":
						f = 10F;
						break;
					case "smith":
						f = 2F;
				}
				return new float[] { f, 1F };
			}
		}

		public static strictfp float getVerticalRecoil(final String weap) {
			final FileConfiguration fc = Loader.plugin.getConfig();
			try {
				fc.load(Config.recoil_config);
				final Object o = fc.get("recoil.values." + weap + ".vertical");
				if(o == null)
					throw new IOException();
				if(o instanceof Double)
					return Double.valueOf((Double) o).floatValue();
				return Float.valueOf((Float) o).floatValue();
			} catch(IOException | InvalidConfigurationException | ClassCastException e) {
				PluginMethods.alert("Impossible d'obtenir le recul personnalisé. Valeur par défaut.");
				float f = 0F;
				switch(weap.toLowerCase()) {
					case "moisin":
						f = 25F;
						break;
					case "ak-47":
						f = 3F;
						break;
					case "bar":
						f = 4F;
						break;
					case "grenadelauncher":
						f = 50F;
						break;
					case "smith":
						f = 5F;
				}
				return f;
			}
		}
	}
}
