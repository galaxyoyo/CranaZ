package com.bp389.cranaz.effects;


public class WeaponRepresenter {
	private String name, horizontalDirection;
	private float verticalRecoil, horizontalRecoil;
	public WeaponRepresenter(String name, String horizontalDir, float verticalRecoil, float horizontalRecoil){
		this.name = name;
		this.horizontalDirection = horizontalDir;
		this.verticalRecoil = verticalRecoil;
		this.horizontalRecoil = horizontalRecoil;
	}
	public String getName(){
		return this.name;
	}
	public String getDefaultHorizontalDirection(){
		return this.horizontalDirection;
	}
	public float getVerticalRecoil(){
		return this.verticalRecoil;
	}
	public float getHorizontalRecoil(){
		return this.horizontalRecoil;
	}
	public float getFloatDirection(){
		return getName().equalsIgnoreCase("RANDOM") ? WeaponAim.HORIZONTAL_RANDOM : (getName().equalsIgnoreCase("LEFT") ? WeaponAim.HORIZONTAL_LEFT : WeaponAim.HORIZONTAL_RIGHT);
	}
}
