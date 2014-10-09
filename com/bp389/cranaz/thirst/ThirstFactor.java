package com.bp389.cranaz.thirst;

import java.io.File;
import java.io.IOException;

import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * Classe statique contenant les méthodes relatives au plugin de soif
 * @author BlackPhantom
 *
 */
public final class ThirstFactor {
	private static JavaPlugin jp;
	public static final File config = new File("plugins/CranaZ/Thirst/config.yml");
	public static void init(JavaPlugin plugin){jp = plugin;}
	/**
	 * 
	 * @return Le pourcentage à retirer à chaque cycle, dans la config
	 */
	public static int getPercentRet(){
		FileConfiguration fc = jp.getConfig();
		try {
	        fc.load(config);
	        return fc.getInt("thirst.config.pourcentageRetire");
        } catch (IOException | InvalidConfigurationException e) {return 5;}
	}
	/**
	 * 
	 * @return La valeur flottante à retirer de la barre d'exp
	 */
	public static strictfp float getPRF(){
		return Float.valueOf(Integer.valueOf(getPercentRet()).floatValue() / 100F).floatValue();
	}
	/**
	 * 
	 * @return Le délai entre chaque cycle
	 */
	public static int getDelay(){
		FileConfiguration fc = jp.getConfig();
		try {
	        fc.load(config);
	        return fc.getInt("thirst.config.delaiEnSecondes");
        } catch (IOException | InvalidConfigurationException e) {return 120;}
	}
	/**
	 * Enregistre la configuration
	 */
	public static void registerDC(){
		if(!config.exists()){
			FileConfiguration fc = jp.getConfig();
			fc.set("thirst.config.delaiEnSecondes", 240);
			fc.set("thirst.config.pourcentageRetire", 5);
			try {
	            fc.save(config);
            } catch (IOException e) {}
		}
	}
}
