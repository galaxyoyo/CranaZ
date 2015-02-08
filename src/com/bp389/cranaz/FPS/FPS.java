package com.bp389.cranaz.FPS;

import java.io.File;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.bp389.PluginMethods;
import com.bp389.cranaz.Loadable;
import com.bp389.cranaz.Util;
import com.bp389.cranaz.YamlObj;


public final class FPS extends Loadable {
	public static final File fps_config = new File("plugins/CranaZ/configuration/FPS.yml");
	public static final String GLOBAL_EXPLOSION_DELAY = "arenas.delai_explosion";
	@Override
	public void onEnable() {
	    if(!fps_config.exists())
	    	Util.saveToYaml(fps_config, new YamlObj(GLOBAL_EXPLOSION_DELAY, 30));
	}
	@Override
	public boolean onCommand(CommandSender sender, Command c, String lbl, String[] args) {
	    if(c.getName().equalsIgnoreCase("fps")){
	    	if(!(sender instanceof Player)){
	    		sender.sendMessage("Connectez-vous en tant que joueur.");
	    		return true;
	    	}
	    	Player p = (Player)sender;
	    	if(args.length <= 0)
	    		return false;
	    	if(args[0].equalsIgnoreCase("arena")){
	    		if(args.length <= 1)
	    			return false;
	    		if(args[1].equalsIgnoreCase("create")){
	    			if(p.hasPermission("cranaz.fps.arena.create") || p.isOp()){
	    				if(args.length >= 3){
	    					try{
	    						final long l = Integer.valueOf(args[2]).longValue();
	    						if(l < FPSIO.getExplosionDelay() * 2L){
	    							PluginMethods.alert(p, "La durée d'un match doit être au moins 2x supérieure");
	    							PluginMethods.alert(p, "au temps d'explosion d'une bombe.");
	    							return true;
	    						}
	    						FPSIO.onCreationMode(p, l, args[3]);
	    					}catch(NumberFormatException e){
	    						PluginMethods.alert(p, "Certains arguments sont invalides, l'usage est :");
	    						PluginMethods.alert(p, "/fps arena create <durée match, secondes> <nom arène>");
	    					}
	    				}
	    				else
	    					arguments(p);
	    			}
	    			else
	    				noAccess(p);
	    		}
	    	}
	    }
	    else if(c.getName().equalsIgnoreCase("exit")){
	    	if(!(sender instanceof Player)){
	    		sender.sendMessage("Connectez-vous en tant que joueur.");
	    		return true;
	    	}
	    	final Player p = (Player)sender;
	    	if(!Arena.isInArena(p)){
	    		PluginMethods.alert(p, "Tu n'est pas dans une arène !");
	    		return true;
	    	}
	    	final Arena a = Arena.hasJoined.get(p);
	    	a.forceRemove(p, a.isInGame(p), true);
	    	PluginMethods.gsay(p, "Tu as quitté l'arène");
	    }
		return true;
	}
	public static void noAccess(final Player p){
		PluginMethods.alert(p, "Vous n'avez pas accès à cette commande !");
	}
	public static void arguments(final Player p){
		PluginMethods.alert(p, "Il faut plus d'arguments pour cette commande !");
	}
}
