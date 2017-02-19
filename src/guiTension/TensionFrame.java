package guiTension;
/*
 * @author JJR Kotze (email 15692183@sun.ac.za)
 * For Department of Civil Engineering, Stellenbosch University
 * 29 October 2012 (Version 1.0 of CFS design)
 */
import java.awt.BorderLayout;
import java.awt.Dimension;
import javax.swing.*;
import dSM.DSMCompression;
import java.text.DecimalFormat;
import java.util.*;
import section.*;
import java.awt.*;
import capacityDesign.Tension;
import materials.*;
import guiComp.*;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class TensionFrame extends JFrame implements ActionListener{
	
	protected JTextField fastners,kt,appliedForce, diameter,Capacity,Fail;
	protected JComboBox secCombo,matCombo;
	protected String title;
	protected DecimalFormat df = new DecimalFormat("#.####");
	protected SectionDatabase sd = Parameters.getSectionDatabase();
	protected MaterialDatabase md = Parameters.getMaterialDatabase();
	
	public TensionFrame(String title){
		super(title);		
		this.setVisible(true);
		this.setLayout(new BorderLayout());
		//Panels
		JPanel panel1 = new JPanel();
		JPanel panel2 = new JPanel();
		JPanel panel3 = new JPanel();
		
		panel1.setLayout(new GridLayout(6,3));
		panel2.setLayout(new GridLayout(1,1));
		panel3.setLayout(new GridLayout(2,3));
		
		panel1.setPreferredSize(new Dimension(400,300));
		panel2.setPreferredSize(new Dimension(400,40));
		panel3.setPreferredSize(new Dimension(400,80));
		
		//Labels
		JLabel secname = new JLabel("Section Name : ");
		JLabel matname = new JLabel("Material Name : ");
		JLabel nfast = new JLabel("number Fasteners : ");
		JLabel KT = new JLabel("Correction Factor (kt) : ");
		JLabel aForce = new JLabel("Applied Force : ");
		JLabel hdia = new JLabel("Hole Diameter : ");
		JLabel capacity = new JLabel("Capacity : ");
		JLabel fail = new JLabel("Fail? : ");
		
		JLabel none1 = new JLabel("");
		JLabel none2 = new JLabel("");
		JLabel none3 = new JLabel("");
		JLabel mm = new JLabel("mm");
		JLabel kN1 = new JLabel("kN");
		JLabel kN2 = new JLabel("kN");
		
		secname.setHorizontalAlignment(SwingConstants.RIGHT);
		matname.setHorizontalAlignment(SwingConstants.RIGHT);
		nfast.setHorizontalAlignment(SwingConstants.RIGHT);
		KT.setHorizontalAlignment(SwingConstants.RIGHT);
		aForce.setHorizontalAlignment(SwingConstants.RIGHT);
		hdia.setHorizontalAlignment(SwingConstants.RIGHT);
		capacity.setHorizontalAlignment(SwingConstants.RIGHT);
		fail.setHorizontalAlignment(SwingConstants.RIGHT);
		
		none1.setHorizontalAlignment(SwingConstants.LEFT);
		none2.setHorizontalAlignment(SwingConstants.LEFT);
		none3.setHorizontalAlignment(SwingConstants.LEFT);
		mm.setHorizontalAlignment(SwingConstants.LEFT);
		kN1.setHorizontalAlignment(SwingConstants.LEFT);
		kN2.setHorizontalAlignment(SwingConstants.LEFT);
		
		//Text fields
		fastners = new JTextField("");
		kt = new JTextField("");
		appliedForce = new JTextField("");
		diameter = new JTextField("");
		Capacity = new JTextField("");
		Fail = new JTextField("");
		
		fastners.setHorizontalAlignment(SwingConstants.RIGHT);
		kt.setHorizontalAlignment(SwingConstants.RIGHT);
		appliedForce.setHorizontalAlignment(SwingConstants.RIGHT);
		diameter.setHorizontalAlignment(SwingConstants.RIGHT);
		Capacity.setHorizontalAlignment(SwingConstants.RIGHT);
		Fail.setHorizontalAlignment(SwingConstants.RIGHT);
		
		
		//Buttons
		JButton addSec = new JButton("Add Section");
		JButton addMat = new JButton("Add Material");
		JButton calculate = new JButton("Calculate");
		
		addSec.setHorizontalAlignment(SwingConstants.CENTER);
		addMat.setHorizontalAlignment(SwingConstants.CENTER);
		calculate.setHorizontalAlignment(SwingConstants.CENTER);
		
		addSec.addActionListener(this);
		addMat.addActionListener(this);
		calculate.addActionListener(this);
		
		//combo box
		secCombo = new JComboBox();
		matCombo = new JComboBox();
		
		//populate seccombo box lC sections
		Map<String,LippedCSection> lc = sd.getLCsections(); 
		Set<String> names = lc.keySet();
		Iterator<String> namesLC = names.iterator();
		while(namesLC.hasNext()){
			String key = namesLC.next();
			secCombo.addItem(key);
		}
		//populate seccombo box zl sections
		Map<String,LippedZSection> zl = sd.getZLsections();
		Set<String> names2 = zl.keySet();
		Iterator<String> namesZL = names2.iterator();
		while(namesZL.hasNext()){
			String key = namesZL.next();
			secCombo.addItem(key);
		}
		//populate seccombo box general sections
		Map<String,GeneralSection> gs = sd.getGeneralsections();
		Set<String> names3 = gs.keySet();
		Iterator<String> namesGS = names3.iterator();
		while(namesGS.hasNext()){
			String key = namesGS.next();
			secCombo.addItem(key);
		}
		//populate matcombo box with all materials
		Map<String,MaterialProperties> materials = md.getMaterials();
		Set<String> matNames = materials.keySet();
		Iterator<String> matIter = matNames.iterator();
		while(matIter.hasNext()){
			String key = matIter.next();
			matCombo.addItem(key);
		}
		
		panel1.add(secname);
		panel1.add(secCombo);
		panel1.add(addSec);
		
		panel1.add(matname);
		panel1.add(matCombo);
		panel1.add(addMat);
		
		panel1.add(nfast);
		panel1.add(fastners);
		panel1.add(none1);
		
		panel1.add(KT);
		panel1.add(kt);
		panel1.add(none2);
		
		panel1.add(aForce);
		panel1.add(appliedForce);
		panel1.add(kN1);
		
		panel1.add(hdia);
		panel1.add(diameter);
		panel1.add(mm);
		
		panel2.add(calculate);
		
		panel3.add(capacity);
		panel3.add(Capacity);
		panel3.add(kN2);
		
		panel3.add(fail);
		panel3.add(Fail);
		panel3.add(none3);
		
		this.add(panel1,BorderLayout.NORTH);
		this.add(panel2,BorderLayout.CENTER);
		this.add(panel3,BorderLayout.SOUTH);
		
		this.setLocation(10,10);
		pack();	
		
	}
	
	public void actionPerformed(ActionEvent e) {
		if(e.getActionCommand().equals("Calculate")){
			String secN = secCombo.getSelectedItem().toString();
			String matN = matCombo.getSelectedItem().toString();
			double kT = Double.parseDouble(kt.getText());
			int nfast = (int)Double.parseDouble(fastners.getText());
			double dia = Double.parseDouble(diameter.getText());
			double force = Double.parseDouble(appliedForce.getText());
			
			Tension tens = new Tension(secN,matN,kT,nfast,dia,force);
			Capacity.setText(df.format(tens.ultimateSectionCapacity()/1000.));
			
			if(tens.willItFail()){
				Fail.setText("YES");
			}else{
				Fail.setText("NO");
			}
			
		}else if(e.getActionCommand().equals("Add Material")){
			
			new AddMaterialFrameTension("Add Material",this);
			
		}else if(e.getActionCommand().equals("Add Section")){
			
			new AddSectionTypeFrameTension("Section Type",this);
			
		}
		
	}

}
