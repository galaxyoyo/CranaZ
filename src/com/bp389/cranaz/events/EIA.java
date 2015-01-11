package com.bp389.cranaz.events;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.craftbukkit.v1_8_R1.entity.CraftEntity;
import org.bukkit.craftbukkit.v1_8_R1.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_8_R1.inventory.CraftItemStack;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType.SlotType;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import net.citizensnpcs.api.event.NPCRightClickEvent;

import net.minecraft.server.v1_8_R1.Blocks;
import net.minecraft.server.v1_8_R1.PacketPlayOutEntityEquipment;

import com.bp389.cranaz.Loadable;
import com.bp389.cranaz.MathUtil;
import com.bp389.cranaz.ia.GoMenu;
import com.bp389.cranaz.ia.ResPackMenu;
import com.bp389.cranaz.ia.VirtualSpawner;
import com.bp389.cranaz.ia.ZIA;
import com.bp389.cranaz.ia.entities.EnhancedZombie;
import com.shampaggon.crackshot.events.WeaponPreShootEvent;
import com.shampaggon.crackshot.events.WeaponScopeEvent;

public final class EIA extends GEvent implements Listener {

	private static List<Player> hasShoot = new ArrayList<Player>();

	public EIA(final JavaPlugin jp) {
		super(jp);
	}

	@Override
	public Class<? extends Loadable> getRelativePlugin() {
		return ZIA.class;
	}

	/*
	 * 
	 */

	public static void addToConfig(final String name) {
		final FileConfiguration fc = GEvent.static_plugin.getConfig();
		try {
			fc.load(new File("plugins/CranaZ/Divers/torteela.yml"));
			final List<String> ls = fc.getStringList("torteela.players");
			ls.remove("TST_null");
			if(!ls.contains(name))
				ls.add(name);
			fc.set("torteela.players", ls);
			fc.save(new File("plugins/CranaZ/Divers/torteela.yml"));
			GEvent.temps.add(name);
		} catch(final Exception e) {
			e.printStackTrace();
		}
	}

	@EventHandler
	public void entityExplode(final EntityExplodeEvent e) {
		new BukkitRunnable() {

			@Override
			public void run() {
				for(final Entity entity : e.getEntity().getNearbyEntities(60D, 20D, 60D)) {
					final CraftEntity ce = (CraftEntity) entity;
					if(ce.getHandle() instanceof EnhancedZombie)
						((EnhancedZombie) ce.getHandle()).move(ce.getLocation());
				}
			}
		}.runTaskAsynchronously(this.plugin);
	}

	@SuppressWarnings("deprecation")
	@EventHandler
	public void weaponShoot(final WeaponPreShootEvent e) {
		final Block b1 = e.getPlayer().getTargetBlock(null, 250);
		if(b1 == null)
			return;
		e.getPlayer().getWorld().playSound(e.getPlayer().getTargetBlock(null, 150).getLocation(), Sound.ORB_PICKUP, 10F, 40F);
		boolean b = true;
		final Location l = b1.getLocation();
		final Material m = b1.getType();
		switch(b1.getType()) {
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
		if(!b) {
			e.getPlayer().playSound(e.getPlayer().getLocation(), Sound.GLASS, 15F, 63F);
			l.getWorld().playSound(l, Sound.GLASS, 25F, 1F);
			new BukkitRunnable() {

				@Override
				public void run() {
					if(e.getPlayer().getWorld().getBlockAt(l) == null || e.getPlayer().getWorld().getBlockAt(l).getType() == Material.AIR)
						e.getPlayer().getWorld().getBlockAt(l).setType(m);
				}
			}.runTaskLater(this.plugin, 6000L);
		}
		if(EIA.hasShoot.contains(e.getPlayer()))
			return;
		EIA.hasShoot.add(e.getPlayer());
		new BukkitRunnable() {

			@Override
			public void run() {
				EIA.hasShoot.remove(e.getPlayer());
			}
		}.runTaskLater(this.plugin, 30L * 20L);
		new BukkitRunnable() {

			@Override
			public void run() {
				final CraftPlayer cp = (CraftPlayer) e.getPlayer();
				//55 15 55
				for(final net.minecraft.server.v1_8_R1.Entity e : MathUtil.get_NMS_optimizedEntities(cp.getHandle(), 55D, 15D, 55D)){
					if(e instanceof EnhancedZombie && ((EnhancedZombie)e).getGoalTarget() == null){
						new BukkitRunnable(){
							
							@Override
							public void run(){
								((EnhancedZombie)e).move(cp.getLocation());
							}
						}.runTask(plugin);
					}
				}
			}
		}.runTaskAsynchronously(this.plugin);
	}

	@EventHandler
	public void mosinScope(final WeaponScopeEvent e) {
		if(e.getWeaponTitle().equalsIgnoreCase("Moisin"))
			if(e.isZoomIn()) {
				GEvent.helmets.put(e.getPlayer(), e.getPlayer().getInventory().getHelmet());
				e.getPlayer().getInventory().setHelmet(new ItemStack(Material.PUMPKIN));
				ZIA.Utils.sendPacketPos(e.getPlayer().getLocation(), 50, new PacketPlayOutEntityEquipment(e.getPlayer().getEntityId(), 3,
				        new net.minecraft.server.v1_8_R1.ItemStack(Blocks.AIR)), e.getPlayer());
			} else {
				e.getPlayer().getInventory().setHelmet(GEvent.helmets.get(e.getPlayer()));
				GEvent.helmets.remove(e.getPlayer());
			}
	}

	@SuppressWarnings({ "static-access", "deprecation" })
	@EventHandler
	public void invClick(final InventoryClickEvent e) {
		if(GEvent.helmets.containsKey(e.getWhoClicked()) && e.getSlotType() == SlotType.ARMOR) {
			e.setCancelled(true);
			return;
		}
		if(e.getInventory().equals(this.goGui) || e.getInventory().equals(this.resGui) || this.is.contains(e.getInventory())) {
			e.setCancelled(true);
			e.getWhoClicked().closeInventory();
			if(e.getCurrentItem().equals(GoMenu.alone)) {
				this.playings.add((Player) e.getWhoClicked());
				ZIA.Utils.noSPK_tp((Player) e.getWhoClicked());
			} else if(e.getCurrentItem().equals(GoMenu.many)) {
				final Inventory i = Bukkit.getServer().createInventory(null, ZIA.Utils.iSize(), "Choisir un ami");
				for(final Player p : Bukkit.getServer().getOnlinePlayers()) {
					final ItemStack is = new ItemStack(Material.DIAMOND);
					final ItemMeta im = is.getItemMeta();
					im.setDisplayName(p.getName());
					im.setLore(Arrays.asList("Partir avec " + p.getDisplayName()));
					is.setItemMeta(im);
					i.addItem(is);
				}
				this.is.add(i);
				e.getWhoClicked().openInventory(i);
			} else if(e.getCurrentItem().equals(ResPackMenu.light))
				((Player) e.getWhoClicked()).setResourcePack(ZIA.Utils.getPackLink(ZIA.Utils.LIGHT));
			else if(e.getCurrentItem().equals(ResPackMenu.heavy))
				((Player) e.getWhoClicked()).setResourcePack(ZIA.Utils.getPackLink(ZIA.Utils.HEAVY));
			else if(this.is.contains(e.getInventory())) {
				if(e.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase(e.getWhoClicked().getName())) {
					((Player) e.getWhoClicked()).sendMessage("§r§cVeuillez selectionner un joueur autre que vous-même.§r");
					return;
				}
				final Player p = Bukkit.getPlayerExact(e.getCurrentItem().getItemMeta().getDisplayName()), p1 = (Player) e.getWhoClicked();
				if(p == null || GEvent.playings.contains(p)) {
					((Player) e.getWhoClicked()).sendMessage("§r§cVeuillez selectionner un joueur qui ne joue pas.§r");
					return;
				}
				ZIA.ps.put(p, p1);
				p1.sendMessage("Demande envoyée.");
				p.sendMessage((String[]) Arrays.asList(e.getWhoClicked().getName() + " souhaiterait partir avec vous.", "- /cranaz accept pour accepter.",
				        "- /cranaz decline pour refuser.").toArray());
				this.playings.add((Player) e.getWhoClicked());
				ZIA.Utils.noSPK_tp((Player) e.getWhoClicked());
			}
		}
	}

	@EventHandler
	public void zombieDie(final EntityDeathEvent e) {
		if(((CraftEntity) e.getEntity()).getHandle() instanceof EnhancedZombie) {
			final EnhancedZombie cez = (EnhancedZombie) ((CraftEntity) e.getEntity()).getHandle();
			final List<ItemStack> lis = e.getDrops();
			if(cez.ipf)
				if(cez.getEquipment(0) != null)
					lis.add(CraftItemStack.asBukkitCopy(cez.getEquipment(0)));
			final VirtualSpawner vs = VirtualSpawner.getNearbySpawner(e.getEntity().getLocation(), 45);
			if(vs != null)
				if(vs.getCount() < 25)
					vs.countPlus();
		}
	}

	@EventHandler
	public void playerRespawn(final PlayerRespawnEvent e) {
		e.setRespawnLocation(new Location(e.getPlayer().getWorld(), 110.5D, 56D, 71.5D));
		e.getPlayer().getWorld().setSpawnLocation(110, 56, 71);
	}

	@EventHandler
	public void playerJoin(final PlayerJoinEvent e) {
		if(!VirtualSpawner.init) {
			VirtualSpawner.startupAll(e.getPlayer().getWorld(), this.plugin);
			VirtualSpawner.init = true;
		}
		if(!ZIA.RandomSpawns.init)
			ZIA.RandomSpawns.init(this.plugin, e.getPlayer().getWorld());
		if(!GEvent.temps.contains(e.getPlayer().getName()))
			e.getPlayer().sendMessage(
			        new String[] { "§r§cIl t'est fortement conséillé d'aller voir Torteela dans le couloir à ta gauche.",
			                "§cTu pourras ainsi disposer du resource pack le plus récent.§r" });
		e.getPlayer().setResourcePack(ZIA.Utils.getPackLink(ZIA.Utils.LIGHT));
	}

	@EventHandler
	public void playerDie(final PlayerDeathEvent e) {
		GEvent.playings.remove(e.getEntity());
		try {
			if(e.getEntity().getLastDamageCause().getCause() == DamageCause.ENTITY_ATTACK) {
				final EnhancedZombie ez = ZIA.spawnZombie(e.getEntity().getLocation());
				ez.setPlayerEquipment(e.getEntity());
				e.setDeathMessage(e.getEntity().getKiller() == null ? e.getEntity().getDisplayName() + ZombieMessage.random().toString() : e.getEntity()
				        .getDisplayName() + " a été tué par " + e.getEntity().getKiller().getDisplayName());
			}
		} catch(final NullPointerException e2) {}
	}

	@EventHandler
	public void npcRightClick(final NPCRightClickEvent e) {
		if(e.getNPC().getName().equalsIgnoreCase("darkauron")) {
			if(!GEvent.temps.contains(e.getClicker().getName())) {
				e.getClicker().sendMessage("§r§c[Darkauron -> toi]§r - Tu devrais aller voir Torteela a ma gauche.");
				return;
			}
			e.getClicker().openInventory(GEvent.goGui);
		} else if(e.getNPC().getName().equalsIgnoreCase("torteela")) {
			EIA.addToConfig(e.getClicker().getName());
			e.getClicker().openInventory(GEvent.resGui);
		}
	}

	public enum ZombieMessage {
		ASSAULT(" est tombé sous l'assaut des zombies. Paix à son âme."), ZOMBIFICATION(" a été infecté. Triste nouvelle."), HORDE(
		        " est mort sous une horde de zombies. Saluons son courage.");

		private String s;
		private static final Random r = new Random();

		public static ZombieMessage random() {
			return ZombieMessage.values()[ZombieMessage.r.nextInt(ZombieMessage.values().length)];
		}

		ZombieMessage(final String textName) {
			this.s = textName;
		}

		@Override
		public String toString() {
			return this.s;
		}
	}
}
