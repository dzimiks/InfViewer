package testing;

import models.Attribute;
import models.Entity;
import models.InformationResource;
import models.Warehouse;

public class TestDB {

	public TestDB(String name) {
		// TODO Auto-generated constructor stub

		InformationResource ir1 = new InformationResource("IR1");
		InformationResource ir2 = new InformationResource("IR2");

		Entity e01 = new Entity("E1");
		Entity e02 = new Entity("E2");
		Entity e03 = new Entity("E3");
		Entity e04 = new Entity("E4");

		ir1.addChild(e01);
		ir1.addChild(e03);

		ir2.addChild(e02);
		ir2.addChild(e04);

		Attribute a01 = new Attribute("Atribut01");
		Attribute a02 = new Attribute("Atribut02");
		Attribute a03 = new Attribute("Atribut03");
		Attribute a04 = new Attribute("Atribut04");
		Attribute a05 = new Attribute("Atribut05");
		Attribute a06 = new Attribute("Atribut06");
		Attribute a07 = new Attribute("Atribut07");
		Attribute a08 = new Attribute("Atribut08");
		Attribute a09 = new Attribute("Atribut09");
		Attribute a10 = new Attribute("Atribut10");
		Attribute a11 = new Attribute("Atribut11");
		Attribute a12 = new Attribute("Atribut12");
		
		e01.addChild(a01);
		e01.addChild(a02);
		e01.addChild(a03);
		
		e02.addChild(a04);
		e02.addChild(a05);
		e02.addChild(a06);
		e02.addChild(a07);
		
		e03.addChild(a08);
		e03.addChild(a09);
		e03.addChild(a10);
		
		e04.addChild(a11);
		e04.addChild(a12);
		
		print(ir1);
		print(new InformationResource(ir1));
		
		
		
	}
	
	private void print(InformationResource ir){

		System.out.println(ir.getName());
		for(int i = 0; i < ir.getChildCount(); i++){
			System.out.println("Entity: " + ir.getChildAt(i));
			for(int j = 0; j < ir.getChildAt(i).getChildCount(); j++){
				System.out.println("Attribute: " + ir.getChildAt(i).getChildAt(j));
			}
		}
		System.out.println("---------------------------------");
		
	}

}
