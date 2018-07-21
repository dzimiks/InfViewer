package models;

import java.util.ArrayList;

public class FilterParams {
	private String query;
	private ArrayList<Object> objects;

	public FilterParams() {
		super();
		this.query = "";
		this.objects = new ArrayList<>();
	}

	public FilterParams(String query, ArrayList<Object> objects) {
		super();
		this.query = query;
		this.objects = objects;
	}

	public String getQuery() {
		return query;
	}

	public void setQuery(String query) {
		this.query = query;
	}

	public ArrayList<Object> getObjects() {
		return objects;
	}

	public void setObjects(ArrayList<Object> objects) {
		this.objects = objects;
	}

	public void addObject(Object object) {
		objects.add(object);
	}
}
