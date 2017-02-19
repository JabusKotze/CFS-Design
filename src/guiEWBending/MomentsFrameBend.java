package guiEWBending;
/*
 * @author JJR Kotze (email 15692183@sun.ac.za)
 * For Department of Civil Engineering, Stellenbosch University
 * 29 October 2012 (Version 1.0 of CFS design)
 */
import java.awt.BorderLayout;
import java.awt.Dimension;
import javax.swing.*;
import java.util.*;
import section.*;
import java.awt.*;
import materials.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import guiMemory.*;

public class MomentsFrameBend extends JFrame implements ActionListener {
	protected String title;
	protected JTextField M1,M2,M3,M4,M5,Mmax,Mresist;
	protected Moments M = GetMemory.getMoments();
	
	
	public MomentsFrameBend(String title){
		super(title);
		this.setVisible(true);
		this.setLayout(new BorderLayout());
		
		//panels
		JPanel panel1 = new JPanel();
		JPanel panel2 = new JPanel();
		
		panel1.setLayout(new GridLayout(7,3));
		panel2.setLayout(new GridLayout(1,1));
		
		panel1.setPreferredSize(new Dimension(500,250));
		panel2.setPreferredSize(new Dimension(500,40));
		
		//North
		JLabel m1 = new JLabel("M1 = ");
		JLabel m2 = new JLabel("M2 = ");
		JLabel m3 = new JLabel("M3 = ");
		JLabel m4 = new JLabel("M4 = ");
		JLabel m5 = new JLabel("M5 = ");
		JLabel mmax = new JLabel("Mmax = ");
		JLabel mresist = new JLabel("Moment to resist = ");
		
		m1.setHorizontalAlignment(SwingConstants.RIGHT);
		m2.setHorizontalAlignment(SwingConstants.RIGHT);
		m3.setHorizontalAlignment(SwingConstants.RIGHT);
		m4.setHorizontalAlignment(SwingConstants.RIGHT);
		m5.setHorizontalAlignment(SwingConstants.RIGHT);
		mmax.setHorizontalAlignment(SwingConstants.RIGHT);
		mresist.setHorizontalAlignment(SwingConstants.RIGHT);
		
		M1 = new JTextField(Double.toString(M.getM1()));
		M2 = new JTextField(Double.toString(M.getM2()));
		M3 = new JTextField(Double.toString(M.getM3()));
		M4 = new JTextField(Double.toString(M.getM4()));
		M5 = new JTextField(Double.toString(M.getM5()));
		Mmax = new JTextField(Double.toString(M.getMmax()));
		Mresist = new JTextField(Double.toString(M.getMaxAppliedMoment()));
		
		M1.setHorizontalAlignment(SwingConstants.RIGHT);
		M2.setHorizontalAlignment(SwingConstants.RIGHT);
		M3.setHorizontalAlignment(SwingConstants.RIGHT);
		M4.setHorizontalAlignment(SwingConstants.RIGHT);
		M5.setHorizontalAlignment(SwingConstants.RIGHT);
		Mmax.setHorizontalAlignment(SwingConstants.RIGHT);
		Mresist.setHorizontalAlignment(SwingConstants.RIGHT);
		
		//tooltip text
		M1.setToolTipText("Smaller bending Moment At edge of unbraced length, for SS set to 0.0");
		M2.setToolTipText("Larger bending moment at edge of un-braced length, for SS set to 0.0");
		M3.setToolTipText("Absolute Moment at quarter point of unbraced length, set as 0 if not known");
		M4.setToolTipText("Absolute Centre point moment of unbraced length, set as 0 if not known");
		M5.setToolTipText("Absolute Moments at three quarter point of unbraced length, set as 0 if not known");
		Mmax.setToolTipText("Absolute Maximum moment within unbraced length, set as 0 if not known");
		Mresist.setToolTipText("Moment section should resist, specify as negative if Compression occurs at web of C sections");
		
		
		JLabel knm1 = new JLabel("kNm");
		JLabel knm2 = new JLabel("kNm");
		JLabel knm3 = new JLabel("kNm");
		JLabel knm4 = new JLabel("kNm");
		JLabel knm5 = new JLabel("kNm");
		JLabel knm6 = new JLabel("kNm");
		JLabel knm7 = new JLabel("kNm");
		
		knm1.setHorizontalAlignment(SwingConstants.LEFT);
		knm2.setHorizontalAlignment(SwingConstants.LEFT);
		knm3.setHorizontalAlignment(SwingConstants.LEFT);
		knm4.setHorizontalAlignment(SwingConstants.LEFT);
		knm5.setHorizontalAlignment(SwingConstants.LEFT);
		knm6.setHorizontalAlignment(SwingConstants.LEFT);
		knm7.setHorizontalAlignment(SwingConstants.LEFT);
		
		panel1.add(m1);
		panel1.add(M1);
		panel1.add(knm1);		
		panel1.add(m2);
		panel1.add(M2);
		panel1.add(knm2);		
		panel1.add(m3);
		panel1.add(M3);
		panel1.add(knm3);		
		panel1.add(m4);
		panel1.add(M4);
		panel1.add(knm4);		
		panel1.add(m5);
		panel1.add(M5);
		panel1.add(knm5);		
		panel1.add(mmax);
		panel1.add(Mmax);
		panel1.add(knm6);		
		panel1.add(mresist);
		panel1.add(Mresist);
		panel1.add(knm7);
		
		this.add(panel1,BorderLayout.NORTH);
		
		//South
		JButton set = new JButton("SET");
		set.setHorizontalAlignment(SwingConstants.CENTER);
		set.addActionListener(this);
		panel2.add(set);
		
		this.add(panel2,BorderLayout.SOUTH);
		this.setLocation(300,300);
		pack();		
		
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getActionCommand().equals("SET")){
			
			M.setM1(Double.parseDouble(M1.getText()));
			M.setM2(Double.parseDouble(M2.getText()));
			M.setM3(Double.parseDouble(M3.getText()));
			M.setM4(Double.parseDouble(M4.getText()));
			M.setM5(Double.parseDouble(M5.getText()));
			M.setMmax(Double.parseDouble(Mmax.getText()));
			M.setMaxAppliedMoment(Double.parseDouble(Mresist.getText()));
			
			this.setVisible(false);
			
		}
		
	}

}
