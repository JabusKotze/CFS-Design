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


public class LoadApplicationFrameBend extends JFrame implements ActionListener{
	protected String title;
	protected JComboBox distr,appl,bracing;
	protected Loads L = GetMemory.getLoads();
	protected Moments M = GetMemory.getMoments();
	
	public LoadApplicationFrameBend(String title){
		super(title);
		this.setVisible(true);
		this.setLayout(new BorderLayout());
		
		
		//panels
		JPanel panel1 = new JPanel();
		JPanel panel2 = new JPanel();
		
		panel1.setLayout(new GridLayout(3,2));
		panel2.setLayout(new GridLayout(1,1));
		
		panel1.setPreferredSize(new Dimension(450,150));
		panel2.setPreferredSize(new Dimension(450,40));
		
		//north
		JLabel d = new JLabel("Load Distribution : ");
		JLabel app = new JLabel("Load Application : ");
		JLabel brace = new JLabel("Bracing : ");
		
		d.setHorizontalAlignment(SwingConstants.RIGHT);
		app.setHorizontalAlignment(SwingConstants.RIGHT);
		brace.setHorizontalAlignment(SwingConstants.RIGHT);
		
		distr = new JComboBox();
		appl = new JComboBox();
		bracing = new JComboBox();
		
		distr.addItem("Uniformly Distributed Simply Supported");
		distr.addItem("Other");
		
		appl.addItem("Shear Centre");
		appl.addItem("Compression Flange");
		appl.addItem("Tension Flange");
		
		bracing.addItem("No Bracing");
		bracing.addItem("Centre Bracing");
		bracing.addItem("Third point Bracing");
		
		panel1.add(d);
		panel1.add(distr);
		panel1.add(app);
		panel1.add(appl);
		panel1.add(brace);
		panel1.add(bracing);
		
		this.add(panel1,BorderLayout.NORTH);
		
		//south
		JButton set = new JButton("SET");
		set.setHorizontalAlignment(SwingConstants.CENTER);
		set.addActionListener(this);
		
		panel2.add(set);
		
		this.add(panel2,BorderLayout.SOUTH);
		
		this.setLocation(300, 300);
		pack();
		
		
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getActionCommand().equals("SET")){
			String d = distr.getSelectedItem().toString();
			String app = appl.getSelectedItem().toString();
			String brace = bracing.getSelectedItem().toString();
			
			if(d.equals("Uniformly Distributed Simply Supported")){
				d = "uniSS";
				M.setMmax(0.0);
				new MomentsFrameBend("Moments");
			}else{
				d = "Other";
				new MomentsFrameBend("Moments");
			}
			
			if(app.equals("Shear Centre")){
				app = "SC";
			}else if(app.equals("Tension Flange")){
				app = "TF";				
			}else{
				app = "CF";
			}
			
			if(brace.equals("No Bracing")){
				brace = "No";
			}else if(brace.equals("Centre Bracing")){
				brace = "C";
			}else{
				brace = "T";
			}
			
			L.setBracing(brace);
			L.setLoadApp(app);
			L.setLoadDistr(d);
			
			this.setVisible(false);
			
		}
		
	}
}
