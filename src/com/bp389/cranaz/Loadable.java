package com.bp389.cranaz;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;

/**
 * Classe abstraite mère de toutes les sous-parties du plugin
 * 
 * @author BlackPhantom
 * 
 */
public abstract class Loadable {

	public void onLoad() {}

	public void onEnable() {}

	public void onDisable() {}

	public boolean onCommand(final CommandSender sender, final Command c, final String lbl, final String[] args) {
		return true;
	}

	public FileConfiguration getConfig() {
		return Loader.plugin.getConfig();
	}
}
