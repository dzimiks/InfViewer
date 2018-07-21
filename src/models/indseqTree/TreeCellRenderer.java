package models.indseqTree;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTree;
import javax.swing.tree.DefaultTreeCellRenderer;



public class TreeCellRenderer extends DefaultTreeCellRenderer{
	public void TreeCellRendered() {
		//setOpaque(true);
	
		// TODO Auto-generated constructor stub
	}

	  public Component getTreeCellRendererComponent(
              JTree tree,
              Object value,
              boolean sel,
              boolean expanded,
              boolean leaf,
              int row,
              boolean hasFocus) {
                  super.getTreeCellRendererComponent(tree, value, sel,expanded, leaf, row,hasFocus);
                  
                  NodeIndSeq node =(NodeIndSeq)value;
                  if (node.getChildCount()>0) setIcon(null);
                	  
                  
                  
                  
                  JPanel panel=new JPanel(new FlowLayout(FlowLayout.CENTER));
                  panel.setBackground(Color.WHITE);
                  panel.setMinimumSize(new Dimension(240,35));

                  for (int i=0;i<node.getData().size();i++){
                	   ElementPane elementPane=new ElementPane(node.getData().get(i));
                	   panel.add(elementPane);
					}
                  if (sel){
                	  panel.setBackground(Color.BLUE);
                  }
                  
                  return panel;

}
	  
	  
		private class ElementPane extends JPanel{
			public ElementPane (NodeElement nodeElement){
				//setPreferredSize(new Dimension(120,25));
				setBorder(BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.LOWERED));
				
				
				for (int i=0;i<nodeElement.getKeyValue().size();i++){
					JLabel lblKeyvalue=new JLabel(String.valueOf(nodeElement.getKeyValue().get(i).getValue()));
					add(lblKeyvalue);	
					if (i<nodeElement.getKeyValue().size()-1){
						add(new JLabel("|"));
					}
					
				}
				
				JLabel lblAddress=new JLabel(String.valueOf(nodeElement.getBlockAddress()));
				
				//lblAddress.setMinimumSize(new Dimension(60,20));
				lblAddress.setBackground(Color.YELLOW);
				lblAddress.setBorder(BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.LOWERED));
				lblAddress.setHorizontalAlignment(CENTER);
				add(lblAddress);				
			}
		}

}
