package com.bp389.cranaz;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

import com.bp389.cranaz.FPS.FPS;
import com.bp389.cranaz.bags.Bags;
import com.bp389.cranaz.bags.IPlayerFactor;
import com.bp389.cranaz.effects.Bleed;
import com.bp389.cranaz.effects.Effects;
import com.bp389.cranaz.ia.ZIA;
import com.bp389.cranaz.ia.entities.EnhancedZombie;
import com.bp389.cranaz.items.CItems;
import com.bp389.cranaz.legacy.Legacy;
import com.bp389.cranaz.legacy.NQHandler;
import com.bp389.cranaz.loots.Loots;
import com.bp389.cranaz.thirst.ThirstFactor;

/**
 * Classe permettant le chargement adéquat des sous-parties du plugin à l'aide
 * de méthodes statiques
 * 
 * @author BlackPhantom
 * 
 */
public final class Loader {

	public static JavaPlugin plugin;
	private static final int PLUGIN_COUNT = 7;

	public static final void init(final JavaPlugin jp) {
		Loader.plugin = jp;
	}

	public static final void loadAll(final JavaPlugin jp) {
		Loader.initAll(jp);
		// loadClass(CZBMain.class);
		// loadClass(CZE.class);
		final CItems czi = new CItems();
		czi.onEnable();

		final Effects cze = new Effects();
		cze.onEnable();

		final Legacy czl = new Legacy();
		czl.onEnable();
		// loadClass(LAP.class);
		final ZIA zia = new ZIA();
		zia.onEnable();
		
		final FPS fps = new FPS();
		fps.onEnable();
	}

	public static final void initAll(final JavaPlugin jp) {
		IPlayerFactor.init(jp);
		Bleed.init(jp);
		NQHandler.init(jp);
		ThirstFactor.init(jp);
		EnhancedZombie.initPlugin(jp);
	}

	public static final void parseCommand(final CommandSender cs, final Command c, final String lbl, final String[] args) {
		for(int i = 0; i < Loader.PLUGIN_COUNT; i++)
			switch(i) {
				case 0:
					final Bags czbm = new Bags();
					czbm.onCommand(cs, c, lbl, args);
					break;
				case 1:
					final Effects cze = new Effects();
					cze.onCommand(cs, c, lbl, args);
					break;
				case 2:
					final CItems czi = new CItems();
					czi.onCommand(cs, c, lbl, args);
					break;
				case 3:
					final Legacy czl = new Legacy();
					czl.onCommand(cs, c, lbl, args);
					break;
				case 4:
					final Loots lap = new Loots();
					lap.onCommand(cs, c, lbl, args);
					break;
				case 5:
					final ZIA zia = new ZIA();
					zia.onCommand(cs, c, lbl, args);
					break;
				case 6:
					final FPS fps = new FPS();
					fps.onCommand(cs, c, lbl, args);
			}
	}
}
