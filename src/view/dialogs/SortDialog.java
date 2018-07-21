package view.dialogs;

import java.io.Serializable;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;

import models.Attribute;
import models.Entity;
import models.tree.Node;
import view.MainView;

@SuppressWarnings("serial")
public class SortDialog extends JDialog implements Serializable {

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
	private JCheckBox[] checkBoxes;
	private JComboBox<String> types;
	
	public SortDialog(Entity entity, int size) {
		this.entity = entity;
		this.size = size;
		this.attributes = entity.getChildren();
		this.attr = new Attribute("Attribute");
		
		setLayout(null);
		setTitle("Sort Block");
		setSize(260, 380);
		setLocationRelativeTo(MainView.getInstance());
		initialize();
	}

	public void initialize() {
		this.labels = new JLabel[size];
		this.checkBoxes = new JCheckBox[size];
		this.types = new JComboBox<>();
		this.types.addItem("Ascending");
		this.types.addItem("Descending");
		this.types.setBounds(x_left, y, width, height);
		add(types);
		y += 40;
		
		for (int i = 0; i < size; i++) {
			this.attr = (Attribute) this.attributes.get(i);
			this.labels[i] = new JLabel(parseName(attr.getName()));
			this.checkBoxes[i] = new JCheckBox();
			
			this.labels[i].setBounds(x_left, y, width, height);
			this.checkBoxes[i].setBounds(x_right, y, width, height);

			y += 40;
			add(labels[i]);
			add(checkBoxes[i]);
		}
		
		JButton btnOk = new JButton("OK");
		JButton btnCancel = new JButton("Cancel");

		btnOk.setBounds(60, y + 50, 50, height);
		btnCancel.setBounds(120, y + 50, 80, height);

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
}