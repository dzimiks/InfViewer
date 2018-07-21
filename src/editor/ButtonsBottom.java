package editor;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;

import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import metaschema.Serializer;
import models.InformationResource;
import models.Warehouse;
import models.tree.Node;
import view.MainView;
import view.tree.TreeView;

public class ButtonsBottom extends JPanel {

	private JButton btnSave;

	public ButtonsBottom(InformationResource ir, TextArea ta, Editor ed, TreeView tw) {
		btnSave = new JButton("Save");

		/// Cuvati fajl

		this.add(btnSave);

		btnSave.addActionListener(e -> {
			Writer writer = null;
			try {
				writer = new BufferedWriter(
						new OutputStreamWriter(new FileOutputStream(ir.getName() + ".json"), "utf-8"));
				writer.write(ta.getText());
			} catch (IOException ex) {
				// Report
				ex.getStackTrace();
			} finally {
				try {
					writer.close();
				} catch (Exception ex) {
					/* ignore */
				}
				JOptionPane.showMessageDialog(null, "Uspesno ste sacuvali informacioni resurs!");
				ed.setVisible(false);
				for (int i = 0; i < Warehouse.getInstance().getChildCount(); i++) {
					Node n = (Node) Warehouse.getInstance().getChildAt(i);
					if (n.getName().equals(ir.getName())) {
						Warehouse.getInstance().remove(i);
						JsonParser parser = new JsonParser();
						JsonObject obj = parser.parse(ta.getText()).getAsJsonObject();
						InformationResource res = Serializer.deserializeInfResource(obj);
						Warehouse.getInstance().addChild(res);
						break;
					}

				}
			}
			tw.refresh();
			MainView.getInstance().getTreeView().repaint();
			MainView.getInstance().getTreeView().refresh();
		});

	}

}
