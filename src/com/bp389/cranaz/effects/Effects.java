package com.bp389.cranaz.effects;

import static com.bp389.cranaz.effects.AimData.AK47;
import static com.bp389.cranaz.effects.AimData.AK74U;
import static com.bp389.cranaz.effects.AimData.BAR;
import static com.bp389.cranaz.effects.AimData.M320H;
import static com.bp389.cranaz.effects.AimData.MOSIN;
import static com.bp389.cranaz.effects.AimData.P90;
import static com.bp389.cranaz.effects.AimData.PP70B;
import static com.bp389.cranaz.effects.AimData.SMITH;
import static com.bp389.cranaz.effects.AimData.writeWeapon;

import com.bp389.cranaz.Loadable;

public class Effects extends Loadable {

	@Override
	public void onEnable() {
		if(!AimData.recoil_config.exists())
			writeWeapon(AK47, AK74U, MOSIN, BAR, SMITH, P90, PP70B, M320H);
		AimData.putWeaponData(AK47, AK74U, MOSIN, BAR, SMITH, P90, PP70B, M320H);
	}
	/**
	 * Pour la commande /die -> réanimation, CF Events
	 */
	/*
	 * @Override public boolean onCommand(CommandSender sender, Command command,
	 * String label, String[] args) {
	 * if(command.getName().equalsIgnoreCase("die")){ if(!(sender instanceof
	 * Player)) return true; Player p = (Player)sender;
	 * if(!ReviveHandler.isHemoragic(p)){
	 * p.sendMessage("§r§cVous n'etes pas à terre !§r"); return true; }
	 * EventsFactor.rh.die(p); } return true; }
	 */
}
