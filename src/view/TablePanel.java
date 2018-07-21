
package view;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.sql.Clob;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import javax.swing.DefaultListSelectionModel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTable;

import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import javax.swing.table.DefaultTableModel;

import models.FilterParams;
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
import state.RelationsState;
import state.StateManager;

public class TablePanel extends JPanel {

	private Entity entity;
	public String[] columnNames;
	private JTable table;
	private JScrollPane js;
	private DefaultTableModel model;
	private int lastBlockSize = 0;
	private int fetchNumber = 0;
	private boolean isLastNext = true;
	private int deletedCounter = 0;
	private boolean[] deletedIndexes;
	
	private StateManager stateManager;
	
	
	public TablePanel(Entity entity) {
		
		this.entity = entity;
		this.setLayout(new BorderLayout());

		columnNames = new String[entity.getChildCount()];

		for (int i = 0; i < entity.getChildCount(); i++) {
			columnNames[i] = entity.getChildren().get(i).getName();
		}
		
		model = new DefaultTableModel(columnNames, 0);
		
//		if (((Package) entity.getParent()).getType().equals("sql")) {
//			try {
//				fetchRecords();
//			}
//			catch (SQLException e) {
//				e.printStackTrace();
//			}
//		}
//		else {
//			entity.getFileData();
//			
//			for(Record r : entity.fetchNextBlock(10)){
//				model.addRow(r.getPodaci().toArray());
//			}			
//		}
		
		lastBlockSize = 10;


		fetchNumber++;
		deletedIndexes = new boolean[10];
		
		table = new JTable(model);
		
		table.getSelectionModel().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		
		table.setPreferredScrollableViewportSize(new Dimension(900, 300));

		table.setFillsViewportHeight(true);
		js = new JScrollPane(table);
		this.add(js);
		
		stateManager = MainView.getInstance().getDesktopView().getStateManager();
		

	}
	
	// TODO	
	// =========================================
	public void fetchRecords() throws SQLException {
		System.out.println("Fetching DB records...");
		
		while (model.getRowCount() != 0) {
			model.removeRow(0);
		}
		
		ArrayList<Record> records = new ArrayList<>();
		StringBuilder sb = new StringBuilder();
		sb.append("SELECT * FROM " + entity.getName());
		System.out.println("===========");
		System.out.println(sb.toString());
		System.out.println("===========");
		
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
			
					if (value instanceof ClobImpl) {
						value = (ClobImpl) value;
						record.addObject(clobToString((Clob) value));
					}
					else
						record.addObject(value);
				}
			}
			
			model.addRow(record.getPodaci().toArray());
			records.add(record);
		}
		
		resultSet.close();
		statement.close();
	}
	
	private String clobToString(Clob data) {
	    StringBuilder sb = new StringBuilder();
	    
	    try {
	        Reader reader = data.getCharacterStream();
	        BufferedReader br = new BufferedReader(reader);

	        String line;
	        
	        while (null != (line = br.readLine())) {
	            sb.append(line);
	        }
	        
	        br.close();
	    } 
	    catch (SQLException e) {
	    
	    }
	    catch (IOException e) {

	    }
	    return sb.toString();
	}
	
	public void setSelectedRecord(Record record) {
		if (TabbedView.activePanel == null) {
			return;
		}
		
		TablePanel panel = (TablePanel) TabbedView.activePanel;
		int rowCount = panel.getModel().getRowCount() - 1;
		int rowIndex = 0;
		Record currRecord = new Record(entity);
		
		if (rowCount != -1) {
			for (int i = 0; i < panel.getModel().getRowCount(); i++) {
				Object obj = new Object();
				
				for (int j = 0; j < panel.getTable().getColumnCount(); j++) {
					obj = TabbedView.activePanel.getTable().getModel().getValueAt(i, j);
					currRecord.addObject(obj);
				}
				
				boolean flag = false;
				System.out.println(record);
				System.out.println(currRecord);
				
//				for (int j = 0; j < panel.getTable().getColumnCount(); j++) {
//					if (record.getPodaci().get(i).getClass() == DateType.class) {
//						try {
//							System.out.println("AAAAAAAAAAA " + new DateType(record.getPodaci().get(i).toString() + ".0"));
//							record.getPodaci().set(i, new DateType(record.getPodaci().get(i).toString() + ".0"));
//						} 
//						catch (ParseException e) {
//							e.printStackTrace();
//						}
//					}
//				}
				
				for (int j = 0; j < entity.getChildCount(); j++) {
					String[] recordParts = record.toString().split("\\|");
					String[] parts = currRecord.toString().split("\\|");
					
					if (!parts[j].equals(recordParts[j])) {
						flag = false;
						break;
					}
					else 
						flag = true;
				}
				
				if (flag) {
					System.out.println("Row is set!");
					rowIndex = i;
					break;
				}
				
				for (int j = 0; j < panel.getTable().getColumnCount(); j++) {
					obj = TabbedView.activePanel.getTable().getModel().getValueAt(i, j);
					currRecord.removeObject(obj);
				}
			}
			
			panel.getTable().setRowSelectionInterval(rowIndex, rowIndex);
			panel.getTable().scrollRectToVisible(panel.getTable().getCellRect(rowIndex, 0, true));
		}
	}
	
	public void setSelectedRecord(int rowIndex) {
		if (TabbedView.activePanel == null) {
			return;
		}
		
		TablePanel panel = (TablePanel) TabbedView.activePanel;
//		int rowIndex = panel.getModel().getRowCount() - 1;
		
		if (rowIndex != -1) {
			panel.getTable().setRowSelectionInterval(rowIndex, rowIndex);
			panel.getTable().scrollRectToVisible(panel.getTable().getCellRect(rowIndex, 0, true));
		}
	}
	// =========================================
	
	public Entity getEntity() {
		return entity;
	}

	public void setEntity(Entity entity) {
		this.entity = entity;
	}

	public void getNext(int numberOfRows) {
		
		if(!isLastNext){
			entity.incCounter(lastBlockSize-deletedCounter);
		}
		
		// Brise stare redove
		while (model.getRowCount() != 0) {
			model.removeRow(0);
		}

		if(numberOfRows == -1){
			numberOfRows = lastBlockSize;
		}
		
		for(Record r : entity.fetchNextBlock(numberOfRows)){
			model.addRow(r.getPodaci().toArray());
		}
		model.fireTableDataChanged();
		lastBlockSize = numberOfRows;

		fetchNumber++;
		
		deletedCounter = 0;
		deletedIndexes = new boolean[numberOfRows];
		
		isLastNext = true;
		
		MainView.getInstance().getDesktopView().updateTab();
	}

	public void getPrev(int numberOfRows){
		
		if(isLastNext){
			entity.reduceCounter(lastBlockSize-deletedCounter);
		}
		
		while(model.getRowCount() != 0){
			model.removeRow(0);
		}
		
		if(numberOfRows == -1){
			numberOfRows = lastBlockSize;
		}
		
		for(Record r : entity.fetchPrevBlock(numberOfRows)){
			model.addRow(r.getPodaci().toArray());
		}
		
		model.fireTableDataChanged();
		lastBlockSize = numberOfRows;
		
		fetchNumber++;
		
		deletedCounter = 0;
		deletedIndexes = new boolean[numberOfRows];
		
		isLastNext = false;
		
		MainView.getInstance().getDesktopView().updateTab();
		
		
	}
	
	//Metoda za dodavanje novog recorda u tabelu
	public void addRecordToTableModel(Record r){
		model.addRow(r.getPodaci().toArray());
		model.fireTableDataChanged();		
	}
	
	public void deleteDBItem() {
		StringBuilder sb = new StringBuilder();
		sb.append("DELETE FROM " + entity.getName() + " WHERE ");

		for (Node node : entity.getChildren()) {
			if (node instanceof Attribute) {
				Attribute attr = (Attribute) node;
				
				if (attr.isPrimaryKey()) {
					sb.append(attr.getName() + " = ? AND ");
				}
			}
		}
		
		sb.delete(sb.length() - 5, sb.length());
		System.out.println(sb.toString());

		try {
			PreparedStatement statement = Warehouse.getInstance().getDbConnection().prepareStatement(sb.toString());
//			Record record = new Record(entity);
			int selectedRow = TabbedView.activePanel.getTable().getSelectedRow();
			int cnt = 1;
			
			for (int i = 0; i < TabbedView.activePanel.getTable().getColumnCount(); i++) {
//				record.addObject(TabbedView.activePanel.getTable().getModel().getValueAt(selectedRow, i));
				Object object = TabbedView.activePanel.getTable().getModel().getValueAt(selectedRow, i);
				Attribute attr = (Attribute) entity.getChildAt(i);
				
				if (attr.isPrimaryKey()) {
					setByType(statement, cnt++, object);
				}
			}
			
			int updatedRows = statement.executeUpdate();
			statement.close();
			System.out.println("Deleting DB record...");
			TabbedView.activePanel.fetchRecords();
			
		} catch (SQLException e1) {
			e1.printStackTrace();
		}

		MainView.getInstance().getDesktopView().updateTab();
		stateManager.setEmptyState();
		stateManager.getDesktopView().setPanelComponents(stateManager.getCurrentState());
	}
	
	private void setByType(PreparedStatement statement, int index, Object obj) throws SQLException {
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
	
	public void deleteSelectedItem(){
		
		if(!((Package)entity.getParent()).getType().equals("serial")) return;
		if(table.getSelectedRow() == -1) return;
		
		int num = 0;
		for(int i = 0; i < deletedIndexes.length && i-num <= table.getSelectedRow() ; i++){
			if(deletedIndexes[i]){
				num++;
			}
		}
		
		if(isLastNext){
			entity.setDeletedIndex(table.getSelectedRow()-lastBlockSize+num);
		}else{
			entity.setDeletedIndex(table.getSelectedRow()+num);
		}
		
		deletedIndexes[num + table.getSelectedRow()] = true;
		
		deletedCounter++;
		model.removeRow(table.getSelectedRow());
		model.fireTableDataChanged();
	}
	
	
	public int getFetchNumber() {
		return fetchNumber;
	}

	public long getRecordCount() {
		return entity.getRecordNumber();
	}

	public int getBlockSize() {
		return model.getRowCount() * entity.getRecordSize();
	}

	public String[] getColumnNames() {
		return columnNames;
	}

	public void setColumnNames(String[] columnNames) {
		this.columnNames = columnNames;
	}

	public JTable getTable() {
		return table;
	}

	public void setTable(JTable table) {
		this.table = table;
	}

	public JScrollPane getJs() {
		return js;
	}

	public void setJs(JScrollPane js) {
		this.js = js;
	}

	public DefaultTableModel getModel() {
		return model;
	}

	public void setModel(DefaultTableModel model) {
		this.model = model;
	}

	public int getLastBlockSize() {
		return lastBlockSize;
	}

	public void setLastBlockSize(int lastBlockSize) {
		this.lastBlockSize = lastBlockSize;
	}

	public void setFetchNumber(int fetchNumber) {
		this.fetchNumber = fetchNumber;
	}
	
}

