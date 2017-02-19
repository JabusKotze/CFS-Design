package gui;
/*
 * @author JJR Kotze (email 15692183@sun.ac.za)
 * For Department of Civil Engineering, Stellenbosch University
 * 29 October 2012 (Version 1.0 of CFS design)
 */
import java.awt.BorderLayout;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import java.awt.*;
public class AddSectionTypeFrame extends JFrame implements ActionListener{
			protected String name;
			protected DSMBENDING dsmb;
			
			public AddSectionTypeFrame(String name,DSMBENDING dsmb){
				super(name);
				this.dsmb = dsmb;
				this.setVisible(true);				
				this.setLayout(new GridLayout(3,1));
				this.setPreferredSize(new Dimension(300,150));
				//Buttons
				JButton LC = new JButton("Lipped Channel");
				JButton LZ = new JButton("Lipped Z");
				JButton GS = new JButton("General Section");
				LC.setHorizontalAlignment(SwingConstants.CENTER);
				LZ.setHorizontalAlignment(SwingConstants.CENTER);
				GS.setHorizontalAlignment(SwingConstants.CENTER);
				LC.addActionListener(this);
				LZ.addActionListener(this);
				GS.addActionListener(this);
				
				this.add(LC);
				this.add(LZ);
				this.add(GS);
				
				
				this.setLocation(710, 10);
				pack();
			}

			
			public void actionPerformed(ActionEvent e) {
				if(e.getActionCommand().equals("Lipped Channel")){
					new LippedChannelFrame("Create Lipped Channel",dsmb);
				}else if(e.getActionCommand().equals("Lipped Z")){
					new LippedZFrame("Create Lipped Z Section",dsmb);
				}else if(e.getActionCommand().equals("General Section")){
					new GeneralSectionFrame("Create General Section",dsmb);
				}
				
			}
			
			
}
