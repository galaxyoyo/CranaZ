package com.bp389.cranaz.api;

import java.util.logging.Level;

import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import com.bp389.cranaz.effects.Bleed;
import com.bp389.cranaz.effects.WeaponAim;

public final class EffectsAPI extends CranaZAPI{
	public EffectsAPI(JavaPlugin plugin) {
	    super(plugin);
    }
	
	/**
	 * Applique un effet sur le joueur cible
	 * @param target Le joueur cible
	 * @param effect L'effet à appliquer
	 */
	public void playEffectOn(final Player target, Effect effect){
		switch(effect){
			case AMPHETAMIN:
				Bleed.amphet(target);
				break;
			case ARTERIAL_POISON:
				Bleed.arterialPoison(target);
				break;
			case ANTALGIQUES:
				Bleed.antalgiques(target);
				break;
			case BANDAGE:
				Bleed.bandages(target);
				break;
			case BLOOD_BAG_BIG:
				Bleed.blood(1, target);
				break;
			case BLOOD_BAG_SMALL:
				Bleed.blood(0, target);
				break;
			case MUSHROOM:
				Bleed.mush(target);
				break;
			case NEUROTOXIC_POISON:
				Bleed.neurotoxicPoison(target);
				break;
			default:
				console(Level.WARNING, "Impossible de jouer l'effet " + effect.toString() + ": inconnu");
		}
	}
	/**
	 * Fait un effet de recul sur la cible
	 * @param weapon_name Le nom de l'arme -> CrackShot
	 * @param target Le joueur cible
	 * @see #aim(float, float, Player)
	 */
	public void aim(final String weapon_name, final Player target){
		WeaponAim.handleAim(weapon_name, target);
	}
	/**
	 * Fait un effet de recul sur la cible
	 * @param verticalAim Le recul vertical (0-90)
	 * @param horizontalAim Le recul horizontal (0-90)
	 * @param target Le joueur cible
	 */
	public void aim(final float verticalAim, final float horizontalAim, final Player target){
		WeaponAim.handleAim(verticalAim, target, horizontalAim, WeaponAim.HORIZONTAL_RIGHT);
	}
}
