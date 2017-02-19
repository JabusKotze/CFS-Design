package gui;
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

public class DSMBENDING extends JFrame implements ActionListener{
	
	
	protected JTextField Mol, Mod, Length, maxMoment, ultMomentCap, Fail;
	protected JLabel heading, L, moc, mol, mod, mbe, mbl, mbd, mb, m, MM, MPa, kNm;
	protected JTable table;
	protected JPanel pane3;
	protected JComboBox secCombo, matCombo,Axis;
	protected SectionDatabase sd = Parameters.getSectionDatabase();
	protected MaterialDatabase md = Parameters.getMaterialDatabase();
	
	public DSMBENDING(String title){
		super(title);
		this.setVisible(true);
		
		this.setLayout(new BorderLayout());
		
		JPanel pane1 = new JPanel();
		JPanel pane2 = new JPanel();
		pane3 = new JPanel();
		
		pane1.setLayout(new BorderLayout());
		pane2.setLayout(new BorderLayout());
		pane3.setLayout(new BorderLayout());
		
		pane1.setPreferredSize(new Dimension(850,150));
		pane2.setPreferredSize(new Dimension(850,150));
		pane3.setPreferredSize(new Dimension(850,45));
		
		JPanel panel1 = new JPanel();
		JPanel panel2 = new JPanel();
		JPanel panel3 = new JPanel();
		JPanel panel4 = new JPanel();
		JPanel panel5 = new JPanel();
		JPanel panel6 = new JPanel();
		
		
		panel1.setLayout(new GridLayout(3,5));
		panel2.setLayout(new GridLayout(1,1));
		panel3.setLayout(new GridLayout(1,6));
		panel4.setLayout(new GridLayout(1,1));
		panel5.setLayout(new GridLayout(1,6));
		panel6.setLayout(new GridLayout(1,1));
		
		
		panel1.setPreferredSize(new Dimension(700,100));
		panel2.setPreferredSize(new Dimension(700,50));
		panel3.setPreferredSize(new Dimension(700,50));
		panel4.setPreferredSize(new Dimension(700,50));
		panel5.setPreferredSize(new Dimension(700,50));
		panel6.setPreferredSize(new Dimension(700,30));
		
		
		//Panel1
		JLabel sname = new JLabel("Section Name: ");
		JLabel mname = new JLabel("Material Name: ");
		JLabel MOL = new JLabel("Local Load Factor: ");
		JLabel MOD = new JLabel("Distortional Load Factor: ");
		JLabel axis = new JLabel("Axis of Bending");
		JLabel none1 = new JLabel("");
		JLabel none2 = new JLabel("");
		JLabel none3 = new JLabel("");
		
		sname.setHorizontalAlignment(SwingConstants.RIGHT);
		mname.setHorizontalAlignment(SwingConstants.RIGHT);
		MOL.setHorizontalAlignment(SwingConstants.RIGHT);
		MOD.setHorizontalAlignment(SwingConstants.RIGHT);
		axis.setHorizontalAlignment(SwingConstants.RIGHT);
		
		Mol = new JTextField("");
		Mod = new JTextField("");
		
		
		secCombo = new JComboBox();
		matCombo = new JComboBox();
		Axis = new JComboBox();
		Mol.setHorizontalAlignment(SwingConstants.LEFT);
		Mod.setHorizontalAlignment(SwingConstants.LEFT);
			
		
		JButton addMaterial = new JButton("Add Material");
		JButton addSection = new JButton("Add Section");
		addMaterial.setHorizontalAlignment(SwingConstants.CENTER);
		addSection.setHorizontalAlignment(SwingConstants.CENTER);
		addMaterial.addActionListener(new AddMaterialButtonListener(this));
		addSection.addActionListener(new AddSectionButtonListener(this));
		
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
		//populate Axis combo box
				Axis.addItem("X");
				Axis.addItem("Y");
		
		panel1.add(sname);
		panel1.add(secCombo);
		panel1.add(addSection);
		panel1.add(MOL);
		panel1.add(Mol);
		
		panel1.add(mname);
		panel1.add(matCombo);
		panel1.add(addMaterial);
		panel1.add(MOD);
		panel1.add(Mod);
		
		panel1.add(axis);
		panel1.add(Axis);
		panel1.add(none1);
		panel1.add(none2);
		panel1.add(none3);
		pane1.add(panel1,BorderLayout.NORTH);
		
		
		//tooltip text
		Mol.setToolTipText("Local buckling factor from CUFSM graph: the first turning point");
		Mod.setToolTipText("Distortional buckling factor from CUFSM graph: the second turning point");
		
		//panel 2
		JButton setloadfact = new JButton("Set Load Factors");
		setloadfact.setHorizontalAlignment(SwingConstants.CENTER);
		setloadfact.addActionListener(new SetLoadFactorButtonListener(this));
		panel2.add(setloadfact);
		pane1.add(panel2,BorderLayout.SOUTH);
		this.add(pane1,BorderLayout.NORTH);
		
		//panel 3
		Length = new JTextField("");
		maxMoment = new JTextField("");
		Length.setHorizontalAlignment(SwingConstants.LEFT);
		maxMoment.setHorizontalAlignment(SwingConstants.LEFT);
		
		JLabel length = new JLabel("Length = ");
		JLabel maxmoment = new JLabel("Moment to Resist: ");
		JLabel mm = new JLabel("mm");
		JLabel kN = new JLabel("kNm");
		mm.setHorizontalAlignment(SwingConstants.LEFT);
		kN.setHorizontalAlignment(SwingConstants.LEFT);
		length.setHorizontalAlignment(SwingConstants.RIGHT);
		maxmoment.setHorizontalAlignment(SwingConstants.RIGHT);
		
		panel3.add(length);
		panel3.add(Length);
		panel3.add(mm);
		panel3.add(maxmoment);
		panel3.add(maxMoment);
		panel3.add(kN);
		pane2.add(panel3,BorderLayout.NORTH);
		
		
		//Panel 4
		JButton calculate = new JButton("Calculate");
		calculate.setHorizontalAlignment(SwingConstants.CENTER);
		calculate.addActionListener(new CalculateButtonListener(this));
		calculate.setSize(new Dimension(100,20));
		panel4.add(calculate);
		pane2.add(panel4,BorderLayout.CENTER);
		
		//Panel5
		ultMomentCap = new JTextField("");
		Fail = new JTextField("");
		ultMomentCap.setHorizontalAlignment(SwingConstants.LEFT);
		Fail.setHorizontalAlignment(SwingConstants.LEFT);
		
		JLabel uMC = new JLabel("Bending Capacity = ");
		JLabel fail = new JLabel("Will the section fail: ");
		JLabel kN2 = new JLabel("kNm");
		JLabel none = new JLabel("");
		none.setHorizontalAlignment(SwingConstants.LEFT);
		kN2.setHorizontalAlignment(SwingConstants.LEFT);
		uMC.setHorizontalAlignment(SwingConstants.RIGHT);
		fail.setHorizontalAlignment(SwingConstants.RIGHT);
		
		panel5.add(uMC);
		panel5.add(ultMomentCap);
		panel5.add(kN2);
		panel5.add(fail);
		panel5.add(Fail);
		panel5.add(none);
		pane2.add(panel5,BorderLayout.SOUTH);
		this.add(pane2,BorderLayout.CENTER);
		
		//Panel 6
		JButton genTable = new JButton("Generate Table for Different Effective Lengths");
		genTable.setHorizontalAlignment(SwingConstants.CENTER);
		genTable.addActionListener(this);
		panel6.add(genTable);
		
		pane3.add(panel6,BorderLayout.CENTER);
		this.add(pane3,BorderLayout.SOUTH);
		
		this.setLocation(10, 10);
		pack();
		
	}
	
//	public static void main(String[] args) {
//		
//		new DSMBENDING("DSM BENDING");
//		
//	}

	
	public void actionPerformed(ActionEvent e) {
		if(e.getActionCommand().equals("Generate Table for Different Effective Lengths")){
			String heading = secCombo.getSelectedItem().toString();
			new TableBendingFrame(heading,this);
		}
		
	}

}
