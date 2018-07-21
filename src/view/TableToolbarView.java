package view;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import javax.swing.DefaultListSelectionModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.JToolBar;
import javax.swing.SpinnerModel;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableModel;

import constants.Constants;
import models.Attribute;
import models.Entity;
import models.Package;
import models.Record;
import models.Relation;
import models.Warehouse;
import models.datatypes.CharType;
import models.datatypes.DateType;
import models.datatypes.VarCharType;
import models.files.IndSeqFile;
import models.tree.Node;
import state.AddState;
import state.RelationsState;
import state.SearchState;
import state.SortState;
import state.StateManager;
import state.UpdateState;

@SuppressWarnings("serial")
public class TableToolbarView extends JPanel {

	private JButton nextBlock;
	private JButton search;
	private JButton sort;
	private JButton add;
	private JButton update;
	private JButton delete;
	private JButton prevBlock;
	private JButton doAction;
	private JButton showRelations;

	private JButton dbFetch;
	private JButton dbFilter;
	private JButton dbSort;
	private JButton dbAdd;
	private JButton dbUpdate;
	private JButton dbDelete;
	private JButton dbShowRelations;
	
	private SpinnerModel numberModel;
	private JSpinner blockFactor;

	private JTextField blocksFetched;
	private JTextField fileSize;
	private JTextField recordSize;
	private JTextField recordNumber;
	private JTextField blockNumber;

	private JToolBar fileToolbar;

	private JToolBar dbToolbar;
	private Entity entity;
	
	private StateManager st;
	private DesktopView desktopView;

	public TableToolbarView(Entity entity, StateManager st, DesktopView dw) {
		this.entity = entity;
		this.desktopView = dw;
		this.st = st;
		
		setFileToolbar();
		setDBToolbar();
		setLayout(new BorderLayout());

		this.fileToolbar.setVisible(false);
		this.dbToolbar.setVisible(false);

		this.add(fileToolbar, BorderLayout.NORTH);
		this.add(dbToolbar, BorderLayout.SOUTH);
	}
	
	public void changeView(Entity entity) {
		if (entity != null && ((Package) entity.getParent()).getType().equals("sql")) {
			this.fileToolbar.setVisible(false);
			this.dbToolbar.setVisible(true);
//			this.removeAll();
//			this.add(dbToolbar, BorderLayout.NORTH);
//			this.update(getGraphics());
		}
		else if (entity != null) {
			this.fileToolbar.setVisible(true);
			this.dbToolbar.setVisible(false);
//			this.removeAll();
//			this.add(fileToolbar, BorderLayout.NORTH);
//			this.update(getGraphics());s
		}
	}

	private void setFileToolbar() {
		fileToolbar = new JToolBar();
		fileToolbar.setLayout(new WrapLayout(20, 15, 10));
		
		this.prevBlock = new JButton("Fetch Previous Block");
		this.prevBlock.setIcon(new ImageIcon(Constants.FETCH_PREV_BLOCK_ICON));
		prevBlock.addActionListener( new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				TabbedView.activePanel.getPrev((int) numberModel.getValue());
				MainView.getInstance().getDesktopView().updateTab();
				countUp();
			}
		});
		
		this.nextBlock = new JButton("Fetch Next Block");
		this.nextBlock.setIcon(new ImageIcon(Constants.FETCH_NEXT_BLOCK_ICON));
		this.nextBlock.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				TabbedView.activePanel.getNext((int) numberModel.getValue());
				MainView.getInstance().getDesktopView().updateTab();
				countUp();
			}
		});
	
		this.numberModel = new SpinnerNumberModel(10, 1, 100, 1);
		this.blockFactor = new JSpinner(numberModel);
		
		this.search = new JButton("Search");
		this.search.setIcon(new ImageIcon(Constants.SEARCH_ICON));
		this.search.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
			/**	searchDialog = new SearchState(entity, entity.getChildren().size());
				searchDialog.setVisible(true);**/
				st.setSearchState();
				st.getCurrentState().changeEntity(TabbedView.activePanel.getEntity());
				st.getCurrentState().changePanel();
				desktopView.setPanelComponents(st.getCurrentState());
				desktopView.repaint();
				//desktopView.setPanelComponents(stateManager.getCurrentState());
			}
		});
		
		this.sort = new JButton("Sort");
		this.sort.setIcon(new ImageIcon(Constants.SORT_ICON));
		this.sort.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
			/**	sortDialog = new SortState(entity, entity.getChildren().size());
				sortDialog.setVisible(true);**/
				st.setSortState();
				st.getCurrentState().changeEntity(TabbedView.activePanel.getEntity());
				st.getCurrentState().changePanel();
				desktopView.setPanelComponents(st.getCurrentState());
				desktopView.repaint();
				//desktopView.setPanelComponents(stateManager.getCurrentState());
			}
		});
		
		this.blocksFetched = new JTextField("0");
		this.blocksFetched.setEditable(false);
		this.blocksFetched.setPreferredSize(new Dimension(50, 30));
		this.blocksFetched.setHorizontalAlignment(SwingConstants.CENTER);
		
		this.fileSize = new JTextField();
		this.fileSize.setText(String.valueOf(0 + " KB"));
		this.fileSize.setColumns(8);
		this.fileSize.setEnabled(false);
		
		this.recordSize = new JTextField();
		this.recordSize.setText("0 KB");
		this.recordSize.setColumns(7);
		this.recordSize.setEnabled(false);
		
		this.add = new JButton("Add");
		this.add.setIcon(new ImageIcon(Constants.ADD_ICON));
		this.add.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
			/**	addDialog = new AddState(entity, entity.getChildren().size());
				addDialog.setVisible(true);**/
				st.setAddState();
				st.getCurrentState().changeEntity(TabbedView.activePanel.getEntity());
				st.getCurrentState().changePanel();
				desktopView.setPanelComponents(st.getCurrentState());
				desktopView.repaint();
				//desktopView.setPanelComponents(stateManager.getCurrentState());
			}
		});
		
		this.update = new JButton("Update");
		this.update.setIcon(new ImageIcon(Constants.UPDATE_ICON));
		this.update.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				
				TabbedView.activePanel.getPrev(-1);
				TabbedView.activePanel.getNext(-1);
				
			/**	updateDialog = new UpdateState(entity, entity.getChildren().size());
				updateDialog.setVisible(true);
				st.setUpdateState();
				st.getCurrentState().changeEntity(TabbedView.activePanel.getEntity());
				st.getCurrentState().changePanel();
				desktopView.setPanelComponents(st.getCurrentState());
				desktopView.repaint();
				//desktopView.setPanelComponents(stateManager.getCurrentState());

				updateDialog = new UpdateDialog(entity, entity.getChildren().size(), tabbedView);
				updateDialog.setVisible(true);
**/
			}
		});
		
		this.delete = new JButton("Delete");
		this.delete.setIcon(new ImageIcon(Constants.DELETE_ICON));
		this.delete.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				TabbedView.activePanel.deleteSelectedItem();
				st.setEmptyState();
				st.getCurrentState().changePanel();
				desktopView.setPanelComponents(st.getCurrentState());
				desktopView.repaint();
			
			}
		});
		
		this.recordNumber = new JTextField();
		this.recordNumber.setText("0");
		this.recordNumber.setColumns(7);
		this.recordNumber.setEnabled(false);
		
		this.doAction = new JButton("Submit");
		this.doAction.setIcon(new ImageIcon(Constants.SUBMIT_ICON));
		this.doAction.addActionListener(e -> {
			st.getCurrentState().submit();
		});
		
//		this.blockNumber = new JTextField();
//		this.blockNumber.setText("0");
//		this.blockNumber.setColumns(7);
//		this.blockNumber.setEnabled(false);
		
		showRelations = new JButton("Show relations");
		showRelations.setIcon(new ImageIcon(Constants.SHOW_RELATIONS_ICON));
		showRelations.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				
				
				st.setRelationsState();
				st.getCurrentState().changePanel();
				desktopView.setPanelComponents(st.getCurrentState());
				desktopView.repaint();
					
				Entity activeEntity = TabbedView.getActivePanel().getEntity();

				ArrayList<Relation> relations = activeEntity.getRelations();
				
				int selectedIndex = TabbedView.getActivePanel().getTable().getSelectedRow();
				
				Record currentRecord = activeEntity.readRecordFrom(selectedIndex, true);

				for(int i = 0; i < relations.size(); i++){ // Prolazim kroz sve svoje relacije					
										
					Entity referencedEntity = relations.get(i).getReferencedEntity();
					
					ArrayList<Attribute> referringAttributes = relations.get(i).getReferringAttributes();
					ArrayList<Attribute> referencedAttributes = relations.get(i).getReferencedAttributes();
					
					ArrayList<Integer> referringIndex = new ArrayList<>();
					ArrayList<Integer> referencedIndex = new ArrayList<>();
					ArrayList<Record> recordsToShow = new ArrayList<>();
					
					for(int j = 0; j < referringAttributes.size(); j++){ // Za svaku relaciju prolazim kroz listu Atributa
																					// po kojima je u vezi sa drugim entitetom
						
						for(int k = 0; k < activeEntity.getChildCount(); k++){ // Prolazim kroz sve svoje atribute i gledam koji imaju isto
																			// ime kao reffering attribute
							
							if(((Attribute)activeEntity.getChildAt(k)).getName().equals(referringAttributes.get(j).getName())){
								
								referringIndex.add(k);
								//System.out.println("1. Atribut " + ((Attribute)activeEntity.getChildAt(k)).getName() + " Indeks " + j);
							}
						}
					}
					
					for(int h = 0; h < referencedAttributes.size() ; h++ ){
						
						for(int p = 0; p < referencedEntity.getChildCount(); p++){
							
							if(((Attribute)referencedEntity.getChildAt(p)).getName().equals(referencedAttributes.get(h).getName())){
								
								referencedIndex.add(p);
								//System.out.println("2. Atribut " + ((Attribute)referencedEntity.getChildAt(p)).getName() + " Indeks " + p);
							}
							
						}						
					}
					
					//Entity referencedEntity = relations.get(i).getReferencedEntity();

					referencedEntity.getFileData();
					
					for(int j = 0; j < referencedEntity.getRecordNumber(); j++){
						Record newRecord = referencedEntity.readRecordFrom(j, false);
						boolean isOkRecord = true;
						
						for(int m = 0; m < referencedIndex.size(); m++){
							if(!newRecord.getPodaci().get(referencedIndex.get(m)).equals(currentRecord.getPodaci().get(referringIndex.get(m)))){
								isOkRecord = false;
							}
						}			
						
						if(isOkRecord){
							recordsToShow.add(newRecord);
						}		
					}
					//System.out.println("ReferencedIndex " + referencedIndex.size() + " Reffering Index " + referringIndex.size());
					((RelationsState)st.getCurrentState()).addNewTab(referencedEntity, recordsToShow);
					desktopView.repaint();
				}
				
			}
		});
		
		JButton makeTree = new JButton("Make Tree");
		makeTree.setIcon(new ImageIcon(Constants.MAKE_TREE_ICON));
		makeTree.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				System.out.println(entity.getUrl());
				IndSeqFile file = new IndSeqFile(entity.getUrl(), "test", false);

				try {
					file.loadTree(entity);
				} 
				catch (IOException e1) {
					e1.printStackTrace();
				}
			}
		});
		
		
		fileToolbar.add(prevBlock);
		fileToolbar.add(nextBlock);
		fileToolbar.add(makeTree);
		fileToolbar.add(new JLabel("Block Factor: "));
		fileToolbar.add(blockFactor);
		fileToolbar.add(new JLabel("Number of Blocks Fetched: "));
		fileToolbar.add(blocksFetched);
		fileToolbar.add(search);
		fileToolbar.add(sort);
		fileToolbar.add(add);
		fileToolbar.add(update);
		fileToolbar.add(delete);
		fileToolbar.add(new JLabel("File Size: "));
		fileToolbar.add(fileSize);
		fileToolbar.add(new JLabel("Record Size: "));
		fileToolbar.add(recordSize);
		fileToolbar.add(new JLabel("Number of Records: "));
		fileToolbar.add(recordNumber);
		fileToolbar.add(showRelations);
		fileToolbar.add(doAction);
//		fileToolbar.add(new JLabel("Number of Blocks: "));
//		fileToolbar.add(blockNumber);
	}
	
	
	private void setDBToolbar() {
		dbToolbar = new JToolBar();
		dbToolbar.setLayout(new WrapLayout(20, 15, 10));
		
		dbFetch = new JButton("Fetch Data");
		dbFetch.setIcon(new ImageIcon(Constants.FETCH_NEXT_BLOCK_ICON));
		dbFetch.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				try {
					TabbedView.activePanel.fetchRecords();
				} 
				catch (SQLException e) {
					e.printStackTrace();
				}
			}
		});
		
		dbAdd = new JButton("Add");
		dbAdd.setIcon(new ImageIcon(Constants.ADD_ICON));
		dbAdd.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				st.setAddDBState();
				st.getCurrentState().changeEntity(TabbedView.activePanel.getEntity());
				st.getCurrentState().changePanel();
				desktopView.setPanelComponents(st.getCurrentState());
				desktopView.repaint();
			}
		});
		
		dbUpdate = new JButton("Update");
		dbUpdate.setIcon(new ImageIcon(Constants.UPDATE_ICON));
		dbUpdate.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				st.setUpdateDBState();
				st.getCurrentState().changeEntity(TabbedView.activePanel.getEntity());
				st.getCurrentState().changePanel();
				desktopView.setPanelComponents(st.getCurrentState());
				desktopView.repaint();
			}
		});
		
		dbDelete = new JButton("Delete");
		dbDelete.setIcon(new ImageIcon(Constants.DELETE_ICON));
		dbDelete.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				TabbedView.activePanel.deleteDBItem();
				st.setEmptyState();
				st.getCurrentState().changePanel();
				desktopView.setPanelComponents(st.getCurrentState());
				desktopView.repaint();
			
			}
		});
		
		dbFilter = new JButton("Filter");
		dbFilter.setIcon(new ImageIcon(Constants.SEARCH_ICON));
		dbFilter.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				st.setFilterDBState();
				st.getCurrentState().changeEntity(TabbedView.activePanel.getEntity());
				st.getCurrentState().changePanel();
				desktopView.setPanelComponents(st.getCurrentState());
				desktopView.repaint();
			}
		});
		
		dbSort = new JButton("Sort");
		dbSort.setIcon(new ImageIcon(Constants.SORT_ICON));
		dbSort.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				st.setSortDBState();
				st.getCurrentState().changeEntity(TabbedView.activePanel.getEntity());
				st.getCurrentState().changePanel();
				desktopView.setPanelComponents(st.getCurrentState());
				desktopView.repaint();
			}
		});
		
		this.doAction = new JButton("Submit");
		this.doAction.setIcon(new ImageIcon(Constants.SUBMIT_ICON));
		this.doAction.addActionListener(e -> {
			st.getCurrentState().submit();
		});
		
		dbShowRelations = new JButton("Show relations");
		dbShowRelations.setIcon(new ImageIcon(Constants.SHOW_RELATIONS_ICON));
		dbShowRelations.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				st.setRelationsState();
				st.getCurrentState().changePanel();
				desktopView.setPanelComponents(st.getCurrentState());
				desktopView.repaint();
					
				Entity activeEntity = TabbedView.getActivePanel().getEntity();
				ArrayList<Relation> relations = activeEntity.getRelations();
				int selectedIndex = TabbedView.getActivePanel().getTable().getSelectedRow();
//				Record currentRecord = activeEntity.readRecordFrom(selectedIndex, true);
				Record currentRecord = new Record(entity);
				
				for (int i = 0; i < TabbedView.activePanel.getTable().getColumnCount(); i++) {
					Object obj = TabbedView.activePanel.getTable().getModel().getValueAt(selectedIndex, i);
					currentRecord.addObject(obj);
				}

				for(int i = 0; i < relations.size(); i++){ // Prolazim kroz sve svoje relacije					
					Entity referencedEntity = relations.get(i).getReferencedEntity();
					ArrayList<Attribute> referringAttributes = relations.get(i).getReferringAttributes();
					ArrayList<Attribute> referencedAttributes = relations.get(i).getReferencedAttributes();
					ArrayList<Integer> referringIndex = new ArrayList<>();
					ArrayList<Integer> referencedIndex = new ArrayList<>();
					ArrayList<Record> recordsToShow = new ArrayList<>();
					
					for(int j = 0; j < referringAttributes.size(); j++){ // Za svaku relaciju prolazim kroz listu Atributa
																					// po kojima je u vezi sa drugim entitetom
						
						for(int k = 0; k < activeEntity.getChildCount(); k++){ // Prolazim kroz sve svoje atribute i gledam koji imaju isto
																			// ime kao reffering attribute
							
							if(((Attribute)activeEntity.getChildAt(k)).getName().equals(referringAttributes.get(j).getName())){
								
								referringIndex.add(k);
								//System.out.println("1. Atribut " + ((Attribute)activeEntity.getChildAt(k)).getName() + " Indeks " + j);
							}
						}
					}
					
					for(int h = 0; h < referencedAttributes.size() ; h++ ){
						
						for(int p = 0; p < referencedEntity.getChildCount(); p++){
							
							if(((Attribute)referencedEntity.getChildAt(p)).getName().equals(referencedAttributes.get(h).getName())){
								
								referencedIndex.add(p);
								//System.out.println("2. Atribut " + ((Attribute)referencedEntity.getChildAt(p)).getName() + " Indeks " + p);
							}
							
						}						
					}
					
					// TODO
//					referencedEntity.getFileData();
					
//					for(int j = 0; j < TabbedView.activePanel.getTable().getModel().getRowCount(); j++){
////						Record newRecord = referencedEntity.readRecordFrom(j, false);
//						Record newRecord = new Record(entity);
//						boolean isOkRecord = true;
//
//						for (int ind = 0; ind < TabbedView.activePanel.getTable().getColumnCount(); ind++) {
//							Object obj = TabbedView.activePanel.getTable().getModel().getValueAt(j, ind);
//							newRecord.addObject(obj);
//						}
//						
//						for(int m = 0; m < referencedIndex.size(); m++){
//							
//							if(!newRecord.getPodaci().get(referencedIndex.get(m)).equals(currentRecord.getPodaci().get(referringIndex.get(m)))){
//								isOkRecord = false;
//							}
//						}			
//						
//						if(isOkRecord){
//							recordsToShow.add(newRecord);
//						}		
//					}
					
					((RelationsState)st.getCurrentState()).addNewDBTab(referencedEntity, referringAttributes, referencedAttributes);
					desktopView.repaint();
				}
				
			}
		});
		
		dbToolbar.add(dbFetch);
		dbToolbar.addSeparator();
		dbToolbar.add(dbAdd);
		dbToolbar.add(dbUpdate);
		dbToolbar.add(dbDelete);
		dbToolbar.addSeparator();
		dbToolbar.add(dbFilter);
		dbToolbar.add(dbSort);
		dbToolbar.add(dbShowRelations);		
		dbToolbar.addSeparator();
		dbToolbar.add(doAction);
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
	
	private void countUp(){
		blocksFetched.setText(String.valueOf(Integer.parseInt(blocksFetched.getText())+1));
	}
	
	public int getBlockFactor(){
		return (int)numberModel.getValue();
	}
	
	public void setRecordSize(){
		recordSize.setText(TabbedView.activePanel.getBlockSize()+" B");
	}
	
	public void setFileSize(){
		fileSize.setText(TabbedView.activePanel.getEntity().getFileSize() + " KB");
	}
	
	public void setRecordNumber(){
		recordNumber.setText(String.valueOf(TabbedView.activePanel.getRecordCount()));
		recordNumber.repaint();
	}

	public void setFetchNumber() {
		blocksFetched.setText(String.valueOf(TabbedView.activePanel.getFetchNumber()));
	}
	
	@Override
	public String toString() {
		return "" + getBlockFactor();
	}

	public JButton getDbFilter() {
		return dbFilter;
	}

	public void setDbFilter(JButton dbFilter) {
		this.dbFilter = dbFilter;
	}

	public JButton getDbSort() {
		return dbSort;
	}

	public void setDbSort(JButton dbSort) {
		this.dbSort = dbSort;
	}

	public JButton getDbAdd() {
		return dbAdd;
	}

	public void setDbAdd(JButton dbAdd) {
		this.dbAdd = dbAdd;
	}

	public JButton getDbUpdate() {
		return dbUpdate;
	}

	public void setDbUpdate(JButton dbUpdate) {
		this.dbUpdate = dbUpdate;
	}

	public JToolBar getDbToolbar() {
		return dbToolbar;
	}

	public JButton getNextBlock() {
		return nextBlock;
	}

	public void setNextBlock(JButton nextBlock) {
		this.nextBlock = nextBlock;
	}

	public JButton getSearch() {
		return search;
	}

	public void setSearch(JButton search) {
		this.search = search;
	}

	public JButton getSort() {
		return sort;
	}

	public void setSort(JButton sort) {
		this.sort = sort;
	}

	public JButton getAdd() {
		return add;
	}

	public void setAdd(JButton add) {
		this.add = add;
	}

	public JButton getUpdate() {
		return update;
	}

	public void setUpdate(JButton update) {
		this.update = update;
	}

	public JButton getDelete() {
		return delete;
	}

	public void setDelete(JButton delete) {
		this.delete = delete;
	}

	public JTextField getBlocksFetched() {
		return blocksFetched;
	}

	public void setBlocksFetched(JTextField blocksFetched) {
		this.blocksFetched = blocksFetched;
	}

	public JTextField getFileSize() {
		return fileSize;
	}

	public void setFileSize(JTextField fileSize) {
		this.fileSize = fileSize;
	}

	public JTextField getRecordSize() {
		return recordSize;
	}

	public void setRecordSize(JTextField recordSize) {
		this.recordSize = recordSize;
	}

	public JTextField getRecordNumber() {
		return recordNumber;
	}

	public void setRecordNumber(JTextField recordNumber) {
		this.recordNumber = recordNumber;
	}

	public JTextField getBlockNumber() {
		return blockNumber;
	}

	public void setBlockNumber(JTextField blockNumber) {
		this.blockNumber = blockNumber;
	}

	public JToolBar getFileToolbar() {
		return fileToolbar;
	}

	public void setFileToolbar(JToolBar fileToolbar) {
		this.fileToolbar = fileToolbar;
	}

	public Entity getEntity() {
		return entity;
	}

	public void setEntity(Entity entity) {
		this.entity = entity;
	}

	public void setBlockFactor(JSpinner blockFactor) {
		this.blockFactor = blockFactor;
	}

	public StateManager getStateManager() {
		return st;
	}

	public JButton getPrevBlock() {
		return prevBlock;
	}

	public void setPrevBlock(JButton prevBlock) {
		this.prevBlock = prevBlock;
	}

	public JButton getShowRelations() {
		return showRelations;
	}

	public void setShowRelations(JButton showRelations) {
		this.showRelations = showRelations;
	}

	public SpinnerModel getNumberModel() {
		return numberModel;
	}

	public void setNumberModel(SpinnerModel numberModel) {
		this.numberModel = numberModel;
	}

	public StateManager getSt() {
		return st;
	}

	public void setSt(StateManager st) {
		this.st = st;
	}

	public DesktopView getDesktopView() {
		return desktopView;
	}

	public void setDesktopView(DesktopView desktopView) {
		this.desktopView = desktopView;
	}
	
	

}