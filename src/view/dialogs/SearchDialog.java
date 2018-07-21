package view.dialogs;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.Serializable;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

import models.Attribute;
import models.Entity;
import models.Record;
import models.files.SequentialFile;
import models.files.SerialFile;
import models.files.UIFile;
import models.tree.Node;
import view.MainView;
import view.TabbedView;

@SuppressWarnings("serial")
public class SearchDialog extends JDialog implements Serializable {

	private int height = 25;
	private int width = 150;
	private int x_left = 50;
	private int x_right = 180;
	private int y = 10;
	
	private int size;
	private Entity entity;
	private ArrayList<Node> attributes;
	private Attribute attr;
	
	private JLabel[] labels;
	private JTextField[] textFields;
	
	public SearchDialog(Entity entity, int size) {
		this.entity = entity;
		this.size = size;
		this.attributes = entity.getChildren();
		this.attr = new Attribute("Attribute");
		
		setLayout(null);
		setTitle("Search Block");
		initialize();
		setSize(400, 350);
		setLocationRelativeTo(MainView.getInstance());
	}

	public void initialize() {
		this.labels = new JLabel[size];
		this.textFields = new JTextField[size];
		
		for (int i = 0; i < size; i++) {
			this.attr = (Attribute) this.attributes.get(i);
			this.labels[i] = new JLabel(parseName(attr.getName()));
			this.textFields[i] = new JTextField();
			
			this.labels[i].setBounds(x_left, y, width, height);
			this.textFields[i].setBounds(x_right, y, width, height);

			y += 40;
			add(labels[i]);
			add(textFields[i]);
		}
		
		JButton btnOk = new JButton("OK");
		
		btnOk.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				
				ArrayList<String> searchAttributes = new ArrayList<>();
				ArrayList<Record> searchResults = new ArrayList<>();
				
				for(int i = 0; i < textFields.length; i++){
					searchAttributes.add(textFields[i].getText());
				}
				
				UIFile uifile = null;
				
//				if(TabbedView.getActivePanel().getEntity().getType().equals("serial")){
//					
//					uifile = new SerialFile(TabbedView.getActivePanel().getEntity().getName(), TabbedView.getActivePanel().getEntity().getUrl());
//				}
//				else if(TabbedView.getActivePanel().getEntity().getType().equals("sequential")){
//					
//					for(int i = 0; i < attributes.size(); i++){
//						if(((Attribute)attributes.get(i)).isPrimaryKey() && textFields[i].getText().equals("")){
//							JOptionPane.showMessageDialog(null, "You must enter primary key values", "Error", 1);
//							return;
//						}
//					}
//					
//					uifile = new SequentialFile(TabbedView.getActivePanel().getEntity().getName(), TabbedView.getActivePanel().getEntity().getUrl());
//
//				}
//				else{
//					//INDEKS SEKVENCIJALNE
//					
//				}
				
				searchResults = uifile.findRecord(searchAttributes, true);
				
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
				}
			}
		});
		
		JButton btnCancel = new JButton("Cancel");
		
		btnCancel.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				
			}
		});

		btnOk.setBounds(135, y + 50, 50, height);
		btnCancel.setBounds(195, y + 50, 80, height);

		add(btnOk);
		add(btnCancel);
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
	
}

