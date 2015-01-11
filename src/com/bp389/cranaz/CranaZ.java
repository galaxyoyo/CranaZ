package com.bp389.cranaz;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import com.bp389.PluginMethods;
import com.bp389.cranaz.events.EBags;
import com.bp389.cranaz.events.EEffects;
import com.bp389.cranaz.events.EIA;
import com.bp389.cranaz.events.EItemsLegacy;
import com.bp389.cranaz.events.ELoots;
import com.bp389.cranaz.events.EThirst;
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
		Loader.init(this);
		PluginMethods.minit(this.getLogger());
		PluginMethods.files();
		Loader.loadAll(this);
	}

	@Override
	public void onEnable() {
		final PluginManager pm = this.getServer().getPluginManager();
		pm.registerEvents(new EBags(this), this);
		pm.registerEvents(new EEffects(this), this);
		pm.registerEvents(new EIA(this), this);
		pm.registerEvents(new EItemsLegacy(this), this);
		pm.registerEvents(new ELoots(this), this);
		pm.registerEvents(new EThirst(this), this);
	}

	@Override
	public void onDisable() {
		final ZIA zia = new ZIA();
		zia.onDisable();
	}

	@Override
	public boolean onCommand(final CommandSender sender, final Command command, final String label, final String[] args) {
		Loader.parseCommand(sender, command, label, args);
		return true;
	}
}
