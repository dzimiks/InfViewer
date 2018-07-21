package state;

import java.awt.Color;
import java.awt.FlowLayout;
import java.util.ArrayList;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JTable;

import models.Attribute;
import models.Entity;
import models.InformationResource;
import models.Record;
import models.Relation;
import view.RelationPanel;
import view.TablePanel;

public class RelationsState extends State{
	
	private Entity entity;
	private InformationResource parentInfRes;
	private ArrayList<Relation> relations;
	private JTabbedPane tabbedPane;
	private ArrayList<Entity> entities;
	private ArrayList<RelationPanel> relationPanels;
	
	public RelationsState() {
	}
	
	public RelationsState(Entity entity) {
		this.entity = entity;
		this.parentInfRes = (InformationResource)entity.getParent();
		this.relations = entity.getRelations();
		//this.tabbedPane = new JTabbedPane();
		setLayout(new FlowLayout());
	}
	
	public void showRelation(Entity entity){
		this.entity = entity;
		this.parentInfRes = (InformationResource)entity.getParent();
		this.relations = entity.getRelations();
		
		for(int i = 0; i < entity.getRelationCount(); i++){
			String refEntityName = relations.get(i).getReferencedEntity().getName();
			TablePanel relPanel = new TablePanel(entity.getRelationAt(i).getReferencedEntity());
			tabbedPane.addTab(refEntityName, relPanel);
		}
	}
	
	public void addNewTab(Entity entity, ArrayList<Record> records){
		RelationPanel relationPanel = new RelationPanel(entity, records);
		tabbedPane.addTab(entity.getName(), relationPanel);
		int index = tabbedPane.getTabCount() - 1;
		tabbedPane.setSelectedIndex(index);
		this.revalidate();
		this.repaint();
	}
	
	public void addNewDBTab(Entity entity, ArrayList<Attribute> referringAttributes, ArrayList<Attribute> referencedAttributes) {
		RelationPanel relationPanel = new RelationPanel(entity, referringAttributes, referencedAttributes);
		tabbedPane.addTab(entity.getName(), relationPanel);
		int index = tabbedPane.getTabCount() - 1;
		tabbedPane.setSelectedIndex(0);
		this.revalidate();
		this.repaint();
	}
	
	public void myRemoveAll(){
		tabbedPane.removeAll();
	}

	@Override
	public void changePanel() {
		this.removeAll();
		this.tabbedPane = new JTabbedPane();
		add(tabbedPane);
		super.revalidate();
		super.repaint();
	}

	@Override
	public void changeEntity(Entity entity) {
		this.entity = entity;
	}

	@Override
	public void submit() {
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

	public JTabbedPane getTabbedPane() {
		return tabbedPane;
	}

	public void setTabbedPane(JTabbedPane tabbedPane) {
		this.tabbedPane = tabbedPane;
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
