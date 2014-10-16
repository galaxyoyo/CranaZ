package com.bp389.cranaz.events;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.java.JavaPlugin;

import com.bp389.cranaz.Loadable;
import com.bp389.cranaz.bags.Bags;
import com.bp389.cranaz.bags.IPlayerFactor;
import com.bp389.cranaz.items.Items;

public final class EBags extends GEvent implements Listener {
	public EBags(JavaPlugin plugin) {
	    super(plugin);
    }
	@Override
    public Class<? extends Loadable> getRelativePlugin() {
		return Bags.class;
    }
	
	/*
	 * 
	 */
	
	@EventHandler
	public void playerLogin(PlayerLoginEvent e){
		IPlayerFactor.loadPlayer(e.getPlayer());
	}
	@EventHandler
	public void playerQuit(PlayerQuitEvent e){
		IPlayerFactor.unloadPlayer(e.getPlayer());
	}
	@EventHandler
	public void playerKick(PlayerKickEvent e){
		IPlayerFactor.unloadPlayer(e.getPlayer());
	}
	@EventHandler
	public void inventoryClick(InventoryClickEvent e)
	{
		if(e.getClick() == ClickType.RIGHT && e.getCurrentItem().getType() == Items.BAG)
		{
			Player p = (Player)e.getWhoClicked();
			p.openInventory(IPlayerFactor.bagOf(p));
			e.setCancelled(true);
		}
	}
	@EventHandler
	public void playerDie4(PlayerDeathEvent e){
		IPlayerFactor.clearBag(e.getEntity());
	}
}
