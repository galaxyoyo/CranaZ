package com.bp389.cranaz;


/**
 * Classe conteneur permettant une sérialisation plus facile.
 * @author BlackPhantom
 *
 */
public class YamlObj {
	public String path;
	public Object obj;
	public YamlObj(String path, Object obj){
		this.path = path;
		this.obj = obj;
	}
}
