package com.bp389.cranaz.effects;

import java.io.File;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import com.bp389.PluginMethods;
import com.bp389.cranaz.Loader;
import com.bp389.cranaz.Util;
import com.bp389.cranaz.Util.MathUtil;
import com.bp389.cranaz.loots.LootRefactor;

public final class WeaponAim {

	public static final float HORIZONTAL_LEFT = 0F, HORIZONTAL_RIGHT = 1F, HORIZONTAL_RANDOM = 2F, GLOBAL_SCALE = 15F;
	public static final int GS_INT = Float.valueOf(GLOBAL_SCALE).intValue();

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
		final boolean b = p.getAllowFlight();
		p.setAllowFlight(true);
		WeaponAim.fluidlyAim(Config.getVerticalRecoil(weap), p, ft[0], ft[1]);
		p.setAllowFlight(b);	
	}

	public static void handleAim(final float vertical, final Player p, final float hz, final float hz_dir){
		final boolean b = p.getAllowFlight();
		p.setAllowFlight(true);
		WeaponAim.fluidlyAim(vertical, p, hz, hz_dir);
		p.setAllowFlight(b);
	}

	private static void fluidlyAim(final float vertical, final Player p, final float horizontal, final float hz_direction) {
		final Location l = p.getLocation();
		final float scale = vertical / GLOBAL_SCALE, scale2 = horizontal / GLOBAL_SCALE,
				hz_dir = hz_direction == HORIZONTAL_RANDOM ? Integer.valueOf(LootRefactor.random.nextInt(2)).floatValue() : hz_direction;
		new BukkitRunnable() {

			float f0 = l.getPitch(), f1 = l.getYaw();

			@Override
			public void run() {
				for(int i = 0; i < GS_INT; ++i) {
					if(this.f0 - scale < -90F) {
						l.setPitch(-90F);
						p.teleport(l);
						if(hz_dir == WeaponAim.HORIZONTAL_LEFT) {
							if(this.f1 - scale2 < -180) {
								l.setYaw(180F - Double.valueOf(MathUtil.getDifference(f1 - scale2, -180D)).floatValue());
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
							l.setYaw(-180F + Double.valueOf(MathUtil.getDifference(f1 - scale2, 180D)).floatValue());
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
					if(hz_dir == WeaponAim.HORIZONTAL_LEFT) {
						if(this.f1 - scale2 < -180) {
							l.setYaw(180F - Double.valueOf(MathUtil.getDifference(f1 - scale2, -180D)).floatValue());
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
						l.setYaw(-180F + Double.valueOf(MathUtil.getDifference(f1 - scale2, 180D)).floatValue());
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

		public static final File recoil_config = new File("plugins/CranaZ/configuration/recoil.yml");

		public static strictfp float[] getHorizontalRecoil(final String weap) {
			final Object o = Util.getFromYaml(Config.recoil_config, "recoil.values." + weap + ".horizontal.value");
			final String s = ((String)Util.getFromYaml(recoil_config, "recoil.values." + weap + ".horizontal.direction", "RIGHT")).toUpperCase();
			if(o == null){
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
						break;
					case "ak74u":
						f = 0.5F;
				}
				return new float[] { f, 1F };
			}
			if(o instanceof Double)
				return new float[] { Double.valueOf((Double) o).floatValue(), s.equals("LEFT") ? 0F : (s.equals("RIGHT") ? 1F : 2F) };
			return new float[] { Float.valueOf((Float) o).floatValue(), s.equals("LEFT") ? 0F : (s.equals("RIGHT") ? 1F : 2F) };

		}

		public static strictfp float getVerticalRecoil(final String weap) {
				final Object o = Util.getFromYaml(recoil_config, "recoil.values." + weap + ".vertical");
				if(o == null){
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
							break;
						case "ak74u":
							f = 2F;
					}
					return f;
				}
				if(o instanceof Double)
					return Double.valueOf((Double) o).floatValue();
				return Float.valueOf((Float) o).floatValue();
		}
	}
}
