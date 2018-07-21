package view.dialogs;

import java.io.Serializable;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JTextField;

import models.Attribute;
import models.Entity;
import models.tree.Node;
import view.MainView;

@SuppressWarnings("serial")
public class DeleteDialog extends JDialog implements Serializable {

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
	
	public DeleteDialog(Entity entity, int size) {
		this.entity = entity;
		this.size = size;
		this.attributes = entity.getChildren();
		this.attr = new Attribute("Attribute");
		
		setLayout(null);
		setTitle("Delete Record");
		setSize(400, 350);
		setLocationRelativeTo(MainView.getInstance());
		initialize();
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
		JButton btnCancel = new JButton("Cancel");

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
}