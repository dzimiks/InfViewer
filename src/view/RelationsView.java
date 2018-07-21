package view;

import java.util.ArrayList;

import javax.swing.DefaultListSelectionModel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.SingleSelectionModel;

import models.Entity;
import models.InformationResource;
import models.Record;
import models.Relation;

public class RelationsView extends JTabbedPane {
	
	private Entity entity;
	private InformationResource parentInfRes;
	private ArrayList<Relation> relations;
	private ArrayList<Entity> entities;
	private ArrayList<RelationPanel> relationPanels;
	
	
	public  RelationsView() {
		super();
	}
	
	public RelationsView(Entity entity) {
		super();
		this.entity = entity;
		//KACA NE ZNA DA KOMITUJE!!!
	}

	public void addNewTab(Entity entity, ArrayList<Record> records){
			RelationPanel relationPanel = new RelationPanel(entity, records);
			this.addTab(entity.getName(), relationPanel);
			int index = this.getTabCount() - 1;
			this.setSelectedIndex(index);
			this.repaint();
	}
	



	public void myRemoveAll(){
		this.removeAll();
	}

	public Entity getEntity() {
		return entity;
	}

	public void setEntity(Entity entity) {
		this.entity = entity;
	}

	public InformationResource getParentInfRes() {
		return parentInfRes;
	}

	public void setParentInfRes(InformationResource parentInfRes) {
		this.parentInfRes = parentInfRes;
	}

	public ArrayList<Relation> getRelations() {
		return relations;
	}

	public void setRelations(ArrayList<Relation> relations) {
		this.relations = relations;
	}

	public ArrayList<Entity> getEntities() {
		return entities;
	}

	public void setEntities(ArrayList<Entity> entities) {
		this.entities = entities;
	}

	public ArrayList<RelationPanel> getRelationPanels() {
		return relationPanels;
	}

	public void setRelationPanels(ArrayList<RelationPanel> relationPanels) {
		this.relationPanels = relationPanels;
	}


	

}
