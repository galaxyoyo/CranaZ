package com.bp389.cranaz.effects;

import com.bp389.cranaz.Loadable;
import com.bp389.cranaz.Util;
import com.bp389.cranaz.YamlObj;

public class Effects extends Loadable {

	@Override
	public void onEnable() {
		if(!WeaponAim.Config.recoil_config.exists())
			Util.saveToYaml(WeaponAim.Config.recoil_config, new YamlObj(verticalPath("Moisin"), 25F),
					new YamlObj(verticalPath("BAR"), 4F),
					new YamlObj(verticalPath("AK-47"), 3F),
					new YamlObj(verticalPath("Smith"), 5F),
					new YamlObj(verticalPath("AK74u"), 2F),
					new YamlObj(verticalPath("GrenadeLauncher"), 50F),

					new YamlObj(horizontalPath("Moisin"), 3F),
					new YamlObj(horizontalPath("BAR"), 1.5F),
					new YamlObj(horizontalPath("AK-47"), 1F),
					new YamlObj(horizontalPath("Smith"), 2F),
					new YamlObj(horizontalPath("AK74u"), 0.5F),
					new YamlObj(horizontalPath("GrenadeLauncher"), 5F),

					new YamlObj(horizontalDir("Moisin"), "RIGHT"),
					new YamlObj(horizontalDir("BAR"), "RIGHT"),
					new YamlObj(horizontalDir("AK-47"), "RIGHT"),
					new YamlObj(horizontalDir("Smith"), "LEFT"),
					new YamlObj(horizontalDir("AK74u"), "LEFT"),
					new YamlObj(horizontalDir("GrenadeLauncher"), "LEFT"));
	}
	public String verticalPath(String name){
		return "recoil.values." + name + ".vertical";
	}
	public String horizontalPath(String name){
		return "recoil.values." + name + ".horizontal.value";
	}
	public String horizontalDir(String name){
		return "recoil.values." + name + ".horizontal.direction";
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
