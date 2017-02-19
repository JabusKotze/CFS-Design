package guiEWComp;
/*
 * @author JJR Kotze (email 15692183@sun.ac.za)
 * For Department of Civil Engineering, Stellenbosch University
 * 29 October 2012 (Version 1.0 of CFS design)
 */


import java.awt.BorderLayout;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import gui.*;
import java.awt.*;
public class AddSectionTypeFrameEWComp extends JFrame implements ActionListener{
			protected String name;
			protected EWCompFrame cF;
			
			public AddSectionTypeFrameEWComp(String name, EWCompFrame cF){
				super(name);
				this.cF = cF;
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
					new LippedCsectionComp("Create Lipped Channel",cF);
				}else if(e.getActionCommand().equals("Lipped Z")){
					new LippedZComp("Create Lipped Z Section",cF);
				}else if(e.getActionCommand().equals("General Section")){
					new GeneralSecComp("Create General Section",cF);
				}
				
			}
			
			
}