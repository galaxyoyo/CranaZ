package com.bp389.cranaz.api;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import com.bp389.cranaz.loots.EnumPacks;
import com.bp389.cranaz.loots.LootRefactor;
import com.bp389.cranaz.loots.LootableArmorStand;
import com.bp389.cranaz.loots.Packs;


public final class LootsAPI extends CranaZAPI {
	private LootRefactor lrf = new LootRefactor();
	public LootsAPI(JavaPlugin plugin) {
	    super(plugin);
    }
	/**
	 * Ajoute un objet modifi� � l'inventaire du joueur
	 * @param player Le joueur cible
	 * @param item L'objet
	 * @param count Le nombre
	 */
	public void addItemTo(final Player player, final Packs item, int count){
		final ItemStack is = item.item();
		is.setAmount(count);
		player.getInventory().addItem(is);
	}
	/**
	 * 
	 * @param thePack Le pack
	 * @return Un tableau d'objets contenant al�atoirement les objets d'un pack. Le degre de rarete est different selon chaque objet.
	 */
	public ItemStack[] getRandomPackContents(EnumPacks thePack){
		return lrf.randomToTab(thePack, 27);
	}
	/**
	 * 
	 * @param pack Le pack
	 * @return Un objet al�atoire du pack en tenant compte de la raret� relative
	 * @see #randomItem(EnumPacks)
	 */
	public ItemStack relativeRandomItem(EnumPacks pack){
		return lrf.randomIcon(pack);
	}
	/**
	 * 
	 * @param pack Le pack
	 * @return Un objet al�atoire du pack sans raret� relative (=m�me probabilit� pour chaque objet)
	 * @see #relativeRandomItem(EnumPacks)
	 */
	public ItemStack randomItem(EnumPacks pack){
		return pack.items().get(LootRefactor.random.nextInt(pack.items().size())).item();
	}
	/**
	 * Fait spawner un loot sur la position indiqu�e qui ne sera pas enregsitr� (pour la d�co ;) ?)
	 * @param l La position du loot
	 * @param item L'objet du loot
	 * @see #relativeRandomItem(EnumPacks)
	 * @see #randomItem(EnumPacks)
	 */
	public void spawnUnregistredLoot(Location l, ItemStack item){
		new LootableArmorStand(LootRefactor.mainServer, LootableArmorStand.genLootable(l), item).spawnThis();
	}
}
