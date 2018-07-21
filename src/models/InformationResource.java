/***********************************************************************
 * Module:  InformationResourse.java
 * Author:  Mihailo
 * Purpose: Defines the Class InformationResourse
 ***********************************************************************/

package models;

import java.io.File;
import java.util.*;

import models.tree.Node;

@SuppressWarnings("serial")
public class InformationResource extends Node {

	public InformationResource(String name) {
		super(name);
	}
	
	public InformationResource(InformationResource ir){
		super(ir.getName());
		
		for(int i = 0; i < ir.getChildCount(); i++){
			addChild(new Entity((Entity)ir.getChildAt(i)));
		}
	}

	public boolean loadEntities() {
		// TODO: implement
		// Returns true if entities are loeaded, otherwise returns false
		return false;
	}

	public boolean updateEntities() {
		// TODO: implement
		// Returns true if entities are refreshed, otherwise returns false
		return false;
	}

	public boolean updateEntity(int entityId) {
		// TODO: implement
		// Returns true if entity with id "entityId" is updated, otherwise
		// returns false
		return false;
	}
}