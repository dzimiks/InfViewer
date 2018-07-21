/***********************************************************************
 * Module:  MenuBarView.java
 * Author:  Mihailo
 * Purpose: Defines the Class MenuBarView
 ***********************************************************************/

package view;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.util.*;

import javax.swing.ImageIcon;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.KeyStroke;

import constants.Constants;
import controllers.MenuBarController;
import javafx.scene.control.MenuItem;
import view.tree.TreeView;

public class MenuBarView extends JMenuBar{
	private MenuBarController menuBarController;
	private TreeView treeView;
	public MenuBarView(TreeView treeView) {
		this.treeView = treeView;
		menuBarController = new MenuBarController(this, treeView);
		JMenu file = new JMenu("File");
		JMenuItem importResource = new JMenuItem("Import new resource");
		importResource.setMnemonic(KeyEvent.VK_I);
		importResource.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_I, ActionEvent.CTRL_MASK));
		importResource.setIcon(new ImageIcon(Constants.IMPORT_ICON));
		importResource.addActionListener(menuBarController.getImportResourceListener());
		/**JMenuItem addMeta = new JMenuItem("Add new meta scheme");
		addMeta.setMnemonic(KeyEvent.VK_N);
		addMeta.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N, ActionEvent.CTRL_MASK));
		addMeta.setIcon(new ImageIcon(Constants.ADD_ICON));**/
		JMenuItem editMeta = new JMenuItem("Edit meta schema");
		editMeta.setMnemonic(KeyEvent.VK_E);
		editMeta.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_E, ActionEvent.CTRL_MASK));
		editMeta.setIcon(new ImageIcon(Constants.EDIT_ICON));
		editMeta.addActionListener(menuBarController.getEditMetaListener());
		
		file.setMnemonic(KeyEvent.VK_F);
		file.add(importResource);
		file.addSeparator();
		//file.add(addMeta);
		file.add(editMeta);
		
		JMenu help = new JMenu("Help");
		JMenuItem about = new JMenuItem("About");
		about.addActionListener(menuBarController.getAboutFrameListener());
		about.setMnemonic(KeyEvent.VK_A);
		about.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_A, ActionEvent.CTRL_MASK));
		
		help.setMnemonic(KeyEvent.VK_H);
		help.add(about);
		
		this.add(file);
		this.add(help);
	}
}