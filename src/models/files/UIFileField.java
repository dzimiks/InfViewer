package models.files;

public class UIFileField {

	public static final String TYPE_VARCHAR = "TYPE_VARCHAR";
	public static final String TYPE_CHAR = "TYPE_CHAR";
	public static final String TYPE_INTEGER = "TYPE_INTEGER";
	public static final String TYPE_NUMERIC = "TYPE_NUMERIC";
	public static final String TYPE_DECIMAL = "TYPE_DECIMAL";
	public static final String TYPE_DATETIME = "TYPE_DATETIME";

	private String fieldName;
	private String fieldType;
	private int fieldLength;
	private boolean fieldPK;

	public UIFileField(String fieldName, String fieldType, int fieldLength, boolean fieldPK) {
		super();
		this.fieldName = fieldName;
		this.fieldType = fieldType;
		this.fieldLength = fieldLength;
		this.fieldPK = fieldPK;
	}

	public int getFieldLength() {
		return fieldLength;
	}

	public void setFieldLength(int fieldLength) {
		this.fieldLength = fieldLength;
	}

	public String getFieldName() {
		return fieldName;
	}

	public void setFieldName(String fieldName) {
		this.fieldName = fieldName;
	}

	public boolean isFieldPK() {
		return fieldPK;
	}

	public void setFieldPK(boolean fieldPK) {
		this.fieldPK = fieldPK;
	}

	public String getFieldType() {
		return fieldType;
	}

	public void setFieldType(String fieldType) {
		this.fieldType = fieldType;
	}

	public String toString() {
		return fieldName;
	}
}
