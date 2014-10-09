package com.bp389.cranaz;

import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import net.citizensnpcs.api.event.NPCRightClickEvent;
import net.minecraft.server.v1_7_R3.Blocks;
import net.minecraft.server.v1_7_R3.PacketPlayOutEntityEquipment;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.craftbukkit.v1_7_R3.entity.CraftEntity;
import org.bukkit.craftbukkit.v1_7_R3.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_7_R3.inventory.CraftItemStack;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.hanging.HangingBreakByEntityEvent;
import org.bukkit.event.hanging.HangingPlaceEvent;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType.SlotType;
import org.bukkit.event.player.PlayerExpChangeEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.event.server.ServerListPingEvent;
import org.bukkit.event.world.ChunkLoadEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import com.bp389.PluginMethods;
import com.bp389.cranaz.bags.IPlayerFactor;
import com.bp389.cranaz.effects.Bleed;
import com.bp389.cranaz.ia.GoMenu;
import com.bp389.cranaz.ia.ResPackMenu;
import com.bp389.cranaz.ia.VirtualSpawner;
import com.bp389.cranaz.ia.ZIA;
import com.bp389.cranaz.ia.entities.EnhancedZombie;
import com.bp389.cranaz.items.Items;
import com.bp389.cranaz.legacy.NQHandler;
import com.bp389.cranaz.loots.EnumPacks;
import com.bp389.cranaz.loots.LAP;
import com.bp389.cranaz.loots.LootRefactor;
import com.bp389.cranaz.loots.LootRefactor.ThreadSpawner;
import com.bp389.cranaz.thirst.ThirstFactor;
import com.bp389.cranaz.thirst.ThirstRunnable;
import com.shampaggon.crackshot.events.WeaponPreShootEvent;
import com.shampaggon.crackshot.events.WeaponScopeEvent;

import javax.imageio.ImageIO;

/**
 * Classe générale des events.
 * FIXME Fragmenter en plusieurs classes
 * @author BlackPhantom
 *
 */
public class Events implements Listener {
	//TODO GLOBAL
	public static final Random rand = new Random();
	//public static ReviveHandler rh;
	private static JavaPlugin jp;
	public static Inventory goGui, resGui;
	public static ArrayList<Player> playings = new ArrayList<Player>();
	public static ArrayList<String> temps = new ArrayList<String>();
	public static ArrayList<Inventory> is = new ArrayList<Inventory>();
	public static HashMap<Player, ItemStack> helmets = new HashMap<Player, ItemStack>();
	@SuppressWarnings("static-access")
	public Events(JavaPlugin jp){
		this.jp = jp;
		FileConfiguration fc = jp.getConfig();
		File cfg = new File("plugins/CranaZ/Divers/torteela.yml");
		try {
			boolean b = false;
			if(!cfg.exists()){
				cfg.createNewFile();
				fc.set("torteela.players", (List<String>)Arrays.asList("TST_null"));
				fc.save(cfg);
			}
			else
				b = true;
			if(b)
			{
				fc.load(cfg);
				temps = (ArrayList<String>)fc.getStringList("torteela.players");
			}
		} catch (Exception e) {}
	}

	/*
	 * 
	 */

	//TODO CranaZ bags
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
		if(e.getClick() == ClickType.SHIFT_RIGHT && e.getCurrentItem().getType() == Items.BAG)
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

	/*
	 * 
	 */

	//TODO CranaZ Effects
	@EventHandler
	public void playerBleed(EntityDamageEvent e)
	{
		if(e.getEntityType() == EntityType.PLAYER){
			//Player p = (Player)e.getEntity();
			//Damageable d = (Damageable)p;
			//double d0 = d.getHealth() - e.getDamage();
			/*if(rh.isHemoragic(p)){
				e.setCancelled(true);
				return;
			}*/
			if(e.getEntity() instanceof LivingEntity){
				boolean b = false;
				switch(e.getCause()){
				case ENTITY_ATTACK:
					b = true;
					break;
				case CONTACT:
					b = true;
					break;
				case FALL:
					b = true;
					break;
				case THORNS:
					b = true;
					break;
				case PROJECTILE:
					b = true;
					break;
				case BLOCK_EXPLOSION:
					b = true;
					break;
				case ENTITY_EXPLOSION:
					b = true;
					break;
				}
				if(b){
					Bleed.bleedEffect((LivingEntity)e.getEntity());
				}
			}
			/*if(d0 <= 0D){
				e.setCancelled(true);
				rh.hemoragize((Player)e.getEntity());
				return;
			}
			else if(d0 < 9D){
				if(!BloodCycle.ps.contains(p))
					new BloodCycle(p).runTaskTimer(jp, 0L, 8L * 20L);
			}*/



			/*if(d0 > 0D && d0 < 11D && d0 > 9D){
				Bleed.light.apply(p);
				warn(p, Bleed.messages.get(new Random().nextInt(Bleed.messages.size() - 1)));
			}
			if(d0 > 0D && d0 < 9D && d0 > 7D){
				if(this.rand.nextInt(2) == 0){
					Bleed.hurt.apply(p);
				}
			}
			if(d0 > 0D && d0 < 7D && d0 > 6D){
				if(this.rand.nextInt(2) == 0){
					Bleed.grave.apply(p);
				}
			}
			if(d0 > 0D && d0 < 6D && d0 > 5D){
				if(this.rand.nextInt(2) == 0){
					Bleed.damages(p);
				}
			}
			if(d0 > 0D && d0 < 5D && d0 > 4D){
				if(this.rand.nextInt(3) == 0){
					Bleed.heavy.apply(p);
				}
			}
			if(d0 > 0D && d0 < 4D && d0 > 3D){
				if(this.rand.nextInt(2) == 0){
					Bleed.instinct.apply(p);
				}
			}
			if(d0 > 0D && d0 < 3D && !p.hasPotionEffect(PotionEffectType.REGENERATION)){
				if(this.rand.nextInt(2) == 0)
					Bleed.instinct2.apply(p);
			}*/
		}
	}
	@EventHandler
	public void playerAchieved(EntityDamageByEntityEvent e){
		if(e.getEntity() instanceof LivingEntity){
			Bleed.bleedEffect((LivingEntity)e.getEntity());
		}
		/*if(e.getEntity().getType() == EntityType.PLAYER){
			if(e.getDamager().getType() == EntityType.PLAYER && rh.isHemoragic((Player)e.getEntity()))
				rh.achieve((Player)e.getEntity(), (Player)e.getDamager());
			else if(rh.isHemoragic((Player)e.getEntity()))
				e.setCancelled(true);
		}*/
	}
	@EventHandler
	public void playerPing(final ServerListPingEvent e){
		e.setMotd("§r§l§1CranaZ §kPost-apocalyptique zombi.\n§r§4§lVenez §r§4§mmourir§r §4§lSURVIVRE !");
		try {
			BufferedImage bi = ImageIO.read(Events.class.getResource("cranaz.png"));
			e.setServerIcon(Bukkit.getServer().loadServerIcon(bi));
		} catch (Exception e1) {}
	}
	@EventHandler
	public void playerInt(PlayerInteractEntityEvent e){
		//rh.hemoragize(e.getPlayer());
		if(e.getRightClicked().getType() != EntityType.PLAYER)
			return;
		Player p = ((Player)e.getRightClicked());
		if(e.getPlayer().getItemInHand().getType() == Material.PUMPKIN_PIE 
				|| e.getPlayer().getItemInHand().getType() == Material.APPLE 
				|| e.getPlayer().getItemInHand().getType() == Material.GOLDEN_APPLE
				|| e.getPlayer().getItemInHand().getType() == Material.PAPER){
			if(Items.Subs.Poison.NEUROTOXIC.compareTo(e.getPlayer().getItemInHand())){
				Bleed.neurtoxicPoison(p);
				if(e.getPlayer().getItemInHand().getAmount() == 1){
					e.getPlayer().getInventory().setItemInHand(new ItemStack(Material.AIR, 1));
				}
				else
					e.getPlayer().getItemInHand().setAmount(e.getPlayer().getItemInHand().getAmount() - 1);
			}
			else if(Items.Subs.Drugs.AMPHETAMIN.compareTo(e.getPlayer().getItemInHand())){
				Bleed.amphet(p);
				if(e.getPlayer().getItemInHand().getAmount() == 1){
					e.getPlayer().getInventory().setItemInHand(new ItemStack(Material.AIR, 1));
				}
				else
					e.getPlayer().getItemInHand().setAmount(e.getPlayer().getItemInHand().getAmount() - 1);
			}
			else if(Items.Subs.Poison.ARTERIAL.compareTo(e.getPlayer().getItemInHand())){
				Bleed.arterialPoison(p);
				if(e.getPlayer().getItemInHand().getAmount() == 1){
					e.getPlayer().getInventory().setItemInHand(new ItemStack(Material.AIR, 1));
				}
				else
					e.getPlayer().getItemInHand().setAmount(e.getPlayer().getItemInHand().getAmount() - 1);
			}
			else if(e.getPlayer().getItemInHand().getType() == Material.APPLE){
				/*if(rh.isHemoragic(p)){
					rh.revive(p, Material.APPLE);
					e.getPlayer().sendMessage("§r§bVous avez réanimé " + p.getDisplayName() + ".§r");
					if(e.getPlayer().getItemInHand().getAmount() == 1){
						e.getPlayer().getInventory().setItemInHand(new ItemStack(Material.AIR, 1));
					}
					else
						e.getPlayer().getItemInHand().setAmount(e.getPlayer().getItemInHand().getAmount() - 1);
					return;
				}*/
				Bleed.blood(0, p);
				if(e.getPlayer().getItemInHand().getAmount() == 1){
					e.getPlayer().getInventory().setItemInHand(new ItemStack(Material.AIR, 1));
				}
				else
					e.getPlayer().getItemInHand().setAmount(e.getPlayer().getItemInHand().getAmount() - 1);
			}
			else if(e.getPlayer().getItemInHand().getType() == Material.GOLDEN_APPLE){
				/*if(rh.isHemoragic(p)){
					rh.revive(p, Material.GOLDEN_APPLE);
					e.getPlayer().sendMessage("§r§bVous avez réanimé " + p.getDisplayName() + ".§r");
					if(e.getPlayer().getItemInHand().getAmount() == 1){
						e.getPlayer().getInventory().setItemInHand(new ItemStack(Material.AIR, 1));
					}
					else
						e.getPlayer().getItemInHand().setAmount(e.getPlayer().getItemInHand().getAmount() - 1);
					return;
				}*/
				Bleed.blood(1, p);
				if(e.getPlayer().getItemInHand().getAmount() == 1){
					e.getPlayer().getInventory().setItemInHand(new ItemStack(Material.AIR, 1));
				}
				else
					e.getPlayer().getItemInHand().setAmount(e.getPlayer().getItemInHand().getAmount() - 1);
			}
			else if(e.getPlayer().getItemInHand().getType() == Material.PAPER){
				Bleed.bandages(p);
				if(e.getPlayer().getItemInHand().getAmount() == 1){
					e.getPlayer().getInventory().setItemInHand(new ItemStack(Material.AIR, 1));
				}
				else
					e.getPlayer().getItemInHand().setAmount(e.getPlayer().getItemInHand().getAmount() - 1);
			}
		}
	}
	@EventHandler
	public void playerDrug(PlayerInteractEvent e){
		if(e.getAction() == Action.LEFT_CLICK_AIR || e.getAction() == Action.LEFT_CLICK_BLOCK){
			if(e.getPlayer().getItemInHand().getType() == Material.PUMPKIN_PIE 
					|| e.getPlayer().getItemInHand().getType() == Material.APPLE 
					|| e.getPlayer().getItemInHand().getType() == Material.GOLDEN_APPLE
					|| e.getPlayer().getItemInHand().getType() == Material.PAPER){
				if(Items.Subs.Drugs.AMPHETAMIN.compareTo(e.getPlayer().getItemInHand())){
					Bleed.amphet(e.getPlayer());
					if(e.getPlayer().getItemInHand().getAmount() == 1){
						e.getPlayer().getInventory().setItemInHand(new ItemStack(Material.AIR, 1));
					}
					else
						e.getPlayer().getItemInHand().setAmount(e.getPlayer().getItemInHand().getAmount() - 1);
				}
				else if(e.getPlayer().getItemInHand().getType() == Material.RED_MUSHROOM){
					Bleed.mush(e.getPlayer());
					if(e.getPlayer().getItemInHand().getAmount() == 1){
						e.getPlayer().getInventory().setItemInHand(new ItemStack(Material.AIR, 1));
					}
					else
						e.getPlayer().getItemInHand().setAmount(e.getPlayer().getItemInHand().getAmount() - 1);
				}
				else if(e.getPlayer().getItemInHand().getType() == Material.APPLE){
					Bleed.blood(0, e.getPlayer());
					if(e.getPlayer().getItemInHand().getAmount() == 1){
						e.getPlayer().getInventory().setItemInHand(new ItemStack(Material.AIR, 1));
					}
					else
						e.getPlayer().getItemInHand().setAmount(e.getPlayer().getItemInHand().getAmount() - 1);
				}
				else if(e.getPlayer().getItemInHand().getType() == Material.GOLDEN_APPLE){
					Bleed.blood(1, e.getPlayer());
					if(e.getPlayer().getItemInHand().getAmount() == 1){
						e.getPlayer().getInventory().setItemInHand(new ItemStack(Material.AIR, 1));
					}
					else
						e.getPlayer().getItemInHand().setAmount(e.getPlayer().getItemInHand().getAmount() - 1);
				}
				else if(e.getPlayer().getItemInHand().getType() == Material.PAPER){
					Bleed.bandages(e.getPlayer());
					if(e.getPlayer().getItemInHand().getAmount() == 1){
						e.getPlayer().getInventory().setItemInHand(new ItemStack(Material.AIR, 1));
					}
					else
						e.getPlayer().getItemInHand().setAmount(e.getPlayer().getItemInHand().getAmount() - 1);
				}
			}
		}
	}

	/*
	 * 
	 */

	//TODO CranaZ Items et Legacy
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
				jp.getServer().getPluginManager().callEvent(new BlockBreakEvent(e.getClickedBlock(), e.getPlayer()));
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
			}.runTaskLater(jp, 6000L);
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
			}.runTaskLater(jp, 6000L);
		}
		e.setCancelled(true);
		if(!cancel)
			e.getBlock().setType(Material.AIR);
	}

	/*
	 * 
	 */

	//TODO CranaZ Loots
	@EventHandler
	public void chunkLoaded(ChunkLoadEvent e){
		List<ThreadSpawner> lts = LAP.factor.hasLoots(e.getChunk());
		if(lts == null)
			return;
		for(final ThreadSpawner ts : lts){
			ts.respawnItem();
		}
	}
	@SuppressWarnings("static-access")
	@EventHandler
	public void playerJoin(PlayerJoinEvent e){
		if(!LootRefactor.spawnsOk){
			LootRefactor.init(e.getPlayer().getWorld(), this.jp);
			LAP.startSpawns();
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
			if(LAP.factor.getLootAt(block.getLocation()) != EnumPacks.NULL)
			{
				if(LAP.factor.isLootBlock(block.getLocation(), LAP.factor.getLootAt(block.getLocation())))
				{
					if(LAP.factor.loots.containsKey(e.getClickedBlock().getLocation())){
						e.getPlayer().openInventory(LAP.factor.loots.get(e.getClickedBlock().getLocation()).getInventoryOf());
					}
					else{
						e.getPlayer().openInventory(LAP.factor.randomInvLoot(EnumPacks.values()[this.rand.nextInt(EnumPacks.values().length)]));
					}
				}
			}
		}
	}

	/*
	 * 
	 */

	//TODO CranaZ Thirst
	@EventHandler
	public void playerJoin2(PlayerJoinEvent e)
	{
		if(!e.getPlayer().hasPlayedBefore())
			e.getPlayer().setExp(0.99F);
		if(!e.getPlayer().hasPermission("cranaz.thirst.no") && !e.getPlayer().isOp()){
			new ThirstRunnable(e.getPlayer()).runTaskTimer(jp, 100, ThirstFactor.getDelay() * 20);
		}
	}
	@EventHandler
	public void playerDrink(PlayerItemConsumeEvent e)
	{
		if(e.getItem().getType() == Material.POTION){
			PluginMethods.gsay(e.getPlayer(), "Vous avez bu et vous sentez mieux.");
			e.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 60 * 20, 0));
			e.getPlayer().setExp(0.99F);
		}
	}
	@EventHandler
	public void expPicked(PlayerExpChangeEvent e){
		e.setAmount(0);
	}
	@EventHandler
	public void playerDie2(PlayerDeathEvent e){
		Bukkit.getScheduler().runTaskAsynchronously(jp, new FillExp(e.getEntity()));
	}
	class FillExp extends BukkitRunnable
	{
		private Player p;
		public FillExp(Player p){this.p = p;}
		@Override
		public void run() {
			while(p.isDead());
			Bukkit.getScheduler().runTask(jp, new BukkitRunnable(){
				@Override
				public void run() {
					p.setExp(0.99F);
				}
			});
		}
	}

	/*
	 * 
	 */
	
	//TODO CranaZ Zombie IA
	
	public static void addToConfig(String name){
		FileConfiguration fc = jp.getConfig();
		try {
			fc.load(new File("plugins/CranaZ/Divers/torteela.yml"));
			List<String> ls = fc.getStringList("torteela.players");
			ls.remove("TST_null");
			if(!ls.contains(name))
				ls.add(name);
			fc.set("torteela.players", ls);
			fc.save(new File("plugins/CranaZ/Divers/torteela.yml"));
			temps.add(name);
		} catch (Exception e) {e.printStackTrace();}
	}
	@EventHandler
	public void entityExplode(final EntityExplodeEvent e){
		Bukkit.getScheduler().runTask(jp, new BukkitRunnable(){
			@Override
			public void run() {
				for(Entity entity : e.getEntity().getNearbyEntities(60D, 20D, 60D)){
					CraftEntity ce = (CraftEntity)entity;
					if(ce.getHandle() instanceof EnhancedZombie){
						((EnhancedZombie)ce.getHandle()).move(ce.getLocation());
					}
				}
			}
		});
	}
	@SuppressWarnings("deprecation")
	@EventHandler
	public void weaponShoot(final WeaponPreShootEvent e)
	{
		Block b1 = e.getPlayer().getTargetBlock(null, 250);
		if(b1 == null)
			return;
		e.getPlayer().getWorld().playSound(e.getPlayer().getTargetBlock(null, 150).getLocation(), Sound.ORB_PICKUP, 10F, 40F);
		boolean b = true;
		final Location l = b1.getLocation();
		final Material m = b1.getType();
		switch(b1.getType()){
		case GLASS:
			b1.breakNaturally();
			b = false;
			break;
		case THIN_GLASS:
			b1.breakNaturally();
			b = false;
			break;
		case ICE:
			b1.breakNaturally();
			b = false;
			break;
		}
		if(!b){
			e.getPlayer().playSound(e.getPlayer().getLocation(), Sound.GLASS, 15F, 63F);
			l.getWorld().playSound(l, Sound.GLASS, 25F, 1F);
			new BukkitRunnable(){
				@Override
				public void run() {
					if(e.getPlayer().getWorld().getBlockAt(l) == null || e.getPlayer().getWorld().getBlockAt(l).getType() == Material.AIR){
						e.getPlayer().getWorld().getBlockAt(l).setType(m);
					}
				}
			}.runTaskLater(jp, 6000L);
		}
		Bukkit.getScheduler().runTask(jp, new BukkitRunnable(){
			@Override
			public void run() {
				for(Entity entity : e.getPlayer().getNearbyEntities(55D, 15D, 55D)){
					CraftPlayer cp = (CraftPlayer)e.getPlayer();
					CraftEntity ce = (CraftEntity)entity;
					if(ce.getHandle() instanceof EnhancedZombie){
						((EnhancedZombie)ce.getHandle()).move(cp.getLocation());
					}
				}
			}
		});
	}
	@EventHandler
	public void mosinScope(WeaponScopeEvent e){
		if(e.getWeaponTitle().equalsIgnoreCase("Moisin")){
			if(e.isZoomIn()){
				helmets.put(e.getPlayer(), e.getPlayer().getInventory().getHelmet());
				e.getPlayer().getInventory().setHelmet(new ItemStack(Material.PUMPKIN));
				ZIA.Utils.sendPacketPos(e.getPlayer().getLocation(), 50, new PacketPlayOutEntityEquipment(e.getPlayer().getEntityId(), 3, new net.minecraft.server.v1_7_R3.ItemStack(Blocks.AIR)), e.getPlayer());
			}
			else{
				e.getPlayer().getInventory().setHelmet(helmets.get(e.getPlayer()));
				helmets.remove(e.getPlayer());
			}
		}
	}
	@SuppressWarnings({ "static-access", "deprecation" })
	@EventHandler
	public void invClick(InventoryClickEvent e){
		if(helmets.containsKey(((Player)e.getWhoClicked())) && e.getSlotType() == SlotType.ARMOR){
			e.setCancelled(true);
			return;
		}
		if(e.getInventory().equals(this.goGui) || e.getInventory().equals(this.resGui) || this.is.contains(e.getInventory())){
			e.setCancelled(true);
			e.getWhoClicked().closeInventory();
			if(e.getCurrentItem().equals(GoMenu.alone)){
				this.playings.add((Player)e.getWhoClicked());
				ZIA.Utils.noSPK_tp((Player)e.getWhoClicked());
			}
			else if(e.getCurrentItem().equals(GoMenu.many)){
				Inventory i = Bukkit.getServer().createInventory(null, ZIA.Utils.iSize(), "Choisir un ami");
				for(Player p : Bukkit.getServer().getOnlinePlayers()){
					ItemStack is = new ItemStack(Material.DIAMOND);
					ItemMeta im = is.getItemMeta();
					im.setDisplayName(p.getName());
					im.setLore(Arrays.asList("Partir avec " + p.getDisplayName()));
					is.setItemMeta(im);
					i.addItem(is);
				}
				this.is.add(i);
				e.getWhoClicked().openInventory(i);
			}
			else if(e.getCurrentItem().equals(ResPackMenu.light)){
				((Player)e.getWhoClicked()).setResourcePack(ZIA.Utils.getPackLink(ZIA.Utils.LIGHT));
			}
			else if(e.getCurrentItem().equals(ResPackMenu.heavy)){
				((Player)e.getWhoClicked()).setResourcePack(ZIA.Utils.getPackLink(ZIA.Utils.HEAVY));
			}
			else if(this.is.contains(e.getInventory())){
				if(e.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase(e.getWhoClicked().getName())){
					((Player)e.getWhoClicked()).sendMessage("§r§cVeuillez selectionner un joueur autre que vous-même.§r");
					return;
				}
				Player p = Bukkit.getPlayerExact(e.getCurrentItem().getItemMeta().getDisplayName()), p1 = (Player)e.getWhoClicked();
				if(p == null || playings.contains(p)){
					((Player)e.getWhoClicked()).sendMessage("§r§cVeuillez selectionner un joueur qui ne joue pas.§r");
					return;
				}
				ZIA.ps.put(p, p1);
				p1.sendMessage("Demande envoyée.");
				p.sendMessage((String[]) Arrays.asList(e.getWhoClicked().getName() + " souhaiterait partir avec vous.",
						"- /cranaz accept pour accepter.",
						"- /cranaz decline pour refuser.").toArray());
				this.playings.add((Player)e.getWhoClicked());
				ZIA.Utils.noSPK_tp((Player)e.getWhoClicked());
			}
		}
	}
	@EventHandler
	public void zombieDie(EntityDeathEvent e){
		if(((CraftEntity)e.getEntity()).getHandle() instanceof EnhancedZombie){
			EnhancedZombie cez = (EnhancedZombie)((CraftEntity)e.getEntity()).getHandle();
			List<ItemStack> lis = e.getDrops();
			if(cez.ipf){
				if(cez.getEquipment(0) != null){
					lis.add(CraftItemStack.asBukkitCopy(cez.getEquipment(0)));
				}
			}
			VirtualSpawner vs = VirtualSpawner.getNearbySpawner(e.getEntity().getLocation(), 45);
			if(vs != null){
				if(vs.getCount() < 25)
					vs.countPlus();
			}
		}
	}
	@EventHandler
	public void playerRespawn(PlayerRespawnEvent e){
		e.setRespawnLocation(new Location(e.getPlayer().getWorld(), 110.5D, 56D, 71.5D));
		e.getPlayer().getWorld().setSpawnLocation(110, 56, 71);
	}
	@EventHandler
	public void playerJoin3(PlayerJoinEvent e){
		if(!VirtualSpawner.init){
			VirtualSpawner.startupAll(e.getPlayer().getWorld(), jp);
			VirtualSpawner.init = true;
		}
		if(!ZIA.RandomSpawns.init){
			ZIA.RandomSpawns.init(jp, e.getPlayer().getWorld());
		}
		if(!temps.contains(e.getPlayer().getName())){
			e.getPlayer().sendMessage(new String[]{"§r§cIl t'est fortement conséillé d'aller voir Torteela dans le couloir à ta gauche.",
			"§cTu pourras ainsi disposer du resource pack le plus récent.§r"});
		}
		e.getPlayer().setResourcePack(ZIA.Utils.getPackLink(ZIA.Utils.LIGHT));
	}
	@EventHandler
	public void playerDie3(PlayerDeathEvent e){
		playings.remove(e.getEntity());
		try{
			if(e.getEntity().getLastDamageCause().getCause() == DamageCause.ENTITY_ATTACK){
				EnhancedZombie ez = ZIA.spawnZombie(e.getEntity().getLocation());
				ez.setPlayerEquipment(e.getEntity());
				e.setDeathMessage(e.getEntity().getKiller() == null ? e.getEntity().getDisplayName() + ZombieMessage.random().toString() : e.getEntity().getDisplayName() + " a été tué par " + e.getEntity().getKiller().getDisplayName());
			}
		}catch(NullPointerException e2){}
	}
	@EventHandler
	public void npcRightClick(NPCRightClickEvent e){
		if(e.getNPC().getName().equalsIgnoreCase("darkauron")){
			if(!temps.contains(e.getClicker().getName())){
				e.getClicker().sendMessage("§r§c[Darkauron -> toi]§r - Tu devrais aller voir Torteela a ma gauche.");
				return;
			}
			e.getClicker().openInventory(goGui);
		}
		else if(e.getNPC().getName().equalsIgnoreCase("torteela")){
			addToConfig(e.getClicker().getName());
			e.getClicker().openInventory(resGui);
		}
	}
	public enum ZombieMessage
	{
		ASSAULT(" est tombé sous l'assaut des zombies. Paix à son âme."),
		ZOMBIFICATION(" a été infecté. Triste nouvelle."),
		HORDE(" est mort sous une horde de zombies. Saluons son courage.");
		private String s;
		private static final Random r = new Random();
		public static ZombieMessage random(){
			return values()[r.nextInt(values().length)];
		}
		ZombieMessage(String textName){
			this.s = textName;
		}
		@Override
		public String toString() {
			return s;
		}
	}
}
