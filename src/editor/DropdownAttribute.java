package editor;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;

import models.Attribute;
import models.Entity;
import models.tree.Node;

public class DropdownAttribute extends JDialog{
	
	//n == ir
	public DropdownAttribute(boolean choice, Node n, Editor ed, boolean delete) {
		
		JComboBox<Object> comboBox = new JComboBox<>();
		this.setLocationRelativeTo(null);
		this.setSize(500, 400);
		
		JLabel labela;
		
		//Adding buttons to dialog
		this.setLayout(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.insets = new Insets(15, 15, 15, 15);
		
		gbc.gridx = 0;
		gbc.gridy = 1;
		gbc.gridwidth = 1;
		add(comboBox, gbc);
		
		if(choice == true){
			//new
			//choosing entity
			this.setTitle("Add attribute");
			labela = new JLabel("Choose entity you want to add attribute to:");
			for(Node node : n.getChildren()){
				comboBox.addItem((node));
			}
			
		}else{
			//change
			//creating unique attribute and entity combination
			this.setTitle("Edit attribute");
			labela = new JLabel("Choose attribute you want to modify:");
			for(Node node: n.getChildren()){
				for(Node se : node.getChildren()){
					comboBox.addItem(new String(""+ node.getName()+"\\"+se));
				}
			}
			
		}
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.gridwidth = 2;
		this.add(labela, gbc);
		
		gbc.gridy = 1;
		gbc.gridx++;
		gbc.gridwidth = 1;
		JButton btnChoose = new JButton("Choose");
		this.add(btnChoose, gbc);
		btnChoose.addActionListener(e -> {
			if(choice){
				MiniEditor me = new MiniEditor(new Attribute(""), (Node)comboBox.getSelectedItem(), false, ed);
				me.setVisible(true);
				me.setLocationRelativeTo(null);
			}else{
				//Getting entity for selected attribute
				Node entity = null;
				Node attribute = null;
				int count = 0;
				boolean endLooop = false;
				for(Node o : n.getChildren()){
					entity = o;
					for(Node u : o.getChildren()){
						attribute = u;
						if(comboBox.getSelectedIndex() == count){
							endLooop = true;
							break;
						}
						count++;
					}
					if(endLooop){
						break;
					}
				}
				if(delete){
					entity.remove(attribute);
					dispose();
					ed.update();
					return; 
				}
				MiniEditor me = new MiniEditor(attribute, entity, true, ed);
				me.setVisible(true);
				me.setLocationRelativeTo(null);
			}
			
			//Destroying JDialog
			dispose();
		});
		
	}
}
