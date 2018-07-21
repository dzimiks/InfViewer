package models.datatypes;

import java.io.Serializable;

@SuppressWarnings("serial")
public class VarCharType implements Comparable<VarCharType>, Serializable {

	private String string;
	private int length;

	public VarCharType(int length, String string) {
		this.length = length;
		this.string = string;
	}

	public String get() {
		return string;
	}

	public void set(String s) throws Exception {
		if (s.length() > length) {
			throw new Exception("String of length " + s.length() + 
								" cannot be fit in a varchar (" + length + ")");
		}

		this.string = s.trim();
	}

	public String getString() {
		return string;
	}

	@Override
	public String toString() {
		return string;
	}

	@Override
	public int compareTo(VarCharType type) {
		return string.compareTo(type.string);
	}
	
	@Override
	public boolean equals(Object obj) {
		if(!(obj instanceof VarCharType)) 
			return false;
		
		VarCharType varChar = (VarCharType) obj;
		return this.string.equals(varChar.getString());
	}
}