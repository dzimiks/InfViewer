package state;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.RandomAccessFile;
import java.io.Serializable;
import java.util.ArrayList;

import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import models.Attribute;
import models.Entity;
import models.Record;
import models.tree.Node;
import view.TabbedView;

@SuppressWarnings("serial")
public class SortState extends State implements Serializable {

	private int height = 25;
	private int width = 150;
	private int x_left = 50;
	private int x_right = 180;
	private int y = 10;

	private int size;
	private Entity entity;
	private ArrayList<Node> attributes;
	private Attribute attr;

	private JLabel[] labels;
	private JCheckBox[] checkBoxes;
	private JComboBox<String> types;
	private long limit = 0;
	private int comparator;

	private StateManager stateManager;

	ArrayList<Integer> indeksi = new ArrayList<>();

	public SortState(Entity entity, int size, StateManager stateManager) {
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
		height = 25;
		width = 150;
		x_left = 50;
		x_right = 180;
		y = 10;
		this.labels = new JLabel[size];
		this.checkBoxes = new JCheckBox[size];
		this.types = new JComboBox<>();
		this.types.addItem("Ascending");
		this.types.addItem("Descending");
		this.types.setBounds(x_left, y, width, height);
		add(types);
		y += 40;

		for (int i = 0; i < size; i++) {
			this.attr = (Attribute) this.attributes.get(i);
			this.labels[i] = new JLabel(parseName(attr.getName()));
			this.checkBoxes[i] = new JCheckBox();

			this.labels[i].setBounds(x_left, y, width, height);
			this.checkBoxes[i].setBounds(x_right, y, width, height);

			y += 40;
			add(labels[i]);
			add(checkBoxes[i]);
		}
	}

	private String parseName(String name) {
		int n = name.length();
		StringBuilder sb = new StringBuilder();
		sb.append(name.charAt(0));

		for (int i = 1; i < n; i++) {
			char c = name.charAt(i);

			if (c >= 'A' && c <= 'Z') {
				c += 32;
				sb.append(" ");
			}

			sb.append(c);
		}

		sb.append(": ");
		return sb.toString();
	}

	@Override
	public void changeEntity(Entity entity) {
		this.entity = entity;
		this.size = entity.getChildCount();
		this.attributes = entity.getChildren();
	}

	@Override
	public void submit() {

		entity.getFileData();
		limit = entity.getRecordNumber();

		fillWithEnters();
		System.out.println("Sortiram");

		// Uzimamo selektovane checkboxove
		for (int i = 0; i < checkBoxes.length; i++) {
			JCheckBox cb = checkBoxes[i];

			if (cb.isSelected()) {
				indeksi.add(new Integer(i));
			}
		}

		if (types.getSelectedIndex() == 0) {
			comparator = 1;
		} else {
			comparator = -1;
		}

		int blockSize = 1;

		this.entity = TabbedView.activePanel.getEntity();

		boolean ok = true;

		String input = entity.getUrl();
		String[] niz = entity.getUrl().split("\\.");
		String output = niz[0] + "O." + niz[1];

		System.out.println(limit + "--");

		while (blockSize <= limit) {

			System.out.println(blockSize);

			if (ok) {

				try {
					mergeBlocksOfSize(blockSize, input, output);
					ok = false;
				} catch (IOException e) {
					e.printStackTrace();
				}
			} else {
				try {
					mergeBlocksOfSize(blockSize, output, input);
					ok = true;
				} catch (IOException e) {
					e.printStackTrace();
				}

			}

			blockSize *= 2;
		}

		if (ok) {
			copyFromTo(output, input);
		}
		copyFromTo(input, output);
		copyFromTo(output, input);

		System.out.println("Finish sorting!");

		JOptionPane.showMessageDialog(new JFrame(), "Successful sort!", "Success", JOptionPane.INFORMATION_MESSAGE);

		return;
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

}