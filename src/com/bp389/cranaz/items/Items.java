package com.bp389.cranaz.items;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.ShapelessRecipe;
import org.bukkit.inventory.meta.BookMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * Classe contenant divers objets modifiés au consensus de CranaZ,
 * ainsi que de nouvelles recettes
 * @author BlackPhantom
 *
 */
public final class Items
{
	public static Random random = new Random();
	public static final Material BAG = Material.QUARTZ;
	private static JavaPlugin jp;
	public static final void init(JavaPlugin plugin){jp = plugin;}
	public static void recipes()
	{
		ArrayList<ShapedRecipe> shapes = new ArrayList<ShapedRecipe>();
		ArrayList<ShapelessRecipe> unshapes = new ArrayList<ShapelessRecipe>();
		ShapedRecipe machette = new ShapedRecipe(customISword());
		machette.shape(" F ", " F ", " B ");
		machette.setIngredient('F', Material.IRON_INGOT);
		machette.setIngredient('B', Material.STICK);
		shapes.add(machette);
		
		ShapedRecipe machette1 = new ShapedRecipe(customISword());
		machette1.shape("F  ", "F  ", "B  ");
		machette1.setIngredient('F', Material.IRON_INGOT);
		machette1.setIngredient('B', Material.STICK);
		shapes.add(machette1);
		
		ShapedRecipe machette2 = new ShapedRecipe(customISword());
		machette2.shape("  F", "  F", "  B");
		machette2.setIngredient('F', Material.IRON_INGOT);
		machette2.setIngredient('B', Material.STICK);
		shapes.add(machette2);
		
		ShapedRecipe sac = new ShapedRecipe(bagItemStack());
		sac.shape("   ", "CKC", "CCC");
		sac.setIngredient('C', Material.LEATHER);
		sac.setIngredient('K', Material.CHEST);
		shapes.add(sac);
		
		ShapelessRecipe amphet = new ShapelessRecipe(customPPie());
		amphet.addIngredient(Material.POTION);
		amphet.addIngredient(Material.SUGAR);
		amphet.addIngredient(Material.REDSTONE);
		amphet.addIngredient(Material.FERMENTED_SPIDER_EYE);
		amphet.addIngredient(Material.RED_MUSHROOM);
		unshapes.add(amphet);
		
		ShapelessRecipe neurotoxic = new ShapelessRecipe(Items.Subs.Poison.NEUROTOXIC.toItem());
		neurotoxic.addIngredient(Material.POTION);
		neurotoxic.addIngredient(Material.FERMENTED_SPIDER_EYE);
		neurotoxic.addIngredient(Material.RED_MUSHROOM);
		neurotoxic.addIngredient(Material.CARROT);
		unshapes.add(neurotoxic);
		
		ShapedRecipe blouse = new ShapedRecipe(hospitalShirt());
		blouse.shape("C C", "CLC", "CCC");
		blouse.setIngredient('C', Material.LEATHER);
		blouse.setIngredient('L', Material.WOOL);
		shapes.add(blouse);
		
		ShapedRecipe shirt = new ShapedRecipe(genTShirt(new ItemStack(Material.LEATHER_CHESTPLATE)));
		shirt.shape("C C", "CCC", "CCC");
		shirt.setIngredient('C', Material.LEATHER);
		shapes.add(shirt);
		
		ShapedRecipe pant = new ShapedRecipe(genTShirt(new ItemStack(Material.LEATHER_LEGGINGS)));
		pant.shape("CCC", "C C", "C C");
		pant.setIngredient('C', Material.LEATHER);
		shapes.add(pant);
		
		ShapedRecipe hat = new ShapedRecipe(genTShirt(new ItemStack(Material.LEATHER_HELMET)));
		hat.shape("CCC", "C C", "   ");
		hat.setIngredient('C', Material.LEATHER);
		shapes.add(hat);
		
		ShapedRecipe hat2 = new ShapedRecipe(genTShirt(new ItemStack(Material.LEATHER_HELMET)));
		hat2.shape("   ", "CCC", "C C");
		hat2.setIngredient('C', Material.LEATHER);
		shapes.add(hat2);
		
		ShapedRecipe boots = new ShapedRecipe(genTShirt(new ItemStack(Material.LEATHER_BOOTS)));
		boots.shape("C C", "C C", "   ");
		boots.setIngredient('C', Material.LEATHER);
		shapes.add(boots);
		
		ShapedRecipe boots2 = new ShapedRecipe(genTShirt(new ItemStack(Material.LEATHER_BOOTS)));
		boots2.shape("   ", "C C", "C C");
		boots2.setIngredient('C', Material.LEATHER);
		shapes.add(boots2);
		
		ShapedRecipe iAxe = new ShapedRecipe(customIAxe());
		iAxe.shape(" FF", " BF", " B ");
		iAxe.setIngredient('B', Material.STICK);
		iAxe.setIngredient('F', Material.IRON_INGOT);
		shapes.add(iAxe);
		
		ShapedRecipe bow = new ShapedRecipe(customBow());
		bow.shape("FB ", "F B", "FB ");
		bow.setIngredient('F', Material.STRING);
		bow.setIngredient('B', Material.STICK);
		shapes.add(bow);
		/*
		 * 
		 */
		for(int i = 0;i < shapes.size();i++){
			Bukkit.getServer().addRecipe(shapes.get(i));
		}
		for(int i = 0;i < unshapes.size();i++){
			Bukkit.getServer().addRecipe(unshapes.get(i));
		}
	}
	/**
	 * 
	 * @param item L'objet source
	 * @param name Le nom de l'objet
	 * @param lore Sa description
	 * @return L'objet modifie
	 */
	private static final ItemStack getLoredItem(final ItemStack item, final String name, final String... lore){
		final ItemStack is = item;
		final ItemMeta im = is.getItemMeta();
		im.setDisplayName(name);
		im.setLore(Arrays.asList(lore));
		is.setItemMeta(im);
		return is;
	}
	/**
	 * 
	 * @param m Le type
	 * @see #getLoredItem(ItemStack, String, String...)
	 */
	private static final ItemStack getLoredItem(final Material m, final String n, final String... ss){
		return getLoredItem(new ItemStack(m), n, ss);
	}
	public static ItemStack hospitalShirt(){
		ItemStack is = new ItemStack(Material.LEATHER_CHESTPLATE);
		LeatherArmorMeta im = (LeatherArmorMeta)is.getItemMeta();
		im.setDisplayName("Blouse");
		im.setColor(Color.WHITE);
		is.setItemMeta(im);
		return is;
	}
	public static ItemStack getWrittenBook(final String title, final String dotSplitLore, final String... pages){
		ItemStack is = new ItemStack(Material.WRITTEN_BOOK);
		BookMeta bm = (BookMeta)is.getItemMeta();
		bm.setPages(pages);
		bm.setTitle(title);
		bm.setLore(Arrays.asList(dotSplitLore.split(".")));
		is.setItemMeta(bm);
		return is;
	}
	public static ItemStack genTShirt(ItemStack lFrom)
	{
		ItemStack temp = lFrom;
		LeatherArmorMeta lam = (LeatherArmorMeta)temp.getItemMeta();
		Color c = Color.MAROON;
		switch(temp.getType())
		{
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
		switch(random.nextInt(17))
		{
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
	public static ItemStack customFence(){
		return getLoredItem(Material.FENCE, "Barricade", "Pour se protéger en toutes circonstances !");
	}
	public static ItemStack customSSword(){
		return getLoredItem(Material.STONE_SWORD, "Massue", "Pour zigouiller du macchabé !");
	}
	public static ItemStack bagItemStack(){
		return getLoredItem(BAG, "Sac a dos", 
				ChatColor.ITALIC + "Permet de porter des objets.", ChatColor.ITALIC + "Shift + clic droit pour l'ouvrir" + ChatColor.RESET);
	}
	public static ItemStack customIAxe(){
		return getLoredItem(Material.IRON_AXE, "Hache", "Bien aiguisee en plus !");
	}
	public static ItemStack customISword(){
		return getLoredItem(Material.IRON_SWORD, "Machette", "Fais gaffe a ta main !");
	}
	public static ItemStack customBow(){
		return getLoredItem(Material.BOW, "Arc", "Pour tuer du zombie en toute discretion !");
	}
	public static ItemStack getAmmoStack(ItemStack from){
		if(from.getType() == Material.BLAZE_POWDER){
			return getLoredItem(from, "Balles de Mosin Nagant", 
					ChatColor.ITALIC + "Balles pour fusil de sniper type Mosin", ChatColor.ITALIC + "Rechargement balle par balle" + ChatColor.RESET);
		}
		else if(from.getType() == Material.GOLD_NUGGET){
			return getLoredItem(from, "Chargeur(s) d'AK-47", "Chargeurs pour fusil d'assaut type AK-47");
		}
		else if(from.getType() == Material.FIREBALL){
			return getLoredItem(from, "Grenades de M320H", 
					ChatColor.ITALIC + "Grenades pour lance-grenades type M320H", ChatColor.ITALIC + "Une seule grenade par chambre" + ChatColor.RESET);
		}
		else if(from.getType() == Material.MAGMA_CREAM){
			return getLoredItem(from, "Chargeur(s) de BAR Browning", "Chargeurs pour fusil mitrailleur type BAR");
		}
		else if(from.getType() == Material.SLIME_BALL){
			return getLoredItem(from, "Balles de Smith", "Balles pour colt type Smith");
		}
		return from;
	}
	public static ItemStack customPaper(){
		return getLoredItem(Material.PAPER, "Bandages", "Ca peut servir...");
	}
	public static ItemStack customGApple(){
		return getLoredItem(Material.GOLDEN_APPLE, "Grande poche de sang", "On transfuse ?");
	}
	public static ItemStack customApple(){
		return getLoredItem(Material.APPLE, "Petite poche de sang", "On transfuse ?");
	}
	public static ItemStack customPPie(){
		return getLoredItem(Material.PUMPKIN_PIE, "Amphétamines", "C'est de la bonne");
	}
	public static ItemStack customCamo(){
		return getLoredItem(Material.CHAINMAIL_CHESTPLATE, "Tenue de camouflage - plastron", "Permet d'etre vu de moins loin - 15 %");
	}
	public static ItemStack customCamo_helmet(){
		return getLoredItem(Material.CHAINMAIL_HELMET, "Tenue de camouflage - casque", "Permet d'etre vu de moins loin - 5 %");
	}
	public static ItemStack customCamo_boots(){
		return getLoredItem(Material.CHAINMAIL_BOOTS, "Tenue de camouflage - bottes", "Permet d'etre vu de moins loin - 20 %");
	}
	public static ItemStack customCamo_pants(){
		return getLoredItem(Material.CHAINMAIL_LEGGINGS, "Tenue de camouflage - pantalon", "Permet d'etre vu de moins loin - 20 %");
	}
	public static ItemStack customWater(){
		return getLoredItem(Material.POTION, "Bouteille d'eau", "Pour tous les types de soif !");
	}
	public static class Subs
	{
		public static enum Drugs
		{
			AMPHETAMIN(customPPie());
			private ItemStack theIS;
			Drugs(ItemStack i){
				this.theIS = i;
			}
			public ItemStack toItem(){
				return theIS;
			}
			public boolean compareTo(ItemStack another){
				ItemMeta im = theIS.getItemMeta(), im2 = another.getItemMeta();
				return im.getDisplayName().equalsIgnoreCase(im2.getDisplayName());
			}
		}
		public static enum Poison
		{
			NEUROTOXIC(neurotoxic()),
			ARTERIAL(arterial());
			private ItemStack theIS;
			Poison(ItemStack i){
				this.theIS = i;
			}
			public ItemStack toItem(){
				return theIS;
			}
			public boolean compareTo(ItemStack another){
				ItemMeta im = theIS.getItemMeta(), im2 = another.getItemMeta();
				return im.getDisplayName().equalsIgnoreCase(im2.getDisplayName());
			}
			private static ItemStack arterial(){
				return getLoredItem(Material.PUMPKIN_PIE, "Poison arteriel", "Augmente la pression sanguine de la cible.",
						"Cause des dommages directs moyens",
						"Fait effet après 45 secondes.");
			}
			private static ItemStack neurotoxic(){
				return getLoredItem(Material.PUMPKIN_PIE, "Poison neurotoxique", "Affaiblit fortement le joueur cible.",
						"Ne cause AUCUN dommage direct.",
						"Fait effet 2 minutes après injection.");
			}
		}
	}
	public static class Diaries
	{
		public interface DiaryEnum
		{
			public ItemStack toItemStack();
			public String[] pages();
			public String title();
		}
		public enum Utils implements DiaryEnum
		{
	        RULES("Règles", "Staff de CranaZ", getRules()),
	        STAFF("Membres du staff", "Staff de CranaZ", Arrays.asList("Membres du staff de CranaZ:\n\n" +
	        		"- Arnialo -> Responsable de projet, graphiste.\n" +
	        		"- Manercraft -> Responsable de projet et architecte.\n" +
	        		"- BlackPhantom -> Développeur, testeur (c'est moi !).\n",
	        		"- TheFOXgAME -> Builder.\n" +
	    	        "- Alastar -> Chef de construction, builder.\n" +
	    	        "...")),
	        SURVIVE("Guide de survie I", "Staff de CranaZ", getSurvive1()),
	        CRAFTS("Crafter sereinement", "BlackPhantom389", Arrays.asList("Dans un monde impitoyable," +
	        		" il est important de savoir comment créer soi-même ce dont l'on a besoin." +
	        		" Je suis sûr que vous connaissez déjà beaucoup de choses, mais dans ce monde," +
	        		" beaucoup de choses sont uniques.",
	        		"Vous trouverez sur les pages suivantes" +
	        		" une liste(à peu près exhaustive)des plans de fabrication" +
	        		" de divers objets.",
	        		"Amphétamines\n" +
	        		"Leur craft est relativement complexe, mais n'est pas ordonné.\n" +
	        		"Ingrédients:\n" +
	        		"- Eau\n" +
	        		"- Sucre\n" +
	        		"- Redstone\n" +
	        		"- Champignon(petit)\n" +
	        		"- Oeil d'araignée pourri\n",
	        		"Sac à dos\n" +
	        		"Nécessite beaucoup de cuir. Craft ordonné\n" +
	        		"Plan du craft:\n" +
	        		"C0C   C = Cuir\n" +
	        		"CKC   K = Coffre\n" +
	        		"CCC   0 = Vide\n",
	        		"Poison neurotoxique:\n" +
	        		"Craft complexe. Non ordonné.\n" +
	        		"Ingrédients:\n" +
	        		"- Eau\n" +
	        		"- Oeil d'araignée pourri\n" +
	        		"- Champignon(petit)\n" +
	        		"- Carotte"));
	        @Override
            public ItemStack toItemStack() {
	            return theBook;
            }

			@Override
            public String[] pages() {
	            return (String[])theMeta.getPages().toArray();
            }

			@Override
            public String title() {
	            return theMeta.getTitle();
            }
			private ItemStack theBook;
			private BookMeta theMeta;
			Utils(String title, String author, List<String> pages){
				theBook = new ItemStack(Material.WRITTEN_BOOK);
				theMeta = (BookMeta)theBook.getItemMeta();
				theMeta.setTitle(title);
				theMeta.setAuthor(author);
				theMeta.setPages(pages);
				theBook.setItemMeta(theMeta);
			}
			private static List<String> getSurvive1(){
				FileConfiguration fc = jp.getConfig();
				try {
	                fc.load(new File("plugins/CranaZ/Divers/survie.yml"));
                } catch (IOException | InvalidConfigurationException e) {}
				return fc.getStringList("survie");
			}
			private static List<String> getRules(){
				FileConfiguration fc = jp.getConfig();
				try {
	                fc.load(new File("plugins/CranaZ/Divers/regles.yml"));
                } catch (IOException | InvalidConfigurationException e) {}
				return fc.getStringList("regles");
			}
			public static void giveUtils(Player p){
				p.getInventory().addItem(Utils.RULES.toItemStack(), Utils.STAFF.toItemStack(), Utils.SURVIVE.toItemStack());
			}
		}
		public enum Bedroom implements DiaryEnum
		{
			JORDAN_KENS("Journal de Jordan - Chapitre 1", "Jordan Kens", Arrays.asList("08/07/2014\n\n" +
					"Une nouvelle maladie semble prendre forme.\n" +
					"Elle découle de la rage, cela m'inquiète.\n" +
					"En tout cas, ils en parlent partout.\n" +
					"On verra bien.",
					"28/07/2014\n\n" +
					"La maladie semble se propager. Mon père se méfie,\n" +
					"il dit que c'est encore un coup du gouvernement.\n" +
					"Je ne sais pas quoi penser.",
					"11/09/2014\n\n" +
					"La maladie a muté et est maintenant grave,\n" +
					"et mon père, fervant adepte de l'apocalypse zombie,\n" +
					"vient de rentrer de l'armurerie. Il a acheté un Mosin\n" +
					"et des balles. Nous ésperons ne pas avoir à nous en servir.",
					"20/11/2014\n\n" +
					"Nous nous terrons maintenant dans notre maison depuis ma dernière\n" +
					"page de journal. Les zombies prennent notre maison d'assaut,\n" +
					"mais mon père avait prévu le coup. J'espère que nous aurons de quoi manger.",
					"12/02/2015\n\n" +
					"Beaucoup d'humains sont infectés.\n" +
					"Nous allons démenager sur une île où nous serons tranquilles pendant que\n" +
					"ces saloperies pourrissent.",
					"20/05/2017\n\n" +
					"Je viens d'avoir 16 ans, et je vais apprendre à tirer." +
					"J'aurai dû prendre mon arc, mais je l'ai laissé dans notre ancienne maison." +
					"Les cadavres de zombies s'empilent devant la maison. J'en ai la nausée.",
					"18/07/2020\n\n" +
					"Ils travaillent sur un antidote, j'èspère qu'ils y arriveront.",
					"22/01/2022\n\n" +
					"Ils ont presque fini leur recherche. Vivement qu'ils trouvent\n" +
					"la formule, les balles manquent.",
					"27/02/2022\n\n" +
					"Je commence à plaider l'hyposthèse du suicide collectif.\n" +
					"Les zombies se sont infiltrés dans notre complexe suite à\n" +
					"une nouvelle mutation. Ils ont tué les scientifiques et\n" +
					"l'espoir d'antidote est quasiment nul.",
					"15/08/2024\n\n" +
					"Je n'en peux plus. C'est trop dur. Les balles manquent,\n" +
					"on crève de faim, et comme je vais crever bientôt, je peux le\n" +
					"dire, j'en peux plus d'être puceau.",
					"07/04/2025\n\n" +
					"Nous ne sommes plus assez. Les balles sont si rares...\n" +
					"C'est la dernière fois que j'écris, je vais mourir bientôt.\n" +
					"Adieu."));
			private ItemStack theBook;
			private BookMeta theMeta;
			Bedroom(String title, String author, List<String> pages){
				theBook = new ItemStack(Material.WRITTEN_BOOK);
				theMeta = (BookMeta)theBook.getItemMeta();
				theMeta.setTitle(title);
				theMeta.setAuthor(author);
				theMeta.setPages(pages);
				theBook.setItemMeta(theMeta);
			}
			@Override
            public ItemStack toItemStack() {
	            return theBook;
            }

			@Override
            public String[] pages() {
	            return (String[])theMeta.getPages().toArray();
            }

			@Override
            public String title() {
	            return theMeta.getTitle();
            }
		}
	}
}
