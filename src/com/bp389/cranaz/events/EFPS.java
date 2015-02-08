package com.bp389.cranaz.events;

import static com.bp389.PluginMethods.alert;
import static com.bp389.PluginMethods.gsay;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.entity.Damageable;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;

import com.bp389.PluginMethods;
import com.bp389.cranaz.Loadable;
import com.bp389.cranaz.Loader;
import com.bp389.cranaz.Util.MathUtil;
import com.bp389.cranaz.FPS.Arena;
import com.bp389.cranaz.FPS.ArenaSchedule;
import com.bp389.cranaz.FPS.ArenaTemp;
import com.bp389.cranaz.FPS.FPS;
import com.bp389.cranaz.FPS.FPSIO;
import com.bp389.cranaz.FPS.ObjectiveExplosion;
import com.bp389.cranaz.FPS.classes.Classes;
import com.bp389.cranaz.FPS.classes.GameClass;
import com.bp389.cranaz.FPS.classes.Trait;
import com.shampaggon.crackshot.events.WeaponPreShootEvent;


public final class EFPS extends GEvent implements Listener{
	public EFPS(JavaPlugin jp) {
		super(jp);
	}
	@Override
	public Class<? extends Loadable> getRelativePlugin() {
		return FPS.class;
	}

	@EventHandler
	public void playerDamaged(final EntityDamageEvent e){
		if(e.getEntityType() != EntityType.PLAYER)
			return;
		final Player p = (Player)e.getEntity();
		if(Arena.isInArena(p) && ((Damageable)p).getHealth() - e.getDamage() <= 0D){
			e.setCancelled(true);
			Arena a = Arena.hasJoined.get(p);
			p.setHealth(20D);
			GameClass.handlePlayerClass(p);
			p.teleport(a.getTeam(p) == Arena.A ? a.getTeamAStartLocation() : a.getTeamBStartLocation());
		}
	}
	@EventHandler
	public void playerInteract(final PlayerInteractEvent e){
		if(e.getAction() != Action.LEFT_CLICK_BLOCK && e.getAction() != Action.RIGHT_CLICK_BLOCK)
			return;
		if(FPSIO.hm.containsKey(e.getPlayer()) && FPSIO.hm.get(e.getPlayer()) != null){
			e.setCancelled(true);
			if(e.getAction() == Action.LEFT_CLICK_BLOCK){
				gsay(e.getPlayer(), "Création annulée.");
				FPSIO.hm.remove(e.getPlayer());
			} 

			final ArenaTemp at = FPSIO.hm.get(e.getPlayer());
			if(at == null)
				return;
			final Player p = e.getPlayer();
			switch(at.state){
				case 0:
					at.lobby = MathUtil.trueLoc(e.getClickedBlock().getLocation());
					gsay(p, "Lobby défini. Définissez le point de départ A.");
					break;
				case 1:
					at.teamAStart = MathUtil.trueLoc(e.getClickedBlock().getLocation());
					gsay(p, "Point de départ A défini. Définissez le point B.");
					break;
				case 2:
					at.teamBStart = MathUtil.trueLoc(e.getClickedBlock().getLocation());
					gsay(p, "Point de départ B défini. Définissez l'objectif A.");
					break;
				case 3:
					at.aObj = MathUtil.trueLoc(e.getClickedBlock().getLocation());
					gsay(p, "Objectif A défini. Définissez l'objectif B.");
					break;
				case 4:
					at.bObj = MathUtil.trueLoc(e.getClickedBlock().getLocation());
					gsay(p, "Objectif B défini. Définissez la position globale de l'arène.");
					break;
				case 5:
					at.mainArena = MathUtil.trueLoc(e.getClickedBlock().getLocation());
					gsay(p, "Position définie. Définissez la position de sortie.");
					break;
				case 6:
					at.exitLoc = MathUtil.trueLoc(e.getClickedBlock().getLocation());
					gsay(p, "Création terminée. Ajoutez des panneaux de téléportation.");
					FPSIO.hm.remove(e.getPlayer());
					FPSIO.createAndStartArena(at);

			}
			at.state += 1;
			return;
		}

		final Block b = e.getClickedBlock();
		if(b.getType() == Material.SIGN || b.getType() == Material.SIGN_POST || b.getType() == Material.WALL_SIGN){
			final Sign s = (Sign)b.getState();
			if(s.getLine(0).startsWith("goto fight")){
				final Arena a = FPSIO.getArenaFromName(s.getLine(1));
				if(a == null || s == null)
					return;
				a.addPlayer(e.getPlayer(), s);
			}
			else if(s.getLine(0).startsWith("exit fight")){
				if(!Arena.isInArena(e.getPlayer())){
					e.getPlayer().sendMessage(ChatColor.RED + "Tu n'es pas dans une arène !" + ChatColor.RESET);
					return;
				}
				final Arena a = Arena.hasJoined.get(e.getPlayer());
				if(a == null)
					return;
				a.forceRemove(e.getPlayer(), false, true);
			}
			else if(s.getLine(0).startsWith("choose class") && Arena.isInArena(e.getPlayer())){
				e.getPlayer().openInventory(GameClass.generateMenu());
			}
		}
		else if(b.getType() == Material.REDSTONE_BLOCK){
			if(!Arena.objectives.containsKey(b.getLocation()))
				return;
			final Arena a = Arena.objectives.get(b.getLocation());
			int i = a.getObjectivePos(b.getLocation()), i2 = a.getTeam(e.getPlayer());
			if(i == Arena.NOT || i2 == Arena.NOT)
				return;
			if(i == Arena.A)
				return;
			if(i != i2 && i == Arena.B && i2 == Arena.A){
				if(ObjectiveExplosion.goingTo.containsKey(b.getLocation())){
					e.getPlayer().sendMessage("Cet objectif est déjà amorçé !");
					return;
				}
				a.broadcastGame(ChatColor.DARK_BLUE + "L'objectif " + (i == Arena.A ? "A" : "B") + " explose dans " + String.valueOf(FPSIO.getExplosionDelay()) + " secondes !" + ChatColor.RESET);
				new ObjectiveExplosion(b, a).runTaskLater(Loader.plugin, FPSIO.getExplosionDelay() * 20L);
			} else if(i == i2 && i == Arena.B){
				if(!ObjectiveExplosion.goingTo.containsKey(b.getLocation()))
					return;
				a.broadcastGame(ChatColor.DARK_BLUE + e.getPlayer().getDisplayName() + " a désamorcé l'objectif " + (i == Arena.A ? "A" : "B") + " !" + ChatColor.RESET);
				ObjectiveExplosion.goingTo.get(b.getLocation()).cancel();
				ObjectiveExplosion.goingTo.remove(b.getLocation());
				new ArenaSchedule(a).runTaskLater(Loader.plugin, a.getMatchDelay() * 20L);
			}
		}
	}
	@EventHandler
	public void airstrike(final WeaponPreShootEvent e){
		if(e.getWeaponTitle().equalsIgnoreCase("Airstrike") && Arena.isInArena(e.getPlayer())){
			if(!GameClass.getPlayerClass(e.getPlayer()).hasTrait(Trait.AIRSTRIKE_CALLER)){
				PluginMethods.alert(e.getPlayer(), "Tu n'es pas habilité à appeler du soutien aérien !");
				e.setCancelled(true);
			}
		}
	}
	@EventHandler
	public void playerBreak(final BlockBreakEvent e){
		if(Arena.isInArena(e.getPlayer()) && !e.getPlayer().isOp()){
			e.setCancelled(true);
			return;
		}
		final Block b = e.getBlock();
		if(b.getType() == Material.SIGN || b.getType() == Material.SIGN_POST || b.getType() == Material.WALL_SIGN){
			Sign s = (Sign)e.getBlock().getState();
			if((s.getLine(0).startsWith("goto fight") || s.getLine(0).startsWith("exit fight")) && !e.getPlayer().isOp())
				e.setCancelled(true);
		}
	}
	@EventHandler 
	public void playerPlace(final BlockPlaceEvent e){
		final Block b = e.getBlockPlaced();
		if(b.getType() == Material.SIGN || b.getType() == Material.SIGN_POST || b.getType() == Material.WALL_SIGN){
			Sign s = (Sign)b.getState();
			if(s.getLine(0).startsWith("goto fight") || s.getLine(0).startsWith("exit fight")){
				if(!e.getPlayer().isOp()){
					e.setCancelled(true);
					alert(e.getPlayer(), "Vous n'avez pas le droit de placer ce type de panneaux !");
					return;
				}
				else if(!Arena.arenas.containsKey(s.getLine(1))){
					e.setCancelled(true);
					alert(e.getPlayer(), "Cette arène n'existe pas !");
					return;
				}
			}

		}
	}
	@EventHandler
	public void playerJoin(final PlayerJoinEvent pje){
		if(FPSIO.init)
			return;
		FPSIO.init = true;
		FPSIO.w = pje.getPlayer().getWorld();
		FPSIO.startArenas();
	}
	@EventHandler
	public void playerChooseClass(final InventoryClickEvent e){
		if(e.getInventory().getTitle().equalsIgnoreCase("CHOISIR UNE CLASSE")){
			e.setCancelled(true);
			final Material m = e.getCurrentItem().getType();
			final Player p = (Player)e.getWhoClicked();
			Classes c = Classes.ASSAULT;
			if(m == Classes.HEALER.getIcon())
				c = Classes.HEALER;
			else if(m == Classes.ASSAULT.getIcon())
				c = Classes.ASSAULT;
			else if(m == Classes.HEAVY.getIcon())
				c = Classes.HEAVY;
			else if(m == Classes.SNIPER.getIcon())
				c = Classes.SNIPER;
			else if(m == Classes.RUSHER.getIcon())
				c = Classes.RUSHER;
			else if(m == Classes.TACTICAL.getIcon())
				c = Classes.TACTICAL;
			GameClass.setPlayerClass(p, c);
			PluginMethods.gsay(p, "Tu seras " + c.getDisplayedName());
		}
	}
}
