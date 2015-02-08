package com.bp389.cranaz.ia;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Random;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.v1_8_R1.CraftWorld;
import org.bukkit.craftbukkit.v1_8_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import com.bp389.PluginMethods;
import com.bp389.cranaz.Loadable;
import com.bp389.cranaz.Loader;
import com.bp389.cranaz.Util;
import com.bp389.cranaz.YamlObj;
import com.bp389.cranaz.events.GEvent;
import com.bp389.cranaz.ia.entities.CustomEntityType;
import com.bp389.cranaz.ia.entities.EnhancedZombie;
import com.bp389.cranaz.items.Items;
import com.shampaggon.crackshot.CSUtility;

public class ZIA extends Loadable {

	public static HashMap<Player, Player> ps = new HashMap<Player, Player>();
	public static final CSUtility csu = new CSUtility();
	public static final File ia_config = new File("plugins/CranaZ/configuration/IA.yml");

	@Override
	public void onEnable() {
		PluginMethods.info("CranaZombieIA enregistre les entités...");
		final File f = new File("plugins/CranaZ/configuration/res_packs.yml");
		if(!f.exists()) {
			Util.saveToYaml(f, new YamlObj("resources.packs.light", "https://dl.dropboxusercontent.com/u/79959333/Dev/CranaZ/Light.zip"),
					new YamlObj("resources.packs.heavy", "https://dl.dropboxusercontent.com/u/79959333/Dev/CranaZ/Ultra.zip"));
		}
		if(!ia_config.exists()){
			Util.saveToYaml(ia_config, new YamlObj("zombies.detection.vision_necessaire", true),
					new YamlObj("zombies.detection.sneak", 6),
					new YamlObj("zombies.detection.marche", 15),
					new YamlObj("zombies.detection.sprint", 25),
					new YamlObj("zombies.spawn.mort_joueur", true),
					new YamlObj("zombies.spawn.facteur_attenuation", 12));
		}

		CustomEntityType.registerEntities();
		new GoMenu();
		new ResPackMenu();
		PluginMethods.info("CranaZombieIA demarré avec succès");
	}

	@Override
	public void onDisable() {
		PluginMethods.info("CranaZombieIA efface les entités...");
		CustomEntityType.unregisterEntities();
		for(final VirtualSpawner vs : VirtualSpawner.al)
			vs.saveAll();
		PluginMethods.info("CranaZombieIA éteint avec succès");
		PluginMethods.mdestroy();
	}

	@SuppressWarnings("deprecation")
	@Override
	public boolean onCommand(final CommandSender sender, final Command command, final String label, final String[] args) {
		if(command.getName().equalsIgnoreCase("cranaz"))
			if(args.length > 0) {
				if(args[0].equalsIgnoreCase("accept")) {
					if(sender instanceof Player) {
						final Player pt = (Player) sender;
						if(ZIA.ps.containsKey(pt)) {
							pt.teleport(ZIA.ps.get(pt), TeleportCause.PLUGIN);
							GEvent.playings.add(pt);
							pt.getInventory().clear();
							final ItemStack is = Items.mass();
							is.setDurability(Integer.valueOf(85).shortValue());
							pt.getInventory().addItem(Items.water(), Items.genTShirt(new ItemStack(Material.LEATHER_HELMET)), is,
									csu.generateWeapon("Smith"), Items.getAmmoStack(new ItemStack(Material.SLIME_BALL, 3)));
							ZIA.ps.remove(pt);
						} else
							pt.sendMessage("§r§cVous n'avez pas de requête.");
					} else
						sender.sendMessage("Veuillez vous connecter en tant que joueur.");
				} else if(args[0].equalsIgnoreCase("decline")) {
					if(sender instanceof Player) {
						final Player pt = (Player) sender;
						if(ZIA.ps.containsKey(pt)) {
							ZIA.ps.remove(pt);
							pt.sendMessage("Demande refusée.");
						} else
							pt.sendMessage("§r§cVous n'avez pas de requête.");
					} else
						sender.sendMessage("Veuillez vous connecter en tant que joueur.");
				} else if(args[0].equalsIgnoreCase("state")) {
					if(!(sender instanceof Player) || !sender.isOp())
						return true;
					final Player p = (Player) sender;
					final VirtualSpawner vs = VirtualSpawner.getNearbySpawner(p.getLocation(), 100);
					if(vs == null) {
						sender.sendMessage("§r§cAucun spawner trouvé dans les 50 blocs autour de vous.");
						return true;
					}
					sender.sendMessage((String[]) Arrays.asList("Position: " + vs.getExactLocation().toString(), "Compteur: " + vs.getCountString() + " / 25",
							"Actif: " + (vs.isRunning() ? "oui" : "non")).toArray());
				} else if(args[0].equalsIgnoreCase("zombie")) {
					if(sender.hasPermission("cranaz.spawn_zombie") || sender.isOp()) {
						if(sender instanceof Player) {
							final Player player = (Player) sender;
							if(args.length > 1)
								for(int i = 0; i < Integer.valueOf(args[1]); i++)
									ZIA.spawnZombie((CraftWorld) player.getWorld(), (CraftPlayer) player);
							else
								ZIA.spawnZombie((CraftWorld) player.getWorld(), (CraftPlayer) player);
							PluginMethods.gsay(player, "Zombie(s) modifié(s) spawné(s) sur votre position.");
						} else
							sender.sendMessage("Veuillez vous connecter en tant que joueur.");
					} else
						PluginMethods.alert((Player) sender, "Vous n'avez pas la permission.");
				} else if(args[0].equalsIgnoreCase("utils")) {
					if(sender instanceof Player)
						Items.Diaries.Utils.giveUtils((Player) sender);
					else
						sender.sendMessage("Veuillez vous connecter en tant que joueur.");
				} else if(args[0].equalsIgnoreCase("spawn")) {
					if(sender instanceof Player) {
						if(sender.hasPermission("cranaz.define.spawn_point")) {
							RandomSpawns.setSpawnLoc(((Player) sender).getTargetBlock(null, 50).getLocation());
							PluginMethods.gsay((Player) sender, "Point de spawn aléatoire défini");
						} else
							PluginMethods.alert((Player) sender, "Vous n'avez pas la permission !");
					} else
						sender.sendMessage("Veuillez vous connecter en tant que joueur.");
				} else if(args[0].equalsIgnoreCase("spawner")) {
					if(sender instanceof Player) {
						if(sender.hasPermission("cranaz.define.spawn_point")) {
							final VirtualSpawner vs = new VirtualSpawner(((Player) sender).getLocation(), Loader.plugin);
							vs.setRunning(true);
						} else
							PluginMethods.alert((Player) sender, "Vous n'avez pas la permission !");
					} else
						sender.sendMessage("Veuillez vous connecter en tant que joueur.");
				} else {
					sender.sendMessage("/cranaz reload - Recharger CranaZ");
					sender.sendMessage("/cranaz zombie [X] - Spawner X zombies modifiés");
					sender.sendMessage("/cranaz utils - Obtenir les utilitaires(règles...");
					sender.sendMessage("/cranaz spawner - Définir une zone de spawn virtuelle.");
					sender.sendMessage("/cranaz state - Obtenir l'etat du spawner le plus proche dans 50 blocs.");
					sender.sendMessage("/cranaz spawn - Définir un point de spawn aléatoire.");
				}
			} else {
				sender.sendMessage("/cranaz reload - Recharger CranaZ");
				sender.sendMessage("/cranaz zombie [X] - Spawner X zombies modifiés");
				sender.sendMessage("/cranaz utils - Obtenir les utilitaires(règles...");
				sender.sendMessage("/cranaz spawner - Définir une zone de spawn virtuelle.");
				sender.sendMessage("/cranaz state - Obtenir l'etat du spawner le plus proche dans 50 blocs.");
				sender.sendMessage("/cranaz spawn - Définir un point de spawn aléatoire.");
			}
		return true;
	}

	/**
	 * Spawne un zombie modifié
	 * 
	 * @param l
	 *            La position
	 * @return Le zombie
	 */
	public static EnhancedZombie spawnZombie(final Location l) {
		final EnhancedZombie customEntity = new EnhancedZombie(((CraftWorld) l.getWorld()).getHandle());
		customEntity.setLocation(l.getX(), l.getY() + 1D, l.getZ(), l.getYaw(), l.getPitch());
		((CraftWorld) l.getWorld()).getHandle().addEntity(customEntity, SpawnReason.CUSTOM);
		return customEntity;
	}

	/**
	 * Spawne un zombie sur la position d'un joueur
	 * 
	 * @param world
	 *            Le monde CraftBukkit
	 * @param player
	 *            Le joueur CraftBukkit
	 */
	public static EnhancedZombie spawnZombie(final CraftWorld world, final CraftPlayer player) {
		final Location loc = player.getTargetBlock(null, 50).getLocation();
		final EnhancedZombie customEntity = new EnhancedZombie(world.getHandle());
		customEntity.setLocation(loc.getX(), loc.getY() + 1D, loc.getZ(), loc.getYaw(), loc.getPitch());
		world.getHandle().addEntity(customEntity, SpawnReason.CUSTOM);
		return customEntity;
	}

	public static class RandomSpawns {

		private static ArrayList<Location> locs;
		private static org.bukkit.World w;
		public static boolean init = false;

		public static void init(final JavaPlugin plugin, final org.bukkit.World world) {
			RandomSpawns.w = world;
			try {
				RandomSpawns.locs = RandomSpawns.getSpawnPoints();
			} catch(final Exception e) {}
		}

		public static void setSpawnLoc(final Location l) {
			final int i = new File("plugins/CranaZ/database/spawns/").listFiles().length;
			String s;
			if(i == 0)
				s = "spawn0";
			else
				s = "spawn" + String.valueOf(i + 1);
			Util.saveToYaml("plugins/CranaZ/database/spawns/" + s + ".yml", new YamlObj("coords.locX", l.getX()),
					new YamlObj("coords.locY", l.getY()), new YamlObj("coords.locZ", l.getZ()));
		}

		public static Location randomLoc() {
			if(RandomSpawns.locs == null)
				return null;
			return RandomSpawns.locs.get(new Random().nextInt(RandomSpawns.locs.size()));
		}

		public static ArrayList<Location> getSpawnPoints() throws Exception {
			final File f = new File("plugins/CranaZ/database/spawns/");
			if(f.listFiles().length <= 0)
				return null;
			final ArrayList<Location> all = new ArrayList<Location>();
			for(final File f1 : f.listFiles())
				all.add(new Location(RandomSpawns.w, (double)Util.getFromYaml(f1, "coords.locX"), (double)Util.getFromYaml(f1, "coords.locY"), (double)Util.getFromYaml(f1, "coords.locZ")));
			return all;
		}
	}
}
