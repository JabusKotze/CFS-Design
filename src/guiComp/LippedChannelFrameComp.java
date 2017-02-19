package guiComp;
/*
 * @author JJR Kotze (email 15692183@sun.ac.za)
 * For Department of Civil Engineering, Stellenbosch University
 * 29 October 2012 (Version 1.0 of CFS design)
 */
import java.awt.*;
import javax.swing.*;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DecimalFormat;
import section.*;
import gui.*;

public class LippedChannelFrameComp extends JFrame implements ActionListener{
	protected DSMCOMPRESSION dsmc;	
	protected String name;
	protected JTextField h,w,coatedThickness,d,mass,area,centroidY,iXX,Zex,rx,iYY,ZeyL,ZeyW,ry,J,By;
	protected DecimalFormat df = new DecimalFormat("#.####");
	public LippedChannelFrameComp(String name,DSMCOMPRESSION dsmc){
		super(name);		
		this.setVisible(true);
		this.setLayout(new BorderLayout());
		this.dsmc = dsmc;
		
		JPanel panel1 = new JPanel();
		JPanel panel2 = new JPanel();
		JPanel panel3 = new JPanel();
		
		panel1.setLayout(new GridLayout(17,1));
		panel1.setPreferredSize(new Dimension(150,700));
		
		panel2.setLayout(new GridLayout(17,1));
		panel2.setPreferredSize(new Dimension(150,700));
		
		panel3.setLayout(new GridLayout(17,1));
		panel3.setPreferredSize(new Dimension(150,700));
		
		JLabel height = new JLabel("Height");
		JLabel width = new JLabel("Width");
		JLabel cT = new JLabel("Coated Thickness");
		JLabel lipL = new JLabel("Lip Length");
		JLabel m = new JLabel("Mass");
		JLabel A = new JLabel("Area");
		JLabel Cy = new JLabel("Centroid aY");
		JLabel ixx = new JLabel("iXX");
		JLabel zex = new JLabel("Zex");
		JLabel rX = new JLabel("rx");
		JLabel iyy = new JLabel("iYY");
		JLabel zeyl = new JLabel("Zey lip at top");
		JLabel zeyw = new JLabel("Zey web at top");
		JLabel rY = new JLabel("ry");
		JLabel j = new JLabel("J");
		JLabel by = new JLabel("Monosymmetric Constant");
		
		JLabel mma = new JLabel("mm");
		JLabel mmb = new JLabel("mm");
		JLabel mmc = new JLabel("mm");
		JLabel mmd = new JLabel("mm");
		JLabel mme = new JLabel("mm");
		JLabel mmf = new JLabel("mm");
		JLabel mmg = new JLabel("mm");
		
		JLabel kgm = new JLabel("kg/m");
		JLabel mm2 = new JLabel("x1000 mm^2");
		JLabel mm64a = new JLabel("x10(6) mm^4");
		JLabel mm64b = new JLabel("x10(6) mm^4");
		JLabel mm3a = new JLabel("x1000 mm^3");
		JLabel mm3b = new JLabel("x1000 mm^3");
		JLabel mm3c = new JLabel("x1000 mm^3");
		JLabel mm4 = new JLabel("x1000 mm^4");
		JLabel no = new JLabel("");
		
		mma.setHorizontalAlignment(SwingConstants.LEFT); 
		mmb.setHorizontalAlignment(SwingConstants.LEFT); 
		mmc.setHorizontalAlignment(SwingConstants.LEFT); 
		mmd.setHorizontalAlignment(SwingConstants.LEFT); 
		mme.setHorizontalAlignment(SwingConstants.LEFT); 
		mmf.setHorizontalAlignment(SwingConstants.LEFT); 
		mmg.setHorizontalAlignment(SwingConstants.LEFT); 
		
		kgm.setHorizontalAlignment(SwingConstants.LEFT); 
		mm2.setHorizontalAlignment(SwingConstants.LEFT); 
		
		mm64a.setHorizontalAlignment(SwingConstants.LEFT); 
		mm64b.setHorizontalAlignment(SwingConstants.LEFT); 		
		
		mm3a.setHorizontalAlignment(SwingConstants.LEFT);
		mm3b.setHorizontalAlignment(SwingConstants.LEFT);
		mm3c.setHorizontalAlignment(SwingConstants.LEFT);
		mm4.setHorizontalAlignment(SwingConstants.LEFT); 
		
		height.setHorizontalAlignment(SwingConstants.RIGHT); 
		width.setHorizontalAlignment(SwingConstants.RIGHT); 
		cT.setHorizontalAlignment(SwingConstants.RIGHT); 
		lipL.setHorizontalAlignment(SwingConstants.RIGHT); 
		m.setHorizontalAlignment(SwingConstants.RIGHT); 
		A.setHorizontalAlignment(SwingConstants.RIGHT); 
		Cy.setHorizontalAlignment(SwingConstants.RIGHT); 
		ixx.setHorizontalAlignment(SwingConstants.RIGHT); 
		zex.setHorizontalAlignment(SwingConstants.RIGHT); 
		rX.setHorizontalAlignment(SwingConstants.RIGHT); 
		iyy.setHorizontalAlignment(SwingConstants.RIGHT); 
		zeyl.setHorizontalAlignment(SwingConstants.RIGHT); 
		zeyw.setHorizontalAlignment(SwingConstants.RIGHT); 
		rY.setHorizontalAlignment(SwingConstants.RIGHT); 
		j.setHorizontalAlignment(SwingConstants.RIGHT); 
		by.setHorizontalAlignment(SwingConstants.RIGHT); 
		
		h = new JTextField("0");
		w =  new JTextField("0");
		coatedThickness =  new JTextField("0");
		d =  new JTextField("0");
		mass =  new JTextField("0");
		area =  new JTextField("0");
		centroidY =  new JTextField("0");
		iXX =  new JTextField("0");
		Zex =  new JTextField("0");
		rx =  new JTextField("0");
		iYY =  new JTextField("0");
		ZeyL =  new JTextField("0");
		ZeyW =  new JTextField("0");
		ry =  new JTextField("0");
		J =  new JTextField("0");
		By =  new JTextField("0");
		
		
		h.setHorizontalAlignment(SwingConstants.RIGHT); 
		w.setHorizontalAlignment(SwingConstants.RIGHT); 
		coatedThickness.setHorizontalAlignment(SwingConstants.RIGHT); 
		d.setHorizontalAlignment(SwingConstants.RIGHT); 
		mass.setHorizontalAlignment(SwingConstants.RIGHT); 
		area.setHorizontalAlignment(SwingConstants.RIGHT);
		centroidY.setHorizontalAlignment(SwingConstants.RIGHT); 
		iXX.setHorizontalAlignment(SwingConstants.RIGHT); 
		Zex.setHorizontalAlignment(SwingConstants.RIGHT); 
		rx.setHorizontalAlignment(SwingConstants.RIGHT); 
		iYY.setHorizontalAlignment(SwingConstants.RIGHT); 
		ZeyL.setHorizontalAlignment(SwingConstants.RIGHT); 
		ZeyW.setHorizontalAlignment(SwingConstants.RIGHT); 
		ry.setHorizontalAlignment(SwingConstants.RIGHT); 
		J.setHorizontalAlignment(SwingConstants.RIGHT); 
		By.setHorizontalAlignment(SwingConstants.RIGHT); 
		
		//Tooltip text
				area.setToolTipText("Specify as 0.0 if not konwn and press calculate to see the values");
				centroidY.setToolTipText("Specify as 0.0 if not konwn and press calculate to see the values");
				iXX.setToolTipText("Specify as 0.0 if not konwn and press calculate to see the values");
				Zex.setToolTipText("Specify as 0.0 if not konwn and press calculate to see the values");
				rx.setToolTipText("Specify as 0.0 if not konwn and press calculate to see the values");
				iYY.setToolTipText("Specify as 0.0 if not konwn and press calculate to see the values");
				ZeyL.setToolTipText("Specify as 0.0 if not konwn and press calculate to see the values");
				ZeyW.setToolTipText("Specify as 0.0 if not konwn and press calculate to see the values");
				ry.setToolTipText("Specify as 0.0 if not konwn and press calculate to see the values");
				J.setToolTipText("Specify as 0.0 if not konwn and press calculate to see the values");
				By.setToolTipText("Specify as 0.0 if not konwn and press calculate to see the values");
		
		panel1.add(height);
		panel1.add(width);
		panel1.add(cT);
		panel1.add(lipL);
		panel1.add(m);
		panel1.add(A);
		panel1.add(Cy);
		panel1.add(ixx);
		panel1.add(zex);
		panel1.add(rX);
		panel1.add(iyy);
		panel1.add(zeyl);
		panel1.add(zeyw);
		panel1.add(rY);
		panel1.add(j);
		panel1.add(by);
		
		
		
		panel2.add(h);
		panel3.add(mma);		
		
		panel2.add(w);
		panel3.add(mmb);		
		
		panel2.add(coatedThickness);
		panel3.add(mmc);
		
		panel2.add(d);
		panel3.add(mmd);
		
		panel2.add(mass);
		panel3.add(kgm);
		
		panel2.add(area);
		panel3.add(mm2);
		
		panel2.add(centroidY);
		panel3.add(mme);
		
		panel2.add(iXX);
		panel3.add(mm64a);
		
		panel2.add(Zex);
		panel3.add(mm3a);
		
		panel2.add(rx);
		panel3.add(mmf);
		
		panel2.add(iYY);
		panel3.add(mm64b);
		
		panel2.add(ZeyL);
		panel3.add(mm3b);
		
		panel2.add(ZeyW);
		panel3.add(mm3c);
		
		panel2.add(ry);
		panel3.add(mmg);
		
		panel2.add(J);
		panel3.add(mm4);
		
		panel2.add(By);
		panel3.add(no);
		
		//Buttons
		JButton submit = new JButton("Submit");
		JButton calc = new JButton("Calculate Properties");
		submit.setHorizontalAlignment(SwingConstants.CENTER);
		calc.setHorizontalAlignment(SwingConstants.CENTER);
		submit.addActionListener(this);
		calc.addActionListener(this);
		
		panel1.add(submit);
		panel2.add(calc);
		panel3.add(no);
		
		this.add(panel1,BorderLayout.WEST);
		this.add(panel2, BorderLayout.CENTER);
		this.add(panel3, BorderLayout.EAST);
		
		this.setLocation(1010, 10);
		pack();
		
		
	}

	
	public void actionPerformed(ActionEvent e) {
		if(e.getActionCommand().equals("Submit")){
			
			double hoogte = Double.parseDouble(h.getText());
			double wydte = Double.parseDouble(w.getText());
			double lip = Double.parseDouble(d.getText());
			double ct = Double.parseDouble(coatedThickness.getText());
			double m = Double.parseDouble(mass.getText());
			double a= Double.parseDouble(area.getText());
			double ay = Double.parseDouble(centroidY.getText());
			double ixx = Double.parseDouble(iXX.getText());
			double zex = Double.parseDouble(Zex.getText());
			double rX = Double.parseDouble(rx.getText());
			double iyy = Double.parseDouble(iYY.getText());
			double zeyl = Double.parseDouble(ZeyL.getText());
			double zeyw = Double.parseDouble(ZeyW.getText());
			double rY = Double.parseDouble(ry.getText());
			double j = Double.parseDouble(J.getText());
			double by = Double.parseDouble(By.getText());
			String name = "CL "+hoogte+"x"+wydte+"x"+lip+"x"+ct;
			
			LippedCSection lc = new LippedCSection(name,hoogte,wydte,ct,lip,m,a,ay,ixx,zex,rX,iyy,zeyl,zeyw,rY,j,by);
			SectionDatabase sd = Parameters.getSectionDatabase();
			sd.addLCSection(name,lc);
			dsmc.secCombo.addItem(name);
			dsmc.secCombo.setSelectedItem(name);
			this.setVisible(false);
			
		}else if(e.getActionCommand().equals("Calculate Properties")){
			double hoogte = Double.parseDouble(h.getText());
			double wydte = Double.parseDouble(w.getText());
			double lip = Double.parseDouble(d.getText());
			double ct = Double.parseDouble(coatedThickness.getText());
			double m = Double.parseDouble(mass.getText());
			double a= Double.parseDouble(area.getText());
			double ay = Double.parseDouble(centroidY.getText());
			double ixx = Double.parseDouble(iXX.getText());
			double zex = Double.parseDouble(Zex.getText());
			double rX = Double.parseDouble(rx.getText());
			double iyy = Double.parseDouble(iYY.getText());
			double zeyl = Double.parseDouble(ZeyL.getText());
			double zeyw = Double.parseDouble(ZeyW.getText());
			double rY = Double.parseDouble(ry.getText());
			double j = Double.parseDouble(J.getText());
			double by = Double.parseDouble(By.getText());
			String name = "CL "+hoogte+"x"+wydte+"x"+lip+"x"+ct;
			
			LippedCSection lc = new LippedCSection(name,hoogte,wydte,ct,lip,m,a,ay,ixx,zex,rX,iyy,zeyl,zeyw,rY,j,by);
			a = lc.getArea()/1000.;
			ay = lc.getCentroidY();
			ixx = lc.getIXX()/1000000.;
			zex = lc.getSectionModulusX()/1000.;
			rX = lc.getPolarRX();
			iyy = lc.getIYY()/1000000.;
			zeyl = lc.getSectionModulusYLipTop()/1000.;
			zeyw = lc.getSectionModulusYWebTop()/1000.;
			rY = lc.getPolarRY();
			j = lc.getJ()/1000.;
			by = lc.getMonosymmetricConstBy();
			
			area.setText(df.format(a));
			centroidY.setText(df.format(ay));
			iXX.setText(df.format(ixx));
			Zex.setText(df.format(zex));
			rx.setText(df.format(rX));
			iYY.setText(df.format(iyy));
			ZeyL.setText(df.format(zeyl));
			ZeyW.setText(df.format(zeyw));
			ry.setText(df.format(rY));
			J.setText(df.format(j));
			By.setText(df.format(by));
			
		}
		
	}
}

