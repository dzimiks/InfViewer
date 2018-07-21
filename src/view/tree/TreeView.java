package view.tree;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.DefaultTreeSelectionModel;
import javax.swing.tree.TreeSelectionModel;

import controllers.TreeController;
import models.Entity;
import models.Warehouse;
import models.tree.Node;
import view.MainView;

@SuppressWarnings("serial")
public class TreeView extends JPanel {

	private Node root;
	private InfViewerTree tree;
	private TreeController controller;
	private DefaultTreeModel treeModel;
	
	public TreeView() {
		this.controller = new TreeController(this);
		initialize();
		
	}

	private void initialize() {
		this.root = Warehouse.getInstance();
		this.setPreferredSize(new Dimension(250, 1000));

		treeModel = new DefaultTreeModel(root);
		treeModel.setRoot(root);

		this.tree = new InfViewerTree();
		this.tree.setModel(treeModel);

		TreeCellRendered tcr = new TreeCellRendered();
		this.tree.setCellRenderer(tcr);
		this.tree.setShowsRootHandles(true);

		TreeSelectionModel selectionModel = new DefaultTreeSelectionModel();
		selectionModel.setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
		this.tree.setSelectionModel(selectionModel);

		this.setLayout(new BorderLayout());
		this.setMinimumSize(new Dimension(1000, 1000));

		int height = 1000;
		JScrollPane treeScrollPane = new JScrollPane(tree);
		treeScrollPane.setSize(new Dimension(50, height));
		treeScrollPane.setMaximumSize(new Dimension(50, height));
		treeScrollPane.setMinimumSize(new Dimension(50, height));
		treeScrollPane.setPreferredSize(new Dimension(50, height));
		treeScrollPane.setAutoscrolls(true);

		this.add(treeScrollPane, BorderLayout.NORTH);
	}

	public InfViewerTree getTree() {
		return tree;
	}

	public Node getSelectedNode() {
		Object o = tree.getLastSelectedPathComponent();

		if (o instanceof Node)
			return (Node) o;

		return null;
	}
	
	public void refresh() {
		treeModel.reload();
	}
}