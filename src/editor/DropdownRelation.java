package editor;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import models.Entity;
import models.InformationResource;
import models.Relation;
import models.tree.Node;

public class DropdownRelation extends JDialog {
	public DropdownRelation(InformationResource ir, Editor ed, boolean changeExisting, boolean delete) {

		// Initialisation
		this.setLocationRelativeTo(null);
		this.setSize(400, 300);
		this.setLayout(new GridBagLayout());
		this.setTitle("Add/Edit relation");

		JLabel labela = new JLabel("Choose parent entity for relation:");
		JComboBox<Entity> comboBox = new JComboBox<>();
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.insets = new Insets(15, 15, 15, 15);
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.gridwidth = 2;
		this.add(labela, gbc);
		
		gbc.gridx = 0;
		gbc.gridy = 1;
		gbc.gridwidth = 1;
		this.add(comboBox, gbc);

		for (Node n : ir.getChildren()) {
			comboBox.addItem((Entity) n);
		}

		JButton btnChoose = new JButton("Choose");

		gbc.gridx = 1;
		this.add(btnChoose, gbc);

		btnChoose.addActionListener(e -> {
			if (changeExisting) {
				SelectRelation sr = new SelectRelation((Entity) comboBox.getSelectedItem(), ed, true);
				sr.setVisible(true);
				sr.setLocationRelativeTo(null);
			}else{
				MiniEditor me = new MiniEditor(null, (Entity)comboBox.getSelectedItem(), false, ed);
				me.setVisible(true);
				me.setLocationRelativeTo(null);
				
			}
			dispose();
		});

	}
}
