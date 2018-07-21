/***********************************************************************
 * Module:  Entity.java
 * Author:  Mihailo
 * Purpose: Defines the Class Entity
 ***********************************************************************/

package models;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.RandomAccessFile;
import java.text.ParseException;
import java.util.*;

import models.datatypes.CharType;
import models.datatypes.DateType;
import models.datatypes.VarCharType;
import models.indseqTree.KeyElement;
import models.indseqTree.NodeElement;
import models.indseqTree.NodeIndSeq;
import models.indseqTree.Pair;
import models.indseqTree.Tree;
import models.tree.Node;
import view.TabbedView;

@SuppressWarnings("serial")
public class Entity extends Node {

	private String url;
	public ArrayList<Relation> relations;
	private int readFrom = 0;
	private long fileSize = 0;
	private int recordSize = 0;
	private long recordNumber = 0;
	private ArrayList<Boolean> sakriti;

	public Entity(String name) {
		super(name);
		relations = new ArrayList<>();
	}

	public Entity(String name, String url) {
		super(name);
		this.url = url;
		this.relations = new ArrayList<>();
	}

	// Copy constructor
	public Entity(Entity e) {
		super(e.getName());
		url = e.getUrl();
		relations = new ArrayList<>();
		for (int i = 0; i < e.getChildCount(); i++) {
			addChild(new Attribute((Attribute) e.getChildAt(i)));
		}

		for (int i = 0; i < e.getRelationCount(); i++) {
			addRelation(new Relation(e.getRelationAt(i), this));
		}
	}

	// Dodaje novu relaciju
	public void addRelations(Relation newRelation) {
		if (newRelation == null)
			return;
		if (this.relations == null)
			this.relations = new java.util.ArrayList<Relation>();
		if (!this.relations.contains(newRelation))
			this.relations.add(newRelation);
	}

	// Brise relaciju
	public void removeRelations(Relation oldRelation) {
		if (oldRelation == null)
			return;
		if (this.relations != null)
			if (this.relations.contains(oldRelation))
				this.relations.remove(oldRelation);
	}

	// Brise sve relacije
	public void removeAllRelations() {
		if (relations != null)
			relations.clear();
	}

	public Attribute findAttributeByName(String name) {
		for (Node a : this.getChildren())
			if (a instanceof Attribute && a.getName().equals(name))
				return (Attribute) a;

		return null;
	}

	public Relation getRelationAt(int index) {
		return this.relations.get(index);
	}

	public int getRelationCount() {
		return this.relations.size();
	}

	public void addRelation(Relation r) {
		this.relations.add(r);
	}

	public ArrayList<Relation> getRelations() {
		return relations;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public void getFileData() {
		try {
			RandomAccessFile afile = new RandomAccessFile(url, "r");

			fileSize = afile.length();

			recordSize = 0;
			
			for (Node n : getChildren()) {
				recordSize += ((Attribute) n).getLength();
			}

			recordNumber = fileSize / (recordSize + 2);
			recordNumber++;

			//Moracemo da promenimo kad dodamo paket TODO
			if(((Package)getParent()).getType().equals("serial")){
				if(sakriti == null){
					sakriti = new ArrayList<>();
				}
				for(int i = sakriti.size(); i < recordNumber; i++){
					sakriti.add( new Boolean(false));
				}
			}
			
			
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public int getRecordSize() {
		int recordSize = 0;

		for (Node n : getChildren()) {
			recordSize += ((Attribute) n).getLength();
		}

		return recordSize;
	}

	public int getReadFrom() {
		return readFrom;
	}

	public long getRecordNumber() {
		// TODO
		getFileData();
		return recordNumber;
	}

	public Record[] fetchNextBlock(int numberOfRows) {

		Record[] data = new Record[numberOfRows];

		for (int i = 0; i < numberOfRows; i++) {

			if(sakriti != null && sakriti.get(readFrom)){
				i--;
			}else{
				data[i] = readRecord();
			}

			incCounter();
		}

		return data;
	}

	public Record[] fetchPrevBlock(int numberOfRows){
		
		Record[] data = new Record[numberOfRows];
		
		for(int i = numberOfRows-1; i >= 0; i--){
			
			reduceCounter();
			if(sakriti != null && sakriti.get(readFrom)){
				i++;
			}else{
				data[i] = readRecord();
			}
		}
		
		return data;	
	}
	
	
	public Record readRecord() {

		Record newRecord = new Record();

		String line = null;

		try {
			RandomAccessFile afile = new RandomAccessFile(url, "r");
			afile.seek(readFrom * (getRecordSize() + 2));

			byte[] buffer = new byte[getRecordSize()];

			afile.read(buffer);

			line = new String(buffer);

			int beginIndex = 0;

			for (Node n : getChildren()) {
				Attribute a = (Attribute) n;
				int endIndex = beginIndex + a.getLength();

				if (a.getValueClass() == CharType.class) {
					newRecord.addObject((Object) new CharType(a.getLength(), line.substring(beginIndex, endIndex)));
				} else if (a.getValueClass() == VarCharType.class) {
					newRecord.addObject((Object) new VarCharType(a.getLength(), line.substring(beginIndex, endIndex)));
				} else if (a.getValueClass() == Integer.class) {

					try {
						newRecord.addObject(
								(Object) new Integer(Integer.parseInt(line.substring(beginIndex, endIndex).trim())));
					} catch (NumberFormatException e) {
						newRecord.addObject((Object) new Integer(0));
					}

				} else if (a.getValueClass() == Boolean.class) {
					newRecord.addObject((Object) new Boolean(line.substring(beginIndex, endIndex).trim()));
				} else if (a.getValueClass() == DateType.class) {
					newRecord.addObject((Object) new DateType(line.substring(beginIndex, endIndex)));
				}

				beginIndex = endIndex;

			}
			afile.close();

		} catch (IOException | ParseException e) {

			System.out.println(line);
			e.printStackTrace();
		}

		return newRecord;
	}

	//Vraca record
	public Record readRecordFrom(int index, boolean ok) {
		
		if(index >= getRecordNumber()) return null;
		
		Record newRecord = new Record();
		
		String line = null;

		try {
			RandomAccessFile afile = new RandomAccessFile(url, "r");
			
			afile.seek((((ok) ? readFrom - TabbedView.activePanel.getLastBlockSize() : 0) +index) * (getRecordSize() + 2));

			byte[] buffer = new byte[getRecordSize()];

			afile.read(buffer);

			line = new String(buffer);

			int beginIndex = 0;

			for (Node n : getChildren()) {
				Attribute a = (Attribute) n;
				int endIndex = beginIndex + a.getLength();

				if (a.getValueClass() == CharType.class) {
					newRecord.addObject((Object) new CharType(a.getLength(), line.substring(beginIndex, endIndex)));
				} else if (a.getValueClass() == VarCharType.class) {
					newRecord.addObject((Object) new VarCharType(a.getLength(), line.substring(beginIndex, endIndex)));
				} else if (a.getValueClass() == Integer.class) {

					try {
						newRecord.addObject(
								(Object) new Integer(Integer.parseInt(line.substring(beginIndex, endIndex).trim())));
					} catch (NumberFormatException e) {
						newRecord.addObject((Object) new Integer(0));
					}

				} else if (a.getValueClass() == Boolean.class) {
					newRecord.addObject((Object) new Boolean(line.substring(beginIndex, endIndex).trim()));
				} else if (a.getValueClass() == DateType.class) {
					newRecord.addObject((Object) new DateType(line.substring(beginIndex, endIndex)));
				}

				beginIndex = endIndex;

			}
			afile.close();

		} catch (IOException | ParseException e) {

			System.out.println(line);
			e.printStackTrace();
		}

		return newRecord;
	}
	
	public void incCounter(int by) {

		for(int i = 0; i < by; i++){
			
			if(sakriti != null && sakriti.get(readFrom) == true){
				i--;
			}
			
			if(++readFrom == getRecordNumber()){
				readFrom = 0;
			}
		}
	}
	
	private void incCounter(){
		readFrom++;
		if(readFrom == getRecordNumber()){
			readFrom = 0;
		}
	}
	
	private void reduceCounter(){
		readFrom--;
		if(readFrom < 0){
			readFrom+=getRecordNumber();
		}
	}
	
	public void reduceCounter(int by) {
		
		for(int i = 0; i < by; i++){
			
			if(--readFrom < 0){
				readFrom = (int) (getRecordNumber() - 1);
			}
			
			if(sakriti != null && sakriti.get(readFrom) == true){
				i--;
			}
			
			
		}

	}

	
	public void setDeletedIndex(int index){
		if(sakriti == null){
			return;
		}
		System.out.println("deleting "+(int) ((readFrom+index)%getRecordNumber()));
		
		int indexForDelete = (int) ((readFrom+index)%getRecordNumber());
		
		if(indexForDelete < 0){
			indexForDelete+=getRecordNumber();
		}
		sakriti.set(indexForDelete, true);
		
		
		
	}

	public long getFileSize() {
		File f = new File(url);
		return f.length() / 1024;
	}	
	
	public void appendFile(ArrayList<String> podaci){
		
		try {
			FileWriter fw = new FileWriter(url, true);
			fw.write(" \n");
			
			for(int i = 0; i < getChildCount(); i++){
				StringBuilder sb = new StringBuilder();
				
				String toWrite = podaci.get(i);
				
				Attribute a = (Attribute) getChildAt(i);
				
				for(int j = 0; j < a.getLength() && j < toWrite.length(); j++){
					sb.append(toWrite.charAt(j));
				}
				
				while(sb.toString().length() != a.getLength()){
					sb.append(" ");
				}
				
				fw.write(sb.toString());
				
			}
			

			fw.close();
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	public String getPrimaryKey(Record record){
		StringBuilder sb = new StringBuilder();
		
		for(int i = 0; i < this.getChildCount(); i++){
			if(((Attribute)this.getChildAt(i)).isPrimaryKey()){
				sb.append(record.getPodaci().get(i).toString());
			}
		}
		
		return sb.toString();
	}
	
	public String getPrimaryKeyString(ArrayList<String> terms){
		StringBuilder sb = new StringBuilder();
		
		for(int i = 0; i < this.getChildCount(); i++){
			if(((Attribute)this.getChildAt(i)).isPrimaryKey()){
				sb.append(terms.get(i));
			}
		}
		
		return sb.toString();
	}
	
	public void writeIn(int index, ArrayList<String> podaci){
		try {
			RandomAccessFile afile = new RandomAccessFile(url, "rws");
			
			afile.seek((index)*(getRecordSize()+2));
			
			String line = "";
			for(int i = 0; i < getChildCount(); i++){
				

				StringBuilder sb = new StringBuilder();
				
				String toWrite = podaci.get(i);
				
				Attribute a = (Attribute) getChildAt(i);
				
				for(int j = 0; j < a.getLength() && j < toWrite.length(); j++){
					sb.append(toWrite.charAt(j));
				}
				
				while(sb.toString().length() != a.getLength()){
					sb.append(" ");
				}
				
				line += sb.toString();
			}
			
			byte[] data = new byte[getRecordSize()+2];
			
			for(int i = 0; i < line.length(); i++){
				data[i] = (byte) line.charAt(i); 
			}
			
			
//			data[data.length-2] = ' ';
					
			data[data.length-1] = '\n';
			
			afile.write(data);
			
			afile.close();
			System.out.println("Uspesno!");
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	public Tree makeTree(int numberOfRows){
		int row = 0; 
		int count = 1;
		Queue<Pair> queue = new LinkedList<>();
		ArrayList<Node> attributes = this.getChildren();
		while(row < this.getRecordNumber()){
			Record record = this.readRecordFrom(row, false);
			ArrayList<Object> podaci = record.getPodaci();
			NodeIndSeq node = new NodeIndSeq();
			List<KeyElement> keyList = new LinkedList<>();
			for(int i = 0 ; i < podaci.size() ; i++){
				Attribute attribute = (Attribute)(attributes.get(i));
				if(attribute.isPrimaryKey()){
					keyList.add(new KeyElement(attribute.getValueClass().toString(), String.valueOf(podaci.get(i))));
				}	
			}
			node.addData(new NodeElement(count, keyList));
			row = row + numberOfRows;
			count++;
			if(row < this.getRecordNumber()){
				Record record2 = this.readRecordFrom(row, false);
				ArrayList<Object> podaci2 = record2.getPodaci();
				List<KeyElement> keyList2 = new LinkedList<>();
				for(int i = 0 ; i < podaci2.size() ; i++){
					Attribute attribute = (Attribute)(attributes.get(i));
					if(attribute.isPrimaryKey()){
						keyList2.add(new KeyElement(attribute.getValueClass().toString(), String.valueOf(podaci2.get(i))));
					}	
				}
				node.addData(new NodeElement(count, keyList2));
			}
			row = row + numberOfRows;
			count++;
			if(row >= this.getRecordNumber()){
				queue.add(new Pair(node, true));
			}else{
				queue.add(new Pair(node, false));
			}
		}
		boolean spin = true;
		while(queue.size() != 1){
			spin = true;
			while(spin){
				NodeIndSeq node = new NodeIndSeq();
				if(queue.peek().isLast()){
					node.addData(queue.peek().getNode().getData().get(0).clone());
					node.addChild(queue.poll().getNode())	;
					queue.add(new Pair(node, true));
					spin = false;
				}else{
					node.addData(queue.peek().getNode().getData().get(0).clone());
					node.addChild(queue.poll().getNode());
					if(queue.peek().isLast()){
						node.addData(queue.peek().getNode().getData().get(0).clone());
						node.addChild(queue.poll().getNode());
						queue.add(new Pair(node, true));
						spin = false;
					}else{
						node.addData(queue.peek().getNode().getData().get(0).clone());
						node.addChild(queue.poll().getNode());
						queue.add(new Pair(node, false));
					}
				}
			}
		}
		Tree tree = new Tree();
		tree.setRootElement(queue.poll().getNode());
		return tree;
	}
	
	public void writeInSek(int index, ArrayList<String> podaci){
		
		String line = "";
		for(int i = 0; i < getChildCount(); i++){
			

			StringBuilder sb = new StringBuilder();
			
			String toWrite = podaci.get(i);
			
			Attribute a = (Attribute) getChildAt(i);
			
			for(int j = 0; j < a.getLength() && j < toWrite.length(); j++){
				sb.append(toWrite.charAt(j));
			}
			
			while(sb.toString().length() != a.getLength()){
				sb.append(" ");
			}
			
			line += sb.toString();
		}
		
		ArrayList<String> stringovi = new ArrayList<>();
		
		try {
			BufferedReader br = new BufferedReader(new FileReader(url));
			
			String s = br.readLine();
			
			while(s != null){
				
				stringovi.add(s);
				
				s = br.readLine();
			}
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		stringovi.add(index, line);
		
		try{
			PrintWriter pw = new PrintWriter(new FileWriter(url));
			
			for(String s: stringovi){
				pw.println(s);
			}
			pw.close();
			
		}catch (IOException e) {
			e.printStackTrace();
		}
		
		System.out.println("Uspesno dodat podatak");
		
		
	}
	
	
}