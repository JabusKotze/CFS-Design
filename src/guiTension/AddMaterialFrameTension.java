package guiTension;
/*
 * @author JJR Kotze (email 15692183@sun.ac.za)
 * For Department of Civil Engineering, Stellenbosch University
 * 29 October 2012 (Version 1.0 of CFS design)
 */
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import section.*;
import materials.*;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import gui.*;

public class AddMaterialFrameTension extends JFrame implements ActionListener {
		protected TensionFrame tF;
		
		protected String name;
		protected JTextField mname, fy,fu,E,G,v;
		protected MaterialDatabase md = Parameters.getMaterialDatabase();
		
		public AddMaterialFrameTension(String name, TensionFrame tF){
			super(name);
			this.tF = tF;
			this.setVisible(true);				
			this.setLayout(new BorderLayout());
			this.setPreferredSize(new Dimension(450,350));
			
			JPanel panel1 = new JPanel();
			JPanel panel2 = new JPanel();
			JPanel panel3 = new JPanel();
			
			panel1.setLayout(new GridLayout(7,1));
			panel2.setLayout(new GridLayout(7,1));
			panel3.setLayout(new GridLayout(7,1));
			panel1.setPreferredSize(new Dimension(200,350));
			panel2.setPreferredSize(new Dimension(150,350));
			panel3.setPreferredSize(new Dimension(100,350));
			
			JLabel matn = new JLabel("Material Name: ");
			JLabel yield = new JLabel("Yield stress (fy): ");
			JLabel tensile = new JLabel("Ultimate Tensile stress (fu): ");
			JLabel youngs = new JLabel("E :");
			JLabel g = new JLabel("G :");
			JLabel V = new JLabel("poisson (v) : ");
			
			matn.setHorizontalAlignment(SwingConstants.RIGHT);
			yield.setHorizontalAlignment(SwingConstants.RIGHT);
			tensile.setHorizontalAlignment(SwingConstants.RIGHT);
			youngs.setHorizontalAlignment(SwingConstants.RIGHT);
			g.setHorizontalAlignment(SwingConstants.RIGHT);
			V.setHorizontalAlignment(SwingConstants.RIGHT);
			
			
			JLabel mpa1 = new JLabel("MPa");
			JLabel mpa2 = new JLabel("MPa");
			JLabel mpa3 = new JLabel("MPa");
			JLabel mpa4 = new JLabel("MPa");
			JLabel none1 = new JLabel("");
			JLabel none2 = new JLabel("");
			JLabel none3 = new JLabel("");
			JLabel none4 = new JLabel("");
			
			mpa1.setHorizontalAlignment(SwingConstants.LEFT);
			mpa2.setHorizontalAlignment(SwingConstants.LEFT);
			mpa3.setHorizontalAlignment(SwingConstants.LEFT);
			mpa4.setHorizontalAlignment(SwingConstants.LEFT);
			
			//Jtextfields
			mname = new JTextField("");
			fy = new JTextField("");
			fu = new JTextField("");
			E = new JTextField("");
			G = new JTextField("");
			v = new JTextField("");
			
			mname.setHorizontalAlignment(SwingConstants.LEFT);
			fy.setHorizontalAlignment(SwingConstants.LEFT);
			fu.setHorizontalAlignment(SwingConstants.LEFT);
			E.setHorizontalAlignment(SwingConstants.LEFT);
			G.setHorizontalAlignment(SwingConstants.LEFT);
			v.setHorizontalAlignment(SwingConstants.LEFT);
			
			//JButton
			JButton addMat = new JButton("Add Material");
			addMat.setHorizontalAlignment(SwingConstants.CENTER);
			addMat.addActionListener(this);
			
			panel1.add(matn);
			panel1.add(yield);
			panel1.add(tensile);
			panel1.add(youngs);
			panel1.add(g);
			panel1.add(V);
			panel1.add(none1);
			
			panel2.add(mname);
			panel2.add(fy);
			panel2.add(fu);
			panel2.add(E);
			panel2.add(G);
			panel2.add(v);
			panel2.add(addMat);
			
			panel3.add(none2);
			panel3.add(mpa1);
			panel3.add(mpa2);
			panel3.add(mpa3);
			panel3.add(mpa4);
			panel3.add(none3);
			panel3.add(none4);
			
			this.add(panel1,BorderLayout.WEST);
			this.add(panel2,BorderLayout.CENTER);
			this.add(panel3,BorderLayout.EAST);
			
			this.setLocation(710, 10);
			pack();
			
		}
	
	public void actionPerformed(ActionEvent e) {
		if(e.getActionCommand().equals("Add Material")){
			String name = mname.getText();
			double yield = Double.parseDouble(fy.getText());
			double tensile  = Double.parseDouble(fu.getText());
			double youngs = Double.parseDouble(E.getText());
			double g = Double.parseDouble(E.getText());
			double V = Double.parseDouble(v.getText());
			
			md.addMaterial(name, new MaterialProperties(name,yield,tensile,youngs,V,g));
			tF.matCombo.addItem(name);
			
		}
		
	}

}

