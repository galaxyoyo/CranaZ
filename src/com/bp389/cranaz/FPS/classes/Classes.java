package com.bp389.cranaz.FPS.classes;

import java.util.Arrays;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.bp389.cranaz.items.Items;


public enum Classes {
	HEALER(Material.APPLE, "M�decin", "Une classe orient�e sur le soin. Contient:",
			"- Un AK-47 et 3 chargeurs",
			"- Une grande poche de sang",
			"- Deux rouleaux de bandages",
			"- Deux antalgiques."),
			ASSAULT(Items.getCSUWeapon("AK-47", 1).getType(), "Fusilier", "Une classe polyvalente id�ale pour l'attaque. Contient:",
					"- Un AK-47 et 5 chargeurs",
					"- Un rouleau de bandages",
					"- 3 grenades � fragmentation",
					"- 3 grenades flash d�sorientant l'ennemi",
					"",
					ChatColor.RED + "Peut se d�ployer en parachute" + ChatColor.RESET),
					HEAVY(Items.getCSUWeapon("BAR", 1).getType(), "Appui lourd", "Tr�s orient� sur la puissance de feu. Contient:",
							"- Un BAR Browning et 10 chargeurs",
							"- Un rouleau de bandages",
							"- 5 grenades � fragmentation"),
							SNIPER(Items.getCSUWeapon("Moisin", 1).getType(), "Tireur d'�lite", "Efficace � longue distance. Id�al en d�fense. Contient:",
									"- Un Mosin Nagant et 10 balles",
									"- Un Smith Colt et 2 chargeurs",
									"- Un rouleau de bandages",
									"",
									ChatColor.RED + "Peut se d�ployer en parachute" + ChatColor.RESET),
									RUSHER(Material.IRON_SWORD, "Fonceur", "Tout sur l'action ! Contient:",
											"- Un AK-47 et 3 chargeurs",
											"- Une machette !",
											"- Une dose d'amph�tamines !",
											"- Une grenade Flash"),
											TACTICAL(Material.COMPASS, "Tacticien", "Polyvalent et ax� sur la tactique. Contient:",
													"- Un BAR Browning et 5 chargeurs",
													"- Un rouleau de bandages",
													"- Deux grenades flash",
													"- Un marqueur de soutien a�rien",
													"",
													ChatColor.RED + "Habilit� � appeler une frappe a�rienne" + ChatColor.RESET);
													

	private ItemStack icon;
	private String name;
	private String[] desc;
	Classes(Material icon, String name, String... desc){
		this.icon = new ItemStack(icon);
		this.name = name;
		this.desc = desc;
	}
	public Material getIcon(){
		return icon.getType();
	}
	public ItemStack getFullIcon(){
		final ItemStack is = icon.clone();
		final ItemMeta im = is.getItemMeta();
		im.setDisplayName(name);
		im.setLore(Arrays.asList(desc));
		is.setItemMeta(im);
		return is;
	}
	public String getDisplayedName(){
		return name;
	}
	public String[] getDisplayedDescription(){
		return desc;
	}
	public static ItemStack[] getAllIcons(){
		final ItemStack[] ist = new ItemStack[values().length];
		for(int i = 0;i < values().length;++i)
			ist[i] = values()[i].getFullIcon();
		return ist;
	}
}
