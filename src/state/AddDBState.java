package state;

import java.awt.TextField;
import java.io.IOException;
import java.io.Serializable;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Vector;

import javax.sound.midi.Synthesizer;
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
import models.Warehouse;
import models.datatypes.CharType;
import models.datatypes.DateType;
import models.datatypes.VarCharType;
import models.tree.Node;
import view.MainView;
import view.TabbedView;

@SuppressWarnings("serial")
public class AddDBState extends State implements Serializable {

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
	private JLabel[] types;
	private JTextField[] textFields;

	public AddDBState(Entity entity, int size, StateManager stateManager) {
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
		y = 10;
		this.labels = new JLabel[size];
		this.textFields = new JTextField[size];
		this.types = new JLabel[size];
		
		int max = -1;
		
		//Creating labels and textfields
		for (int i = 0; i < size; i++) {
			this.attr = (Attribute) this.attributes.get(i);
			this.labels[i] = new JLabel(attr.getName());
			this.textFields[i] = new JTextField();
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

	// Vraca tip atributa u odredjenom formatu
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
						+ " mustn't be longer than " + attribute.getLength() + " characters", "Error",
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
			sb.append("INSERT INTO " + entity.getName() + " (");

			for (Node node : entity.getChildren())
				if (node instanceof Attribute)
					sb.append(node.getName() + ",");

			sb.deleteCharAt(sb.length() - 1);
			sb.append(") VALUES(");

			for (int i = 0; i < entity.getChildCount(); i++)
				sb.append("?,");

			sb.deleteCharAt(sb.length() - 1);
			sb.append(")");
			System.out.println(sb.toString());

			try {
				PreparedStatement statement = Warehouse.getInstance().getDbConnection().prepareStatement(sb.toString());
				Record record = new Record(entity);
				int selectedRow = TabbedView.activePanel.getTable().getModel().getRowCount();
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
						System.out.println("Add DB state: ne valja nesto");
					}

					value.add(object);
				}

				for (int i = 0; i < entity.getChildCount(); i++) {
					record.addObject(value.get(i));
				}

				for (int i = 0; i < entity.getChildCount(); i++) {
					// Attribute attr = (Attribute) entity.getChildAt(i);
					setByType(statement, i + 1, record.getPodaci().get(i));
				}

				int updatedRows = statement.executeUpdate();
				statement.close();
				TabbedView.activePanel.fetchRecords();
//				TabbedView.activePanel.setSelectedRecord(selectedRow);
				TabbedView.activePanel.setSelectedRecord(record);
				
			} catch (SQLException e1) {
				e1.printStackTrace();
			}

			MainView.getInstance().getDesktopView().updateTab();
//			stateManager.setEmptyState();
//			stateManager.getDesktopView().setPanelComponents(stateManager.getCurrentState());
		}
	}

	private void setByType(PreparedStatement statement, int index, Object obj) throws SQLException {
		if (obj instanceof CharType) {
			statement.setString(index, ((CharType) obj).get());
		} 
		else if (obj instanceof VarCharType) {
			statement.setString(index, ((VarCharType) obj).get());
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