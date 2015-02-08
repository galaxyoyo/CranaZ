package com.bp389.cranaz.FPS.classes;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import com.bp389.cranaz.Util;
import com.bp389.cranaz.Util.MathUtil;
import com.bp389.cranaz.FPS.Arena;
import com.bp389.cranaz.api.EquipSlot;


public abstract class GameClass {
	protected String name;
	protected ArrayList<ItemStack> contents = new ArrayList<ItemStack>();
	protected ArrayList<Trait> traits = new ArrayList<Trait>();
	protected ItemStack[] equipments = new ItemStack[4];
	protected int team;
	protected static HashMap<Player, GameClass> classes = new HashMap<Player, GameClass>();
	protected GameClass(String name, Trait[] traits, int team, ItemStack... contents){
		this.name = name;
		this.team = team;
		this.contents.addAll(Arrays.asList(contents));
		this.contents.add(new ItemStack(Material.COMPASS));
		this.traits.addAll(Arrays.asList(traits));
	}
	public List<Trait> getTraits(){
		return traits;
	}
	public boolean hasTrait(Trait trait){
		return traits.contains(trait);
	}
	public void addTrait(Trait trait){
		traits.add(trait);
	}
	public String getClassName(){
		return name;
	}
	public ItemStack[] getEquipments(){
		return equipments;
	}
	public Inventory getInventory(){
		return Util.genSimpleInventory(null, InventoryType.PLAYER, (ItemStack[])contents.toArray());
	}
	public ItemStack[] getContents(){
		return Util.fromList(contents);
	}
	
	protected void setEquip(EquipSlot slot, ItemStack is){
		equipments[slot.getID()] = is;
	}
	protected void setOrderedEquip(ItemStack...itemStacks){
		if(itemStacks.length < 4)
			return;
		equipments[0] = itemStacks[0];
		equipments[1] = itemStacks[1];
		equipments[2] = itemStacks[2];
		equipments[3] = itemStacks[3];
	}
	
	public static GameClass getGameClass(Classes clazz, int team){
		switch(clazz){
			case HEALER:
				return new Healer(team);
			case ASSAULT:
				return new Assault(team);
			case HEAVY:
				return new Heavy(team);
			case RUSHER:
				return new Rusher(team);
			case SNIPER:
				return new Sniper(team);
			case TACTICAL:
				return new Tactical(team);
		}
		return null;
	}
	public static void setPlayerClass(Player p, Classes clazz){
		if(classes.containsKey(p))
			classes.remove(clazz);
		final Arena a = Arena.hasJoined.get(p);
		classes.put(p, getGameClass(clazz, a.getTeam(p)));
	}
	public static GameClass getPlayerClass(Player p){
		return classes.get(p);
	}
	@SuppressWarnings("deprecation")
    public static void handlePlayerClass(Player p){
		if(!classes.containsKey(p))
			setPlayerClass(p, Classes.ASSAULT);
		p.getInventory().clear();
		p.getInventory().setContents(getPlayerClass(p).getContents());
		p.getInventory().setArmorContents(getPlayerClass(p).getEquipments());
		p.updateInventory();
	}
	public static Inventory generateMenu(){
		Inventory i = Bukkit.createInventory(null, Double.valueOf(MathUtil.supMultiplier(Classes.values().length, 9D)).intValue(), "CHOISIR UNE CLASSE");
		i.setContents(Classes.getAllIcons());
		return i;
	}
}
