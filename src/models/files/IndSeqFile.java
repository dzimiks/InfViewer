package models.files;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.StringTokenizer;

import models.Entity;
import models.Record;
import models.indseqTree.Tree;

public class IndSeqFile extends UIAbstractFile{

	private String treeFileName;
	private String overZoneFileName;
	private Tree indexTree;
	private String path;
	
	public IndSeqFile(String path,String headerName, boolean directory) {
		super(path,headerName,directory);
		this.path = path;
	}
	
	public IndSeqFile() {

	}
	
	public String getTreePath() {
		return this.path.replace(".stxt", ".tree");
	}
	
	public void loadTree(Entity entity) throws IOException {
		String treePath = getTreePath();
		
		try {
			ObjectInputStream stream = new ObjectInputStream(new FileInputStream(treePath));
			Tree indexTree = (Tree) stream.readObject();
			this.indexTree = indexTree;
			System.out.println("TREE: " + this.indexTree.getRootElement().getData());
			stream.close();
		}
		catch (Exception e) {
			System.err.println("Tree is not available! Building a new one...");
			this.indexTree = entity.makeTree(100);
			writeTree();
		}
	}
	
	public void writeTree() throws IOException {
		String treePath = getTreePath();
		ObjectOutputStream stream = new ObjectOutputStream(new FileOutputStream(treePath));
		stream.writeObject(indexTree);
		stream.close();
	}
	
	public Tree getIndexTree() {
		return indexTree;
	}

	@Override
	public void readHeader() throws IOException{
		String delimiter = "\\/";
		   ArrayList<String> headRec= new ArrayList<String>();
		   RandomAccessFile headerFile=null;
		   Object data[]=null;
		   
		    headerFile = new RandomAccessFile(path+File.separator+headerName,"r");
			while (headerFile.getFilePointer()<headerFile.length() )
          	headRec.add(headerFile.readLine());
			
			headerFile.close();
			
			   int row = 0;
	         
			   for (String record : headRec) {
			      StringTokenizer tokens = new StringTokenizer(record,delimiter);
			 
			      int cols = tokens.countTokens();
			      data = new String[cols];  
			      int col = 0;
			      while (tokens.hasMoreTokens()) {
			         data[col] = tokens.nextToken();
			         if (data[col].equals("field")){
			        	 String fieldName = tokens.nextToken();
			        	 String fieldType = tokens.nextToken();
			        	 int fieldLenght = Integer.parseInt(tokens.nextToken());
			        	 recordSize = recordSize+fieldLenght;
			        	 boolean fieldPK = new Boolean(tokens.nextToken());
			        	 UIFileField field = new UIFileField(fieldName,fieldType,fieldLenght,fieldPK);
			        	 
			        	 fields.add(field);
			         }else if (data[col].equals("path")){
			        	    fileName=tokens.nextToken();
			        	
			        	 
			         }else if (data[col].equals("tree")){
			        	 treeFileName=tokens.nextToken();
			         }else if (data[col].equals("overZone")){
			        	 overZoneFileName=tokens.nextToken();
			         }
			         
			         
			      }
         
		          row++;
		
			   }
			   recordSize=recordSize+2;
			   
			   //postavljanje atributa datoteke
			   RandomAccessFile afile=new RandomAccessFile(path+File.separator+fileName,"r");
			   fileSize=(int) afile.length();
			   numRecords=(long) Math.ceil((fileSize*1.0000)/(recordSize*1.0000));
			   numBlocks=(int) (numRecords/blockFactor)+1;
			   afile.close();
	}
	
	@Override
	public boolean fetchNextBlock() throws IOException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean addRecord(ArrayList<String> records) throws IOException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean updateRecord(Record record) throws IOException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public ArrayList<Record> findRecord(ArrayList<String> terms, boolean all) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean deleteRecord(Record record) throws IOException {
		// TODO Auto-generated method stub
		return false;
	}

}
