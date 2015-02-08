package com.bp389.cranaz.thirst;

import java.io.File;

import org.bukkit.plugin.java.JavaPlugin;

import com.bp389.cranaz.Util;
import com.bp389.cranaz.YamlObj;

/**
 * Classe statique contenant les méthodes relatives au plugin de soif
 * 
 * @author BlackPhantom
 * 
 */
public final class ThirstFactor {

	public static final File config = new File("plugins/CranaZ/configuration/thirst.yml");

	public static void init(final JavaPlugin plugin) {
	}

	/**
	 * 
	 * @return Le pourcentage à retirer à chaque cycle, dans la config
	 */
	public static int getPercentRet() {
		return (int)Util.getFromYaml(config, "thirst.config.pourcentageRetire", 5);
	}

	/**
	 * 
	 * @return La valeur flottante à retirer de la barre d'exp
	 */
	public static strictfp float getPRF() {
		return Float.valueOf(Integer.valueOf(ThirstFactor.getPercentRet()).floatValue() / 100F).floatValue();
	}

	/**
	 * 
	 * @return Le délai entre chaque cycle, en long et en ticks
	 */
	public static long getLongDelay_ticks(){
		return Integer.valueOf(getDelay() * 20).longValue();
	}

	/**
	 * 
	 * @return Le délai entre chaque cycle
	 */
	public static int getDelay() {
		return (int)Util.getFromYaml(config, "thirst.config.delaiEnSecondes", 120);
	}

	/**
	 * Enregistre la configuration
	 */
	public static void registerDC() {
		if(!ThirstFactor.config.exists()) {
			Util.saveToYaml(config, new YamlObj("thirst.config.delaiEnSecondes", 240),
					new YamlObj("thirst.config.pourcentageRetire", 5));
		}
	}
}
