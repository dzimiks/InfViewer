/***********************************************************************
 * Module:  TreeController.java
 * Author:  Mihailo
 * Purpose: Defines the Class TreeController
 ***********************************************************************/

package controllers;

import command.Invoker;
import models.Entity;
import models.tree.Node;
import view.MainView;
import view.tree.TreeView;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.*;

import javax.swing.SwingUtilities;

public class TreeController {
	
   public Invoker invoker;
   
   public TreeView view;
   
   public TreeController(TreeView view){
	   this.view = view;
	   this.view.addMouseListener(new DoubleClickListener());
   }

   class DoubleClickListener implements MouseListener {
		@Override
		public void mouseClicked(MouseEvent e) {
			if (SwingUtilities.isLeftMouseButton(e) && e.getClickCount() == 2) {
				Node node = (Node) view.getSelectedNode();
				if (node != null) {
					if (node instanceof Entity) {
						MainView.getInstance().doTableOpen((Entity) node);
					}
				}
			}
		}

		@Override
		public void mouseEntered(MouseEvent arg0) {
		}

		@Override
		public void mouseExited(MouseEvent arg0) {
		}

		@Override
		public void mousePressed(MouseEvent arg0) {
		}

		@Override
		public void mouseReleased(MouseEvent arg0) {
		}
	}
}