package com.bp389.cranaz;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

import com.bp389.PluginMethods;
import com.bp389.cranaz.FPS.FPS;
import com.bp389.cranaz.events.GEvent;
import com.bp389.cranaz.ia.ZIA;

/**
 * Voir le fichier de licence et la permission d'utilisation accordée à CranaZ
 * et Swiftia Studios et affiliatures directes.
 * 
 * @author BlackPhantom
 * 
 */
public final class CranaZ extends JavaPlugin {

	@Override
	public void onLoad() {
		PluginMethods.minit(this.getLogger());
		PluginMethods.files();
		Loader.init(this);
		Loader.loadAll(this);
	}

	@Override
	public void onEnable() {
		GEvent.registerAllEvents(this);
	}

	@Override
	public void onDisable() {
		PluginMethods.info("Extinction globale de CranaZ...");
		final ZIA zia = new ZIA();
		zia.onDisable();
		
		final FPS fps = new FPS();
		fps.onDisable();
	}

	@Override
	public boolean onCommand(final CommandSender sender, final Command command, final String label, final String[] args) {
		Loader.parseCommand(sender, command, label, args);
		return true;
	}
}
