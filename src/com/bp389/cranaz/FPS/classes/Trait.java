package com.bp389.cranaz.FPS.classes;


public enum Trait {
	SKYDIVER_DEPLOY("deployment.skydiver"),
	AIRSTRIKE_CALLER("gameplay.airstrike_call"),
	NO_TRAIT("global.not");
	private String name;
	Trait(String val){
		name = val;
	}
	public String getTraitValue(){
		return name;
	}
}
