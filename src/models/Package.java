package models;

import models.tree.Node;

@SuppressWarnings("serial")
public class Package extends Node {

	private String type;
	
	public Package(String name, String type) {
		super(name);
		this.type = type;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
}