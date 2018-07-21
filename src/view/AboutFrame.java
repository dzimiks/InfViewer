package view;

import javax.imageio.ImageIO;
import javax.swing.*;

import constants.Constants;
import listeners.StudentContentListener;
import models.Student;

import java.awt.*;
import java.io.File;
import java.io.IOException;

public class AboutFrame extends JFrame {
	
	/**
	 * VersionUID za serijalizaciju.
	 */
	private static final long serialVersionUID = 1;

	private static AboutFrame instance = null;
	
	private StudentContent milosContent;
	private StudentContent mihailoContent;
	private StudentContent katarinaContent;
	private StudentContent vanjaContent;
	
	private StudentPanel panel1;
	private StudentPanel panel2;
	private StudentPanel panel3;
	private StudentPanel panel4;
	
	private AboutFrame() {
		super();
	}
	
	private AboutFrame(Student student1, Student student2, Student student3, Student student4) {
		GridLayout layout = new GridLayout(4, 1);
		layout.setVgap(7);
		setLayout(layout);
		
		panel1 = new StudentPanel(student1);
		panel1.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.ORANGE));
		panel1.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		panel1.setToolTipText("Saznaj više o Milošu");
		
		milosContent = new StudentContent(student1);
		panel1.addMouseListener(new StudentContentListener(milosContent));
		
		panel2 = new StudentPanel(student2);		
		panel2.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.ORANGE));
		panel2.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		panel2.setToolTipText("Saznaj više o Mihailu");
		
		mihailoContent = new StudentContent(student2);
		panel2.addMouseListener(new StudentContentListener(mihailoContent));
		
		panel3 = new StudentPanel(student3);
		panel3.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.ORANGE));
		panel3.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		panel3.setToolTipText("Saznaj više o Katarini");
		
		katarinaContent = new StudentContent(student3);
		panel3.addMouseListener(new StudentContentListener(katarinaContent));
		
		panel4 = new StudentPanel(student4);
		panel4.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		panel4.setToolTipText("Saznaj više o Vanji");
		
		vanjaContent = new StudentContent(student4);
		panel4.addMouseListener(new StudentContentListener(vanjaContent));
		
		add(panel1);
		add(panel2);
		add(panel3);
		add(panel4);

		// Favicon ikonica
		try {
            this.setIconImage(ImageIO.read(new File(Constants.FAVICON)));
        }
        catch (IOException ex) {
            ex.printStackTrace();
        }
		
		setSize(400, 700);
		setMinimumSize(new Dimension(330, 700));
		setTitle("TensorFlow - O nama");
        setLocationRelativeTo(null);
	}
	
	public static AboutFrame getInstance() {

		String milosBio = "Volim društvene igre, životinje, epsku i naučnu fantastiku.\n"
					   + "Životni moto: Sve u životu se dešava sa razlogom, nekad je taj razlog "
					   + "loša odluka.";
		milosBio = milosBio.replaceAll("\n", "<br><br>");
		
		String mikiBio = "Volim programiranje, pse, Rocket League i ovu "		
					   + "ispod mene.\nŽivotni moto: Radi sad da ne moraš kasnije.";
		mikiBio = mikiBio.replaceAll("\n", "<br><br>");
		
		String kacaBio = "Volim mačke, programiranje i ovog iznad mene.\n"
					   + "Životni moto: if (jutro()) { return to_sleep(); }";
		kacaBio = kacaBio.replaceAll("\n", "<br><br>");
		
		String vanjaBio = "Volim basket, Plavšića i da programiram do kasno u noć.\n"
					   + "Životni moto: Svakoga dana, u svakom pogledu, sve više napredujem.";
		vanjaBio = vanjaBio.replaceAll("\n", "<br><br>");
		
		if (instance == null) {
			
			Student s1 = new Student("Miloš", "Milunović", "RN 17/16", Constants.MILOS, milosBio, "mmilunovic16@raf.rs");
			Student s2 = new Student("Mihailo", "Vignjević", "RN 13/16", Constants.MIHAILO, mikiBio, "mvignjevic16@raf.rs");
			Student s3 = new Student("Katarina", "Panić", "RN 14/16", Constants.KATARINA, kacaBio, "kpanic16@raf.rs");
			Student s4 = new Student("Vanja", "Paunović", "RN 35/16", Constants.VANJA, vanjaBio, "vpaunovic16@raf.rs");
			
			instance = new AboutFrame(s1, s2, s3, s4);
		}
		
		return instance;
	}
}