package com.bp389.cranaz.loots;

import static com.bp389.PluginMethods.gsay;
import static com.bp389.PluginMethods.warn;

import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.bp389.cranaz.Loadable;
import com.bp389.cranaz.items.Items;

public class LAP extends Loadable{
	public static LootRefactor factor = new LootRefactor();
	public static void startSpawns(){factor.startSpawns();}
	@SuppressWarnings({ "deprecation", "static-access" })
    @Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if(command.getName().equalsIgnoreCase("clp"))
		{
			if(args.length > 0)
			{
				if(args[0].equalsIgnoreCase("get"))
				{
					if(args.length > 1)
					{
						if(sender instanceof Player)
						{
							Player player = (Player)sender;
							if(player.hasPermission("clp.items.get") || player.isOp())
							{
								ItemStack temp = new ItemStack(Material.AIR);
								switch(args[1])
								{
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
									temp = Packs.ISWORD.item();
									break;
								case "hache":
									temp = Packs.IAXE.item();
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
									temp = Packs.GOLDEN_APPLE.item();
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
									temp = Packs.SSWORD.item();
									break;
								case "smith":
									temp = Items.getAmmoStack(new ItemStack(Material.SLIME_BALL));
									break;
								default:
									sendMessages(player, "Objets disponibles:",
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
											"massue");
									return true;
								}
								if(temp.getType() != Material.AIR){
									player.getInventory().addItem(temp);
								}
							}
							else
								showAccessRefused(player);
						}
						else
							intoPlayer(sender);
							
					}
					else
					{
						sendMessages((Player)sender, "Objets disponibles:",
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
								"massue");
					}
				}
				else if(args[0].equalsIgnoreCase("pack"))
				{
					if(args.length > 1)
					{
						if(sender instanceof Player)
						{
							Player player = (Player)sender;
							if(player.hasPermission("clp.pack.define") || player.isOp())
							{
								EnumPacks thePack = factor.parsePack(args[1]);
								if(thePack.toString().equalsIgnoreCase("NULL")){
									specifyPack(player);
									return true;
								}
								factor.defineSpawnPoint(factor.PACK, player.getTargetBlock(null, 50).getLocation(), factor.parsePack(args[1]));
							}
							else
								showAccessRefused(player);
						}
						else
							intoPlayer(sender);
							
					}
					else
						showHelp(sender);
				}
				else if(args[0].equalsIgnoreCase("loot"))
				{
					if(sender instanceof Player)
					{
						Player player = (Player)sender;
						if(args.length > 1)
						{
							if(player.hasPermission("clp.loot.define") || player.isOp())
							{
								EnumPacks thePack = factor.parsePack(args[1]);
								if(thePack.toString().equalsIgnoreCase("NULL")){
									specifyPack(player);
									return true;
								}
								factor.defineSpawnPoint(factor.LOOT, player.getTargetBlock(null, 50).getLocation(), factor.parsePack(args[1]));
							}
						}
					}
					else
						intoPlayer(sender);
				}
				else if(args[0].equalsIgnoreCase("delpack"))
				{
					if(sender instanceof Player)
					{
						if(sender.hasPermission("clp.pack.delete")){
							Player player = (Player)sender;
							if(factor.deleteSpawnPoint(factor.PACK, player.getTargetBlock(null, 50).getLocation()))
								gsay(player, "Point de pack supprim�.");
							else
								warn(player, "Impossible de supprimer le pack.");
						}
						else
							showAccessRefused((Player)sender);
					}
					else
						intoPlayer(sender);
				}
				else if(args[0].equalsIgnoreCase("del"))
				{
					if(sender instanceof Player)
					{
						if(sender.hasPermission("clp.pack.delete")){
							Player player = (Player)sender;
							if(factor.deleteSpawnPoint(factor.LOOT, player.getTargetBlock(null, 50).getLocation()))
								gsay(player, "Point de pack supprim�.");
							else
								warn(player, "Impossible de supprimer le pack.");
						}
						else
							showAccessRefused((Player)sender);
					}
					else
						intoPlayer(sender);
				}
				else if(args[0].equalsIgnoreCase("packs"))
				{
					sender.sendMessage("|-----Liste des packs-----|");
					sender.sendMessage("CHAMBRE - Pack de type chambre. Contient divers objets.");
					sender.sendMessage("FERME - Contient de la nourriture et des objets de culture.");
					sender.sendMessage("CUISINE - Contient principalement de la nourriture.");
					sender.sendMessage("SUPERETTE - Contient beaucoup d'objets divers.");
					sender.sendMessage("MILITAIRE - Contient des armes, des munitions, etc.");
					sender.sendMessage("HOPITAL - Contient des m�dicaments, drogues, bandages...");
					sender.sendMessage("-------------------------------------------------");
					sender.sendMessage("/clp info <nom du pack en miniscules> - Obtenir les d�tails du pack.");
					sender.sendMessage("-------------------------------------------------");
				}
				else if(args[0].equalsIgnoreCase("info"))
				{
					if(args.length > 1)
					{
						switch(args[1])
						{
						case "chambre":
							sendMessages((Player)sender, "- Porc cru",
									"- Poche de sang",
									"- Bouteille d'eau",
									"- Habits divers",
									"- Ep�e en bois",
									"- Arc",
									"- Fl�ches",
									"- Montre",
									"- Torches",
									"- Carte",
									"- Soupe",
									"- Livre et plume",
									"- Balles de Mosin",
									"- Sac � dos",
									"-------------");
							break;
						case "ferme":
							sendMessages((Player)sender, "- Graines",
									"- Bl�",
									"- Carte",
									"- Faux",
									"- Hache",
									"- Machette",
									"- Bottes",
									"- Oeil d'araign�e pourri",
									"-------------");
							break;
						case "cuisine":
							sendMessages((Player)sender, "- Porc cuit",
									"- Steak",
									"- Poulet",
									"- Soupe",
									"- Pain",
									"- Poisson",
									"- Past�que",
									"- Bouteille d'eau",
									"- Oeil d'araign�e pourri",
									"-------------");
							break;
						case "superette":
							sendMessages((Player)sender, "- Porc cuit",
									"- Steak",
									"- Poulet",
									"- Poisson",
									"- Past�que",
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
									"- Sac � dos",
									"-------------");
							break;
						case "militaire":
							sendMessages((Player)sender, "- Chargeurs d'AK-47",
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
									"- Sac � dos",
									"- Machette",
									"- Arc",
									"- Bottes",
									"- Pantalon",
									"-------------");
							break;
						case "hopital":
							sendMessages((Player)sender, "- Poches de sang",
									"- Bandages",
									"- Amph�tamines",
									"- Pain",
									"- Blouse",
									"- Oeil d'araign�e ferment�",
									"-------------");
							break;
						default:
							sendMessages((Player)sender, "Le pack sp�cifi� n'existe pas !");
						}
					}
					else
						showHelp(sender);
				}
				else
					showHelp(sender);
			}
			else
				showHelp(sender);
		}
		return true;
	}
	public void sendMessages(Player p, String... msgs)
	{
		for(int i = 0;i < msgs.length;i++){
			p.sendMessage(msgs[i]);
		}
	}
	public void specifyPack(Player player)
	{
		warn(player, "Veuillez sp�cifier un pack valide !");
	}
	public void showAccessRefused(Player player)
	{
		warn(player, "Vous n'avez pas la permission !");
	}
	public void intoPlayer(CommandSender sender)
	{
		sender.sendMessage("Veuillez vous connecter en tant que joueur.");
	}
	public void showHelp(CommandSender sender)
	{
		sender.sendMessage("/clp loot <nom du pack> - Definir un point de loot.");
		sender.sendMessage("/clp pack <nom du pack> - Definir un point de pack(coffre).");
		sender.sendMessage("/clp delpack - Supprimer un pack(coffre).");
		sender.sendMessage("/clp del - Supprimer un loot.");
		sender.sendMessage("/clp get [objet] - Recuperer un objet personnalis�. Ne rien mettre pour la liste des objets.");
		sender.sendMessage("/clp packs - Obtenir la liste des packs.");
		sender.sendMessage("/clp info <nom du pack en miniscule> - D�tails d'un pack.");
	}
}