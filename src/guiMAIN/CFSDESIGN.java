package guiMAIN;
/*
 * @author JJR Kotze (email 15692183@sun.ac.za)
 * For Department of Civil Engineering, Stellenbosch University
 * 29 October 2012 (Version 1.0 of CFS design)
 */

/*
 * MAIN METHOD OF CFS DESIGN version 1.0
 */
import java.awt.*;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import gui.*;
import guiTension.*;
import guiEWBending.*;
import guiEWComp.*;

public class CFSDESIGN extends JFrame implements ActionListener{
	

	protected String title;
	
	public CFSDESIGN(String title){
		super(title);
		this.setVisible(true);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setLayout(new GridLayout(6,1));
		int height = 300;
		int width = 400;
		this.setPreferredSize(new Dimension(width,height));
		
		//Buttons
		JButton dsmb = new JButton("DSM: Bending");
		JButton dsmc = new JButton("DSM: Compression");
		JButton ewb = new JButton("EWM: Bending");
		JButton ewc = new JButton("EWM: Compression");
		JButton tension = new JButton("Tension");
		
		//Author label
		JLabel author = new JLabel("Author: JJR Kotze (15692183@sun.ac.za)");
		author.setHorizontalAlignment(SwingConstants.CENTER);
		
		
		dsmb.setHorizontalAlignment(SwingConstants.CENTER);
		dsmc.setHorizontalAlignment(SwingConstants.CENTER);
		ewb.setHorizontalAlignment(SwingConstants.CENTER);
		ewc.setHorizontalAlignment(SwingConstants.CENTER);
		tension.setHorizontalAlignment(SwingConstants.CENTER);
		
		
		dsmb.addActionListener(this);
		dsmc.addActionListener(this);
		ewb.addActionListener(this);
		ewc.addActionListener(this);
		tension.addActionListener(this);
		
		
		this.add(dsmb);
		this.add(dsmc);
		this.add(ewb);
		this.add(ewc);
		this.add(tension);
		this.add(author);
		
		
		this.setLocation(600, 500);
		pack();
		
		
	}
	
	public void actionPerformed(ActionEvent e) {
		if(e.getActionCommand().equals("DSM: Bending")){
			new DSMBENDING("DSM BENDING");
		}else if(e.getActionCommand().equals("DSM: Compression")){
			new DSMCOMPRESSION("DSM COMPRESSION");
		}else if(e.getActionCommand().equals("EWM: Bending")){
			new EWbendingFrame("EWM BENDING");
		}else if(e.getActionCommand().equals("EWM: Compression")){
			new EWCompFrame("EWM COMPRESSION");
		}else if(e.getActionCommand().equals("Tension")){
			new TensionFrame("Tension Design");
		}
		
	}
	
	
	public static void main(String[] args) {
		
		new CFSDESIGN("CFS DESIGN");
		
	}

}
