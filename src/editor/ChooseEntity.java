package editor;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;

import models.Entity;
import models.InformationResource;

public class ChooseEntity extends JDialog{
	private JButton newEntityButton;
	private JButton changEntityButton;
	public ChooseEntity(InformationResource ir, Editor ed) {
		
		//Initialisation
		this.setLocationRelativeTo(null);
		this.setTitle("Add entity");
		this.setSize(400, 300);
		
		JLabel labela = new JLabel("You want to:");
		
		//Adding buttons to dialog
		this.setLayout(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.insets = new Insets(15, 15, 15, 15);
		
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.gridwidth = 2;
		this.add(labela, gbc);
		
		gbc.gridx = 0;
		gbc.gridy = 1;
		gbc.gridwidth = 1;
		newEntityButton = new JButton("Add new entity");
		this.add(newEntityButton, gbc);
		gbc.gridx = 1;
		changEntityButton = new JButton("Change existing entity");
		
		//Starting MiniEditor because new entity will go to ir
		newEntityButton.addActionListener(e ->{
			MiniEditor me = new MiniEditor(new Entity(""), ir, false, ed);
			me.setVisible(true);
			me.setLocationRelativeTo(null);
			
			//Unistava JDialog
			dispose();
		});
		
		//Sarting DromdownEntity so user can choose which entity to change
		changEntityButton.addActionListener(e ->{
			DropdownEntity dropdownEntity = new DropdownEntity(ir, ed, false);
			dropdownEntity.setVisible(true);
			dropdownEntity.setLocationRelativeTo(null);
			
			//Unistava JDialog
			dispose();
		});
		this.add(changEntityButton, gbc);
	}
}
