package state;

import javax.swing.JPanel;

import models.Entity;

public abstract class State extends JPanel {
	
	public abstract void changePanel();

	public abstract void changeEntity(Entity entity);

	public abstract void submit();
}
