package listeners;


import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import view.StudentContent;



public class StudentContentListener implements MouseListener {

	private StudentContent studentBio;
	
	public StudentContentListener(StudentContent studentBio) {
		this.studentBio = studentBio;
	}
	
	@Override
	public void mouseClicked(MouseEvent e) {
		StudentContent frame = new StudentContent(studentBio.getStudent());
		frame.setVisible(true);
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		
	}

	@Override
	public void mouseExited(MouseEvent e) {
		
	}

	@Override
	public void mousePressed(MouseEvent e) {
		
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		
	}
}
