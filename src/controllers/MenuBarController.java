/***********************************************************************
 * Module:  MenuBarController.java
 * Author:  Mihailo
 * Purpose: Defines the Class MenuBarController
 ***********************************************************************/

package controllers;

import command.Invoker;
import editor.Editor;
import metaschema.Serializer;
import models.InformationResource;
import models.Warehouse;
import models.tree.Node;
import view.AboutFrame;
import view.MainView;
import view.MenuBarView;
import view.tree.TreeView;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.*;

import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileNameExtensionFilter;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class MenuBarController {
   public Invoker invoker;
   
   private AboutFrame aboutFrame;
   private MenuBarView menuBar;
   private TreeView treeView;
   
   public MenuBarController(MenuBarView menuBar, TreeView treeView) {
	   this.treeView = treeView;
	   this.menuBar = menuBar;
	   this.aboutFrame = AboutFrame.getInstance();
   }

   public ActionListener getAboutFrameListener(){
	   return new ActionListener() {
		
		@Override
		public void actionPerformed(ActionEvent e) {
			aboutFrame.setVisible(true);
			
		}
	};
   }
   
   public ActionListener getEditMetaListener(){
	   return new ActionListener() {
		
		@Override
		public void actionPerformed(ActionEvent e) {
			try{
					Object node = treeView.getSelectedNode();
					if((Node)node instanceof InformationResource){
						Editor editor = new Editor(new InformationResource((InformationResource)node), treeView);
						MainView.getInstance().getDesktopView().getTabbedView().myRemoveAll();
//						MainView.getInstance().repaint();
						editor.setVisible(true);
					}else{
						JOptionPane.showMessageDialog(new JDialog(), "You have to select informational"
								+ " resource you want to edit");
					}
				}catch(NullPointerException ex){
					JOptionPane.showMessageDialog(new JDialog(), "You have to select informational"
							+ " resource you want to edit");
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
					BufferedReader br = new BufferedReader(new FileReader(jsonFile));
					String fileString = "";
					String line;
					
					while ((line = br.readLine()) != null)
						fileString += line;
					
					JsonParser parser = new JsonParser();
					JsonObject obj = parser.parse(fileString).getAsJsonObject();
					InformationResource infResource = Serializer.deserializeInfResource(obj);
					Warehouse.getInstance().addChild((InformationResource) infResource);
				}
				catch (Exception ex) {
					ex.printStackTrace();
				}
			}
		}
	};
   }
}