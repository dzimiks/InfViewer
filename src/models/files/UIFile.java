
package models.files;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import models.Record;

public interface UIFile {
	public void readHeader() throws IOException;
	public boolean fetchNextBlock() throws IOException;
	public boolean addRecord(ArrayList<String> records) throws IOException;
	public boolean updateRecord(Record record) throws IOException;
	public ArrayList<Record> findRecord(ArrayList<String> terms, boolean all);
	public boolean deleteRecord(Record record) throws IOException;
}


