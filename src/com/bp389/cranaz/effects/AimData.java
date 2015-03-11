package com.bp389.cranaz.effects;

import java.io.File;
import java.util.HashMap;

import com.bp389.PluginMethods;
import com.bp389.cranaz.Util;
import com.bp389.cranaz.YamlObj;


public final class AimData {
	public static final File recoil_config = new File("plugins/CranaZ/configuration/recoil.yml");
	public static final WeaponRepresenter AK47 = new WeaponRepresenter("AK-47", "RIGHT", 3F, 1F),
			MOSIN = new WeaponRepresenter("Moisin", "RIGHT", 25F, 3F),
			BAR = new WeaponRepresenter("BAR", "RIGHT", 4F, 1.5F),
			SMITH = new WeaponRepresenter("Smith", "LEFT", 5F, 2F),
			AK74U = new WeaponRepresenter("AK74u", "LEFT", 2F, 0.5F),
			PP70B = new WeaponRepresenter("PP70b", "RIGHT", 3F, 1F),
			P90 = new WeaponRepresenter("P90", "RIGHT", 2.5F, 0.5F),
			M320H = new WeaponRepresenter("GrenadeLauncher", "LEFT", 50F, 5F);
	public static HashMap<String, WeaponRepresenter> data = new HashMap<String, WeaponRepresenter>();
	private WeaponRepresenter wr;
	private AimData(final WeaponRepresenter wr){
		this.wr = wr;
	}
	
	public static void putWeaponData(WeaponRepresenter wr){
		if(data.containsKey(wr.getName()))
			return;
		final float[] ft = getHorizontalRecoil(wr.getName());
		final float f = getVerticalRecoil(wr.getName());
		data.put(wr.getName(), new WeaponRepresenter(wr.getName(), ft[1] == WeaponAim.HORIZONTAL_RANDOM ? "RANDOM" : (ft[1] == WeaponAim.HORIZONTAL_LEFT ? "LEFT" : "RIGHT"), 
				f, ft[0]));
	}
	public static void putWeaponData(WeaponRepresenter... weaps){
		for(WeaponRepresenter twr : weaps)
			putWeaponData(twr);
	}
	public static WeaponRepresenter getTrueWeapon(final String name){
		return data.containsKey(name) ? data.get(name) : null;
	}
	public WeaponRepresenter getWeaponRepresenter(){
		return this.wr;
	}
	public float getVerticalRecoil(){
		return this.wr.getVerticalRecoil();
	}
	public float getHorizontalRecoil(){
		return this.wr.getHorizontalRecoil();
	}
	public String getWeaponName(){
		return this.wr.getName();
	}
	public String getDefaultHorizontalDirectio(){
		return this.wr.getDefaultHorizontalDirection();
	}
	
	
	
	public static void writeWeapon(String weap, float vR, float hR, String ddir){
		Util.saveToYaml(recoil_config, new YamlObj(verticalPath(weap), vR),
				new YamlObj(horizontalPath(weap), hR),
				new YamlObj(horizontalDir(weap), ddir));
	}
	public static void writeWeapon(WeaponRepresenter... weaps){
		for(WeaponRepresenter wrt : weaps)
			AimData.writeWeapon(wrt.getName(), wrt.getVerticalRecoil(), wrt.getHorizontalRecoil(), wrt.getDefaultHorizontalDirection());
	}
	
	public static String verticalPath(String name){
		return "recoil.values." + name + ".vertical";
	}
	public static String horizontalPath(String name){
		return "recoil.values." + name + ".horizontal.value";
	}
	public static String horizontalDir(String name){
		return "recoil.values." + name + ".horizontal.direction";
	}
	
	public static strictfp float[] getHorizontalRecoil(final String weap) {
		final Object o = Util.getFromYaml(recoil_config, "recoil.values." + weap + ".horizontal.value");
		final String s = ((String)Util.getFromYaml(recoil_config, "recoil.values." + weap + ".horizontal.direction", "RIGHT")).toUpperCase();
		if(o == null){
			PluginMethods.alert("Impossible d'obtenir le recul personnalisé. Valeur par défaut.");
			return new float[] {getDefaultHorizontalRecoil(weap), 2F };
		}
		if(o instanceof Double)
			return new float[] { Double.valueOf((Double) o).floatValue(), s.equals("LEFT") ? 0F : (s.equals("RIGHT") ? 1F : 2F) };
		return new float[] { Float.valueOf((Float) o).floatValue(), s.equals("LEFT") ? 0F : (s.equals("RIGHT") ? 1F : 2F) };

	}

	public static strictfp float getVerticalRecoil(final String weap) {
			final Object o = Util.getFromYaml(recoil_config, "recoil.values." + weap + ".vertical");
			if(o == null){
				PluginMethods.alert("Impossible d'obtenir le recul personnalisé. Valeur par défaut.");
				return getDefaultVerticalRecoil(weap);
			}
			if(o instanceof Double)
				return Double.valueOf((Double) o).floatValue();
			return Float.valueOf((Float) o).floatValue();
	}
	public static float getDefaultHorizontalRecoil(String weap){
		float f = 0F;
		switch(weap.toLowerCase()) {
			case "moisin":
				f = MOSIN.getHorizontalRecoil();
				break;
			case "ak-47":
				f = AK47.getHorizontalRecoil();
				break;
			case "bar":
				f = BAR.getHorizontalRecoil();
				break;
			case "grenadelauncher":
				f = M320H.getHorizontalRecoil();
				break;
			case "smith":
				f = SMITH.getHorizontalRecoil();
				break;
			case "ak74u":
				f = AK74U.getHorizontalRecoil();
				break;
			case "pp70b":
				f = PP70B.getHorizontalRecoil();
				break;
			case "p90":
				f = P90.getHorizontalRecoil();
				break;
		}
		return f;
	}
	
	public static float getDefaultVerticalRecoil(String weap){
		float f = 0F;
		switch(weap.toLowerCase()) {
			case "moisin":
				f = MOSIN.getVerticalRecoil();
				break;
			case "ak-47":
				f = AK47.getVerticalRecoil();
				break;
			case "bar":
				f = BAR.getVerticalRecoil();
				break;
			case "grenadelauncher":
				f = M320H.getVerticalRecoil();
				break;
			case "smith":
				f = SMITH.getVerticalRecoil();
				break;
			case "ak74u":
				f = AK74U.getVerticalRecoil();
				break;
			case "pp70b":
				f = PP70B.getVerticalRecoil();
				break;
			case "p90":
				f = P90.getVerticalRecoil();
				break;
		}
		return f;
	}
}
