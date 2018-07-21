package view.tree;

import java.awt.Component;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JTree;
import javax.swing.tree.DefaultTreeCellRenderer;

import models.Attribute;
import models.Entity;
import models.InformationResource;
import models.Package;
import models.Warehouse;

@SuppressWarnings("serial")
public class TreeCellRendered extends DefaultTreeCellRenderer {

	@Override
	public Component getTreeCellRendererComponent(JTree tree, Object value, boolean selected, boolean expanded,
			boolean leaf, int row, boolean hasFocus) {

		super.getTreeCellRendererComponent(tree, value, selected, expanded, leaf, row, hasFocus);

		String iconPath = null;
		Icon icon = null;

		if (value instanceof Warehouse)
			iconPath = "/treeImages/warehouse.png";
		else if (value instanceof InformationResource)
			iconPath = "/treeImages/information-resource.png";
		else if (value instanceof Package)
			iconPath = "/treeImages/package.png";
		else if (value instanceof Entity)
			iconPath = "/treeImages/entity.png";
		else if (value instanceof Attribute)
			iconPath = "/treeImages/attribute.png";

		try {
			if (iconPath != null) {
				icon = new ImageIcon(this.getClass().getResource(iconPath));
				this.setIcon(icon);
			}
		} 
		catch (Exception e) {
			e.printStackTrace();
		}

		this.setText(value.toString());

		return this;
	}
}