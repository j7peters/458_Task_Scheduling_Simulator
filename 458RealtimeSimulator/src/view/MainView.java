package view;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;

import javax.swing.JFrame;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;

public class MainView extends JFrame implements ActionListener{

	public static void main(String[] args) 
	{
		final MainView frame = new MainView();
		frame.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e)
			{
				System.exit(0);
			}
		});
		frame.setVisible(true);	
	}
	
	MainView()
	{	
		super("Real Time Systems Simulator: CprE 458");
		setSize(700,500);

		//TODO add views here
	}

	
	
	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		
	}

}
