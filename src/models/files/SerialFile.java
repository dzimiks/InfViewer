package models.files;

import java.io.IOException;
import java.util.ArrayList;

import models.Entity;
import models.Record;
import view.TabbedView;

public class SerialFile extends UIAbstractFile {

	public SerialFile() {
		super();
	}

	public SerialFile(String fileName, String path) {
		super(fileName, path);
	}

	public SerialFile(String path, String headerName, boolean directory) {
		super(path, headerName, directory);

	}

	@Override
	public boolean addRecord(ArrayList<String> record) throws IOException {

		Entity e = TabbedView.activePanel.getEntity();
		
		e.appendFile(record);
		
		
		return true;
	}

	@Override
	public boolean updateRecord(Record record) throws IOException {
		return false;
	}
	
	@Override
	public ArrayList<Record> findRecord(ArrayList<String> terms, boolean all) {
		
		filePointer = 0;
		ArrayList<Record> result = new ArrayList<>();
		
				
		for(int i = 0; i < TabbedView.getActivePanel().getEntity().getRecordNumber(); i++){
			
			if(isRowEqual(TabbedView.getActivePanel().getEntity().readRecordFrom(i, false), terms)){
				
				ArrayList<Object> tmpObject = new ArrayList<>();
				
				for(int j = 0; j < TabbedView.getActivePanel().getEntity().readRecordFrom(i, false).getPodaci().size(); j++){
					tmpObject.add(TabbedView.getActivePanel().getEntity().readRecordFrom(i, false).getPodaci().get(j));
				}
				
				result.add(new Record(tmpObject));
			}
		}
		
		return result;
	}

	@Override
	public boolean deleteRecord(Record record) throws IOException {
		// TODO
		
		
		return false;
	}

	@Override
	public boolean fetchNextBlock() throws IOException {

		return true;
	}

	private boolean isRowEqual(Record aData, ArrayList<String> searchRec) {
		boolean result = true;

		for(int i = 0; i < aData.getPodaci().size(); i++){
						
			if(!searchRec.get(i).trim().equals("")){
				
				if(!aData.getPodaci().get(i).toString().trim().equals(searchRec.get(i).trim())){
					result = false;
					return result;
				}
			}
		}

		return result;
	}
}