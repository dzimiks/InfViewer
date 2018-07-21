package editor;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JOptionPane;

import models.Attribute;
import models.Entity;
import models.InformationResource;
import models.Relation;
import models.tree.Node;

public class SelectRelation extends JDialog {
	public SelectRelation(Entity entity, Editor ed, boolean delete) {

		// Initialisation
		this.setLocationRelativeTo(null);
		this.setSize(300, 300);
		this.setLayout(new GridBagLayout());

		JComboBox<String> comboBox = new JComboBox<>();
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.gridx = 0;
		gbc.gridy = 0;
		this.add(comboBox, gbc);

		for (Relation n : entity.getRelations()) {
			ArrayList<Attribute> red = n.getReferencedAttributes();
			ArrayList<Attribute> ring = n.getReferringAttributes();
			// Ako ima relacija
			if (red.size() != 0)
				comboBox.addItem(red.get(0).toString() + " - " + ring.get(0).toString());
		}

		JButton btnChoose = new JButton("Choose");

		gbc.gridx = 1;
		this.add(btnChoose, gbc);

		btnChoose.addActionListener(e -> {
			try {
				if (delete) {
					entity.getRelations().remove(comboBox.getSelectedIndex());
					ed.update();
				} else {
					MiniEditor me = new MiniEditor(entity.getRelationAt(comboBox.getSelectedIndex()), entity, true, ed);
					me.setVisible(true);
					me.setLocationRelativeTo(null);
				}
			} catch (Exception exception) {
				JOptionPane.showMessageDialog(null, "Nema postojecih relacija");
			}
			dispose();
		});

	}
}
