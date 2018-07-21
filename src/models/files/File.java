
package models.files;

import java.io.IOException;
import java.io.RandomAccessFile;


public abstract class File implements UIFile {

	private String name;
	protected String path;
	protected RandomAccessFile randomFile;
	protected int blockFactor = 20;
	protected int recordSize = 2;
	protected int numRecords = 0;
	protected int numBlocks = 0;
	protected int filePointer = 0;
	protected int blocksFetched = 0;
	
	public File(String name, String path) {
		this.name = name;
		this.path = path;
	}

	@Override
	public void readHeader() throws IOException {
	}

	@Override
	public boolean fetchNextBlock() throws IOException {
		return false;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public RandomAccessFile getFile() {
		return randomFile;
	}

	public void setFile(RandomAccessFile file) {
		this.randomFile = file;
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

	public int getNumRecords() {
		return numRecords;
	}

	public void setNumRecords(int numRecords) {
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
}
