package guiEWComp;
/*
 * @author JJR Kotze (email 15692183@sun.ac.za)
 * For Department of Civil Engineering, Stellenbosch University
 * 29 October 2012 (Version 1.0 of CFS design)
 */
import java.awt.*;
import javax.swing.*;
import section.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import guiMemory.*;

public class CompFrame extends JFrame implements ActionListener{
	
	protected String title;
	protected JTextField mComp;
	protected Compression C = GetMemory.getComp();
	public CompFrame(String title){
		super(title);
		this.setLayout(new BorderLayout());
		this.setVisible(true);
		
		JPanel pane1 = new JPanel();
		JPanel pane2 = new JPanel();
		pane1.setLayout(new GridLayout(1,3));
		pane2.setLayout(new GridLayout(1,1));
		pane1.setPreferredSize(new Dimension(300,50));
		pane1.setPreferredSize(new Dimension(300,50));
		
		JLabel comp = new JLabel("Max Compression from Analysis");
		JLabel kN = new JLabel("kN");
		
		mComp = new JTextField(Double.toString(C.getMaxComp()));
		
		comp.setHorizontalAlignment(SwingConstants.RIGHT);
		mComp.setHorizontalAlignment(SwingConstants.RIGHT);
		kN.setHorizontalAlignment(SwingConstants.LEFT);
		
		pane1.add(comp);
		pane1.add(mComp);
		pane1.add(kN);
		
		this.add(pane1,BorderLayout.NORTH);
		
		//button
		JButton submit = new JButton("Submit");
		submit.setHorizontalAlignment(SwingConstants.CENTER);
		submit.addActionListener(this);
		pane2.add(submit);
		
		this.add(pane2,BorderLayout.SOUTH);
		this.setLocation(300,300);
		pack();
		
	}

	
	public void actionPerformed(ActionEvent e) {
		if(e.getActionCommand().equals("Submit")){
			
			double c = Double.parseDouble(mComp.getText());
			C.setMaxComp(c);
			this.setVisible(false);
		}
		
	}

}
