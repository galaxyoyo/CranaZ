package com.bp389.cranaz.events;

import java.util.ArrayList;

import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.craftbukkit.v1_8_R1.entity.CraftEntity;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.hanging.HangingBreakByEntityEvent;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;

import net.minecraft.server.v1_8_R1.Entity;
import net.minecraft.server.v1_8_R1.EntityArmorStand;

import com.bp389.PluginMethods;
import com.bp389.cranaz.Loadable;
import com.bp389.cranaz.loots.LootRefactor;
import com.bp389.cranaz.loots.Loots;

public final class ELoots extends GEvent implements Listener {

	public static final ArrayList<Player> editers = new ArrayList<Player>();
	public ELoots(final JavaPlugin jp) {
		super(jp);
	}

	@Override
	public Class<? extends Loadable> getRelativePlugin() {
		return Loots.class;
	}

	/*
	 * 
	 */

	/*@EventHandler
	public void chunkLoaded(final ChunkLoadEvent e) {
		final List<ThreadSpawner> lts = Loots.factor.hasLoots(e.getChunk());
		if(lts == null)
			return;
		for(final ThreadSpawner ts : lts)
			ts.respawnItem();
	}*/

	@EventHandler
	public void playerJoin(final PlayerJoinEvent e) {
		if(!LootRefactor.spawnsOk) {
			LootRefactor.init(e.getPlayer().getWorld(), this.plugin, e.getPlayer().getServer());
			Loots.startSpawns();
			LootRefactor.spawnsOk = true;
		}
	}

	/*@SuppressWarnings("static-access")
	@EventHandler
	public void playerInteract(final PlayerInteractEvent e) {
		if(e.getAction() == Action.RIGHT_CLICK_BLOCK) {
			final Block block = e.getClickedBlock();
			if(Loots.factor.getLootAt(block.getLocation()) != EnumPacks.NULL)
				if(Loots.factor.isLootBlock(block.getLocation(), Loots.factor.getLootAt(block.getLocation())))
					if(Loots.factor.loots.containsKey(e.getClickedBlock().getLocation()))
						e.getPlayer().openInventory(Loots.factor.loots.get(e.getClickedBlock().getLocation()).getInventoryOf());
					else
						e.getPlayer().openInventory(Loots.factor.randomInvLoot(EnumPacks.values()[this.rand.nextInt(EnumPacks.values().length)]));
		}
	}*/
	@EventHandler
	public void lootBreak(HangingBreakByEntityEvent e){
		if(e.getRemover().getType() == EntityType.PLAYER && e.getEntity().getType() == EntityType.ARMOR_STAND){
			if(((Player)e.getRemover()).isOp())
				return;
		}
		e.setCancelled(true);
	}
	@EventHandler
	public void lootDamage(EntityDamageByEntityEvent e){
		if(e.getDamager().getType() == EntityType.PLAYER && e.getEntity().getType() == EntityType.ARMOR_STAND){
			if(((Player)e.getDamager()).isOp())
				return;
			e.setCancelled(true);
		} else if(e.getEntity().getType() == EntityType.ARMOR_STAND)
			e.setCancelled(true);
	}
	@EventHandler
	public void playerInteractLoot(PlayerInteractAtEntityEvent e){
		if(e.getRightClicked().getType() == EntityType.ARMOR_STAND && ELoots.editers.contains(e.getPlayer())){
			Entity e2 = ((CraftEntity)e.getRightClicked()).getHandle();
			if(Loots.factor.deleteLootPoint((EntityArmorStand)e2))
				PluginMethods.gsay(e.getPlayer(), "Point de loot supprimé.");
			else
				PluginMethods.alert(e.getPlayer(), "Impossible de supprimer le loot.");
			e.setCancelled(true);
		}
		else if(e.getRightClicked().getType() == EntityType.ARMOR_STAND){
			if((e.getPlayer().getItemInHand() != null && e.getPlayer().getItemInHand().getType() != Material.AIR) && !e.getPlayer().isOp())
				e.setCancelled(true);
		}



		if(e.getRightClicked().getType() == EntityType.ARMOR_STAND){
			if((e.getPlayer().getItemInHand().getType() == Material.AIR || e.getPlayer().getItemInHand() == null) &&
					(((ArmorStand)e.getRightClicked()).getItemInHand().getType() == Material.AIR || ((ArmorStand)e.getRightClicked()).getItemInHand() == null))
				e.getPlayer().playSound(e.getPlayer().getLocation(), Sound.ITEM_PICKUP, 1, 1);
		}
	}
}
