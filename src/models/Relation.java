/***********************************************************************
 * Module:  Relation.java
 * Author:  Mihailo
 * Purpose: Defines the Class Relation
 ***********************************************************************/

package models;

import java.util.*;

import models.tree.Node;



public class Relation {
//	private int id;
	private String name;
	private static int idGenerator;

	private Entity referencedEntity;
	private ArrayList<Attribute> referencedAttributes;
	private ArrayList<Attribute> referringAttributes;
	
	public Relation(String referencedAttributes, String referringAttributes) {
		this.referencedAttributes = new ArrayList<>();
		this.referringAttributes = new ArrayList<>();
	}
	
	//Copy constructor
	public Relation(Relation r, Entity e){
		this.referencedAttributes = new ArrayList<>();
		this.referringAttributes = new ArrayList<>();
		
		//Nesto
		for(Attribute a: r.getReferencedAttributes()){
			for(Node n : e.getChildren()){
				if(n.getName().equals(a.getName())){
					this.referencedAttributes.add((Attribute)n);
					this.referencedEntity = (Entity) n.getParent();
				}
			}
		}
		
		//NEsto
		for(Attribute a : r.getReferringAttributes()){
			for(Node n : e.getChildren()){
				if(n.getName().equals(a.getName())){
					this.referringAttributes.add((Attribute)n);
				}
			}
		}
		
	}
	
	public Relation(ArrayList<Attribute> referringAttributes, ArrayList<Attribute> referencedAttributes, Entity referencedEntity) {
		this.name = Integer.toString(idGenerator++);
		this.referringAttributes = referringAttributes;
		this.referencedAttributes = referencedAttributes;
		this.referencedEntity = referencedEntity;
	}
	
	public String getName() {
		return this.name;
	}
	
	@Override
	public String toString() {
		return name.toString();
	}

	public ArrayList<Attribute> getReferencedAttributes() {
		return referencedAttributes;
	}

	public ArrayList<Attribute> getReferringAttributes() {
		return referringAttributes;
	}

	public Entity getReferencedEntity() {
		return referencedEntity;
	}

	public void setReferencedEntity(Entity referencedEntity) {
		this.referencedEntity = referencedEntity;
	}

	public void setReferencedAttributes(ArrayList<Attribute> referencedAttributes) {
		this.referencedAttributes = referencedAttributes;
	}

	public void setReferringAttributes(ArrayList<Attribute> referringAttributes) {
		this.referringAttributes = referringAttributes;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	
	
//	public void dodaj(String referencedAttributes, String referringAttributes){
//		this.referencedAttributes.add(referencedAttributes);
//		this.referringAttributes.add(referringAttributes);
//	}
//
//	public String getEntityNameFromReferencedIndex(int index){
//		String[] n = referencedAttributes.get(index).split("-");
//		return n[0];
//	}
//	
//	public String getAttributeNameFromReferencedIndex(int index){
//		String[] n = referencedAttributes.get(index).split("-");
//		return n[1];
//	}
//	
//	public String getAttributeNameFromReferringIndex(int index){
//		return referringAttributes.get(index);
//	}
	
	
}