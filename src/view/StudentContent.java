package view;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;

import constants.Constants;
import models.Student;

public class StudentContent extends JDialog{
private static StudentContent instance = null;
	
	private Student student;
	
	public StudentContent() {
		super();
	}
	
	public StudentContent(Student student) {
		this.student = student;

		JPanel panel = new JPanel();
		panel.setBorder(BorderFactory.createEmptyBorder(10, 5, 10, 5));
		panel.setLayout(new BorderLayout(20, 30));
		
		JPanel info = new JPanel();
		BoxLayout box = new BoxLayout(info, BoxLayout.Y_AXIS);
		BufferedImage slika = null;

		info.setLayout(box);
		
		try {
			slika = ImageIO.read(new File(student.getPath()));
		} 
		catch (IOException e) {
			e.printStackTrace();
		}
		
		JLabel img = new JLabel(new ImageIcon(slika));
		JLabel ime = new JLabel(student.getIme() + " " + student.getPrezime());
		JLabel indeks = new JLabel(student.getIndeks());
		JLabel imejl = new JLabel(student.getImejl());
		JLabel bio = new JLabel("<html>" + student.getOpis() + "</html>");

		ime.setAlignmentX(Component.CENTER_ALIGNMENT);
		indeks.setAlignmentX(Component.CENTER_ALIGNMENT);
		imejl.setAlignmentX(Component.CENTER_ALIGNMENT);
		bio.setAlignmentX(Component.CENTER_ALIGNMENT);
		
		info.add(ime);
		info.add(indeks);
		info.add(imejl);
		info.add(Box.createVerticalStrut(20));
		info.add(bio);

		panel.add(img, BorderLayout.NORTH);
		panel.add(info, BorderLayout.CENTER);
		add(panel);
		
		// Favicon ikonica
		try {
            this.setIconImage(ImageIO.read(new File(Constants.FAVICON)));
        }
        catch (IOException ex) {
            ex.printStackTrace();
        }
		
		setSize(new Dimension(350, 450));
		setResizable(false);
		setTitle("TensorFlow - " + student.getIme() + " " + student.getPrezime());
        setLocationRelativeTo(null);
	}
	
	public static StudentContent getInstance() {
		
		if (instance == null) {
			instance = new StudentContent();
		}
		
		return instance;
	}

	public Student getStudent() {
		return student;
	}

	public void setStudent(Student student) {
		this.student = student;
	}
}
