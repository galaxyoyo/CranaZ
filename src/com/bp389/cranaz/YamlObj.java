package com.bp389.cranaz;


/**
 * Classe conteneur permettant une s�rialisation plus facile.
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
