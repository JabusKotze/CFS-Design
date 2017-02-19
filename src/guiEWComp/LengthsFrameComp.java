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

public class LengthsFrameComp extends JFrame implements ActionListener {
	
	protected String title;
	protected JTextField Lx,Ly,Lz,Mult;
	protected Lengths L = GetMemory.getLengths();
	public LengthsFrameComp(String title){
		super(title);
		this.setVisible(true);
		this.setLayout(new BorderLayout());
		
		JPanel panel1 = new JPanel();
		JPanel panel2 = new JPanel();
		
		panel1.setLayout(new GridLayout(4,3));
		panel2.setLayout(new GridLayout(1,1));
		panel1.setPreferredSize(new Dimension(300,200));
		panel2.setPreferredSize(new Dimension(300,50));
		
		
		//labels
		JLabel lx = new JLabel("Lx: ");
		JLabel ly = new JLabel("Ly: ");
		JLabel lz = new JLabel("Lz: ");
		JLabel mult = new JLabel("Multiples: ");
		JLabel mm1 = new JLabel("mm");
		JLabel mm2 = new JLabel("mm");
		JLabel mm3 = new JLabel("mm");
		JLabel mm4 = new JLabel("mm");
		
		lx.setHorizontalAlignment(SwingConstants.RIGHT);
		ly.setHorizontalAlignment(SwingConstants.RIGHT);
		lz.setHorizontalAlignment(SwingConstants.RIGHT);
		mult.setHorizontalAlignment(SwingConstants.RIGHT);
		mm1.setHorizontalAlignment(SwingConstants.LEFT);
		mm2.setHorizontalAlignment(SwingConstants.LEFT);
		mm3.setHorizontalAlignment(SwingConstants.LEFT);
		mm4.setHorizontalAlignment(SwingConstants.LEFT);
		
		Lx = new JTextField(Double.toString(L.getLx()));
		Ly = new JTextField(Double.toString(L.getLy()));
		Lz = new JTextField(Double.toString(L.getLz()));
		Mult = new JTextField(Double.toString(L.getMult()));
		
		Lx.setHorizontalAlignment(SwingConstants.RIGHT);
		Ly.setHorizontalAlignment(SwingConstants.RIGHT);
		Lz.setHorizontalAlignment(SwingConstants.RIGHT);
		Mult.setHorizontalAlignment(SwingConstants.RIGHT);
		
		panel1.add(lx);
		panel1.add(Lx);
		panel1.add(mm1);
		panel1.add(ly);
		panel1.add(Ly);
		panel1.add(mm2);
		panel1.add(lz);
		panel1.add(Lz);
		panel1.add(mm3);
		panel1.add(mult);
		panel1.add(Mult);
		panel1.add(mm4);
		
		this.add(panel1,BorderLayout.NORTH);
		
		//button
		JButton set = new JButton("Set Lengths");
		set.setHorizontalAlignment(SwingConstants.CENTER);
		set.addActionListener(this);
		
		panel2.add(set);
		
		this.add(panel2,BorderLayout.SOUTH);
		this.setLocation(300, 300);
		pack();
		
		
		
	}
	
	public void actionPerformed(ActionEvent e) {
		if(e.getActionCommand().equals("Set Lengths")){
			
			double lx = Double.parseDouble(Lx.getText());
			double ly = Double.parseDouble(Ly.getText());
			double lz = Double.parseDouble(Lz.getText());
			int mult = (int)Double.parseDouble(Mult.getText());
			L.setLx(lx);
			L.setLy(ly);
			L.setLz(lz);
			L.setMultiples(mult);		
			
			this.setVisible(false);
		}
		
	}

}
