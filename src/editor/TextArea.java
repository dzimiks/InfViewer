package editor;

import java.io.*;
import java.lang.reflect.Type;

import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import main.Main;
import metaschema.Serializer;
import models.InformationResource;

@SuppressWarnings("serial")
public class TextArea extends JTextArea {

	// TODO getting file path through constructor and showing that file
	public TextArea(InformationResource res) {

		try {
			init(res);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public void init(InformationResource res) throws Exception {
		// File that i want
		this.setText("");
		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		JsonObject obj = Serializer.serializeInfResource(res);
		String ans = gson.toJson(obj);

		/**
		 * Nepotrebno
		 *
		 * //Upisuje u fajl Writer writer = null; try { writer = new
		 * BufferedWriter(new OutputStreamWriter( new
		 * FileOutputStream(res.getName()+"Copy.json"), "utf-8"));
		 * writer.write(ans); } catch (IOException ex) { // Report } finally {
		 * try {writer.close();} catch (Exception ex) {/*ignore} }
		 */

		this.append(ans);
		setEditable(false);
	}
}
