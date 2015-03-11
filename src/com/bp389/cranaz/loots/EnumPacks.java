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
	CHAMBRE(360L, LootItems.RAW_PORK, 
			LootItems.ANTALGIQUES, 
			LootItems.WATER, 
			LootItems.SHIRT, 
			LootItems.WSWORD, 
			LootItems.WATCH, 
			LootItems.TORCH, 
			LootItems.MAP, 
			LootItems.SOUP, 
			LootItems.MOSIN_AM,
			LootItems.BOW, 
			LootItems.ARROW, 
			LootItems.BAG, 
			LootItems.PANT, 
			LootItems.HAT, 
			LootItems.JK_DIARY, 
			LootItems.WBOOK, 
			LootItems.CRAFTBOOK), 
	FERME(420L, LootItems.WSEED, 
			LootItems.WHEAT, 
			LootItems.MAP, 
			LootItems.STHOE, 
			LootItems.AXE, 
			LootItems.MACHETTE, 
			LootItems.BOOTS, 
			LootItems.FSEYE, 
			LootItems.CRAFTBOOK, 
			LootItems.CAMO_BOOTS, 
			LootItems.MASS), 
	CUISINE(420L, LootItems.PORK, 
			LootItems.STEAK, 
			LootItems.CHICKEN, 
			LootItems.SOUP, 
			LootItems.BREAD, 
			LootItems.FISH, 
			LootItems.WMELON,
			LootItems.WATER, 
			LootItems.FSEYE), 
	SUPERETTE(600L, LootItems.PORK, 
			LootItems.STEAK, 
			LootItems.CHICKEN, 
			LootItems.FISH, 
			LootItems.WMELON, 
			LootItems.WATER, 
			LootItems.AXE, 
			LootItems.REDSTONE, 
			LootItems.STHOE, 
			LootItems.INGOT, 
			LootItems.COMPASS, 
			LootItems.MAP,
			LootItems.MILK, 
			LootItems.PAPER, 
			LootItems.BLOOD_BAG, 
			LootItems.BAG, 
			LootItems.CAMO_HELMET, 
			LootItems.CAMO_PANTS, 
			LootItems.CRAFTBOOK,
			LootItems.ANTALGIQUES), 
	MILITAIRE(900L, LootItems.AK47_AM, 
			LootItems.AK47, 
			LootItems.MOSIN, 
			LootItems.M320, 
			LootItems.BAR, 
			LootItems.M320_AM, 
			LootItems.MOSIN_AM, 
			LootItems.BAR_AM, 
			LootItems.MAP, 
			LootItems.COMPASS, 
			LootItems.PAPER, 
			LootItems.BIG_BLOOD_BAG, 
			LootItems.CAMO, 
			LootItems.CAMO_BOOTS, 
			LootItems.BAG, 
			LootItems.MACHETTE, 
			LootItems.BOW, 
			LootItems.BOOTS, 
			LootItems.ARTERIAL), 
	HOPITAL(600L, LootItems.BIG_BLOOD_BAG, 
			LootItems.BLOOD_BAG, 
			LootItems.PAPER, 
			LootItems.AMPHET,
			LootItems.BREAD,
			LootItems.HOSHIRT, 
			LootItems.FSEYE, 
			LootItems.NEUROTOXIC,
			LootItems.ARTERIAL, 
			LootItems.CAMO_PANTS),
	NULL(0L, new LootItems[] { null });

	private final ArrayList<LootItems> items = new ArrayList<LootItems>();
	private long delay;

	private EnumPacks(final long delayInSeconds, final LootItems... pack_items) {
		this.delay = delayInSeconds;
		for(final LootItems pack_item : pack_items)
			this.items.add(pack_item);
	}

	public long delay() {
		return this.delay;
	}

	public ArrayList<LootItems> items() {
		return this.items;
	}
}
