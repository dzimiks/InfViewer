/***********************************************************************
 * Module:  TabbedView.java
 * Author:  Mihailo
 * Purpose: Defines the Class TabbedView
 ***********************************************************************/

package view;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.*;

import javax.crypto.spec.SecretKeySpec;
import javax.swing.JTabbedPane;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import main.Main;
import models.Entity;
import models.Record;

@SuppressWarnings("serial")
public class TabbedView extends JTabbedPane {
		
	private ArrayList<Entity> entities;
	private ArrayList<TablePanel> tables;
	public static TablePanel activePanel;
	
	public TabbedView(){
		entities = new ArrayList<>();
		tables = new ArrayList<>();
	}

	public void addNewTab(Entity entity) {
		if(!entities.contains(entity)){
			TablePanel table = new TablePanel((Entity)entity);
			activePanel = table;
			this.addTab(entity.getName(), table);
			int index = this.getTabCount() - 1;
			this.setSelectedIndex(index);
			
			this.entities.add(entity);
			this.tables.add(table);
			
			MainView.getInstance().getDesktopView().updateTab();
		}
		else{
			int indexOf = entities.indexOf(entity);
			this.setSelectedIndex(this.indexOfComponent(tables.get(indexOf)));

		}

		activePanel = getTabelePanelAt(getSelectedIndex());
	}
	
	public Entity getSelectedEntity() {
		if (getSelectedComponent() == null) 
			return null;
		
		return ((TablePanel) getSelectedComponent()).getEntity();
	}
	
	public Record getSelectedRow() {
		Entity entity = getSelectedEntity();
		
		if (entity == null)
			return null;
		
		TablePanel panel = (TablePanel) getSelectedComponent();
		int selectedRow = panel.getTable().getSelectedRow();
		
		if (selectedRow == -1) 
			return null;
		
		return entity.readRecordFrom(selectedRow, true);
	}

	public static TablePanel getActivePanel() {
		return activePanel;
	}

	public void myRemoveAll(){
	}

	public ArrayList<Entity> getEntities() {
		return entities;
	}
	
	public TablePanel getTabelePanelAt(int x){
		return tables.get(x);
	}
	
}