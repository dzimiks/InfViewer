package editor;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;

import javafx.scene.control.ComboBox;
import models.InformationResource;
import models.tree.Node;

public class DropdownEntity extends JDialog {
	public DropdownEntity(InformationResource ir, Editor ed, boolean delete) {
		
		//Initialisation
		
		this.setLocationRelativeTo(null);
		this.setSize(500, 400);
		this.setLayout(new GridBagLayout());
		this.setTitle("Edit entity");
		JLabel labela;
				
		// change
		JComboBox<Node> comboBox = new JComboBox<>();
		labela = new JLabel("Choose entity you want to modify:");
		
		for (Node n : ir.getChildren()) {
			comboBox.addItem(n);
		}
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

		// Dugme za odabir zeljenog entiteta
		gbc.gridx = 1;
		gbc.gridy = 1;
		JButton btnChoose = new JButton("Choose");
		add(btnChoose, gbc);

		// Pokrece miniEditor za metasemu
		btnChoose.addActionListener(e -> {
			if(delete){
				ir.remove((Node)comboBox.getSelectedItem());
				dispose();
				ed.update();
				return;
			}
			MiniEditor me = new MiniEditor(comboBox.getSelectedItem(), ir, true, ed);
			me.setVisible(true);
			me.setLocationRelativeTo(null);
			
			//Destroying JDialog
			dispose();
		});
	}
}
