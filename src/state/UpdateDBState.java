package state;

import java.io.Serializable;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import models.Attribute;
import models.Entity;
import models.Record;
import models.Warehouse;
import models.datatypes.CharType;
import models.datatypes.DateType;
import models.datatypes.VarCharType;
import models.tree.Node;
import view.MainView;
import view.TabbedView;

@SuppressWarnings("serial")
public class UpdateDBState extends State implements Serializable {

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
	private JLabel[] types;
	private JTextField[] textFields;

	private StateManager stateManager;

	public UpdateDBState(Entity entity, int size, StateManager stateManager) {
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
		this.types = new JLabel[size];

		int selectedRow = TabbedView.activePanel.getTable().getSelectedRow();
		Record record = new Record();
		
		for (int i = 0; i < TabbedView.activePanel.getTable().getColumnCount(); i++) {
			Object obj = TabbedView.activePanel.getTable().getModel().getValueAt(selectedRow, i);
			record.addObject(obj);
//			record.addAttribute((Attribute) entity.getChildAt(i), obj);
		}
		
		String[] parts = record.toString().split("\\|");

		int max = -1;
		for (int i = 0; i < size; i++) {
			this.attr = (Attribute) this.attributes.get(i);
			this.labels[i] = new JLabel(attr.getName());
			this.textFields[i] = new JTextField(parts[i]);
			this.types[i] = new JLabel(getType(attr));
			
			max = Math.max(labels[i].getText().length()*10, max);
		}
		
		//Adding Textfields and labels
		for(int i = 0;i < size; i++){
			
			this.labels[i].setBounds(50, y, max, 25);
			this.textFields[i].setBounds(max+40, y, 150, 25);
			this.types[i].setBounds(max+40+150+20, y, 150, 25);
			y += 40;
			add(labels[i]);
			add(textFields[i]);
			add(types[i]);
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

	private String getType(Attribute attr2) {
		if (attr2.getValueClass() == VarCharType.class) {
			if (attr2.isPrimaryKey()) {
				return "*VarChar (" + attr2.getLength() + ")";
			}
			
			return "VarChar (" + attr2.getLength() + ")";
		} 
		else if (attr2.getValueClass() == CharType.class) {
			if (attr2.isPrimaryKey()) {
				return "*Char (" + attr2.getLength() + ")";
			}
			
			return "Char (" + attr2.getLength() + ")";
		} 
		else if (attr2.getValueClass() == DateType.class) {
			if (attr2.isPrimaryKey()) {
				return "*Date (" + attr2.getLength() + ")";
			}
			
			return "Date (" + attr2.getLength() + ")";
		}
		else if (attr2.getValueClass() == Boolean.class) {
			if (attr2.isPrimaryKey()) {
				return "*Boolean (" + attr2.getLength() + ")";
			}
			
			return "Boolean (" + attr2.getLength() + ")";
		}
		else if (attr2.getValueClass() == Integer.class) {
			if (attr2.isPrimaryKey()) {
				return "*Numeric (" + attr2.getLength() + ")";
			}
			
			return "Numeric (" + attr2.getLength() + ")";
		}

		return null;
	}
	
	@Override
	public void changeEntity(Entity entity) {
		this.entity = entity;
		this.size = entity.getChildCount();
		this.attributes = entity.getChildren();
	}

	@Override
	public void submit() {
		boolean error = false;
		Attribute attribute = null;
		String fieldText = null;

		for (int i = 0; i < size; i++) {
			attribute = ((Attribute) attributes.get(i));
			fieldText = textFields[i].getText();

			if (attribute.isPrimaryKey() && fieldText.equals("")) {
				error = true;
				JOptionPane.showMessageDialog(new JFrame(), "Attribute " + attribute.getName() + " must be entered",
						"Error", JOptionPane.ERROR_MESSAGE);
				break;
			}
			if (attribute.getLength() < fieldText.length()) {
				error = true;
				JOptionPane.showMessageDialog(new JFrame(), "Attribute " + attribute.getName()
						+ " mustn't be longer than" + attribute.getLength() + "characters", "Error",
						JOptionPane.ERROR_MESSAGE);
				break;
			}
			if (!(fieldText.equals("")) && (attribute.getValueClass() == Boolean.class)
					&& !((fieldText.equals("true") || fieldText.equals("false")))) {
				error = true;
				JOptionPane.showMessageDialog(new JFrame(),
						"Attribute " + attribute.getName() + " must be true or false, but is neither", "Error",
						JOptionPane.ERROR_MESSAGE);
				break;
			}
			if (!(fieldText.equals("")) && attribute.getValueClass() == Integer.class) {
				try {
					int a = Integer.parseInt(fieldText);
				} catch (NumberFormatException ex) {
					error = true;
					JOptionPane.showMessageDialog(new JFrame(),
							"Attribute " + attribute.getName() + " should be numeric value but is not", "Error",
							JOptionPane.ERROR_MESSAGE);
					break;
				}
			}
			if (!(fieldText.equals("")) && attribute.getValueClass() == DateType.class) {
				if (!DateType.isValidDate(fieldText)) {
					error = true;
					JOptionPane.showMessageDialog(new JFrame(),
							"Attribute " + attribute.getName() + " should be date format but it's not", "Error",
							JOptionPane.ERROR_MESSAGE);
					break;
				}
			}

		}
		if (!error) {
			StringBuilder sb = new StringBuilder();
			sb.append("UPDATE " + entity.getName() + " SET ");
			
			for (Node node : attributes) {
				if (node instanceof Attribute) {
					sb.append(node.getName() + "=?, ");
				}
			}
			
			sb.delete(sb.length() - 2, sb.length());
			sb.append(" WHERE ");
			
			for (Node node : attributes) {
				if (node instanceof Attribute) {
					if (((Attribute) node).isPrimaryKey()) {
						sb.append(node.getName() + "=? AND ");
					}
				}
			}
			
			sb.delete(sb.length() - 5, sb.length());
			System.out.println(sb.toString());
			
			try {
				PreparedStatement statement = Warehouse.getInstance().getDbConnection().prepareStatement(sb.toString());
				int selectedRow = TabbedView.activePanel.getTable().getSelectedRow();
				Record oldRecord = new Record(entity);
				Record newRecord = new Record(entity);
				ArrayList<Object> value = new ArrayList<>();

				for (int i = 0; i < entity.getChildCount(); i++) {
					Attribute attr = (Attribute) entity.getChildAt(i);
					Object object = null;

					if (attr.getValueClass() == VarCharType.class) {
						object = new VarCharType(attr.getLength(), textFields[i].getText());
					} 
					else if (attr.getValueClass() == CharType.class) {
						object = new CharType(attr.getLength(), textFields[i].getText());
					}
					else if (attr.getValueClass() == DateType.class) {
						try {
							object = new DateType(textFields[i].getText());
						} 
						catch (ParseException e) {
							e.printStackTrace();
						}
					}
					else if (attr.getValueClass() == Boolean.class) {
						object = new Boolean(textFields[i].getText());
					} 
					else if (attr.getValueClass() == Integer.class) {
						object = new Integer(textFields[i].getText());
					} 
					else {
						System.out.println("Update DB state: ne valja nesto");
					}

					value.add(object);
				}

				for (int i = 0; i < entity.getChildCount(); i++) {
					newRecord.addObject(value.get(i));
					newRecord.addAttribute(attribute, value.get(i));
				}
				
				for (int i = 0; i < TabbedView.activePanel.getTable().getColumnCount(); i++) {
					oldRecord.addObject(TabbedView.activePanel.getTable().getModel().getValueAt(selectedRow, i));
					oldRecord.addAttribute(attribute, TabbedView.activePanel.getTable().getModel().getValueAt(selectedRow, i));
				}
				
				for (int i = 0; i < attributes.size(); i++) {
//					Attribute attr = (Attribute) attributes.get(i);
//					setByType(statement, i + 1, newRecord.getAttributes().get(attr));
					setByType(statement, i + 1, newRecord.getPodaci().get(i));
				}
				
				int index = attributes.size() + 1;
				
				for (int i = 0; i < attributes.size(); i++) {
					Attribute attr = (Attribute) attributes.get(i);
					
					if (attr.isPrimaryKey()) {
//						setByType(statement, index++, oldRecord.getAttributes().get(attr));
						setByType(statement, index++, oldRecord.getPodaci().get(i));
					}
				}
				
				int updatedRows = statement.executeUpdate();
				statement.close();
				TabbedView.activePanel.fetchRecords();
//				TabbedView.activePanel.setSelectedRecord(selectedRow);
				TabbedView.activePanel.setSelectedRecord(newRecord);
			} 
			catch (SQLException e) {
				e.printStackTrace();
			}

			MainView.getInstance().getDesktopView().updateTab();
//			stateManager.setEmptyState();
//			stateManager.getDesktopView().setPanelComponents(stateManager.getCurrentState());
		}
	}
	
	private void setByType(PreparedStatement statement, int index, Object obj) throws SQLException {
		System.out.println(obj.toString() + " " + obj.getClass().getName());
		
		if (obj instanceof CharType) {
			statement.setString(index, ((CharType) obj).get());
		} 
		else if (obj instanceof VarCharType || obj instanceof String) {
			VarCharType varchar = new VarCharType(50, obj.toString());
			statement.setString(index, varchar.get());
		} 
		else if (obj instanceof DateType) {
			Date date = ((DateType) obj).getDate();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
			String formatedDate = sdf.format(date);
			statement.setTimestamp(index, Timestamp.valueOf(formatedDate));
		} 
		else if (obj instanceof Boolean) {
			statement.setBoolean(index, (Boolean) obj);
		}
		else if (obj instanceof Integer) {
			statement.setInt(index, (Integer) obj);
		} 
		else {
			System.out.println("Not valid object");
		}
	}
}