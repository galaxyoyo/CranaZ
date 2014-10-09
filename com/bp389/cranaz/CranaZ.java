package com.bp389.cranaz;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

import com.bp389.PluginMethods;
import com.bp389.cranaz.ia.ZIA;

/**
 * Voir le fichier de licence et la permission d'utilisation accordée
 * à CranaZ et Swiftia Studios et affiliatures directes.
 * @author BlackPhantom
 *
 */
public class CranaZ extends JavaPlugin
{
	@Override
	public void onLoad() {
		Loader.init(this);
		PluginMethods.minit(getLogger());
		PluginMethods.files();
		Loader.loadAll(this);
	}
	@Override
	public void onEnable() {
		getServer().getPluginManager().registerEvents(new Events(this), this);
	}
	@Override
	public void onDisable() {
		ZIA zia = new ZIA();
		zia.onDisable();
	}
	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
	    Loader.parseCommand(sender, command, label, args);
	    return true;
	}
}
