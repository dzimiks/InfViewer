package view;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.MenuBar;
import java.io.File;
import java.io.IOException;
import java.util.*;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.SpringLayout.Constraints;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;

import models.Entity;
import observer.NotificationObserver;
import view.tree.TreeView;

@SuppressWarnings("serial")
public class MainView extends JFrame implements observer.MainObserver {

	private ToolbarView toolbar;
	private MenuBarView menuBar;
	private TreeView treeView;
	private DesktopView desktopView;
	private static MainView instance;

	private MainView() {
		initialize();
	}

	private void initialize() {
		treeView = new TreeView();
		setLayout(new BorderLayout());
		setTitle("InfViewer");
		setSize(new Dimension(1200, 700));
		menuBar = new MenuBarView(treeView);
		this.setJMenuBar(menuBar);
		toolbar = new ToolbarView(treeView);
		this.add(toolbar, BorderLayout.PAGE_START);
		this.setLocationRelativeTo(null);
		desktopView = new DesktopView(treeView);
		JScrollPane scrollPane = new JScrollPane(treeView);
		scrollPane.setMinimumSize(new Dimension(200, 600));
		scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);

		JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, scrollPane, desktopView);
		splitPane.setDividerLocation(300);

		try {
			this.setIconImage(ImageIO.read(new File("images/favicon.png")));
		} 
		catch (IOException ex) {
			ex.printStackTrace();
		}
		
		try {
			UIManager.setLookAndFeel("com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel");
		} 
		catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		catch (InstantiationException e) {
			e.printStackTrace();
		}
		catch (IllegalAccessException e) {
			e.printStackTrace();
		}
		catch (UnsupportedLookAndFeelException e) {
			e.printStackTrace();
		}

		this.add(splitPane, BorderLayout.CENTER);
		this.setExtendedState(JFrame.MAXIMIZED_BOTH);
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
	}

	public ToolbarView getToolbar() {
		return toolbar;
	}

	public TreeView getTreeView() {
		return treeView;
	}

	public DesktopView getDesktopView() {
		return desktopView;
	}

	public static MainView getInstance() {
		if (instance == null) {
			return new MainView();
		}
		return instance;
	}

	@Override
	public void update(NotificationObserver notification, Object obj) {
		// TODO Auto-generated method stub

	}
	
	public void doTableOpen(Entity entity) {
		desktopView.getTabbedView().addNewTab(entity);
	}

}