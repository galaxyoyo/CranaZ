package com.bp389.cranaz.thirst;

import java.io.File;
import java.io.IOException;

import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * Classe statique contenant les m�thodes relatives au plugin de soif
 * 
 * @author BlackPhantom
 * 
 */
public final class ThirstFactor {

	private static JavaPlugin jp;
	public static final File config = new File("plugins/CranaZ/Thirst/config.yml");

	public static void init(final JavaPlugin plugin) {
		ThirstFactor.jp = plugin;
	}

	/**
	 * 
	 * @return Le pourcentage � retirer � chaque cycle, dans la config
	 */
	public static int getPercentRet() {
		final FileConfiguration fc = ThirstFactor.jp.getConfig();
		try {
			fc.load(ThirstFactor.config);
			return fc.getInt("thirst.config.pourcentageRetire");
		} catch(IOException | InvalidConfigurationException e) {
			return 5;
		}
	}

	/**
	 * 
	 * @return La valeur flottante � retirer de la barre d'exp
	 */
	public static strictfp float getPRF() {
		return Float.valueOf(Integer.valueOf(ThirstFactor.getPercentRet()).floatValue() / 100F).floatValue();
	}

	/**
	 * 
	 * @return Le d�lai entre chaque cycle
	 */
	public static int getDelay() {
		final FileConfiguration fc = ThirstFactor.jp.getConfig();
		try {
			fc.load(ThirstFactor.config);
			return fc.getInt("thirst.config.delaiEnSecondes");
		} catch(IOException | InvalidConfigurationException e) {
			return 120;
		}
	}

	/**
	 * Enregistre la configuration
	 */
	public static void registerDC() {
		if(!ThirstFactor.config.exists()) {
			final FileConfiguration fc = ThirstFactor.jp.getConfig();
			fc.set("thirst.config.delaiEnSecondes", 240);
			fc.set("thirst.config.pourcentageRetire", 5);
			try {
				fc.save(ThirstFactor.config);
			} catch(final IOException e) {}
		}
	}
}
