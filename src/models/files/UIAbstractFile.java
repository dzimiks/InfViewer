package models.files;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;
import java.io.File;
import javax.swing.event.EventListenerList;

import event.UpdateBlockEvent;
import event.UpdateBlockListener;
import models.Record;

public abstract class UIAbstractFile implements UIFile {

	protected String fileName;
	protected String path;
	protected String headerName;
	protected boolean directory;
	protected RandomAccessFile randomFile;
	protected int blockFactor = 20;
	protected int recordSize = 2;
	protected long numRecords = 0;
	protected int numBlocks = 0;
	protected int filePointer = 0;
	protected int blocksFetched = 0;
	protected int fileSize = 0;
	protected int bufferSize = 0;

	ArrayList<UIFileField> fields = new ArrayList<UIFileField>();
	protected byte[] buffer;
	protected String[][] data = null;

	EventListenerList listenerBlockList = new EventListenerList();
	UpdateBlockEvent updateBlockEvent = null;

	static public final int BROWSE_MODE = 1;
	static public final int ADD_MODE = 2;
	static public final int UPDATE_MODE = 3;
	static public final int DELETE_MODE = 4;
	static public final int FIND_MODE = 5;

	protected int MODE = UIAbstractFile.BROWSE_MODE;

	public UIAbstractFile() {
	}

	public UIAbstractFile(String name, String path) {
		this.fileName = name;
		this.path = path;
	}

	public UIAbstractFile(String path, String headerName, boolean directory) {
		this.path = path;
		this.headerName = headerName;
		this.directory = directory;
		this.fileName = headerName;

	}

	@Override
	public void readHeader() throws IOException {
		
		String delimiter = "\\/";
		ArrayList<String> headRec = new ArrayList<String>();
		Object data[] = null;

		RandomAccessFile headerFile = new RandomAccessFile(path + File.separator + headerName, "r");
		
		while (headerFile.getFilePointer() < headerFile.length())
			headRec.add(headerFile.readLine());

		headerFile.close();

		int row = 0;

		for (String record : headRec) {
			
			StringTokenizer tokens = new StringTokenizer(record, delimiter);

			int cols = tokens.countTokens();
			data = new String[cols];
			int col = 0;
			
			while (tokens.hasMoreTokens()) {
				
				data[col] = tokens.nextToken();
				
				if (data[col].equals("field")) {
					
					String fieldName = tokens.nextToken();
					String fieldType = tokens.nextToken();
					
					int fieldLenght = Integer.parseInt(tokens.nextToken());
					recordSize = recordSize + fieldLenght;
					
					boolean fieldPK = new Boolean(tokens.nextToken());
					
					UIFileField field = new UIFileField(fieldName, fieldType, fieldLenght, fieldPK);

					fields.add(field);
				} else if (data[col].equals("path")) {
					fileName = tokens.nextToken();
				}
			}

			row++;
		}
		
		recordSize += 2;
		randomFile = new RandomAccessFile(path + File.separator + fileName, "r");
		fileSize = (int)randomFile.length();
		numRecords = (long) Math.ceil((fileSize * 1.0000) / (recordSize * 1.0000));
		numBlocks = (int) (numRecords / blockFactor) + 1;
		randomFile.close();
	}
	
	 public void addUpdateBlockListener(UpdateBlockListener l) {	
		 listenerBlockList.add(UpdateBlockListener.class, l);
	 }

	 public void removeUpdateBlockListener(UpdateBlockListener l) {
		 listenerBlockList.remove(UpdateBlockListener.class, l);
	 }
	 
	protected void fireUpdateBlockPerformed() {
	     Object[] listeners = listenerBlockList.getListenerList();
	     for (int i = listeners.length-2; i>=0; i-=2) {
	         if (listeners[i]==UpdateBlockListener.class) {
	             if (updateBlockEvent == null)
	            	 updateBlockEvent = new UpdateBlockEvent(this);
	             ((UpdateBlockListener)listeners[i+1]).updateBlockPerformed(updateBlockEvent);
	         }
	     }

	 }	

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public String getHeaderName() {
		return headerName;
	}

	public void setHeaderName(String headerName) {
		this.headerName = headerName;
	}

	public boolean isDirectory() {
		return directory;
	}

	public void setDirectory(boolean directory) {
		this.directory = directory;
	}

	public RandomAccessFile getRandomFile() {
		return randomFile;
	}

	public void setRandomFile(RandomAccessFile randomFile) {
		this.randomFile = randomFile;
	}

	public int getBlockFactor() {
		return blockFactor;
	}

	public void setBlockFactor(int blockFactor) {
		this.blockFactor = blockFactor;
	}

	public int getRecordSize() {
		return recordSize;
	}

	public void setRecordSize(int recordSize) {
		this.recordSize = recordSize;
	}

	public long getNumRecords() {
		return numRecords;
	}

	public void setNumRecords(long numRecords) {
		this.numRecords = numRecords;
	}

	public int getNumBlocks() {
		return numBlocks;
	}

	public void setNumBlocks(int numBlocks) {
		this.numBlocks = numBlocks;
	}

	public int getFilePointer() {
		return filePointer;
	}

	public void setFilePointer(int filePointer) {
		this.filePointer = filePointer;
	}

	public int getBlocksFetched() {
		return blocksFetched;
	}

	public void setBlocksFetched(int blocksFetched) {
		this.blocksFetched = blocksFetched;
	}

	public int getFileSize() {
		return fileSize;
	}

	public void setFileSize(int fileSize) {
		this.fileSize = fileSize;
	}

	public ArrayList<UIFileField> getFields() {
		return fields;
	}

	public void setFields(ArrayList<UIFileField> fields) {
		this.fields = fields;
	}

	public byte[] getBuffer() {
		return buffer;
	}

	public void setBuffer(byte[] buffer) {
		this.buffer = buffer;
	}

	public String[][] getData() {
		return data;
	}

	public void setData(String[][] data) {
		this.data = data;
	}

	public EventListenerList getListenerBlockList() {
		return listenerBlockList;
	}

	public void setListenerBlockList(EventListenerList listenerBlockList) {
		this.listenerBlockList = listenerBlockList;
	}

	public int getMODE() {
		return MODE;
	}

	public void setMODE(int mODE) {
		MODE = mODE;
	}

	public static int getBrowseMode() {
		return BROWSE_MODE;
	}

	public static int getAddMode() {
		return ADD_MODE;
	}

	public static int getUpdateMode() {
		return UPDATE_MODE;
	}

	public static int getDeleteMode() {
		return DELETE_MODE;
	}

	public static int getFindMode() {
		return FIND_MODE;
	}

	
}



