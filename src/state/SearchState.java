

package state;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

import javafx.scene.control.Tab;
import models.Attribute;
import models.Entity;
import models.Record;
import models.Package;
import models.files.SequentialFile;
import models.files.SerialFile;
import models.files.UIFile;
import models.tree.Node;
import view.MainView;
import view.TabbedView;

@SuppressWarnings("serial")
public class SearchState extends State implements Serializable {

	private int height = 25;
	private int width = 150;
	private int x_left = 50;
	private int x_right = 180;
	private int y = 10;
	
	private int size;
	private Entity entity;
	private ArrayList<Node> attributes;
	private Attribute attr;
	private ArrayList<Boolean> primaryKeys;
	
	private JLabel[] labels;
	private JTextField[] textFields;

	private StateManager stateManager;
	
	private SerialFile serialFile;
	
	public SearchState(Entity entity, int size, StateManager stateManager) {
		this.stateManager = stateManager;
		
		this.entity = entity;
		this.size = size;
		this.attributes = entity.getChildren();
		this.attr = new Attribute("Attribute");
		primaryKeys = new ArrayList<>();
		
		setLayout(null);
	}
	
	@Override
	public void changePanel() {
		this.removeAll();
		height = 25;
		width = 150;
		x_left = 50;
		x_right = 180;
		y = 10;
		this.labels = new JLabel[size];
		this.textFields = new JTextField[size];
		
		for (int i = 0; i < size; i++) {
			this.attr = (Attribute) this.attributes.get(i);
			this.labels[i] = new JLabel(parseName(attr.getName()));
			this.textFields[i] = new JTextField();
			
			if(attr.isPrimaryKey()){
				primaryKeys.add(true);
			}
			else{
				primaryKeys.add(false);
			}
			
			this.labels[i].setBounds(x_left, y, width, height);
			this.textFields[i].setBounds(x_right, y, width, height);

			y += 40;
			add(labels[i]);
			add(textFields[i]);
		}
		

	}
	
	private String parseName(String name) {
		int n = name.length();
		StringBuilder sb = new StringBuilder();
		sb.append(name.charAt(0));
		
		for (int i = 1; i < n; i++) {
			char c = name.charAt(i);
			
			if (c >= 'A' && c <= 'Z') {
				c += 32;
				sb.append(" ");
			}
			
			sb.append(c);
		}
		
		sb.append(": ");
		return sb.toString();
	}

	public Entity getEntity() {
		return entity;
	}

	public void setEntity(Entity entity) {
		this.entity = entity;
	}

	public ArrayList<Node> getAttributes() {
		return attributes;
	}

	public void setAttributes(ArrayList<Node> attributes) {
		this.attributes = attributes;
	}

	public Attribute getAttr() {
		return attr;
	}

	public void setAttr(Attribute attr) {
		this.attr = attr;
	}

	public SerialFile getSerialFile() {
		return serialFile;
	}

	public void setSerialFile(SerialFile serialFile) {
		this.serialFile = serialFile;
	}
	
	@Override
	public void changeEntity(Entity entity) {
		this.entity = entity;
		this.size = entity.getChildCount();
		this.attributes = entity.getChildren();
	}
	
	@Override
	public void submit() {
		
		ArrayList<String> searchAttributes = new ArrayList<>();
		ArrayList<Record> searchResults = new ArrayList<>();
		
		if(!hasValues(textFields)){
			JOptionPane.showMessageDialog(null, "No values entered", "Error", 1);
			return;
		}
		else{
		
			UIFile uifile = null;
			
			if(((Package)entity.getParent()).getType().equals("serial")){
				
				for(int i = 0; i < textFields.length; i++){
					
					searchAttributes.add(textFields[i].getText());
				}
				
				uifile = new SerialFile(TabbedView.getActivePanel().getEntity().getName(), TabbedView.getActivePanel().getEntity().getUrl());
			}
			else if(((Package)entity.getParent()).getType().equals("sequential")){
				
				for(int i = 0; i < textFields.length; i++){
					
					if(primaryKeys.get(i)== true && textFields[i].getText().equals("")){
						JOptionPane.showMessageDialog(null, "Primary key value missing", "Error", 1);
						return;
					}
					searchAttributes.add(textFields[i].getText());
				}
				
				uifile = new SequentialFile(entity.getName(), entity.getUrl());
	
			}
			else{
				//INDEKS SEKVENCIJALNE
				
			}
			
			searchResults = uifile.findRecord(searchAttributes, true);
	
		}
		
		if(searchResults.size() == 0){
			JOptionPane.showMessageDialog(null, "Failed to find record", "Error", 1);
		}
		else{
			
			DefaultTableModel model;
			
			model = TabbedView.getActivePanel().getModel();
			
			while (model.getRowCount() != 0) {
				model.removeRow(0);
			}
			
			model.fireTableDataChanged();
			MainView.getInstance().getDesktopView().updateTab();
			
			
			for(int i = 0; i < searchResults.size(); i++){
				
				Object [] foundRecord = new Object[searchResults.get(i).getPodaci().size()];
				
				for(int j = 0; j < searchResults.get(i).getPodaci().size(); j++){
					foundRecord[j] = searchResults.get(i).getPodaci().get(j);
				}
				
				model.addRow(foundRecord);
			}
			
			model.fireTableDataChanged();
			MainView.getInstance().getDesktopView().updateTab();
			stateManager.setEmptyState();
			stateManager.getCurrentState().changePanel();
			stateManager.getDesktopView().setPanelComponents(stateManager.getCurrentState());
		}
	}
	
	private boolean hasValues(JTextField[] textFields){
		
		for(int i = 0; i < textFields.length; i++){
			if(!textFields[i].getText().equals("")){
				return true;
			}
		}
		
		return false;
	}

	public ArrayList<Boolean> getPrimaryKeys() {
		return primaryKeys;
	}

	public void setPrimaryKeys(ArrayList<Boolean> primaryKeys) {
		this.primaryKeys = primaryKeys;
	}

	public JTextField[] getTextFields() {
		return textFields;
	}

	public void setTextFields(JTextField[] textFields) {
		this.textFields = textFields;
	}

	public StateManager getStateManager() {
		return stateManager;
	}

	public void setStateManager(StateManager stateManager) {
		this.stateManager = stateManager;
	}
	
	
}

