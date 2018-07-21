 package state;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.RandomAccessFile;
import java.io.Serializable;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

import models.Attribute;
import models.Entity;
import models.Record;
import models.Warehouse;
import models.tree.Node;
import view.TabbedView;

@SuppressWarnings("serial")
public class SortDBState extends State implements Serializable {

	private int height = 25;
	private int width = 150;
	private int x_left = 50;
	private int x_right = 180;
	private int y = 10;

	private int size;
	private Entity entity;
	private ArrayList<Node> attributes;
	private Attribute attr;

	private JComboBox<String> types;
	private JComboBox<String> cmbAttributes;
	private JComboBox<String> ondaPo;
	private JButton okBtn = new JButton("Sort");
	private long limit = 0;
	private int comparator;

	private StateManager stateManager;

	private ArrayList<String> sortKriterijumi;

	ArrayList<Integer> indeksi = new ArrayList<>();

	public SortDBState(Entity entity, int size, StateManager stateManager) {
		this.stateManager = stateManager;

		this.entity = entity;
		this.size = size;
		this.attributes = entity.getChildren();
		this.attr = new Attribute("Attribute");

		setLayout(null);
	}

	@Override
	public void changePanel() {
		this.removeAll();
		StringBuilder sb = new StringBuilder();
		height = 25;
		width = 150;
		x_left = 50;
		x_right = 180;
		y = 10;

		this.sortKriterijumi = new ArrayList<>();
		this.cmbAttributes = new JComboBox<>();
		this.ondaPo = new JComboBox<>();
		this.types = new JComboBox<>();
		this.types.addItem("Ascending");
		this.types.addItem("Descending");
		this.types.setBounds(x_left, y, width, height);

		add(types);
		y += 40;

		for (int i = 0; i < size; i++) {
			this.attr = (Attribute) this.attributes.get(i);
			cmbAttributes.addItem(attr.getName());
		}

		this.cmbAttributes.setBounds(x_left, y, width, height);
		add(cmbAttributes);

		y += 40;

		this.ondaPo.setBounds(x_left, y, width, height);
		this.ondaPo.addItem("");
		this.ondaPo.addItem("Onda po");

		this.ondaPo.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				String ascOrdsc;
				if(((String)types.getSelectedItem()).equals("Ascending")){
					ascOrdsc = " ASC";
				}
				else{
					ascOrdsc = " DESC";
				}
				sb.append((String) cmbAttributes.getSelectedItem() + ascOrdsc);
				sortKriterijumi.add(sb.toString());
				createNew();
			}
		});
		add(ondaPo);

	}

	public void createNew() {
		StringBuilder sb = new StringBuilder();
		x_left += 150;
		x_right += 280;
		y = 10;
		if (cmbAttributes != null) {
			cmbAttributes.disable();
			ondaPo.disable();
			types.disable();
		}

		cmbAttributes = new JComboBox<>();
		ondaPo = new JComboBox<>();
		types = new JComboBox<>();
		types.addItem("Ascending");
		types.addItem("Descending");
		types.setBounds(x_left, y, width, height);

		add(types);
		y += 40;

		for (int i = 0; i < size; i++) {
			this.attr = (Attribute) this.attributes.get(i);

			cmbAttributes.addItem(attr.getName());
		}
		cmbAttributes.setBounds(x_left, y, width, height);
		add(cmbAttributes);

		y += 40;

		ondaPo.setBounds(x_left, y, width, height);
		ondaPo.addItem("");
		ondaPo.addItem("Onda po");

		ondaPo.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				String ascOrdsc;
				if(((String)types.getSelectedItem()).equals("Ascending")){
					ascOrdsc = " ASC";
				}
				else{
					ascOrdsc = " DESC";
				}
				sb.append((String) cmbAttributes.getSelectedItem() + ascOrdsc);
				sortKriterijumi.add(sb.toString());
				createNew();
			}
		});
		add(ondaPo);
	}

	@Override
	public void changeEntity(Entity entity) {
		this.entity = entity;
		this.size = entity.getChildCount();
		this.attributes = entity.getChildren();
	}

	@Override
	public void submit() {
		
		StringBuilder sbTmp = new StringBuilder();
		
		String ascOrdsc;
		if(((String)types.getSelectedItem()).equals("Ascending")){
			ascOrdsc = " ASC";
		}
		else{
			ascOrdsc = " DESC";
		}
		sbTmp.append((String) cmbAttributes.getSelectedItem() + ascOrdsc);
		sortKriterijumi.add(sbTmp.toString());
		
		DefaultTableModel model = TabbedView.getActivePanel().getModel();
		ArrayList<Record> records = new ArrayList<>();

		while(model.getRowCount() != 0){
			model.removeRow(0);
		}
		
		StringBuilder sb = new StringBuilder();
		sb.append("SELECT * FROM "  + entity.getName() + " ORDER BY " );
		
		for(int i = 0; i < sortKriterijumi.size(); i++){
			System.out.println(sortKriterijumi.get(i));
			sb.append(sortKriterijumi.get(i));
			if(i < sortKriterijumi.size() - 1){
				sb.append(", ");
			}
		}
		
		System.out.println("===========");
		System.out.println(sb.toString());
		System.out.println("===========");
		
		try{
			PreparedStatement statement = Warehouse.getInstance().getDbConnection().prepareStatement(sb.toString());
			ResultSet resultSet = statement.executeQuery();
			
			
			while (resultSet.next()) {
				Record record = new Record();
				
				for (Node node : entity.getChildren()) {
					if (node instanceof Attribute) {
						Object value = resultSet.getObject(node.getName());
						record.addObject(value);
					}
				}
				
				model.addRow(record.getPodaci().toArray());
				records.add(record);
			}
			
			statement.close();
			resultSet.close();
		}catch(Exception e){
			System.out.println("Nesto ne valja sa sortom " + e.getMessage());
		}
	}

	private void copyFromTo(String input, String outpu) {
		try {
			BufferedReader br = new BufferedReader(new FileReader(input));
			PrintWriter pw = new PrintWriter(new FileWriter(outpu));

			String line = br.readLine();

			String prev = line;

			line = br.readLine();
			while (line != null) {
				boolean isOk = false;
				for (int i = 0; i < prev.toCharArray().length - 1; i++) {
					if (prev.charAt(i) != ' ') {
						isOk = true;
					}
				}

				if (isOk) {
					pw.write(prev + "\n");
				}

				prev = line;
				line = br.readLine();
			}
			boolean isOk = false;
			for (int i = 0; i < prev.toCharArray().length - 1; i++) {
				if (prev.charAt(i) != ' ') {
					isOk = true;
				}
			}
			if (isOk) {
				pw.write(prev);
			}
			pw.close();
			br.close();

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void fillWithEnters() {

		int broj = 1;
		while (broj < limit) {
			broj *= 2;
		}

		ArrayList<String> emptyData = new ArrayList<>();
		for (Node n : entity.getChildren()) {
			emptyData.add(new String(" "));
		}

		for (int i = (int) limit; i < broj; i++) {
			entity.appendFile(emptyData);
		}

		limit = broj;
	}

	private void mergeBlocksOfSize(int blockSize, String input, String output) throws IOException {

		int i = 0, j = 0;

		PrintWriter file = new PrintWriter(new FileWriter(output));

		int blockCount = 0;

		while (blockCount * blockSize < limit) {

			i = j = 0;

			while ((i < blockSize || j < blockSize) && (i < limit || j < limit)) {

				ArrayList<String> recordI = null;
				ArrayList<String> recordJ = null;

				if (j != blockSize && j != limit) {
					recordJ = readFrom(input, j + blockSize * blockCount + blockSize);
				}

				if (i != blockSize && i != limit) {
					recordI = readFrom(input, i + blockCount * blockSize);
				}

				if (recordI == null && recordJ == null) {
					continue;
				}

				if (recordI == null && recordJ != null) {
					file.print(turnArrayListToString(recordJ));
					j++;
					continue;
				}

				if (recordJ == null && recordI != null) {
					file.print(turnArrayListToString(recordI));
					i++;
					continue;
				}

				int whatToDo = whatToDo(recordI, recordJ);

				if (comparator * whatToDo < 0) {
					file.print(turnArrayListToString(recordI));
					i++;
				} else {
					file.print(turnArrayListToString(recordJ));
					j++;
				}

			}
			blockCount += 2;
		}

		file.close();
	}

	private int whatToDo(ArrayList<String> prvi, ArrayList<String> drugi) {

		int ok = 0;

		for (Integer i : indeksi) {

			ok = prvi.get(i).compareTo(drugi.get(i));

			if (ok != 0) {
				break;
			}
		}

		return ok;
	}

	private String turnArrayListToString(ArrayList<String> stringovi) {
		StringBuilder sb = new StringBuilder();

		for (String s : stringovi) {
			sb.append(s);
		}

		sb.append(" \n");

		return sb.toString();
	}

	private ArrayList<String> turnRecordToString(Record r) {

		ArrayList<String> stringovi = new ArrayList<>();

		for (int i = 0; i < r.getPodaci().size(); i++) {

			int length = ((Attribute) entity.getChildAt(i)).getLength();

			StringBuilder sb = new StringBuilder();

			String toWrite = r.getPodaci().get(i).toString();

			sb.append(toWrite.substring(0, Math.min(length, toWrite.length())));

			while (sb.toString().length() != length) {
				sb.append(" ");
			}

			stringovi.add(sb.toString());
		}

		return stringovi;

	}

	private ArrayList<String> readFrom(String fileName, int index) {
		ArrayList<String> stringovi = new ArrayList<>();

		int size = entity.getRecordSize() + 2;

		try {
			RandomAccessFile raf = new RandomAccessFile(fileName, "r");

			raf.seek((index) * size);

			byte[] buffer = new byte[size];

			raf.read(buffer);

			String line = new String(buffer);

			int beginIndex = 0;

			for (Node n : entity.getChildren()) {
				Attribute a = (Attribute) n;

				int endIndex = beginIndex + a.getLength();

				stringovi.add(line.substring(beginIndex, endIndex));

				beginIndex = endIndex;
			}

			raf.close();

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return stringovi;
	}

	public JComboBox<String> getTypes() {
		return types;
	}

	public JComboBox<String> getCmbAttributes() {
		return cmbAttributes;
	}

	public JComboBox<String> getOndaPo() {
		return ondaPo;
	}

}