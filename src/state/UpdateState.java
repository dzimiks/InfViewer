package state;

import java.io.Serializable;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import models.Attribute;
import models.Entity;
import models.Record;
import models.tree.Node;
import view.MainView;
import view.TabbedView;

@SuppressWarnings("serial")
public class UpdateState extends State implements Serializable {

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
	
	private StateManager stateManager;
	
	public UpdateState(Entity entity, int size, StateManager stateManager) {
		this.stateManager = stateManager;
		
		this.entity = entity;
		this.size = size;
		this.attributes = entity.getChildren();
		this.attr = new Attribute("Attribute");
		
		setLayout(null);
/**		setTitle("Update Record");
		setSize(400, 350);
		setLocationRelativeTo(MainView.getInstance());
		initialize();**/
	}
	@Override
	public void changePanel() {
		this.removeAll();
		height = 25;
		width = 150;
		x_left = 50;
		x_right = 180;
		y = 10;
		this.labels = new JLabel[size];
		this.textFields = new JTextField[size];

		int selectedRow = TabbedView.activePanel.getTable().getSelectedRow();
		Record record = new Record(entity);
		
		System.out.println(selectedRow);
		
		for (int i = 0; i < TabbedView.activePanel.getTable().getColumnCount(); i++) {
			Object obj = TabbedView.activePanel.getTable().getModel().getValueAt(selectedRow, i);
			record.addObject(obj);
		}
		
		System.out.println(record);
		String[] parts = record.toString().split("\\|");
		
		for (int i = 0; i < size; i++) {
			this.attr = (Attribute) this.attributes.get(i);
			this.labels[i] = new JLabel(parseName(attr.getName()));
			this.textFields[i] = new JTextField(parts[i]);
			
			this.labels[i].setBounds(x_left, y, width, height);
			this.textFields[i].setBounds(x_right, y, width, height);

			y += 40;
			add(labels[i]);
			add(textFields[i]);
		}
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
	@Override
	public void changeEntity(Entity entity) {
		this.entity = entity;
		this.size = entity.getChildCount();
		this.attributes = entity.getChildren();
	}
	@Override
	public void submit() {
		stateManager.setEmptyState();
		stateManager.getCurrentState().changePanel();
		stateManager.getDesktopView().setPanelComponents(stateManager.getCurrentState());
	}
}