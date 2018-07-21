package view;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.JTree;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultTreeModel;

import models.Entity;
import models.Package;
import models.Warehouse;
import models.indseqTree.TreeCellRenderer;
import state.RelationsState;
import state.State;
import state.StateManager;
import testing.EntityTest;
import view.tree.TreeView;

public class DesktopView extends JPanel {

	private TabbedView tabbedView;
	private TreeView treeView;
	private TableToolbarView tableToolbarView;
	private JSplitPane splitPane;
	private JSplitPane indexSplit;
	private JPanel indexPanel;
	
	private JPanel panel;
	private StateManager stateManager;
	
	public DesktopView(TreeView treeView) {
		this.setLayout(new BorderLayout());
		this.tabbedView = new TabbedView();
		this.stateManager = new StateManager(new Entity("Entity"), this);
		
		this.tableToolbarView = new TableToolbarView(null, stateManager, this);
		
		
		this.panel = new JPanel();
		this.panel.setLayout(new BorderLayout());

		indexPanel = new JPanel();
		indexPanel.setLayout(new BorderLayout());
		indexSplit = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, indexPanel, tabbedView);
		indexSplit.setDividerLocation(0);
		
		this.treeView = treeView;
		
		treeView.getTree().addTreeSelectionListener(new TreeSelectionListener() {

			@Override
			public void valueChanged(TreeSelectionEvent e) {
				Object node = e.getPath().getLastPathComponent();
				if (node == null)
					return;
				if (node instanceof Entity) {
					tabbedView.addNewTab((Entity) node);
					updateTab();
				}
				
				panel.removeAll();
				panel.repaint();
				
				if(node instanceof Entity){
					if(((Package)tabbedView.activePanel.getEntity().getParent()).getType().equals("indexSequential")){
						DefaultTreeModel treeModel=new DefaultTreeModel(tabbedView.activePanel.getEntity().makeTree(100).getRootElement());
						JTree indexTree=new JTree(treeModel);
						TreeCellRenderer rendered=new TreeCellRenderer();
						indexTree.setCellRenderer(rendered);
						JScrollPane scTree=new JScrollPane(indexTree);
						indexSplit.setDividerLocation(400);
						indexSplit.setLeftComponent(scTree);
						splitPane.revalidate();
						splitPane.repaint();
					}else{
						indexSplit.setDividerLocation(0);
						indexSplit.setLeftComponent(indexPanel);
						splitPane.revalidate();
						splitPane.repaint();
					}
				}
				
			}
		});
		tabbedView.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				TabbedView selected = (TabbedView) e.getSource();
				int selectedIndex = selected.getSelectedIndex();
				String title = selected.getTitleAt(selectedIndex);

				for (int i = 0; i < tabbedView.getEntities().size(); i++) {
					if (tabbedView.getEntities().get(i).getName().equals(title)) {
						
						//Setujem aktivni panel, da bi mogao da ga dohvatim iz toolbara
						tabbedView.activePanel = tabbedView.getTabelePanelAt(i);
						updateTab();

					}
				}
					// TODO
					tableToolbarView.setEntity(selected.getActivePanel().getEntity());
					tableToolbarView.changeView(selected.getActivePanel().getEntity());
				//}
				panel.removeAll();
				panel.validate();
				panel.repaint();
				if(((Package)tabbedView.activePanel.getEntity().getParent()).getType().equals("indexSequential")){
					DefaultTreeModel treeModel=new DefaultTreeModel(tabbedView.activePanel.getEntity().makeTree(100).getRootElement());
				    JTree indexTree=new JTree(treeModel);
				    TreeCellRenderer rendered=new TreeCellRenderer();
				    indexTree.setCellRenderer(rendered);
					JScrollPane scTree=new JScrollPane(indexTree);
					indexSplit.setDividerLocation(400);
					indexSplit.setLeftComponent(scTree);
					splitPane.revalidate();
					splitPane.repaint();
				}else{
					indexSplit.setDividerLocation(0);
					indexSplit.setLeftComponent(indexPanel);
					splitPane.revalidate();
					splitPane.repaint();
				}
		}});
		
		splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, indexSplit, panel);
		splitPane.setDividerLocation(350);
		
		this.add(tableToolbarView, BorderLayout.NORTH);
		this.add(splitPane, BorderLayout.CENTER);

	}

	public void updateTab() {
		Package pack = (Package) tabbedView.activePanel.getEntity().getParent();
		
		if (pack.getType().equals("sql")) 
			return;
		
		tableToolbarView.setRecordSize();
		tableToolbarView.setFileSize();
		tableToolbarView.setFetchNumber();
		tableToolbarView.setRecordNumber();
	}
	
	public TabbedView getTabbedView() {
		return tabbedView;
	}

	public void setTabbedView(TabbedView tabbedView) {
		this.tabbedView = tabbedView;
	}

	public TableToolbarView getTableToolbarView() {
		return tableToolbarView;
	}

	public void setPanelComponents(State pan){
		panel.removeAll();
		panel.add(pan, BorderLayout.CENTER);
		panel.revalidate();
		panel.repaint();
	}

	public StateManager getStateManager() {
		return stateManager;
	}

	public JSplitPane getSplitPane() {
		return splitPane;
	}

	public void setSplitPane(JSplitPane splitPane) {
		this.splitPane = splitPane;
	}

	public JPanel getPanel() {
		return panel;
	}

	public void setPanel(JPanel panel) {
		this.panel = panel;
	}
	
	
	
	
}
