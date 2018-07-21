
package models.files;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import models.Record;
import view.TabbedView;

public class SequentialFile extends File {

	public SequentialFile(String name, String path) {
		super(name, path);
	}

	@Override
	public boolean updateRecord(Record record) throws IOException {
		FileOutputStream stream = new FileOutputStream(getChangesFilePath(), true);
		PrintWriter pw = new PrintWriter(stream);
		pw.println(record.toString() + "UPDATE");
		pw.close();
		stream.close();
		return true;
	}

	@Override
	public ArrayList<Record> findRecord(ArrayList<String> terms, boolean all) {
		
		ArrayList<Record> data = new ArrayList<>();
		ArrayList<Record> result = new ArrayList<>();
		
		for(int i = 0; i < TabbedView.getActivePanel().getEntity().getRecordNumber(); i++){
			data.add(TabbedView.getActivePanel().getEntity().readRecordFrom(i, false));
		}
		
		
		
		int low = 0;
		int high = data.size() - 1;
		
		while(low < high){
			
			int mid = low + (high - low ) / 2;
			
			if(getPrimaryKey(data.get(mid)).trim().compareTo(getPrimaryKeyString(terms).trim()) == 0){
				result.add(data.get(mid));
				return result;
			}		
			else if(getPrimaryKey(data.get(mid)).trim().compareTo(getPrimaryKeyString(terms).trim()) < 0){
				low = mid + 1;
			}
			else {
				high = mid;
			}
		}
		
		return result;
	}
	
	public int getBinaryIndex(ArrayList<String> terms){
		ArrayList<Record> data = new ArrayList<>();
		int index = 0;
		
		for(int i = 0; i < TabbedView.getActivePanel().getEntity().getRecordNumber(); i++){
			data.add(TabbedView.getActivePanel().getEntity().readRecordFrom(i, false));
		}
		
		int low = 0;
		int high = data.size() - 1;
		
		while(low < high){
			
			int mid = low + (high - low ) / 2;
			
			if(getPrimaryKey(data.get(mid)).trim().compareTo(getPrimaryKeyString(terms).trim()) == 0){
				index = mid;
				return index;
			}		
			else if(getPrimaryKey(data.get(mid)).trim().compareTo(getPrimaryKeyString(terms).trim()) < 0){
				low = mid + 1;
				index = mid+1;
			}
			else {
				high = mid;
			}
		}
		return index;
	}

	@Override
	public boolean deleteRecord(Record record) throws IOException {
		FileOutputStream stream = new FileOutputStream(getChangesFilePath(), true);
		PrintWriter pw = new PrintWriter(stream);
		pw.println(record.toString() + "DELETE");
		pw.close();
		stream.close();
		return true;
	}
	
	@Override
	public boolean fetchNextBlock() throws IOException {
		return false;
	}
	
	private String getChangesFilePath() {
		return path.replace(".stxt", ".changes");
	}
	
	
	private String getPrimaryKey(Record record){
		return TabbedView.getActivePanel().getEntity().getPrimaryKey(record);
	}
	
	private String getPrimaryKeyString(ArrayList<String> terms){
		return TabbedView.getActivePanel().getEntity().getPrimaryKeyString(terms);
	}

	@Override
	public boolean addRecord(ArrayList<String> records) throws IOException {
		TabbedView.getActivePanel().getEntity().writeInSek(getBinaryIndex(records), records);
		return false;
	}
	
}
