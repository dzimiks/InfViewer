package state;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import models.Attribute;
import models.Entity;
import models.Package;
import models.Record;
import models.datatypes.CharType;
import models.datatypes.DateType;
import models.datatypes.VarCharType;
import models.files.SequentialFile;
import models.files.SerialFile;
import models.files.UIFile;
import models.tree.Node;
import view.DesktopView;
import view.MainView;
import view.TabbedView;

@SuppressWarnings("serial")
public class AddState extends State implements Serializable {

	private int height = 25;
	private int width = 150;
	private int x_left = 50;
	private int x_right = 180;
	private int y = 10;
	
	private int size;
	private Entity entity;
	private ArrayList<Node> attributes;
	private Attribute attr;
	
	private StateManager stateManager;
	
	private JLabel[] labels;
	private JTextField[] textFields;
	
	public AddState(Entity entity, int size, StateManager stateManager) {
		this.stateManager = stateManager;
		this.entity = entity;
		this.size = size;
		this.attributes = entity.getChildren();
		this.attr = new Attribute("Attribute");
		
		setLayout(null);

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
	}
	
	@Override
	public void submit() {
		
		boolean error = false;
		for(int i = 0 ; i < size ; i++){
			Attribute attribute = ((Attribute)attributes.get(i));
			String fieldText = textFields[i].getText();
			if(attribute.isPrimaryKey() && fieldText.equals("")){
				error = true;
				JOptionPane.showMessageDialog(new JFrame(), "Attribute " + attribute.getName() +
						" must be entered", "Error", JOptionPane.ERROR_MESSAGE);
				break;
			}
			if(attribute.getLength() < fieldText.length()){
				error = true;
				JOptionPane.showMessageDialog(new JFrame(), "Attribute " + attribute.getName() +
						" mustn't be longer than" + attribute.getLength() + "characters", "Error", JOptionPane.ERROR_MESSAGE);
				break;
			}
			if(!(fieldText.equals("")) && (attribute.getValueClass() == Boolean.class) && 
					!((fieldText.equals("true") || fieldText.equals("false")))){
				error = true;
				JOptionPane.showMessageDialog(new JFrame(), "Attribute " + attribute.getName() +
						" must be true or false, but is neither", "Error", JOptionPane.ERROR_MESSAGE);
				break;
			}
			if(!(fieldText.equals("")) && attribute.getValueClass() == Integer.class){
				try{
					int a = Integer.parseInt(fieldText);
				}catch(NumberFormatException ex){
					error = true;
					JOptionPane.showMessageDialog(new JFrame(), "Attribute " + attribute.getName() +
							" should be numeric value but is not", "Error", JOptionPane.ERROR_MESSAGE);
					break;
				}
			}
			if(!(fieldText.equals("")) && attribute.getValueClass() == DateType.class){
				if(!DateType.isValidDate(fieldText)){
					error = true;
					JOptionPane.showMessageDialog(new JFrame(), "Attribute " + attribute.getName() +
							" should be date format but it's not", "Error", JOptionPane.ERROR_MESSAGE);
					break;
				}
			}
			
		}
		if(!error){
			
			UIFile uifile = null;
			
			if(((Package)TabbedView.getActivePanel().getEntity().getParent()).getType().equals("serial")){
				uifile = new SerialFile(TabbedView.activePanel.getEntity().getName(), TabbedView.activePanel.getEntity().getUrl());
			}
			else if(((Package)TabbedView.getActivePanel().getEntity().getParent()).getType().equals("sequential")){
				uifile = new SequentialFile(TabbedView.activePanel.getEntity().getName(), TabbedView.activePanel.getEntity().getUrl());
			}
			else{
				// za indeks sekvencijalne
				
			}
			
			ArrayList<String> a = new ArrayList<>();
			
			for(JTextField tf : textFields){
				a.add(tf.getText());
			}
			
			try {
				uifile.addRecord(a);
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			
			MainView.getInstance().getDesktopView().updateTab();
			
			stateManager.setEmptyState();
			stateManager.getDesktopView().setPanelComponents(stateManager.getCurrentState());
		}
		
	}
	
	@Override
	public void changeEntity(Entity entity) {
		this.entity = entity;
		this.size = entity.getChildCount();
		this.attributes = entity.getChildren();
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