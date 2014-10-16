package com.bp389.cranaz.events;

import java.util.List;

import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.world.ChunkLoadEvent;
import org.bukkit.plugin.java.JavaPlugin;

import com.bp389.cranaz.Loadable;
import com.bp389.cranaz.loots.EnumPacks;
import com.bp389.cranaz.loots.LootRefactor;
import com.bp389.cranaz.loots.Loots;
import com.bp389.cranaz.loots.LootRefactor.ThreadSpawner;

public final class ELoots extends GEvent implements Listener{
	public ELoots(JavaPlugin jp) {
	    super(jp);
    }

	@Override
    public Class<? extends Loadable> getRelativePlugin() {
	    return Loots.class;
    }
	
	/*
	 * 
	 */
	
	@EventHandler
	public void chunkLoaded(ChunkLoadEvent e){
		List<ThreadSpawner> lts = Loots.factor.hasLoots(e.getChunk());
		if(lts == null)
			return;
		for(final ThreadSpawner ts : lts){
			ts.respawnItem();
		}
	}
	@EventHandler
	public void playerJoin(PlayerJoinEvent e){
		if(!LootRefactor.spawnsOk){
			LootRefactor.init(e.getPlayer().getWorld(), plugin);
			Loots.startSpawns();
			LootRefactor.spawnsOk = true;
		}
	}
	@SuppressWarnings("static-access")
	@EventHandler
	public void playerInteract(PlayerInteractEvent e)
	{
		if(e.getAction() == Action.RIGHT_CLICK_BLOCK)
		{
			Block block = e.getClickedBlock();
			if(Loots.factor.getLootAt(block.getLocation()) != EnumPacks.NULL)
			{
				if(Loots.factor.isLootBlock(block.getLocation(), Loots.factor.getLootAt(block.getLocation())))
				{
					if(Loots.factor.loots.containsKey(e.getClickedBlock().getLocation())){
						e.getPlayer().openInventory(Loots.factor.loots.get(e.getClickedBlock().getLocation()).getInventoryOf());
					}
					else{
						e.getPlayer().openInventory(Loots.factor.randomInvLoot(EnumPacks.values()[this.rand.nextInt(EnumPacks.values().length)]));
					}
				}
			}
		}
	}
}
