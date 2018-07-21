package models.datatypes;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

@SuppressWarnings("serial")
public class DateType implements Comparable<DateType>, Serializable {

	private Date date;
	private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");

	public DateType() {
		
	}

	public DateType(Date date) {
		this.date = date;
	}

	public DateType(String dateString) throws ParseException {
		this.date = sdf.parse(dateString);
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public static boolean isValidDate(String inDate) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        dateFormat.setLenient(false);
        
        try {
            dateFormat.parse(inDate.trim());
        } catch (ParseException pe) {
            return false;
        }
        return true;
    }
	
	@Override
	public String toString() {
		return sdf.format(date);
	}

	@Override
	public int compareTo(DateType type) {
		return date.compareTo(type.date);
	}
	
	@Override
	public boolean equals(Object obj) {
		if(!(obj instanceof DateType)) 
			return false;
		
		DateType date = (DateType) obj;
		return this.date.equals(date.getDate());
	}
}