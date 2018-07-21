package editor;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;

import models.InformationResource;

public class ChooseAttribute extends JDialog{
	private JButton newAttributeButton;
	private JButton changeAttributeButton;
	public ChooseAttribute(InformationResource ir, Editor ed) {
		
		//Initialisation
		changeAttributeButton = new JButton("Change existing attribute");
		newAttributeButton = new JButton("Add new attribute");
		this.setLocationRelativeTo(null);
		this.setTitle("Add attribute");
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
		this.add(newAttributeButton, gbc);
		
		gbc.gridx = 1;
		this.add(changeAttributeButton, gbc);
		
		//Starting dialog so user can choose where he wants to add new attribute 
		newAttributeButton.addActionListener(e ->{
			DropdownAttribute dropdownAttribute = new DropdownAttribute(true, ir, ed, false);
			dropdownAttribute.setVisible(true);
			dropdownAttribute.setLocationRelativeTo(null);
			
			//Destroying JDialog
			dispose();
		});
		
		//Starting dialog so user can choose which attribute to change
		changeAttributeButton.addActionListener(e ->{
			DropdownAttribute dropdownAttribute = new DropdownAttribute(false, ir, ed, false);
			dropdownAttribute.setVisible(true);
			dropdownAttribute.setLocationRelativeTo(null);
			
			//Destroying JDialog
			dispose();
		});
		
	}
}
