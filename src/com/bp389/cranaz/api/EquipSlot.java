package com.bp389.cranaz.api;


public enum EquipSlot {
	HAND(0),
	BOOTS(1),
	PANTS(2),
	PLATE(3),
	HELMET(4);
	private int id;
	EquipSlot(int id){
		this.id = id;
	}
	public int getID(){
		return this.id;
	}
}
