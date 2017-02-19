package guiTension;
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
import gui.*;

public class GeneralSectionFrameTension extends JFrame implements ActionListener {
	protected TensionFrame tF;	
	protected String title;
	protected JTextField secname,area,nT,I,y;
	
	public GeneralSectionFrameTension(String title, TensionFrame tF){
		super(title);
		this.tF = tF;
		this.setVisible(true);
		this.setLayout(new BorderLayout());
		
		
		//Panels
		JPanel panel1 = new JPanel();
		JPanel panel2 = new JPanel();
		panel1.setLayout(new GridLayout(5,3));
		panel2.setLayout(new GridLayout(1,1));
		panel1.setPreferredSize(new Dimension(380,200));
		panel2.setPreferredSize(new Dimension(380,35));
			
		
		
		//Labels
		JLabel naam = new JLabel("Name : ");
		JLabel Area = new JLabel("Area : ");
		JLabel nt = new JLabel("Nominal Thickness : ");
		JLabel i = new JLabel("I : ");
		JLabel Y = new JLabel("Y : ");
		
		JLabel none1 = new JLabel("");
		JLabel none2 = new JLabel("");
		JLabel none3 = new JLabel("");
		JLabel smm = new JLabel("x1000 mm^2");
		JLabel mm1 = new JLabel("mm");
		JLabel mm2 = new JLabel("mm");
		JLabel mmI = new JLabel("x10(6) mm^4");
		
		naam.setHorizontalAlignment(SwingConstants.RIGHT);
		Area.setHorizontalAlignment(SwingConstants.RIGHT);
		nt.setHorizontalAlignment(SwingConstants.RIGHT);
		i.setHorizontalAlignment(SwingConstants.RIGHT);
		Y.setHorizontalAlignment(SwingConstants.RIGHT);
		
		none1.setHorizontalAlignment(SwingConstants.LEFT);
		none2.setHorizontalAlignment(SwingConstants.LEFT);
		none3.setHorizontalAlignment(SwingConstants.LEFT);
		smm.setHorizontalAlignment(SwingConstants.LEFT);
		mm1.setHorizontalAlignment(SwingConstants.LEFT);
		mm2.setHorizontalAlignment(SwingConstants.LEFT);
		mmI.setHorizontalAlignment(SwingConstants.LEFT);
		
		//textfields
		secname = new JTextField("");
		area = new JTextField("");
		nT = new JTextField("");
		I = new JTextField("");
		y = new JTextField("");
		
		secname.setHorizontalAlignment(SwingConstants.RIGHT);
		area.setHorizontalAlignment(SwingConstants.RIGHT);
		nT.setHorizontalAlignment(SwingConstants.RIGHT);
		I.setHorizontalAlignment(SwingConstants.RIGHT);
		y.setHorizontalAlignment(SwingConstants.RIGHT);
		
		//tooltip text
		y.setToolTipText("Distance from extreme compression fiber to centroid to calculate Z");
		I.setToolTipText("Second Moment of Area");
		nT.setToolTipText("Full thickness including galvanised portion");
		
		//button
		JButton submit = new JButton("Submit");
		submit.setHorizontalAlignment(SwingConstants.CENTER);
		submit.addActionListener(this);
		
		//add components
		panel1.add(naam);
		panel1.add(secname);
		panel1.add(none1);
		
		panel1.add(Area);
		panel1.add(area);
		panel1.add(smm);
		
		panel1.add(nt);
		panel1.add(nT);
		panel1.add(mm1);
		
		panel1.add(i);
		panel1.add(I);
		panel1.add(mmI);
		
		panel1.add(Y);
		panel1.add(y);
		panel1.add(mm2);
		
		panel2.add(submit);
		
		this.add(panel1,BorderLayout.NORTH);
		this.add(panel2,BorderLayout.SOUTH);
		
		this.setLocation(1010, 10);
		pack();
		
	}

	
	public void actionPerformed(ActionEvent e) {
		if(e.getActionCommand().equals("Submit")){
			String N = "GS "+secname.getText();
			double Area = Double.parseDouble(area.getText());
			double t = Double.parseDouble(nT.getText());
			double i = Double.parseDouble(I.getText());
			double Y = Double.parseDouble(y.getText());
			
			GeneralSection gs = new GeneralSection(N,Area,t,i,Y);
			SectionDatabase sd = Parameters.getSectionDatabase();
			sd.addGeneralSection(N,gs);
			tF.secCombo.addItem(N);
		}
		
	}

}
