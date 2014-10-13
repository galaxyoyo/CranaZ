package com.bp389.cranaz.bags;

import static com.bp389.PluginMethods.warn;

import java.io.File;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.bp389.cranaz.Loadable;
import com.bp389.cranaz.items.Items;

public class Bags extends Loadable{
	public static final File getBagFile(String pName){
		return new File(IPlayerFactor.bagsLoc + pName + ".yml");
	}
	@SuppressWarnings("deprecation")
    @Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if(command.getName().equalsIgnoreCase("bags"))
		{
			if(sender instanceof Player)
			{
				if(args.length > 0)
				{
					if(args[0].equalsIgnoreCase("get")){
						Player p = (Player)sender;
						if(p.hasPermission("cranaz.bags.get") || p.isOp()){
							p.getInventory().addItem(Items.bagItemStack());
						}
						else
							warn((Player)sender, "Vous n'avez pas la permission.");
					}
					else if(args[0].equalsIgnoreCase("give"))
					{
						if(args.length > 1)
						{
							Player p = (Player)sender;
							if(p.hasPermission("cranaz.bags.give") || p.isOp()){
								Player p1 = Bukkit.getPlayer(args[1]);
								if(p1 == null){
									warn(p, "Joueur non trouvé.");
									return true;
								}
								p1.getInventory().addItem(Items.bagItemStack());
							}
							else
								warn((Player)sender, "Vous n'avez pas la permission.");
						}
					}
				}
			}
			else
				sender.sendMessage("Connectez-vous en tant que joueur.");
		}
	   return true;
	}
}
