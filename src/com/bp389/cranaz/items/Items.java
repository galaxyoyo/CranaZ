package com.bp389.cranaz.items;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.ShapelessRecipe;
import org.bukkit.inventory.meta.BookMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;

import org.apache.commons.lang.Validate;

import com.bp389.cranaz.Util;
import com.bp389.cranaz.FPS.Arena;
import com.bp389.cranaz.api.EquipSlot;
import com.bp389.cranaz.ia.ZIA;

/**
 * Classe contenant divers objets modifi�s au consensus de CranaZ, ainsi que de
 * nouvelles recettes
 * 
 * @author BlackPhantom
 * 
 */
public final class Items {

	public static Random random = new Random();
	public static final Material BAG = Material.QUARTZ;
	public static final int AK47 = 0, BAR = 1, MOSIN = 2, M320 = 3, SMITH = 4;

	public static void recipes() {
		final ArrayList<ShapedRecipe> shapes = new ArrayList<ShapedRecipe>();
		final ArrayList<ShapelessRecipe> unshapes = new ArrayList<ShapelessRecipe>();
		final ShapedRecipe machette = new ShapedRecipe(Items.machette());
		machette.shape(" F ", " F ", " B ");
		machette.setIngredient('F', Material.IRON_INGOT);
		machette.setIngredient('B', Material.STICK);
		shapes.add(machette);

		final ShapedRecipe machette1 = new ShapedRecipe(Items.machette());
		machette1.shape("F  ", "F  ", "B  ");
		machette1.setIngredient('F', Material.IRON_INGOT);
		machette1.setIngredient('B', Material.STICK);
		shapes.add(machette1);

		final ShapedRecipe machette2 = new ShapedRecipe(Items.machette());
		machette2.shape("  F", "  F", "  B");
		machette2.setIngredient('F', Material.IRON_INGOT);
		machette2.setIngredient('B', Material.STICK);
		shapes.add(machette2);

		final ShapedRecipe sac = new ShapedRecipe(Items.bagItemStack());
		sac.shape("   ", "CKC", "CCC");
		sac.setIngredient('C', Material.LEATHER);
		sac.setIngredient('K', Material.CHEST);
		shapes.add(sac);

		final ShapelessRecipe amphet = new ShapelessRecipe(Items.amphetamines());
		amphet.addIngredient(Material.POTION);
		amphet.addIngredient(Material.SUGAR);
		amphet.addIngredient(Material.REDSTONE);
		amphet.addIngredient(Material.FERMENTED_SPIDER_EYE);
		amphet.addIngredient(Material.RED_MUSHROOM);
		unshapes.add(amphet);

		final ShapelessRecipe neurotoxic = new ShapelessRecipe(Items.Subs.Poison.NEUROTOXIC.toItem());
		neurotoxic.addIngredient(Material.POTION);
		neurotoxic.addIngredient(Material.FERMENTED_SPIDER_EYE);
		neurotoxic.addIngredient(Material.RED_MUSHROOM);
		neurotoxic.addIngredient(Material.CARROT);
		unshapes.add(neurotoxic);

		final ShapedRecipe blouse = new ShapedRecipe(Items.hospitalShirt());
		blouse.shape("C C", "CLC", "CCC");
		blouse.setIngredient('C', Material.LEATHER);
		blouse.setIngredient('L', Material.WOOL);
		shapes.add(blouse);

		final ShapedRecipe shirt = new ShapedRecipe(Items.genTShirt(new ItemStack(Material.LEATHER_CHESTPLATE)));
		shirt.shape("C C", "CCC", "CCC");
		shirt.setIngredient('C', Material.LEATHER);
		shapes.add(shirt);

		final ShapedRecipe pant = new ShapedRecipe(Items.genTShirt(new ItemStack(Material.LEATHER_LEGGINGS)));
		pant.shape("CCC", "C C", "C C");
		pant.setIngredient('C', Material.LEATHER);
		shapes.add(pant);

		final ShapedRecipe hat = new ShapedRecipe(Items.genTShirt(new ItemStack(Material.LEATHER_HELMET)));
		hat.shape("CCC", "C C", "   ");
		hat.setIngredient('C', Material.LEATHER);
		shapes.add(hat);

		final ShapedRecipe hat2 = new ShapedRecipe(Items.genTShirt(new ItemStack(Material.LEATHER_HELMET)));
		hat2.shape("   ", "CCC", "C C");
		hat2.setIngredient('C', Material.LEATHER);
		shapes.add(hat2);

		final ShapedRecipe boots = new ShapedRecipe(Items.genTShirt(new ItemStack(Material.LEATHER_BOOTS)));
		boots.shape("C C", "C C", "   ");
		boots.setIngredient('C', Material.LEATHER);
		shapes.add(boots);

		final ShapedRecipe boots2 = new ShapedRecipe(Items.genTShirt(new ItemStack(Material.LEATHER_BOOTS)));
		boots2.shape("   ", "C C", "C C");
		boots2.setIngredient('C', Material.LEATHER);
		shapes.add(boots2);

		final ShapedRecipe iAxe = new ShapedRecipe(Items.axe());
		iAxe.shape(" FF", " BF", " B ");
		iAxe.setIngredient('B', Material.STICK);
		iAxe.setIngredient('F', Material.IRON_INGOT);
		shapes.add(iAxe);

		final ShapedRecipe bow = new ShapedRecipe(Items.bow());
		bow.shape("FB ", "F B", "FB ");
		bow.setIngredient('F', Material.STRING);
		bow.setIngredient('B', Material.STICK);
		shapes.add(bow);
		/*
		 * 
		 */
		for(int i = 0; i < shapes.size(); i++)
			Bukkit.getServer().addRecipe(shapes.get(i));
		for(int i = 0; i < unshapes.size(); i++)
			Bukkit.getServer().addRecipe(unshapes.get(i));
	}
	
	public static final ItemStack getLoredItem(final ItemStack item, final String name, int amount, final String...lore){
		final ItemStack is = item;
		final ItemMeta im = is.getItemMeta();
		im.setDisplayName(name);
		im.setLore(Arrays.asList(lore));
		is.setItemMeta(im);
		is.setAmount(amount);
		return is;
	}
	/**
	 * 
	 * @param item
	 *            L'objet source
	 * @param name
	 *            Le nom de l'objet
	 * @param lore
	 *            Sa description
	 * @return L'objet modifie
	 */
	public static final ItemStack getLoredItem(final ItemStack item, final String name, final String... lore) {
		return getLoredItem(item, name, 1, lore);
	}

	/**
	 * 
	 * @param m
	 *            Le type
	 * @see #getLoredItem(ItemStack, String, String...)
	 */
	public static final ItemStack getLoredItem(final Material m, final String n, final String... ss) {
		return getLoredItem(m, n, 1, ss);
	}
	/**
	 * 
	 * @param m Le type
	 * @param name Le nom
	 * @param amount Le nombre
	 * @param lore La description
	 * @return Un objet modifi�
	 * @see #getLoredItem(ItemStack, String, int, String...)
	 */
	public static final ItemStack getLoredItem(final Material m, final String name, int amount, final String... lore){
		return getLoredItem(new ItemStack(m), name, amount, lore);
	}

	public static ItemStack hospitalShirt() {
		final ItemStack is = new ItemStack(Material.LEATHER_CHESTPLATE);
		final LeatherArmorMeta im = (LeatherArmorMeta) is.getItemMeta();
		im.setDisplayName("Blouse");
		im.setColor(Color.WHITE);
		is.setItemMeta(im);
		return is;
	}
	
	public static ItemStack setAmount(ItemStack from, int i){
		ItemStack is = from.clone();
		is.setAmount(i);
		return is;
	}
	public static ItemStack getWrittenBook(final String title, final String dotSplitLore, final String... pages) {
		final ItemStack is = new ItemStack(Material.WRITTEN_BOOK);
		final BookMeta bm = (BookMeta) is.getItemMeta();
		bm.setPages(pages);
		bm.setTitle(title);
		bm.setLore(Arrays.asList(dotSplitLore.split(".")));
		is.setItemMeta(bm);
		return is;
	}
	
	public static ItemStack getAmmoStack(int type, int amount){
		ItemStack is = getAmmoStack(new ItemStack(Material.GOLD_NUGGET));
		switch(type){
			case AK47:
				is = getAmmoStack(new ItemStack(Material.GOLD_NUGGET));
				break;
			case BAR:
				is = getAmmoStack(new ItemStack(Material.MAGMA_CREAM));
				break;
			case MOSIN:
				is = getAmmoStack(new ItemStack(Material.BLAZE_POWDER));
				break;
			case M320:
				is = getAmmoStack(new ItemStack(Material.FIREBALL));
				break;
			case SMITH:
				is = getAmmoStack(new ItemStack(Material.SLIME_BALL));
		}
		is.setAmount(amount);
		return is;
	}
	public static ItemStack getAmmoStack(final ItemStack from) {
    	if(from.getType() == Material.BLAZE_POWDER)
    		return Items.getLoredItem(from, "Balles de Mosin Nagant", ChatColor.ITALIC + "Balles pour fusil de sniper type Mosin", ChatColor.ITALIC
    		        + "Rechargement balle par balle" + ChatColor.RESET);
    	else if(from.getType() == Material.GOLD_NUGGET)
    		return Items.getLoredItem(from, "Chargeur(s) d'AK-47", "Chargeurs pour fusil d'assaut type AK-47");
    	else if(from.getType() == Material.FIREBALL)
    		return Items.getLoredItem(from, "Grenades de M320H", ChatColor.ITALIC + "Grenades pour lance-grenades type M320H", ChatColor.ITALIC
    		        + "Une seule grenade par chambre" + ChatColor.RESET);
    	else if(from.getType() == Material.MAGMA_CREAM)
    		return Items.getLoredItem(from, "Chargeur(s) de BAR Browning", "Chargeurs pour fusil mitrailleur type BAR");
    	else if(from.getType() == Material.SLIME_BALL)
    		return Items.getLoredItem(from, "Balles de Smith", "Balles pour colt type Smith");
    	return from;
    }

	public static ItemStack genTShirt(final ItemStack lFrom) {
		final ItemStack temp = lFrom;
		final LeatherArmorMeta lam = (LeatherArmorMeta) temp.getItemMeta();
		Color c = Color.MAROON;
		switch(temp.getType()) {
			case LEATHER_CHESTPLATE:
				lam.setDisplayName("T-Shirt");
				break;
			case LEATHER_LEGGINGS:
				lam.setDisplayName("Pantalon");
				break;
			case LEATHER_BOOTS:
				lam.setDisplayName("Bottes");
				break;
			case LEATHER_HELMET:
				lam.setDisplayName("Casquette");
				break;
		}
		switch(Items.random.nextInt(17)) {
			case 0:
				c = Color.AQUA;
				break;
			case 1:
				c = Color.BLACK;
				break;
			case 2:
				c = Color.BLUE;
				break;
			case 3:
				c = Color.FUCHSIA;
				break;
			case 4:
				c = Color.GRAY;
				break;
			case 5:
				c = Color.GREEN;
				break;
			case 6:
				c = Color.LIME;
				break;
			case 7:
				c = Color.MAROON;
				break;
			case 8:
				c = Color.NAVY;
				break;
			case 9:
				c = Color.OLIVE;
				break;
			case 10:
				c = Color.ORANGE;
				break;
			case 11:
				c = Color.PURPLE;
				break;
			case 12:
				c = Color.RED;
				break;
			case 13:
				c = Color.SILVER;
				break;
			case 14:
				c = Color.TEAL;
				break;
			case 15:
				c = Color.WHITE;
				break;
			case 16:
				c = Color.YELLOW;
				break;
		}
		lam.setColor(c);
		temp.setItemMeta(lam);
		return temp;
	}

	public static ItemStack customFence() {
		return Items.getLoredItem(Material.FENCE, "Barricade", "Pour se prot�ger en toutes circonstances !");
	}

	public static ItemStack flare() {
		return Items.getLoredItem(Material.REDSTONE_TORCH_ON, "Fus�e lumineuse", "Fus�e produisant de la fum�e � l'impact", "Permet d'attirer l'attention");
	}

	public static ItemStack mass() {
		return Items.getLoredItem(Material.STONE_SWORD, "Massue", "Pour zigouiller du macchab� !");
	}

	public static ItemStack bagItemStack() {
		return Items.getLoredItem(Items.BAG, "Sac a dos", ChatColor.ITALIC + "Permet de porter des objets.", ChatColor.ITALIC
		        + "Clic droit dans l'inventaire pour l'ouvrir" + ChatColor.RESET);
	}

	public static ItemStack axe() {
		return Items.getLoredItem(Material.IRON_AXE, "Hache", "Bien aiguisee en plus !");
	}

	public static ItemStack machette() {
		return Items.getLoredItem(Material.IRON_SWORD, "Machette", "Fais gaffe a ta main !");
	}

	public static ItemStack bow() {
		return Items.getLoredItem(Material.BOW, "Arc", "Pour tuer du zombie en toute discretion !");
	}

	public static ItemStack bandages() {
		return Items.getLoredItem(Material.PAPER, "Bandages", "Ca peut servir...");
	}

	public static ItemStack big_bloodBag() {
		return Items.getLoredItem(Material.GOLDEN_APPLE, "Grande poche de sang", "On transfuse ?");
	}

	public static ItemStack small_bloodBag() {
		return Items.getLoredItem(Material.APPLE, "Petite poche de sang", "On transfuse ?");
	}

	public static ItemStack amphetamines() {
		return Items.getLoredItem(Material.PUMPKIN_PIE, "Amph�tamines", "C'est de la bonne");
	}
	
	public static ItemStack antalgiques(){
		return Items.getLoredItem(Material.FERMENTED_SPIDER_EYE, "Antalgiques", "Pour soigner les bobos");
	}

	public static ItemStack camo_plate() {
		return Items.getLoredItem(Material.CHAINMAIL_CHESTPLATE, "Tenue de camouflage - plastron", "Permet d'etre vu de moins loin - 20 %");
	}

	public static ItemStack camo_helmet() {
		return Items.getLoredItem(Material.CHAINMAIL_HELMET, "Tenue de camouflage - casque", "Permet d'etre vu de moins loin - 10 %");
	}

	public static ItemStack camo_boots() {
		return Items.getLoredItem(Material.CHAINMAIL_BOOTS, "Tenue de camouflage - bottes", "Permet d'etre vu de moins loin - 25 %");
	}

	public static ItemStack camo_pants() {
		return Items.getLoredItem(Material.CHAINMAIL_LEGGINGS, "Tenue de camouflage - pantalon", "Permet d'etre vu de moins loin - 25 %");
	}

	public static ItemStack water() {
		return Items.getLoredItem(Material.POTION, "Bouteille d'eau", "Pour tous les types de soif !");
	}
	
	public static ItemStack teamTShirt(int team, EquipSlot slot) {
		return getColoredShirt(slot, team == Arena.A ? Color.RED : Color.BLUE);
	}
	
	public static ItemStack getCSUWeapon(String title, int amount){
		final ItemStack is = ZIA.csu.generateWeapon(title);
		if(amount == 1)
			return is;
		is.setAmount(amount);
		return is;
	}
	public static ItemStack getColoredShirt(EquipSlot slot, Color color){
		if(slot == EquipSlot.HAND)
			return null;
		ItemStack is = null;
		switch(slot){
			case BOOTS:
				is = new ItemStack(Material.LEATHER_BOOTS);
				break;
			case PANTS:
				is = new ItemStack(Material.LEATHER_LEGGINGS);
				break;
			case PLATE:
				is = new ItemStack(Material.LEATHER_CHESTPLATE);
				break;
			case HELMET:
				is = new ItemStack(Material.LEATHER_HELMET);
		}
		Validate.notNull(is);
		LeatherArmorMeta lam = (LeatherArmorMeta)is.getItemMeta();
		lam.setColor(color);
		is.setItemMeta(lam);
		return is;
	}

	public static class Subs {

		public static enum Drugs {
			AMPHETAMIN(Items.amphetamines()),
			ANTALGIQUES(Items.antalgiques());

			private final ItemStack theIS;

			Drugs(final ItemStack i) {
				this.theIS = i;
			}

			public ItemStack toItem() {
				return this.theIS;
			}

			public boolean compareTo(final ItemStack another) {
				final ItemMeta im = this.theIS.getItemMeta(), im2 = another.getItemMeta();
				return im.getDisplayName().equalsIgnoreCase(im2.getDisplayName());
			}
		}

		public static enum Poison {
			NEUROTOXIC(Poison.neurotoxic()), 
			ARTERIAL(Poison.arterial());

			private final ItemStack theIS;

			Poison(final ItemStack i) {
				this.theIS = i;
			}

			public ItemStack toItem() {
				return this.theIS;
			}

			public boolean compareTo(final ItemStack another) {
				final ItemMeta im = this.theIS.getItemMeta(), im2 = another.getItemMeta();
				return im.getDisplayName().equalsIgnoreCase(im2.getDisplayName());
			}

			private static ItemStack arterial() {
				return Items.getLoredItem(Material.PUMPKIN_PIE, "Poison arteriel", "Augmente la pression sanguine de la cible.",
				        "Cause des dommages directs moyens", "Fait effet apr�s 45 secondes.");
			}

			private static ItemStack neurotoxic() {
				return Items.getLoredItem(Material.PUMPKIN_PIE, "Poison neurotoxique", "Affaiblit fortement le joueur cible.",
				        "Ne cause AUCUN dommage direct.", "Fait effet 2 minutes apr�s injection.");
			}
		}
	}

	public static class Diaries {

		public interface DiaryEnum {

			public ItemStack toItemStack();

			public String[] pages();

			public String title();
		}

		public enum Utils implements DiaryEnum {
			RULES("R�gles", "Staff de CranaZ", Utils.getRules()), STAFF("Membres du staff", "Staff de CranaZ", Arrays.asList("Membres du staff de CranaZ:\n\n"
			        + "- Arnialo -> Responsable de projet, graphiste.\n" + "- Manercraft -> Responsable de projet et architecte.\n"
			        + "- BlackPhantom -> D�veloppeur, testeur (c'est moi !).\n", "- TheFOXgAME -> Builder.\n" + "- Alastar -> Chef de construction, builder.\n"
			        + "...")), SURVIVE("Guide de survie I", "Staff de CranaZ", Utils.getSurvive1()), CRAFTS("Crafter sereinement", "BlackPhantom389", Arrays
			        .asList("Dans un monde impitoyable," + " il est important de savoir comment cr�er soi-m�me ce dont l'on a besoin."
			                + " Je suis s�r que vous connaissez d�j� beaucoup de choses, mais dans ce monde," + " beaucoup de choses sont uniques.",
			                "Vous trouverez sur les pages suivantes" + " une liste(� peu pr�s exhaustive)des plans de fabrication" + " de divers objets.",
			                "Amph�tamines\n" + "Leur craft est relativement complexe, mais n'est pas ordonn�.\n" + "Ingr�dients:\n" + "- Eau\n" + "- Sucre\n"
			                        + "- Redstone\n" + "- Champignon(petit)\n" + "- Oeil d'araign�e pourri\n", "Sac � dos\n"
			                        + "N�cessite beaucoup de cuir. Craft ordonn�\n" + "Plan du craft:\n" + "C0C   C = Cuir\n" + "CKC   K = Coffre\n"
			                        + "CCC   0 = Vide\n", "Poison neurotoxique:\n" + "Craft complexe. Non ordonn�.\n" + "Ingr�dients:\n" + "- Eau\n"
			                        + "- Oeil d'araign�e pourri\n" + "- Champignon(petit)\n" + "- Carotte"));

			@Override
			public ItemStack toItemStack() {
				return this.theBook;
			}

			@Override
			public String[] pages() {
				return (String[]) this.theMeta.getPages().toArray();
			}

			@Override
			public String title() {
				return this.theMeta.getTitle();
			}

			private final ItemStack theBook;
			private final BookMeta theMeta;

			Utils(final String title, final String author, final List<String> pages) {
				this.theBook = new ItemStack(Material.WRITTEN_BOOK);
				this.theMeta = (BookMeta) this.theBook.getItemMeta();
				this.theMeta.setTitle(title);
				this.theMeta.setAuthor(author);
				this.theMeta.setPages(pages);
				this.theBook.setItemMeta(this.theMeta);
			}

			@SuppressWarnings("unchecked")
            private static List<String> getSurvive1() {
				return (List<String>)Util.getFromYaml("plugins/CranaZ/divers/survie.yml", "survie");
			}

			@SuppressWarnings("unchecked")
            private static List<String> getRules() {
				return (List<String>)Util.getFromYaml("plugins/CranaZ/divers/regles.yml", "regles");
			}

			public static void giveUtils(final Player p) {
				p.getInventory().addItem(Utils.RULES.toItemStack(), Utils.STAFF.toItemStack(), Utils.SURVIVE.toItemStack());
			}
		}

		public enum Bedroom implements DiaryEnum {
			JORDAN_KENS("Journal de Jordan - Chapitre 1", "Jordan Kens", Arrays.asList("08/07/2014\n\n" + "Une nouvelle maladie semble prendre forme.\n"
			        + "Elle d�coule de la rage, cela m'inqui�te.\n" + "En tout cas, ils en parlent partout.\n" + "On verra bien.", "28/07/2014\n\n"
			        + "La maladie semble se propager. Mon p�re se m�fie,\n" + "il dit que c'est encore un coup du gouvernement.\n"
			        + "Je ne sais pas quoi penser.", "11/09/2014\n\n" + "La maladie a mut� et est maintenant grave,\n"
			        + "et mon p�re, fervant adepte de l'apocalypse zombie,\n" + "vient de rentrer de l'armurerie. Il a achet� un Mosin\n"
			        + "et des balles. Nous �sperons ne pas avoir � nous en servir.", "20/11/2014\n\n"
			        + "Nous nous terrons maintenant dans notre maison depuis ma derni�re\n" + "page de journal. Les zombies prennent notre maison d'assaut,\n"
			        + "mais mon p�re avait pr�vu le coup. J'esp�re que nous aurons de quoi manger.", "12/02/2015\n\n" + "Beaucoup d'humains sont infect�s.\n"
			        + "Nous allons d�menager sur une �le o� nous serons tranquilles pendant que\n" + "ces saloperies pourrissent.", "20/05/2017\n\n"
			        + "Je viens d'avoir 16 ans, et je vais apprendre � tirer." + "J'aurai d� prendre mon arc, mais je l'ai laiss� dans notre ancienne maison."
			        + "Les cadavres de zombies s'empilent devant la maison. J'en ai la naus�e.", "18/07/2020\n\n"
			        + "Ils travaillent sur un antidote, j'�sp�re qu'ils y arriveront.", "22/01/2022\n\n"
			        + "Ils ont presque fini leur recherche. Vivement qu'ils trouvent\n" + "la formule, les balles manquent.", "27/02/2022\n\n"
			        + "Je commence � plaider l'hyposth�se du suicide collectif.\n" + "Les zombies se sont infiltr�s dans notre complexe suite �\n"
			        + "une nouvelle mutation. Ils ont tu� les scientifiques et\n" + "l'espoir d'antidote est quasiment nul.", "15/08/2024\n\n"
			        + "Je n'en peux plus. C'est trop dur. Les balles manquent,\n" + "on cr�ve de faim, et comme je vais crever bient�t, je peux le\n"
			        + "dire, j'en peux plus d'�tre puceau.", "07/04/2025\n\n" + "Nous ne sommes plus assez. Les balles sont si rares...\n"
			        + "C'est la derni�re fois que j'�cris, je vais mourir bient�t.\n" + "Adieu."));

			private final ItemStack theBook;
			private final BookMeta theMeta;

			Bedroom(final String title, final String author, final List<String> pages) {
				this.theBook = new ItemStack(Material.WRITTEN_BOOK);
				this.theMeta = (BookMeta) this.theBook.getItemMeta();
				this.theMeta.setTitle(title);
				this.theMeta.setAuthor(author);
				this.theMeta.setPages(pages);
				this.theBook.setItemMeta(this.theMeta);
			}

			@Override
			public ItemStack toItemStack() {
				return this.theBook;
			}

			@Override
			public String[] pages() {
				return (String[]) this.theMeta.getPages().toArray();
			}

			@Override
			public String title() {
				return this.theMeta.getTitle();
			}
		}
	}
}
