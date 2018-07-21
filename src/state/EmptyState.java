package state;

import javax.swing.JPanel;

import models.Entity;

public class EmptyState extends State{

	@Override
	public void changePanel() {
		setLayout(null);
	}

	@Override
	public void changeEntity(Entity entity) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void submit() {
		// TODO Auto-generated method stub
		
	}
	
	
	
}
