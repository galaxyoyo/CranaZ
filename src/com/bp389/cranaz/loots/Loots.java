package com.bp389.cranaz.loots;

import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.bp389.PluginMethods;
import com.bp389.cranaz.Loadable;
import com.bp389.cranaz.events.ELoots;
import com.bp389.cranaz.items.Items;

public final class Loots extends Loadable {

	public static LootRefactor factor = new LootRefactor();

	public static void startSpawns() {
		Loots.factor.startSpawns();
	}
	
	@SuppressWarnings({ "deprecation", "static-access" })
	@Override
	public boolean onCommand(final CommandSender sender, final Command command, final String label, final String[] args) {
		if(command.getName().equalsIgnoreCase("clp"))
			if(args.length > 0) {
				if(args[0].equalsIgnoreCase("get")) {
					if(args.length > 1) {
						if(sender instanceof Player) {
							final Player player = (Player) sender;
							if(player.hasPermission("clp.items.get") || player.isOp()) {
								ItemStack temp = new ItemStack(Material.AIR);
								switch(args[1].toLowerCase()) {
									case "eau":
										temp = Packs.WATER.item();
										break;
									case "maillot":
										temp = Packs.SHIRT.item();
										break;
									case "pantalon":
										temp = Packs.PANT.item();
										break;
									case "casquette":
										temp = Packs.HAT.item();
										break;
									case "bottes":
										temp = Packs.BOOTS.item();
										break;
									case "machette":
										temp = Packs.MACHETTE.item();
										break;
									case "hache":
										temp = Packs.AXE.item();
										break;
									case "mosin":
										temp = Packs.MOSIN_AM.item();
										break;
									case "ak47":
										temp = Packs.AK47_AM.item();
										break;
									case "m320":
										temp = Packs.M320_AM.item();
										break;
									case "bar":
										temp = Packs.BAR_AM.item();
										break;
									case "arc":
										temp = Packs.BOW.item();
										break;
									case "bandages":
										temp = Packs.PAPER.item();
										break;
									case "sang":
										temp = Packs.BLOOD_BAG.item();
										break;
									case "camo":
										temp = Packs.CAMO.item();
										break;
									case "camo_pant":
										temp = Packs.CAMO_PANTS.item();
										break;
									case "camo_boots":
										temp = Packs.CAMO_BOOTS.item();
										break;
									case "camo_helmet":
										temp = Packs.CAMO_HELMET.item();
										break;
									case "amphet":
										temp = Packs.AMPHET.item();
										break;
									case "blouse":
										temp = Packs.HOSHIRT.item();
										break;
									case "neurotoxic":
										temp = Packs.NEUROTOXIC.item();
										break;
									case "arteriel":
										temp = Packs.ARTERIAL.item();
										break;
									case "massue":
										temp = Packs.MASS.item();
										break;
									case "smith":
										temp = Items.getAmmoStack(new ItemStack(Material.SLIME_BALL));
										break;
									case "antalgiques":
										temp = Packs.ANTALGIQUES.item();
										break;
									default:
										this.sendMessages(player, "Objets disponibles:", 
												"eau", 
												"maillot", 
												"pantalon", 
												"casquette", 
												"bottes", 
												"machette",
												"hache", 
												"mosin - Balles de Mosin", 
												"ak47 - ---------- AK-47", 
												"m320 - ----------- M320H",
												"bar - ------------ BAR Browning", 
												"smith - ----------- Smith", 
												"arc", 
												"bandages", 
												"sang",
												"camo - Plastron de camouflage", 
												"camo_helmet - Casque de camouflage", 
												"camo_boots - Bottes de camouflage",
												"camo_pant - Pantalon de camouflage", 
												"amphet", 
												"blouse", 
												"neurotoxic", 
												"arteriel", 
												"massue",
												"antalgiques");
										return true;
								}
								if(temp.getType() != Material.AIR){
									if(args.length > 2){
										try{
											temp.setAmount(Integer.valueOf(args[2]).intValue());
											player.getInventory().addItem(temp);
											return true;
										}catch(NumberFormatException e){
											PluginMethods.alert(player, "Nombre spécifié invalide");
											return true;
										}
									}
									temp.setAmount(1);
									player.getInventory().addItem(temp);
								}
							} else
								this.showAccessRefused(player);
						} else
							this.intoPlayer(sender);

					} else
						this.sendMessages((Player) sender, "Objets disponibles:", 
								"eau", 
								"maillot", 
								"pantalon", 
								"casquette", 
								"bottes", 
								"machette",
								"hache", 
								"mosin - Balles de Mosin", 
								"ak47 - ---------- AK-47", 
								"m320 - ----------- M320H",
								"bar - ------------ BAR Browning", 
								"smith - ----------- Smith", 
								"arc", 
								"bandages", 
								"sang",
								"camo - Plastron de camouflage", 
								"camo_helmet - Casque de camouflage", 
								"camo_boots - Bottes de camouflage",
								"camo_pant - Pantalon de camouflage", 
								"amphet", 
								"blouse", 
								"neurotoxic", 
								"arteriel", 
								"massue",
								"antalgiques");
				} else if(args[0].equalsIgnoreCase("pack")) {
					if(args.length > 1) {
						if(sender instanceof Player) {
							final Player player = (Player) sender;
							if(player.hasPermission("clp.pack.define") || player.isOp()) {
								final EnumPacks thePack = Loots.factor.parsePack(args[1]);
								if(thePack.toString().equalsIgnoreCase("NULL")) {
									this.specifyPack(player);
									return true;
								}
								Loots.factor
								.defineSpawnPoint(Loots.factor.PACK, player.getTargetBlock(null, 50).getLocation(), Loots.factor.parsePack(args[1]));
							} else
								this.showAccessRefused(player);
						} else
							this.intoPlayer(sender);

					} else
						this.showHelp(sender);
				} else if(args[0].equalsIgnoreCase("loot")) {
					if(sender instanceof Player) {
						final Player player = (Player) sender;
						if(args.length > 1)
							if(player.hasPermission("clp.loot.define") || player.isOp()) {
								final EnumPacks thePack = Loots.factor.parsePack(args[1]);
								if(thePack.toString().equalsIgnoreCase("NULL")) {
									this.specifyPack(player);
									return true;
								}
								Loots.factor
								.defineSpawnPoint(Loots.factor.LOOT, player.getTargetBlock(null, 50).getLocation(), Loots.factor.parsePack(args[1]));
							}
							else
								this.showAccessRefused(player);
					} else
						this.intoPlayer(sender);
				} else if(args[0].equalsIgnoreCase("delpack")) {
					if(sender instanceof Player) {
						if(sender.hasPermission("clp.pack.delete")) {
							final Player player = (Player) sender;
							if(Loots.factor.deleteSpawnPoint(Loots.factor.PACK, player.getTargetBlock(null, 50).getLocation()))
								PluginMethods.gsay(player, "Point de pack supprimé.");
							else
								PluginMethods.warn(player, "Impossible de supprimer le pack.");
						} else
							this.showAccessRefused((Player) sender);
					} else
						this.intoPlayer(sender);
				} else if(args[0].equalsIgnoreCase("del")) {
					if(sender instanceof Player) {
						if(sender.hasPermission("clp.pack.delete") || sender.isOp()) {
							final Player player = (Player) sender;
							if(ELoots.editers.contains(player)){
								PluginMethods.warn(player, "Mode d'édition désactivé.");
								ELoots.editers.remove(player);
							} else{
								PluginMethods.warn(player, "Mode d'édition activé. Clic droit pour supprimer un loot.");
								ELoots.editers.add(player);
							}
						} else
							this.showAccessRefused((Player) sender);
					} else
						this.intoPlayer(sender);
				} else if(args[0].equalsIgnoreCase("packs")) {
					sender.sendMessage("|-----Liste des packs-----|");
					sender.sendMessage("CHAMBRE - Pack de type chambre. Contient divers objets.");
					sender.sendMessage("FERME - Contient de la nourriture et des objets de culture.");
					sender.sendMessage("CUISINE - Contient principalement de la nourriture.");
					sender.sendMessage("SUPERETTE - Contient beaucoup d'objets divers.");
					sender.sendMessage("MILITAIRE - Contient des armes, des munitions, etc.");
					sender.sendMessage("HOPITAL - Contient des médicaments, drogues, bandages...");
					sender.sendMessage("-------------------------------------------------");
					sender.sendMessage("/clp info <nom du pack en miniscules> - Obtenir les détails du pack.");
					sender.sendMessage("-------------------------------------------------");
				} else if(args[0].equalsIgnoreCase("info")) {
					if(args.length > 1)
						switch(args[1]) {
							case "chambre":
								this.sendMessages((Player) sender, "- Porc cru", 
										"- Poche de sang", 
										"- Bouteille d'eau", 
										"- Habits divers", 
										"- Epée en bois",
										"- Arc", 
										"- Fléches", 
										"- Montre", 
										"- Torches", 
										"- Carte",
										"- Soupe", 
										"- Livre et plume", 
										"- Balles de Mosin",
										"- Sac à dos", 
										"-------------");
								break;
							case "ferme":
								this.sendMessages((Player) sender, "- Graines", 
										"- Blé", 
										"- Carte", 
										"- Faux", 
										"- Hache", 
										"- Machette", 
										"- Bottes",
										"- Oeil d'araignée pourri", 
										"-------------");
								break;
							case "cuisine":
								this.sendMessages((Player) sender, "- Porc cuit", 
										"- Steak", 
										"- Poulet", 
										"- Soupe", 
										"- Pain", 
										"- Poisson", 
										"- Pastèque",
										"- Bouteille d'eau", 
										"- Oeil d'araignée pourri", 
										"-------------");
								break;
							case "superette":
								this.sendMessages((Player) sender, "- Porc cuit", 
										"- Steak", 
										"- Poulet", 
										"- Poisson", 
										"- Pastèque", 
										"- Bouteille d'eau",
										"- Machette", 
										"- Redstone", 
										"- Faux", 
										"- Fer", 
										"- Boussole", 
										"- Carte", 
										"- Lait", 
										"- Bandages", 
										"- Poches de sang",
										"- Sac à dos", 
										"-------------");
								break;
							case "militaire":
								this.sendMessages((Player) sender, "- Chargeurs d'AK-47", 
										"- Fusil d'assaut AK-47", 
										"- Fusil de sniper Mosin Nagant",
										"- Lance-grenades M320H", 
										"- Mitrailleuse BAR Browning", 
										"- Grenades pour M320", 
										"- Balles de Mosin",
										"- Chargeurs de BAR", 
										"- Carte", 
										"- Boussole", 
										"- Bandages", 
										"- Poches de sang", 
										"- Tenue de camouflage",
										"- Sac à dos", 
										"- Machette", 
										"- Arc", 
										"- Bottes",
										"- Pantalon", 
										"-------------");
								break;
							case "hopital":
								this.sendMessages((Player) sender, "- Poches de sang", 
										"- Bandages", 
										"- Amphétamines", 
										"- Pain",
										"- Blouse",
										"- Oeil d'araignée fermenté", 
										"-------------");
								break;
							default:
								this.sendMessages((Player) sender, "Le pack spécifié n'existe pas !");
						}
					else
						this.showHelp(sender);
				} else
					this.showHelp(sender);
			} else
				this.showHelp(sender);
		return true;
	}

	public void sendMessages(final Player p, final String... msgs) {
		for(final String msg : msgs)
			p.sendMessage(msg);
	}

	public void specifyPack(final Player player) {
		PluginMethods.warn(player, "Veuillez spécifier un pack valide !");
	}

	public void showAccessRefused(final Player player) {
		PluginMethods.warn(player, "Vous n'avez pas la permission !");
	}

	public void intoPlayer(final CommandSender sender) {
		sender.sendMessage("Veuillez vous connecter en tant que joueur.");
	}

	public void showHelp(final CommandSender sender) {
		sender.sendMessage("/clp loot <nom du pack> - Definir un point de loot.");
		sender.sendMessage("/clp pack <nom du pack> - Definir un point de pack(coffre).");
		sender.sendMessage("/clp delpack - Supprimer un pack(coffre).");
		sender.sendMessage("/clp del - Supprimer un loot.");
		sender.sendMessage("/clp get [objet] (nombre) - Recuperer un objet personnalisé. Ne rien mettre pour la liste des objets.");
		sender.sendMessage("/clp packs - Obtenir la liste des packs.");
		sender.sendMessage("/clp info <nom du pack en miniscule> - Détails d'un pack.");
	}
}
