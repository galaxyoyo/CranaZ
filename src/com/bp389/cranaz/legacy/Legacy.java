package com.bp389.cranaz.legacy;

import java.io.File;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import com.bp389.cranaz.Loadable;
import com.bp389.cranaz.Loader;

public class Legacy extends Loadable {
	public static final File wl = new File("plugins/CranaZ/whitelist/");
	@Override
	public void onEnable() {
		wl.mkdirs();
		//Utils.init(this);
	}
	@Override
	public boolean onCommand(final CommandSender arg0, Command arg1, String arg2, String[] arg3) {
		/*if(arg1.getName().equalsIgnoreCase("czl"))
		{
			if(arg3.length <= 0){
				arg0.sendMessage("Veuillez spécifier une commande.");
				return true;
			}
			if(!(arg0 instanceof Player)){
				arg0.sendMessage("Veuillez vous connecter.");
				return true;
			}
			Player p = (Player)arg0;
			if(arg3[0].equalsIgnoreCase("add")){
				if((p.hasPermission("legacy.whitelist.add") || p.isOp()) && arg3.length >= 2){
					Utils.addToWhitelist(arg3[1], p);
				}
			}
			else if(arg3[0].equalsIgnoreCase("remove")){
				if((p.hasPermission("legacy.whitelist.remove") || p.isOp()) && arg3.length >= 2){
					Utils.removeWhitelist(arg3[1], p);
				}
			}
		}*/
		if(arg1.getName().equalsIgnoreCase("logout"))
		{
			arg0.sendMessage("Déconnexion dans 3 secondes...");
			Bukkit.getScheduler().runTaskLater(Loader.plugin, new BukkitRunnable(){
				@Override
                public void run() {
					if(!(arg0 instanceof Player))
						return;
					Player p = (Player)arg0;
					NQHandler.forceRemove(p);
					p.kickPlayer("Déconnecté.");
                }
			}, 3L * 20L);
		}
		return true;
	}
	/*public static class Utils{
		private static JavaPlugin jp;
		public static void init(JavaPlugin jp){
			Utils.jp = jp;
		}
		public static boolean isPremium(Player p){
			Boolean premium = false;
			try {
			URL url = new URL("https://www.minecraft.net/haspaid.jsp?user=" + URLEncoder.encode(p.getName(), "UTF-8"));
			String pr = new BufferedReader(new InputStreamReader(url.openStream())).readLine().toUpperCase();
			premium = Boolean.valueOf(pr);
			} catch (Exception e) {
			e.printStackTrace();
			}
			return premium.booleanValue();
		}
		public static ArrayList<String> getWhitelist(){
			ArrayList<String> als = new ArrayList<String>();
			als.add("BlackPhantom");
			if(wl.listFiles().length == 0)
				return als;
			for(File f : wl.listFiles()){
				als.add(f.getName());
			}
			return als;
		}
		public static void removeWhitelist(String sp, Player p1){
			File f = new File("plugins/CranaZ/whitelist/" + sp);
			if(f.exists())
				f.delete();
			else
				p1.sendMessage("§r§cCe joueur n'est pas dans la whitelist.");
		}
		public static void addToWhitelist(String sp, Player p1){
			File f = new File("plugins/CranaZ/whitelist/" + sp);
			if(!f.exists()){
				try {
					f.createNewFile();
					p1.sendMessage((String[])Arrays.asList("Le joueur " + sp + " a été ajouté à la whitelist.", "Mieux vaudrait sécuriser son compte.").toArray());
				} catch (IOException e) {
					p1.sendMessage("§r§cImpossible d'ajouter le joueur à la whitelist. Vérifier la console.");
					jp.getLogger().log(Level.SEVERE, "§r§c" + e.getMessage());
				}
			}
			else
				p1.sendMessage("§r§cCe joueur est déjà dans la whitelist.");
		}
		public static boolean isWhitelist(Player p){
			return getWhitelist().contains(p.getName());
		}
	}*/
}
