/***********************************************************************
 * Module:  TableModel.java
 * Author:  Mihailo
 * Purpose: Defines the Class TableModel
 ***********************************************************************/

package models;

import java.util.*;

import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableModel;

@SuppressWarnings("serial")
/**
 * USLESS
 * @author Mihailo
 *
 */
public class TableModel extends DefaultTableModel {
	
	public final Entity entity;
	public String[] columnNames ;

	public TableModel(Entity entity) {
		this.entity = entity;
		columnNames  = new String[entity.getChildCount()];

		for (int i = 0; i < entity.getChildCount(); i++) {
			columnNames [i] = entity.getChildren().get(i).getName();
		}
	}


	@Override
	public int getColumnCount() {
		return columnNames.length;
	}

}