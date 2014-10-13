package com.bp389.cranaz.events;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.hanging.HangingBreakByEntityEvent;
import org.bukkit.event.hanging.HangingPlaceEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import com.bp389.PluginMethods;
import com.bp389.cranaz.Loadable;
import com.bp389.cranaz.items.Items;
import com.bp389.cranaz.legacy.Legacy;
import com.bp389.cranaz.legacy.NQHandler;

public class EItemsLegacy extends GEvent implements Listener{
	public EItemsLegacy(JavaPlugin jp) {
	    super(jp);
    }
	@Override
    public Class<? extends Loadable> getRelativePlugin() {
	    return Legacy.class;
    }
	
	/*
	 * 
	 */
	
	@EventHandler
	public void playerWelcome(PlayerJoinEvent e){
		if(!e.getPlayer().hasPlayedBefore()){
			Bukkit.getServer().broadcastMessage(ChatColor.DARK_AQUA + "Bienvenue à " + e.getPlayer().getName() + " sur CranaZ. Bonne survie à lui !" + ChatColor.RESET);
			PluginMethods.gsay(e.getPlayer(), "Bienvenue à toi. N'oublie pas de lire les règles, et tu peux aussi jeter un oeil au guide du survie.");
			Items.Diaries.Utils.giveUtils(e.getPlayer());
		}
	}

	/*@EventHandler
	public void verifyLogin(final PlayerLoginEvent e){
		if(!CZL.Utils.isPremium(e.getPlayer()) && !CZL.Utils.isWhitelist(e.getPlayer())){
			e.disallow(Result.KICK_WHITELIST, "La connexion a echouee. User not premium ?");
		}
	}*/
	@EventHandler
	public void playerJoin4(final PlayerJoinEvent e){
		if(!e.getPlayer().hasPlayedBefore()){
			e.getPlayer().teleport(new Location(e.getPlayer().getWorld(), 110D, 56D, 71D));
		}
	}
	@EventHandler
	public void spawnProtect(EntityDamageByEntityEvent e){
		if(e.getEntity().getType() == EntityType.PLAYER && e.getDamager().getType() == EntityType.PLAYER){
			final Player p = (Player)e.getEntity();
			e.setCancelled(p.getLocation().distance(new Location(p.getWorld(), 110.5D, 56D, 71.5D)) <= 40D ? true : false);
		}
	}
	@EventHandler
	public void fragileBreak(final PlayerInteractEvent e){
		if(e.getAction() == Action.LEFT_CLICK_BLOCK){
			boolean tb = false;
			switch(e.getClickedBlock().getType()){
			case GLASS:
				tb = true;
				break;
			case THIN_GLASS:
				tb = true;
				break;
			case PAINTING:
				tb = true;
				break;
			}
			final int distance = Double.valueOf(e.getClickedBlock().getLocation().distance(new Location(e.getPlayer().getWorld(), 110D, 56D, 71D))).intValue();
			if(tb && distance > 20){
				plugin.getServer().getPluginManager().callEvent(new BlockBreakEvent(e.getClickedBlock(), e.getPlayer()));
				e.getClickedBlock().setType(Material.AIR);
			}
		}
		else if(e.getAction() == Action.PHYSICAL && e.getClickedBlock().getType() == Material.WHEAT)
			e.setCancelled(true);
	}
	@EventHandler
	public void verifyQuit(final PlayerQuitEvent e){
		if(NQHandler.isInCombat(e.getPlayer()) && !e.getPlayer().isOp()){
			e.setQuitMessage(e.getPlayer().getDisplayName() + " §r§cs'est déconnecté en combat.§r");
			e.getPlayer().damage(100D);
		}
	}
	@EventHandler
	public void playerPlace(final BlockPlaceEvent e){
		if(e.getPlayer().isOp())
			return;
		boolean cancel = true;
		switch(e.getBlock().getType()){
		case SIGN:
			cancel = false;
			break;
		case WALL_SIGN:
			cancel = false;
			break;
		case SIGN_POST:
			cancel = false;
			break;
		case FENCE:
			cancel = false;
			break;
		}
		e.setCancelled(e.getPlayer().isOp() ? false : cancel);
	}
	@EventHandler
	public void itemRemoved(EntityDamageByEntityEvent e){
		if(e.getDamager().getType() == EntityType.PLAYER){
			Player p = (Player)e.getDamager();
			if(p.isOp())
				return;
		}
		if(e.getEntity().getType() == EntityType.ITEM_FRAME || e.getEntity().getType() == EntityType.PAINTING)
			e.setCancelled(true);
		else if(e.getEntity().getType() == EntityType.PLAYER){
			NQHandler.handleNoDeco((Player)e.getEntity());
		}
	}
	@EventHandler
	public void itemFrameBreaked(final HangingBreakByEntityEvent e){
		if(e.getEntity().getType() == EntityType.PAINTING){
			new BukkitRunnable(){
				@Override
				public void run() {
					e.getRemover().getWorld().getBlockAt(e.getEntity().getLocation()).setType(Material.PAINTING);
				}
			}.runTaskLater(plugin, 6000L);
			return;
		}
		if(e.getRemover().getType() == EntityType.PLAYER){
			Player p = (Player)e.getRemover();
			if(p.isOp())
				return;
		}
		e.setCancelled(true);
	}
	@EventHandler
	public void playerDie(PlayerDeathEvent e){
		String s = e.getEntity().getDisplayName();
		if(e.getEntity().getLastDamageCause() == null || e.getEntity().getLastDamageCause().getCause() == null)
			return;
		switch(e.getEntity().getLastDamageCause().getCause()){
		case BLOCK_EXPLOSION:
			e.setDeathMessage(s + " §r§ca explosé !§r");
			break;
		case ENTITY_EXPLOSION:
			e.setDeathMessage(s + " §r§ca explosé !§r");
			break;
		case CONTACT:
			e.setDeathMessage(s + " §r§aa frôlé la mort.§r");
			break;
		case FALL:
			e.setDeathMessage(s + " §r§cs'est ecrasé sur le sol.§r");
			break;
		case FALLING_BLOCK:
			e.setDeathMessage(s + " §r§bs'est fait écraser.§r");
			break;
		case FIRE:
			e.setDeathMessage(s + " §r§ca brûlé !§r");
			break;
		case FIRE_TICK:
			e.setDeathMessage(s + " §r§ca brûlé !§r");
			break;
		case LAVA:
			e.setDeathMessage(s + " §r§ca brûlé !§r");
			break;
		case CUSTOM:
			e.setDeathMessage(s + " §r§dest mort.§r");
			break;
		case DROWNING:
			e.setDeathMessage(s + " §r§9s'est noyé.§r");
			break;
		case LIGHTNING:
			e.setDeathMessage(s + " §r§6s'est fait foudroyer.§r");
			break;
		case PROJECTILE:
			e.setDeathMessage(s + " §r§8s'est fait tirer dessus.§r");
			break;
		case STARVATION:
			e.setDeathMessage(s + " §r§aest mort de faim.§r");
			break;
		case SUFFOCATION:
			e.setDeathMessage(s + " §r§7s'est asphyxié.§r");
			break;
		case SUICIDE:
			e.setDeathMessage(s + " §r§2s'est donné la mort.§r");
			break;
		case VOID:
			e.setDeathMessage(s + " §r§8a fait un tour dans l'espace.§r");
			break;
		case THORNS:
			e.setDeathMessage(s + " §r§as'est piqué mortellement.§r");
			break;
		case POISON:
			e.setDeathMessage(s + " §r§2s'est fait empoisonner.§r");
		}
	}
	@EventHandler
	public void itemFramePlaced(HangingPlaceEvent e){
		if(e.getPlayer().isOp())
			return;
		e.setCancelled(true);
	}
	@EventHandler
	public void blockBreak(final BlockBreakEvent e){
		if(e.getPlayer().isOp())
			return;
		final int distance = Double.valueOf(e.getBlock().getLocation().distance(new Location(e.getPlayer().getWorld(), 110D, 56D, 71D))).intValue();
		if(distance <= 20){
			e.setCancelled(true);
		}
		boolean cancel = true;
		Sound s = null;
		final Material m = e.getBlock().getType();
		final Location l = e.getBlock().getLocation();
		switch(e.getBlock().getType()){
		case GLASS:
			cancel = false;
			s = Sound.GLASS;
			break;
		case THIN_GLASS:
			cancel = false;
			s = Sound.GLASS;
			break;
		case PAINTING:
			cancel = false;
			s = Sound.ITEM_BREAK;
			break;
		case SIGN:
			cancel = false;
			break;
		case WALL_SIGN:
			cancel = false;
			break;
		case SIGN_POST:
			cancel = false;
			break;
		case FENCE:
			cancel = false;
			break;
		}
		if(s != null)
			e.getBlock().getWorld().playSound(e.getBlock().getLocation(), s, 15F, 63F);
		if(!cancel && !(m.toString().toLowerCase().contains("SIGN") || m == Material.FENCE)){
			new BukkitRunnable(){
				@Override
				public void run() {
					if(e.getBlock() == null || e.getBlock().getType() == Material.AIR){
						e.getPlayer().getWorld().getBlockAt(l).setType(m);
					}
				}
			}.runTaskLater(plugin, 6000L);
		}
		e.setCancelled(true);
		if(!cancel)
			e.getBlock().setType(Material.AIR);
	}
}
