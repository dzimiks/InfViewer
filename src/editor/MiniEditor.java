package editor;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

import javax.swing.*;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import main.Main;
import metaschema.Serializer;
import models.*;
import models.tree.Node;

public class MiniEditor extends JDialog {
	JTextArea ta;
	String saved = "";
	InputStream in;
	// Object o is used as identifier as which type oh schema is should open

	// Node on will be node where to add savable element
	// If we add attribute on will be entity to add it
	public MiniEditor(Object o, Node on, boolean changeExisting, Editor ed) {

		// TODO Auto-generated constructor stub
		ta = new JTextArea();
		this.setSize(new Dimension(300, 400));
		this.setLayout(new BorderLayout());
		JScrollPane scroll = new JScrollPane(ta, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
				JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);

		this.add(ta, BorderLayout.CENTER);

		// Getting template schemas
		in = getTemplateSchemas(o);

		// Setting text and initialising save string
		setTextIntoTextArea(changeExisting, o);

		JButton btnSave = new JButton("Sava");
		this.add(btnSave, BorderLayout.SOUTH);

		// Sava za mini editor deserijalizuje objekat i dodaje ga u
		btnSave.addActionListener(e -> {

			// First check if text is JSON file
			JsonObject obj = validate(o);
			if (obj == null)
				return;

			if (o instanceof Attribute) {
				// Atribut
				if (changeExisting) {
					// Setting parameters for current attribute
					Attribute a = Serializer.deserializeAttribute(obj);

					// Just swaping fields
					((Attribute) o).setName(a.getName());
//					((Attribute) o).setType(a.getType());
					((Attribute) o).setLength(a.getLength());
					((Attribute) o).setPrimaryKey(a.isPrimaryKey());
				} else {
					on.addChild(Serializer.deserializeAttribute(obj));
				}

			} else if (o instanceof Entity) {
				// Entity
				if (changeExisting) {
					// Just swaping names
					((Entity) o).setName(Serializer.deserializeEntity(obj).getName());
				} else {
					// Adding new Entity
					Entity newEntity = Serializer.deserializeEntity(obj);
					on.addChild(newEntity);

				}

			} else {
				// Relations
				if (changeExisting) {
					Relation r = (Relation) o;
					Relation newRelation = Serializer.deserializeRelation(obj);
					r.setReferencedAttributes(newRelation.getReferencedAttributes());
					r.setReferringAttributes(newRelation.getReferencedAttributes());
					r.setReferencedEntity(newRelation.getReferencedEntity());
				} else {
					Relation newRelation = Serializer.deserializeRelation(obj);
					((Entity) on).addRelation(newRelation);
				}

			}
			dispose();
			ed.update();

		});

	}

	// Setting text and String for rescue
	private void setTextIntoTextArea(boolean changeExisting, Object o) {
		// TODO Auto-generated method stub
		if (changeExisting) {
			if (o instanceof Entity) {
				// Entity
				Gson gson = new GsonBuilder().setPrettyPrinting().create();
				JsonObject obj = Serializer.serializeEntity((Entity) o);
				String ans = gson.toJson(obj);
				saved = ans;
				ta.append(ans);

			} else if (o instanceof Attribute) {
				// Attribute
				Gson gson = new GsonBuilder().setPrettyPrinting().create();
				JsonObject obj = Serializer.serializeAttribute((Attribute) o);
				String ans = gson.toJson(obj);
				saved = ans;
				ta.append(ans);

			} else {
				// Relation
				Gson gson = new GsonBuilder().setPrettyPrinting().create();
				JsonObject obj = Serializer.serializeRelation((Relation) o);
				String ans = gson.toJson(obj);
				ta.append(ans);

			}
		} else {

			try {
				BufferedReader br = new BufferedReader(new InputStreamReader(in, "UTF-8"));
				String line;
				while ((line = br.readLine()) != null) {
					ta.append(line + "\n");
					saved += line + "\n";
				}

			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}

	// Gets template schemas
	public InputStream getTemplateSchemas(Object o) {
		if (o instanceof Attribute) {
			// Atribut
			return Main.class.getResourceAsStream("/schema/attribute_template.json");

		} else if (o instanceof Entity) {
			// Entity
			return Main.class.getResourceAsStream("/schema/entity_template.json");

		} else {
			// Relations
			return Main.class.getResourceAsStream("/schema/relation_template.json");
		}
	}

	// Restarts miniEditor to saved string
	public void restart() {
		ta.setText(saved);
	}

	// Validates object o to see if its Json object
	public JsonObject validate(Object o) {
		JsonParser parser;
		JsonObject obj;
		try {
			parser = new JsonParser();
			obj = parser.parse(ta.getText()).getAsJsonObject();

			if (o instanceof Entity) {

				// Validacija za name
				try {
					String name = obj.get("name").getAsString();
				} catch (Exception e) {
					JOptionPane.showMessageDialog(null,
							"Nesto nije u redu sa poljem name u vasem entitetu. Polje name je obavezno!");
					restart();
				}

				// Validacija za atribute
				try {
					JsonArray attrs = obj.get("attributes").getAsJsonArray();
				} catch (Exception e) {
					JOptionPane.showMessageDialog(null,
							"Nesto nije u redu sa poljem attributes u vasem entitetu. Polje attributes je obavezno!");
					restart();
				}
				
				//Validacija za relacije
				try{
					JsonArray relations = obj.get("relations").getAsJsonArray();
				}catch (Exception e) {
					JOptionPane.showMessageDialog(null,
							"Nesto nije u redu sa poljem relations u vasem entitetu. Polje relations je obavezno!");
					restart();
				}
			} else if (o instanceof Attribute) {

				// Validacija za name
				try {
					String name = obj.get("name").getAsString();
				} catch (Exception e) {
					JOptionPane.showMessageDialog(null,
							"Nesto nije u redu sa poljem name u vasem atributu. Polje name je obavezno!");
					restart();
				}

				// Validacija za type
				try {
					String type = obj.get("type").getAsString();
				} catch (Exception e) {
					JOptionPane.showMessageDialog(null,
							"Nesto nije u redu sa poljem type u vasem atributu. Polje type je obavezno!");
					restart();
				}

				// Validacija za length
				try {
					int length = obj.get("length").getAsInt();
				} catch (Exception e) {
					JOptionPane.showMessageDialog(null,
							"Nesto nije u redu sa poljem length u vasem atributu. Polje length je obavezno!");
					restart();
				}

				// Validacija za primaryKey
				try {
					boolean primaryKey = obj.get("primaryKey").getAsBoolean();
				} catch (Exception e) {
					JOptionPane.showMessageDialog(null,
							"Nesto nije u redu sa poljem primaryKey u vasem atributu. Polje primaryKey je obavezno!");
					restart();
				}
			} else {

				// Validacija za referringAttributes
				try {
					JsonArray e = obj.get("referringAttributes").getAsJsonArray();
				} catch (Exception e) {
					JOptionPane.showMessageDialog(null,
							"Nesto nije u redu sa poljem referringAttributes u vasoj relaciji. Polje referringAttributes je obavezno!");
					restart();
				}

				// Validacija za referencedAttributes
				try {
					JsonArray e = obj.get("referencedAttributes").getAsJsonArray();
				} catch (Exception e) {
					JOptionPane.showMessageDialog(null,
							"Nesto nije uredu sa poljem referencedAttributes u vasem atributu. Polje refferencedAttributes je obavezno!");
					restart();
				}

			}

		} catch (Exception exception) {
			if (exception instanceof NullPointerException) {
				JOptionPane.showMessageDialog(null, "NESTOO FALI NEMOJ SE PRAVITI PAMETAN");
			} else {
				JOptionPane.showMessageDialog(null, "Niste ispostovali sintaksu JSON fajla");
			}
			restart();
			return null;
		}
		return obj;
	}

}
