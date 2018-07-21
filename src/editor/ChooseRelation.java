package editor;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;

import models.InformationResource;

public class ChooseRelation extends JDialog{
	private JButton newRelationButton;
	private JButton changeRelationButton;
	public ChooseRelation(InformationResource ir, Editor ed) {
		this.setLocationRelativeTo(null);
		this.setTitle("Add relation");
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
		newRelationButton = new JButton("Add new relation");
		newRelationButton.addActionListener(e ->{
			DropdownRelation dropdownRelation = new DropdownRelation(ir, ed, false, false);
			dropdownRelation.setVisible(true);
			dropdownRelation.setLocationRelativeTo(null);
			dispose();
			
		});
		this.add(newRelationButton, gbc);
		
		gbc.gridx = 1;
		changeRelationButton = new JButton("Change existing relation");
		changeRelationButton.addActionListener(e ->{
			DropdownRelation dropdownRelation = new DropdownRelation(ir, ed, true, false);
			dropdownRelation.setVisible(true);
			dropdownRelation.setLocationRelativeTo(null);
			dispose();
		});
		this.add(changeRelationButton, gbc);
	}
}
