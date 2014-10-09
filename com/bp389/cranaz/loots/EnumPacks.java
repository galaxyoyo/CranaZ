package com.bp389.cranaz.loots;

import java.util.ArrayList;

/**
 * Contient une énumération des différents packs disponibles
 * pour les packs de coffres et loots
 * @author BlackPhantom
 *
 */
public enum EnumPacks {
	CHAMBRE(360L,
			Packs.RAW_PORK,
			Packs.APPLE,
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
	FERME(420L,
			Packs.WSEED,
			Packs.WHEAT,
			Packs.MAP,
			Packs.STHOE,
			Packs.IAXE,
			Packs.ISWORD,
			Packs.BOOTS,
			Packs.FSEYE,
			Packs.CRAFTBOOK,
			Packs.CAMO_BOOTS,
			Packs.SSWORD),
	CUISINE(420L,
			Packs.PORK,
			Packs.STEAK,
			Packs.CHICKEN,
			Packs.SOUP,
			Packs.BREAD,
			Packs.FISH,
			Packs.WMELON,
			Packs.WATER,
			Packs.FSEYE),
	SUPERETTE(600L,
			Packs.PORK,
			Packs.STEAK,
			Packs.CHICKEN,
			Packs.FISH,
			Packs.WMELON,
			Packs.WATER,
			Packs.IAXE,
			Packs.REDSTONE,
			Packs.STHOE,
			Packs.INGOT,
			Packs.COMPASS,
			Packs.MAP,
			Packs.MILK,
			Packs.PAPER,
			Packs.APPLE,
			Packs.BAG,
			Packs.CAMO_HELMET,
			Packs.CAMO_PANTS,
			Packs.CRAFTBOOK),
	MILITAIRE(900L,
			Packs.AK47_AM,
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
			Packs.GOLDEN_APPLE,
			Packs.CAMO,
			Packs.CAMO_BOOTS,
			Packs.BAG,
			Packs.ISWORD,
			Packs.BOW,
			Packs.BOOTS,
			Packs.ARTERIAL),
	HOPITAL(600L,
			Packs.GOLDEN_APPLE,
			Packs.APPLE,
			Packs.PAPER,
			Packs.AMPHET,
			Packs.BREAD,
			Packs.HOSHIRT,
			Packs.FSEYE,
			Packs.NEUROTOXIC,
			Packs.ARTERIAL,
			Packs.CAMO_PANTS),
	NULL(0L, new Packs[]{null});
	
	private ArrayList<Packs> items = new ArrayList<Packs>();
	private long delay;
	private EnumPacks(long delayInSeconds, final Packs... pack_items)
	{
		this.delay = delayInSeconds;
		for(int i = 0;i < pack_items.length;i++)
			items.add(pack_items[i]);
	}
	public long delay(){return this.delay;}
	public ArrayList<Packs> items()
	{
		return this.items;
	}
}
