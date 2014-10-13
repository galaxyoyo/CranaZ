package com.bp389.cranaz;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

import com.bp389.cranaz.bags.Bags;
import com.bp389.cranaz.bags.IPlayerFactor;
import com.bp389.cranaz.effects.Bleed;
import com.bp389.cranaz.effects.Effects;
import com.bp389.cranaz.ia.ZIA;
import com.bp389.cranaz.ia.entities.EnhancedZombie;
import com.bp389.cranaz.items.CItems;
import com.bp389.cranaz.items.Items;
import com.bp389.cranaz.legacy.Legacy;
import com.bp389.cranaz.legacy.NQHandler;
import com.bp389.cranaz.loots.Loots;
import com.bp389.cranaz.thirst.ThirstFactor;

/**
 * Classe permettant le chargement adéquat des sous-parties du plugin
 * à l'aide de méthodes statiques
 * @author BlackPhantom
 *
 */
public final class Loader {
	public static JavaPlugin plugin;
	private static final int PLUGIN_COUNT = 6;
	public static final void init(JavaPlugin jp){
		plugin = jp;
	}
	public static final void loadAll(JavaPlugin jp){
		  initAll(jp);
		  //loadClass(CZBMain.class);
		  //loadClass(CZE.class);
		  CItems czi = new CItems();
		  czi.onEnable();
		  
		  Legacy czl = new Legacy();
		  czl.onEnable();
		  //loadClass(LAP.class);
		  ZIA zia = new ZIA();
		  zia.onEnable();
	}
	public static final void initAll(JavaPlugin jp){
		IPlayerFactor.init(jp);
		Bleed.init(jp);
		Items.init(jp);
		NQHandler.init(jp);
		ThirstFactor.init(jp);
		EnhancedZombie.initPlugin(jp);
		ZIA.Utils.ini(jp);
	}
	public static final void parseCommand(CommandSender cs, Command c, String lbl, String[] args){
		for(int i = 0;i < PLUGIN_COUNT;i++){
			switch(i){
			case 0:
				Bags czbm = new Bags();
				czbm.onCommand(cs, c, lbl, args);
				break;
			case 1:
				Effects cze = new Effects();
				cze.onCommand(cs, c, lbl, args);
				break;
			case 2:
				CItems czi = new CItems();
				czi.onCommand(cs, c, lbl, args);
				break;
			case 3:
				Legacy czl = new Legacy();
				czl.onCommand(cs, c, lbl, args);
				break;
			case 4:
				Loots lap = new Loots();
				lap.onCommand(cs, c, lbl, args);
				break;
			case 5:
				ZIA zia = new ZIA();
				zia.onCommand(cs, c, lbl, args);
				break;
			}
		}
	}
}
