package models.datatypes;

import java.io.Serializable;

@SuppressWarnings("serial")
public class CharType implements Comparable<CharType>, Serializable {

	private String string;
	private int length;

	public CharType(int length, String data) {
		this.length = length;
		this.string = data;
	}

	public String get() {
		return string;
	}

	public void set(String s) throws Exception {
		if (s.length() != length) {
			throw new Exception("String of length " + s.length() + 
								" cannot be assigned to char (" + length + ")");
		}

		this.string = s;
	}

	public String getString() {
		return string;
	}
	
	@Override
	public String toString() {
		return string;
	}

	@Override
	public int compareTo(CharType type) {
		return string.compareTo(type.string);
	}

	@Override
	public boolean equals(Object obj) {
		if(!(obj instanceof CharType)) 
			return false;
		
		CharType charType = (CharType) obj;
		return this.string.equals(charType.getString());
	}
}
