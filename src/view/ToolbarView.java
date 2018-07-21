/***********************************************************************
 * Module:  ToolbarView.java
 * Author:  Mihailo
 * Purpose: Defines the Class ToolbarView
 ***********************************************************************/

package view;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.*;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JToolBar;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import constants.Constants;
import controllers.ToolBarController;
import main.Main;
import metaschema.Serializer;
import models.InformationResource;
import models.Warehouse;
import report.ReportActionListenerDzimiks;
import report.ReportActionListenerKaca;
import report.ReportActionListenerMihailo;
import report.ReportActionListnerMilos;
import view.tree.TreeView;

public class ToolbarView extends JToolBar{
	private TreeView treeView;
	private ToolBarController toolbarController;
	public ToolbarView(TreeView treeView) {
		this.treeView = treeView;
		toolbarController = new ToolBarController(treeView);
		JButton importResource = new JButton();
		importResource.setToolTipText("Import new resource");
		importResource.setIcon(new ImageIcon(Constants.IMPORT_ICON));
		importResource.addActionListener(toolbarController.getImportResourceListener());
		
		JButton addMeta = new JButton();
		addMeta.setToolTipText("Add new meta scheme");
		addMeta.setIcon(new ImageIcon(Constants.ADD_ICON));
		addMeta.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					InputStream in = Main.class.getResourceAsStream("/schemas/metaschema_files.json");
					BufferedReader br = new BufferedReader(new InputStreamReader(in, "UTF-8"));
					String fileString = "";
					String line;
					
					while ((line = br.readLine()) != null)
						fileString += line;
					
					Warehouse.getInstance().loadWarehouse(fileString);
					treeView.refresh();
				}
				catch (Exception ex) {
					ex.printStackTrace();
				}
			}
		});
		
		JButton editMeta = new JButton();
		editMeta.setToolTipText("Edit metaschema");
		editMeta.setIcon(new ImageIcon(Constants.EDIT_ICON));
		editMeta.addActionListener(toolbarController.getEditMetaListener());
		
		JButton connectToDB = new JButton();
		connectToDB.setToolTipText("Connect to DB");
		connectToDB.setIcon(new ImageIcon(Constants.DATABASE_ICON));
		connectToDB.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					InputStream in = Main.class.getResourceAsStream("/schemas/metaschema_db.json");
					BufferedReader br = new BufferedReader(new InputStreamReader(in, "UTF-8"));
					String fileString = "";
					String line;
					
					while ((line = br.readLine()) != null)
						fileString += line;
					
					Warehouse.getInstance().loadWarehouse(fileString);
					treeView.refresh();
				}
				catch (Exception ex) {
					ex.printStackTrace();
				}
			}
		});
		
		JButton reportButtonVanja = new JButton("R Vanja");
		JButton reportButtonMilos = new JButton("R Milos");
		JButton reportButtonKaca = new JButton("R Kaca");
		JButton reportButtonMihailo = new JButton("R Mihailo");
		
		reportButtonVanja.addActionListener(new ReportActionListenerDzimiks());
		reportButtonKaca.addActionListener(new ReportActionListenerKaca());
		reportButtonMihailo.addActionListener(new ReportActionListenerMihailo());
		reportButtonMilos.addActionListener(new ReportActionListnerMilos());
		
		
		this.add(importResource);
		this.addSeparator();
		this.add(addMeta);
		this.add(editMeta);
		this.add(connectToDB);
		this.add(reportButtonVanja);
		this.add(reportButtonMilos);
		this.add(reportButtonKaca);
		this.add(reportButtonMihailo);
		
		this.setFloatable(false);
	}
}