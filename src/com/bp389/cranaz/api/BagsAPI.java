package com.bp389.cranaz.api;

import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.plugin.java.JavaPlugin;

import com.bp389.cranaz.bags.IPlayerFactor;


public final class BagsAPI extends CranaZAPI {
	public BagsAPI(JavaPlugin plugin) {
	    super(plugin);
    }
	
	/**
	 * 
	 * @param thePlayer Le joueur
	 * @return Le sac a dos du joueur sous forme d'inventaire
	 */
	public Inventory getPlayerBag(final Player thePlayer){
		return IPlayerFactor.bagOf(thePlayer);
	}
	/**
	 * Efface entierement le contenu du sac a dos d'un joueur
	 * @param target Le joueur cible
	 */
	public void clearBagOf(final Player target){
		IPlayerFactor.clearBag(target);
	}
}
