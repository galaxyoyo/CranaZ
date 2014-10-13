package com.bp389.cranaz.effects;

import com.bp389.cranaz.Loadable;

public class Effects extends Loadable{
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
