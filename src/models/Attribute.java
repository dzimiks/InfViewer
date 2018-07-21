/***********************************************************************
 * Module:  Attribute.java
 * Author:  Mihailo
 * Purpose: Defines the Class Attribute
 ***********************************************************************/

package models;

import models.tree.Node;

@SuppressWarnings("serial")
public class Attribute extends Node {

	private boolean primaryKey;
	private int length;
	private Class<?> valueClass;
	private boolean mandatory;
	
	//Za testiranje
	public Attribute(String name){
		super(name);
	}
	
	public Attribute(Attribute a){
		super(a.getName());
		this.primaryKey = a.isPrimaryKey();
		this.length = a.getLength();
		
	}
	
	public Attribute(String name, boolean primaryKey, int length, Class<?> valueClass, boolean mandatory) {
		super(name);
		this.primaryKey = primaryKey;
		this.length = length;
		this.valueClass = valueClass;
		this.mandatory = mandatory;
	}
	
	//Novi
	public Attribute(String name, boolean primaryKey, int length) {
		super(name);
		this.primaryKey = primaryKey;
		this.length = length;
	}


	public Attribute(String name, boolean primaryKey) {
		super(name);
		this.primaryKey = primaryKey;
	}

	public Class<?> getValueClass() {
		return valueClass;
	}
	
	public boolean isPrimaryKey() {
		return primaryKey;
	}

	public int getLength() {
		return length;
	}

	public void setPrimaryKey(boolean primaryKey) {
		this.primaryKey = primaryKey;
	}

	public void setLength(int length) {
		this.length = length;
	}

	public boolean isMandatory() {
		return mandatory;
	}
}