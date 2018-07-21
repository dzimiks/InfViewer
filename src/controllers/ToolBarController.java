/***********************************************************************
 * Module:  ToolBarController.java
 * Author:  Mihailo
 * Purpose: Defines the Class ToolBarController
 ***********************************************************************/

package controllers;

import command.Invoker;
import editor.Editor;
import main.Main;
import metaschema.Deserializer;
import metaschema.Serializer;
import models.InformationResource;
import models.Warehouse;
import models.tree.Node;
import view.MainView;
import view.tree.TreeView;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.*;

import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileNameExtensionFilter;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class ToolBarController {
	public Invoker invoker;
	private TreeView treeView;

	public ToolBarController(TreeView treeView) {
		this.treeView = treeView;
	}

	public ActionListener getEditMetaListener() {
		return new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				Object node = treeView.getSelectedNode();
				if ((Node) node instanceof InformationResource) {
					Editor editor = new Editor(new InformationResource((InformationResource) node), treeView);
					MainView.getInstance().getDesktopView().getTabbedView().myRemoveAll();
					MainView.getInstance().repaint();
					editor.setVisible(true);
				} else {
					JOptionPane.showMessageDialog(new JDialog(),
							"You have to select informational" + " resource you want to edit");
				}
			}
		};
	}
   public ActionListener getImportResourceListener(){
	   return new ActionListener() {
		
		@Override
		public void actionPerformed(ActionEvent e) {
			JFrame parentFrame = new JFrame("Choose informational resource");
			JFileChooser fileChooser = new JFileChooser();
			fileChooser.setFileFilter(new FileNameExtensionFilter("JSON file", "json"));
			int userSelection = fileChooser.showOpenDialog(parentFrame);
			if (userSelection == JFileChooser.APPROVE_OPTION){
				File jsonFile = fileChooser.getSelectedFile();
				try {
					//InputStream in = Main.class.getResourceAsStream("/schema/ir_template.json");
					BufferedReader br = new BufferedReader(new FileReader(jsonFile));
					String fileString = "";
					String line;
					
					while ((line = br.readLine()) != null)
						fileString += line;
					
					JsonParser parser = new JsonParser();
					JsonObject obj = parser.parse(fileString).getAsJsonObject();
					Deserializer deserialize = new Deserializer(fileString, Warehouse.getInstance());
					InformationResource infResource = deserialize.deserializeInfResource(obj);
					Warehouse.getInstance().addChild((InformationResource) infResource);
					treeView.refresh();
				}
				catch (Exception ex) {
					ex.printStackTrace();
				}
			}
		}
	};
   }
}