package view;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.sql.Clob;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import models.Attribute;
import models.Entity;
import models.Package;
import models.Record;
import models.Relation;
import models.Warehouse;
import models.datatypes.CharType;
import models.datatypes.DateType;
import models.datatypes.VarCharType;
import models.tree.Node;
import net.sourceforge.jtds.jdbc.ClobImpl;

public class RelationPanel extends JPanel{
	
	private Entity entity;
	private DefaultTableModel model;
	private JTable table;
	public String[] columnNames;
	private JScrollPane js;
	private ArrayList<Attribute> referringAttributes;
	private ArrayList<Attribute> referencedAttributes;

	public RelationPanel(Entity entity, ArrayList<Record> records) {
		this.entity = entity;
		this.setLayout(new BorderLayout());

		columnNames = new String[entity.getChildCount()];

		for (int i = 0; i < entity.getChildCount(); i++) {
			columnNames[i] = entity.getChildren().get(i).getName();
		}
		
		model = new DefaultTableModel(columnNames, 0);
		
		if(!((Package) entity.getParent()).getType().equals("sql")) {
			for(Record r : records){
				model.addRow(r.getPodaci().toArray());
				//System.out.println("Ubacujem " + r.getPodaci().toString());
			}
		}
		else {
			try {
				fetchRecords();
			} 
			catch (SQLException e) {
				e.printStackTrace();
			}
		}
		
//		System.out.println("Ubacio sam sve");

		model.fireTableDataChanged();
		
		table = new JTable(model);

		table.setPreferredScrollableViewportSize(new Dimension(900, 300));

		table.setFillsViewportHeight(true);
		js = new JScrollPane(table);
		this.add(js);
	}

	public RelationPanel(Entity entity, ArrayList<Attribute> referringAttributes, ArrayList<Attribute> referencedAttributes) {
		this.entity = entity;
		this.referringAttributes = referringAttributes;
		this.referencedAttributes = referencedAttributes;
		this.setLayout(new BorderLayout());

		columnNames = new String[entity.getChildCount()];

		for (int i = 0; i < entity.getChildCount(); i++) {
			columnNames[i] = entity.getChildren().get(i).getName();
		}
		
		model = new DefaultTableModel(columnNames, 0);
		
		try {
			fetchRecords();
		} 
		catch (SQLException e) {
			e.printStackTrace();
		}
		
		model.fireTableDataChanged();
		
		table = new JTable(model);

		table.setPreferredScrollableViewportSize(new Dimension(900, 300));

		table.setFillsViewportHeight(true);
		js = new JScrollPane(table);
		this.add(js);
	}
	
	private void fetchRecords() throws SQLException {
		System.out.println("Fetching DB records...");
		
		while (model.getRowCount() != 0) {
			model.removeRow(0);
		}
		
		StringBuilder sb = new StringBuilder();
		sb.append("SELECT * FROM " + entity.getName() + " WHERE ");
		
		int selectedRow = TabbedView.activePanel.getTable().getSelectedRow();
		
		for (int i = 0; i < referencedAttributes.size(); i++) {
			int index = 0;
			Object[] attrs = TabbedView.activePanel.getEntity().getChildren().toArray();
			
			for (int j = 0; j < attrs.length; j++) {
				Attribute attr = (Attribute) attrs[j];
				
				if (attr.getName().equals(referencedAttributes.get(i).getName())) {
					index = j;
					break;
				}
			}
			
			Object obj = TabbedView.activePanel.getTable().getModel().getValueAt(selectedRow, index);
			sb.append(referencedAttributes.get(i).getName() + "=" + pijaniApostol(obj) + " AND ");	
		}
		
		sb.delete(sb.length() - 5, sb.length());
		System.out.println(sb.toString());
		
		PreparedStatement statement = Warehouse.getInstance().getDbConnection().prepareStatement(sb.toString());
		ResultSet resultSet = statement.executeQuery();
		
		if (resultSet.getMetaData().getColumnCount() != entity.getChildCount()) {
			System.err.println("Database and MS out of sync.");
			return;
		}
		
		while (resultSet.next()) {
			Record record = new Record();
			
			for (Node node : entity.getChildren()) {
				if (node instanceof Attribute) {
					Object value = resultSet.getObject(node.getName());
			
//					if (value instanceof ClobImpl) {
//						value = (ClobImpl) value;
//						record.addObject(clobToString((Clob) value));
//					}
//					else
						record.addObject(value);
				}
			}
			
			model.addRow(record.getPodaci().toArray());
//			records.add(record);
		}
		
		resultSet.close();
		statement.close();
	}

	public String pijaniApostol(Object obj) {
		return (obj.getClass() == CharType.class || obj.getClass() == VarCharType.class || obj.getClass() == String.class || obj.getClass() == DateType.class) ? ("'" + obj + "'") : obj.toString();
	}
}
