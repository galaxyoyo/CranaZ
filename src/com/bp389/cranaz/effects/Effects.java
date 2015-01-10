package com.bp389.cranaz.effects;

import java.io.IOException;

import org.bukkit.configuration.file.FileConfiguration;

import com.bp389.cranaz.Loadable;
import com.bp389.cranaz.Loader;

public class Effects extends Loadable{
	@Override
	public void onEnable() {
	    if(!WeaponAim.Config.recoil_config.exists()){
	    	try {
	            WeaponAim.Config.recoil_config.createNewFile();
	            final FileConfiguration fc = Loader.plugin.getConfig();
	            fc.set("recoil.values.Moisin", 25F);
	            fc.set("recoil.values.BAR", 4F);
	            fc.set("recoil.values.AK-47", 3F);
	            fc.set("recoil.values.GrenadeLauncher", 50F);
	            fc.save(WeaponAim.Config.recoil_config);
            } catch (IOException e) {}
	    }
	}
	/**
	 * Pour la commande /die -> réanimation, CF Events
	 */
	/*@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
	    if(command.getName().equalsIgnoreCase("die")){
	    	if(!(sender instanceof Player))
	    		return true;
	    	Player p = (Player)sender;
	    	if(!ReviveHandler.isHemoragic(p)){
	    		p.sendMessage("§r§cVous n'etes pas à terre !§r");
	    		return true;
	    	}
	    	EventsFactor.rh.die(p);
	    }
	    return true;
	}*/
}
