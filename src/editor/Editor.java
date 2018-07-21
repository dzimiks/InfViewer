package editor;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.io.File;

import javax.swing.*;

import models.InformationResource;
import view.tree.TreeView;

@SuppressWarnings("serial")
public class Editor extends JFrame{
	
	private JPanel top;
	private TextArea editor;
	private File file;
	private JPanel bottom;
	
	//Only added for update methode
	private InformationResource ir;
	public Editor(InformationResource ir, TreeView tw){
		this.ir = ir;
		top = new ButtonsTop(ir, this);
		editor = new TextArea(ir);
		bottom = new ButtonsBottom(ir, editor, this, tw);
		
		//TODO Deserijalizovati ir
		
		init();
	}
	
	public void init(){
		this.setSize(new Dimension(900,1000));
		this.setTitle("Best editor");
		
		this.setLayout(new BorderLayout());
		this.add(top, BorderLayout.NORTH);
		this.add(editor, BorderLayout.CENTER);
		this.add(bottom, BorderLayout.SOUTH);
		
		JScrollPane scroll = new JScrollPane (editor, 
		JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);

		add(scroll);
	}
	
	public void update(){
		try {
			editor.init(ir);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
		this.repaint();
	}
	
}
