package com.bp389;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public final class PluginMethods {

	private static Logger bLog;
	private static boolean init = false;
	public static final String ls = System.getProperty("line.separator");

	/* Acronyme de consoleSend */
	private static void csend(final Level level, final String message) {
		if(PluginMethods.init != false)
			PluginMethods.bLog.log(level, message);
	}

	/* Initialisation, acronyme de methodInit */
	public static void minit(final Logger log) {
		PluginMethods.bLog = log;
		PluginMethods.init = true;
	}

	/* Création des fichiers de config */
	public static boolean files() {
		/* return new File("plugins/CranaZ/spawners/").mkdirs(); */return true;
	}

	/* Destruction des variables, acronyome de methodDestroy */
	public static void mdestroy() {
		PluginMethods.bLog = null;
		PluginMethods.init = false;
	}

	/* Caster une info dans la console */
	public static void info(final String message) {
		if(PluginMethods.init != false)
			PluginMethods.csend(Level.INFO, message);
		return;
	}

	/* Caster un warning dans la console */
	public static void warn(final String message) {
		if(PluginMethods.init != false)
			PluginMethods.csend(Level.WARNING, message);
	}

	/* Caster une alerte dans la console */
	public static void alert(final String message) {
		if(PluginMethods.init != false)
			PluginMethods.csend(Level.SEVERE, message);
	}

	/* Lire dans un fichier quelconque */
	public static String strReadFrom(final File file) {
		try {
			final BufferedReader reader = new BufferedReader(new FileReader(file));
			int i = 0;
			String rd_str = "";
			while((i = reader.read()) != -1)
				rd_str += (char) i;
			reader.close();
			return rd_str;
		} catch(final Exception e) {
			return "err";
		}
	}

	/* Envoyer une alerte (en rouge) à un joueur */
	public static void alert(final Player player, final String message) {
		player.sendRawMessage(ChatColor.RED + message + ChatColor.RESET);
	}

	/* Envoyer un avertissement (en orange) à un joueur */
	public static void warn(final Player player, final String message) {
		player.sendRawMessage(ChatColor.GOLD + message + ChatColor.RESET);
	}

	/* Envoyer un message positif (en vert) à un joueur */
	public static void gsay(final Player player, final String message) {
		player.sendRawMessage(ChatColor.GREEN + message + ChatColor.RESET);
	}

	/* Ecrire dans un fichier quelconque */
	public static boolean strWriteTo(final File file, final String str, final boolean append) {
		if(file != null && str != null && !str.isEmpty())
			try {
				final BufferedWriter writer = new BufferedWriter(new FileWriter(file, append));
				writer.write(str);
				writer.close();
			} catch(final IOException e) {
				return false;
			}
		return true;
	}
}
