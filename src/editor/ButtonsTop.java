package editor;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JButton;
import javax.swing.JPanel;

import models.InformationResource;

public class ButtonsTop extends JPanel {
	private JButton entitet;
	private JButton atribut;
	private JButton relacija;
	public ButtonsTop(InformationResource ir, Editor ed){
			
		entitet = new JButton("Add entity");
		
		//Starting JDialog so user can choose to create new or change existing entity
		entitet.addActionListener(e ->{
			ChooseEntity chooseEntity = new ChooseEntity(ir, ed);
			chooseEntity.setVisible(true);
			chooseEntity.setLocationRelativeTo(null);
		});
		
		atribut = new JButton("Add attribute");
		
		//Starting JDialog so user can choose to create new or change existing attribute
		atribut.addActionListener(e ->{
			ChooseAttribute chooseAttribute = new ChooseAttribute(ir, ed);
			chooseAttribute.setVisible(true);
			chooseAttribute.setLocationRelativeTo(null);
		});
		
		relacija = new JButton("Add relation");
		
		//TODO change relation have entity
		//Starting JDialog so user can choose to create new or change existring relation
		relacija.addActionListener(e ->{
			ChooseRelation chooseRelation = new ChooseRelation(ir, ed);
			chooseRelation.setVisible(true);
			chooseRelation.setLocationRelativeTo(null);
		});
		
		this.setLayout(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.insets = new Insets(10, 50, 10, 50);
		this.add(entitet, gbc);
		gbc.gridx = 1;
		this.add(atribut, gbc);
		gbc.gridx = 2;
		this.add(relacija, gbc);
		
		JButton deleteEntity = new JButton("Delete Enitty");
		deleteEntity.addActionListener(e -> {
			DropdownEntity de = new DropdownEntity(ir, ed, true);
			de.setVisible(true);
			de.setLocationRelativeTo(null);
		});
		gbc.gridx = 0;
		gbc.gridy = 1;
		this.add(deleteEntity, gbc);
			
		JButton deleteAttritbue = new JButton("Delete Attribute");
		deleteAttritbue.addActionListener(e -> {
			DropdownAttribute da = new DropdownAttribute(false, ir, ed, true);
			da.setVisible(true);
			da.setLocationRelativeTo(null);
		});
		gbc.gridx = 1;
		this.add(deleteAttritbue, gbc);
		
		JButton deleteRelation = new JButton("Delete Relation");
		deleteRelation.addActionListener(e -> {
			DropdownRelation dr = new DropdownRelation(ir, ed, true, true);
			dr.setVisible(true);
			dr.setLocationRelativeTo(null);			
		});
		
		gbc.gridx = 2;
		this.add(deleteRelation, gbc);
		
		
		
		/**this.setLayout(new GridLayout(1, 3, 50, 50));
		this.add(entitet);
		this.add(atribut);
		this.add(relacija);**/
	}
}
