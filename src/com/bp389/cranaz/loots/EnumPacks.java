package com.bp389.cranaz.loots;

import java.util.ArrayList;

/**
 * Contient une énumération des différents packs disponibles pour les packs de
 * coffres et loots
 * 
 * @author BlackPhantom
 * 
 */
public enum EnumPacks {
	CHAMBRE(360L, Packs.RAW_PORK, 
			Packs.ANTALGIQUES, 
			Packs.WATER, 
			Packs.SHIRT, 
			Packs.WSWORD, 
			Packs.WATCH, 
			Packs.TORCH, 
			Packs.MAP, 
			Packs.SOUP, 
			Packs.MOSIN_AM,
			Packs.BOW, 
			Packs.ARROW, 
			Packs.BAG, 
			Packs.PANT, 
			Packs.HAT, 
			Packs.JK_DIARY, 
			Packs.WBOOK, 
			Packs.CRAFTBOOK), 
	FERME(420L, Packs.WSEED, 
			Packs.WHEAT, 
			Packs.MAP, 
			Packs.STHOE, 
			Packs.AXE, 
			Packs.MACHETTE, 
			Packs.BOOTS, 
			Packs.FSEYE, 
			Packs.CRAFTBOOK, 
			Packs.CAMO_BOOTS, 
			Packs.MASS), 
	CUISINE(420L, Packs.PORK, 
			Packs.STEAK, 
			Packs.CHICKEN, 
			Packs.SOUP, 
			Packs.BREAD, 
			Packs.FISH, 
			Packs.WMELON,
			Packs.WATER, 
			Packs.FSEYE), 
	SUPERETTE(600L, Packs.PORK, 
			Packs.STEAK, 
			Packs.CHICKEN, 
			Packs.FISH, 
			Packs.WMELON, 
			Packs.WATER, 
			Packs.AXE, 
			Packs.REDSTONE, 
			Packs.STHOE, 
			Packs.INGOT, 
			Packs.COMPASS, 
			Packs.MAP,
			Packs.MILK, 
			Packs.PAPER, 
			Packs.BLOOD_BAG, 
			Packs.BAG, 
			Packs.CAMO_HELMET, 
			Packs.CAMO_PANTS, 
			Packs.CRAFTBOOK,
			Packs.ANTALGIQUES), 
	MILITAIRE(900L, Packs.AK47_AM, 
			Packs.AK47, 
			Packs.MOSIN, 
			Packs.M320, 
			Packs.BAR, 
			Packs.M320_AM, 
			Packs.MOSIN_AM, 
			Packs.BAR_AM, 
			Packs.MAP, 
			Packs.COMPASS, 
			Packs.PAPER, 
			Packs.BIG_BLOOD_BAG, 
			Packs.CAMO, 
			Packs.CAMO_BOOTS, 
			Packs.BAG, 
			Packs.MACHETTE, 
			Packs.BOW, 
			Packs.BOOTS, 
			Packs.ARTERIAL), 
	HOPITAL(600L, Packs.BIG_BLOOD_BAG, 
			Packs.BLOOD_BAG, 
			Packs.PAPER, 
			Packs.AMPHET,
			Packs.BREAD,
			Packs.HOSHIRT, 
			Packs.FSEYE, 
			Packs.NEUROTOXIC,
			Packs.ARTERIAL, 
			Packs.CAMO_PANTS),
	NULL(0L, new Packs[] { null });

	private final ArrayList<Packs> items = new ArrayList<Packs>();
	private long delay;

	private EnumPacks(final long delayInSeconds, final Packs... pack_items) {
		this.delay = delayInSeconds;
		for(final Packs pack_item : pack_items)
			this.items.add(pack_item);
	}

	public long delay() {
		return this.delay;
	}

	public ArrayList<Packs> items() {
		return this.items;
	}
}
