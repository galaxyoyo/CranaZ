package com.bp389.cranaz.effects;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

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
		final boolean b = p.getAllowFlight();
		p.setAllowFlight(true);
		final WeaponRepresenter twr2 = AimData.getTrueWeapon(weap);
		WeaponAim.fluidlyAim(twr2.getVerticalRecoil(), p, twr2.getHorizontalRecoil(), twr2.getFloatDirection());
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
								Util.relativeTp(p, l.getYaw(), l.getPitch());
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
							Util.relativeTp(p, l.getYaw(), l.getPitch());
							this.f1 = l.getYaw();
							break;
						}
						l.setYaw(this.f1 + scale2);
						this.f1 = l.getYaw();
						Util.relativeTp(p, l.getYaw(), l.getPitch());
						break;
					}
					l.setPitch(this.f0 - scale);
					this.f0 = l.getPitch();
					if(hz_dir == WeaponAim.HORIZONTAL_LEFT) {
						if(this.f1 - scale2 < -180) {
							l.setYaw(180F - Double.valueOf(MathUtil.getDifference(f1 - scale2, -180D)).floatValue());
							Util.relativeTp(p, l.getYaw(), l.getPitch());
							this.f1 = l.getYaw();
							continue;
						}
						l.setYaw(this.f1 - scale2);
						this.f1 = l.getYaw();
						Util.relativeTp(p, l.getYaw(), l.getPitch());
						continue;
					}
					if(this.f1 + scale2 > 180) {
						l.setYaw(-180F + Double.valueOf(MathUtil.getDifference(f1 - scale2, 180D)).floatValue());
						Util.relativeTp(p, l.getYaw(), l.getPitch());
						this.f1 = l.getYaw();
						continue;
					}
					l.setYaw(this.f1 + scale2);
					this.f1 = l.getYaw();
					Util.relativeTp(p, l.getYaw(), l.getPitch());
				}
			}
		}.runTaskAsynchronously(Loader.plugin);
	}
}
