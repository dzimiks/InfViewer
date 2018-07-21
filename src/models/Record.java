
package models;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

public class Record implements Comparable<Record> {
	
	private ArrayList<Object> podaci = new ArrayList<>();
	private Map<Attribute, Object> attributes;
	private Entity entity;
	
	public Record() {
		this.attributes = new LinkedHashMap<>();
	}
	
	public Record(Entity entity) {
		this.entity = entity;
		this.attributes = new LinkedHashMap<>();
	}
	
	public Record(ArrayList<Object> data){
		this.podaci = data;
	}
	
	public void addObject(Object o){
		podaci.add(o);
	}
	
	public void removeObject(Object o){
		podaci.remove(o);
	}
	
	public void addAttribute(Attribute attribute, Object value) {
		if (!entity.getChildren().contains(attribute))
			return;
		
		if (value != null && attribute.getValueClass() != value.getClass()) 
			return;

		attributes.put(attribute, value);
	}
	
	public Map<Attribute, Object> getAttributes() {
		return attributes;
	}
	
	public ArrayList<Object> getPodaci(){
		return this.podaci;
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		String ans = "";
		
		for (Object o : podaci) {
			sb.append(String.valueOf(o) + "|");
		}
		
		ans = sb.toString();
//		ans = ans.replaceAll("\\s+", "");
		return ans.substring(0, ans.length() - 1);
	}

	@Override
	public int compareTo(Record arg0) {
		// TODO Auto-generated method stub
		return 0;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj == null)
			return false;
		
		if (!(obj instanceof Record))
			return false;
		
		Record record = (Record) obj;

		if (record.getPodaci().size() != podaci.size())
			return false;
		
		if (record.getAttributes().size() != podaci.size())
			return false;
		
		for (int i = 0; i < entity.getChildCount(); i++) {
			Attribute attr = (Attribute) entity.getChildAt(i);
			Object valMe = attributes.get(attr);
			Object valOther = record.getAttributes().get(attr);
			
			System.out.println(valMe + " => " + valOther);
			
			if (!valMe.equals(valOther))
				return false;
		}
		
		return true;
	}
}
