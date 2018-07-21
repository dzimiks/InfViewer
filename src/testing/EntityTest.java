package testing;

import models.Attribute;
import models.Entity;

public class EntityTest extends Entity{
	
	public EntityTest(String name){
		super(name);
		Attribute a01 = new Attribute("Atribut01");
		Attribute a02 = new Attribute("Atribut02");
		Attribute a03 = new Attribute("Atribut03");
		Attribute a04 = new Attribute("Atribut04");
		//Attribute a05 = new Attribute("Atribut05");
		
		this.addChild(a01);
		this.addChild(a02);
		this.addChild(a03);
		this.addChild(a04);
		//this.addChild(a05);
		
	}
}
