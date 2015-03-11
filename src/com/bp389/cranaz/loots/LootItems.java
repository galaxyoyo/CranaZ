package com.bp389.cranaz.loots;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import com.bp389.cranaz.items.Items;
import com.bp389.cranaz.items.Items.Diaries;

/**
 * Contient une énumération de tous les objets disponibles dans des packs/loots,
 * avec une rareté différente
 * 
 * @author BlackPhantom
 * 
 */
public enum LootItems {
	// Le premier argument est l'objet représentatif, le deuxième le taux de
	// rareté
	RAW_PORK(new ItemStack(Material.PORK), rarity.MEDIUM), 
	WATER(Items.water(), 10), 
	SHIRT(Items.genTShirt(new ItemStack(Material.LEATHER_CHESTPLATE)), rarity.GREAT), 
	PANT(Items.genTShirt(new ItemStack(Material.LEATHER_LEGGINGS)), rarity.GREAT), 
	BOOTS(Items.genTShirt(new ItemStack(Material.LEATHER_BOOTS)), rarity.GREAT), 
	HAT(Items.genTShirt(new ItemStack(Material.LEATHER_HELMET)), rarity.GREAT), 
	WSWORD(new ItemStack(Material.WOOD_SWORD), rarity.MEDIUM), 
	MACHETTE(Items.machette(), rarity.RARE), 
	WATCH(new ItemStack(Material.WATCH), rarity.MEDIUM), 
	TORCH(new ItemStack(Material.TORCH), rarity.COMMON), 
	MAP(new ItemStack(Material.MAP), rarity.GREAT), 
	SOUP(new ItemStack(Material.MUSHROOM_SOUP), rarity.MEDIUM), 
	MOSIN_AM(Items.getAmmoStack(new ItemStack(Material.BLAZE_POWDER)), rarity.GREAT), 
	BOW(Items.bow(), 9), 
	ARROW(new ItemStack(Material.ARROW, 1), rarity.MEDIUM), 
	PORK(new ItemStack(Material.GRILLED_PORK), rarity.MEDIUM), 
	STEAK(new ItemStack(Material.COOKED_BEEF), rarity.MEDIUM), 
	CHICKEN(new ItemStack(Material.COOKED_CHICKEN), rarity.MEDIUM), 
	BREAD(new ItemStack(Material.BREAD), rarity.MEDIUM), 
	FISH(new ItemStack(Material.COOKED_FISH), rarity.MEDIUM), 
	WMELON(new ItemStack(Material.MELON), rarity.MEDIUM), 
	WSEED(new ItemStack(Material.SEEDS), rarity.MEDIUM), 
	WHEAT(new ItemStack(Material.WHEAT), rarity.MEDIUM), 
	STHOE(new ItemStack(Material.STONE_HOE), rarity.MEDIUM), 
	AXE(Items.axe(), rarity.MEDIUM), 
	REDSTONE(new ItemStack(Material.REDSTONE), rarity.MEDIUM), 
	INGOT(new ItemStack(Material.IRON_INGOT), rarity.GREAT), 
	COMPASS(new ItemStack(Material.COMPASS), rarity.GREAT), 
	MILK(new ItemStack(Material.MILK_BUCKET),rarity.MEDIUM), 
	PAPER(Items.bandages(), rarity.GREAT), 
	BIG_BLOOD_BAG(Items.big_bloodBag(), rarity.RARE), 
	BLOOD_BAG(Items.small_bloodBag(), rarity.GREAT), 
	AK47(LootRefactor.csu.generateWeapon("AK-47"), rarity.RARE), 
	BAR(LootRefactor.csu.generateWeapon("BAR"), rarity.RARE), 
	MOSIN(LootRefactor.csu.generateWeapon("Moisin"), rarity.GREAT), 
	M320(LootRefactor.csu.generateWeapon("GrenadeLauncher"), rarity.RARE), 
	AK47_AM(Items.getAmmoStack(new ItemStack(Material.GOLD_NUGGET)), rarity.GREAT), 
	M320_AM(Items.getAmmoStack(new ItemStack(Material.FIREBALL)), rarity.RARE), 
	BAR_AM(Items.getAmmoStack(new ItemStack(Material.MAGMA_CREAM)), rarity.RARE), 
	BAG(Items.bagItemStack(), rarity.RARE), 
	CAMO(Items.camo_plate(),rarity.GREAT), 
	CAMO_HELMET(Items.camo_helmet(), rarity.MEDIUM),
	CAMO_PANTS(Items.camo_pants(), rarity.RARE), 
	CAMO_BOOTS(Items.camo_boots(), rarity.RARE), 
	AMPHET(Items.amphetamines(), rarity.MEDIUM), 
	HOSHIRT(Items.hospitalShirt(), rarity.GREAT), 
	JK_DIARY(Diaries.Bedroom.JORDAN_KENS.toItemStack(), rarity.RARE), 
	FSEYE(new ItemStack(Material.FERMENTED_SPIDER_EYE), rarity.GREAT), 
	WBOOK(new ItemStack(Material.BOOK_AND_QUILL), rarity.RARE), 
	CRAFTBOOK(Items.Diaries.Utils.CRAFTS.toItemStack(), rarity.GREAT), 
	NEUROTOXIC(Items.Subs.Poison.NEUROTOXIC.toItem(), rarity.RARE), 
	ARTERIAL(Items.Subs.Poison.ARTERIAL.toItem(), rarity.RARE), 
	MASS(Items.mass(), rarity.GREAT), 
	FLARE(Items.flare(), rarity.RARE),
	ANTALGIQUES(Items.antalgiques(), rarity.GREAT);

	private int rare;
	private ItemStack item;

	private LootItems(final ItemStack item, final int rarity) {
		this.item = item;
		this.rare = rarity;
	}

	public ItemStack item() {
		return this.item;
	}

	public int rare() {
		return this.rare;
	}

	public static class rarity {

		public static final int COMMON = 5, MEDIUM = 6, GREAT = 8, RARE = 10;
	}
}
