package gui;
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
import loadFactorscufsm.LoadFactorsCUFSM;
import materials.*;
import guiComp.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class DSMCOMPRESSION extends JFrame implements ActionListener{
	
	
	protected JTextField Fol, Fod, Length, appliedForce, ultCompressionCap, Fail;
	
	
	public JComboBox secCombo;
	public JComboBox matCombo;
	protected SectionDatabase sd = Parameters.getSectionDatabase();
	protected MaterialDatabase md = Parameters.getMaterialDatabase();
	private LoadFactorsCufsm lfcufsm = Parameters.getLoadFactorsCufsm();
	public DSMCOMPRESSION(String title){
		super(title);
		this.setVisible(true);
			
		this.setLayout(new BorderLayout());
		
		JPanel pane1 = new JPanel();
		JPanel pane2 = new JPanel();
		JPanel pane3 = new JPanel();
		
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
		
		
		panel1.setLayout(new GridLayout(2,5));
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
		JLabel FOL = new JLabel("Local load factor: ");
		JLabel FOD = new JLabel("Distortional load factor: ");
		
		
		
		sname.setHorizontalAlignment(SwingConstants.RIGHT);
		mname.setHorizontalAlignment(SwingConstants.RIGHT);
		FOL.setHorizontalAlignment(SwingConstants.RIGHT);
		FOD.setHorizontalAlignment(SwingConstants.RIGHT);
		
		
		Fol = new JTextField("");
		Fod = new JTextField("");
		
		
		secCombo = new JComboBox();
		matCombo = new JComboBox();
		
		Fol.setHorizontalAlignment(SwingConstants.LEFT);
		Fod.setHorizontalAlignment(SwingConstants.LEFT);
		
		//tooltip text
		Fol.setToolTipText("Local buckling factor from CUFSM graph: the first turning point");
		Fod.setToolTipText("Distortional buckling factor from CUFSM graph: the second turning point");
					
		//buttons
		JButton addMaterial = new JButton("Add Material");
		JButton addSection = new JButton("Add Section");
		addMaterial.setHorizontalAlignment(SwingConstants.CENTER);
		addSection.setHorizontalAlignment(SwingConstants.CENTER);
		addMaterial.addActionListener(this);
		addSection.addActionListener(this);
		
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
		
		panel1.add(sname);
		panel1.add(secCombo);
		panel1.add(addSection);
		panel1.add(FOL);
		panel1.add(Fol);
		
		panel1.add(mname);
		panel1.add(matCombo);
		panel1.add(addMaterial);
		panel1.add(FOD);
		panel1.add(Fod);
		
		
		pane1.add(panel1,BorderLayout.NORTH);		
		
		
		
		//panel 2
		JButton setloadfact = new JButton("Set Load Factors");
		setloadfact.setHorizontalAlignment(SwingConstants.CENTER);
		setloadfact.addActionListener(this);
		panel2.add(setloadfact);
		pane1.add(panel2,BorderLayout.SOUTH);
		this.add(pane1,BorderLayout.NORTH);
		
		//panel 3
		Length = new JTextField("");
		appliedForce = new JTextField("");
		Length.setHorizontalAlignment(SwingConstants.LEFT);
		appliedForce.setHorizontalAlignment(SwingConstants.LEFT);
		
		JLabel length = new JLabel("Length = ");
		JLabel maxComp = new JLabel("Max Force from Analysis");
		JLabel mm = new JLabel("mm");
		JLabel kN = new JLabel("kN");
		mm.setHorizontalAlignment(SwingConstants.LEFT);
		kN.setHorizontalAlignment(SwingConstants.LEFT);
		length.setHorizontalAlignment(SwingConstants.RIGHT);
		maxComp.setHorizontalAlignment(SwingConstants.RIGHT);
		
		panel3.add(length);
		panel3.add(Length);
		panel3.add(mm);
		panel3.add(maxComp);
		panel3.add(appliedForce);
		panel3.add(kN);
		pane2.add(panel3,BorderLayout.NORTH);
		
		
		//Panel 4
		JButton calculate = new JButton("Calculate");
		calculate.setHorizontalAlignment(SwingConstants.CENTER);
		calculate.addActionListener(this);
		calculate.setSize(new Dimension(100,20));
		panel4.add(calculate);
		pane2.add(panel4,BorderLayout.CENTER);
		
		//Panel5
		ultCompressionCap = new JTextField("");
		Fail = new JTextField("");
		ultCompressionCap.setHorizontalAlignment(SwingConstants.LEFT);
		Fail.setHorizontalAlignment(SwingConstants.LEFT);
		
		JLabel uCC = new JLabel("Compression Capacity = ");
		JLabel fail = new JLabel("Will the section fail: ");
		JLabel kN2 = new JLabel("kN");
		JLabel none = new JLabel("");
		none.setHorizontalAlignment(SwingConstants.LEFT);
		kN2.setHorizontalAlignment(SwingConstants.LEFT);
		uCC.setHorizontalAlignment(SwingConstants.RIGHT);
		fail.setHorizontalAlignment(SwingConstants.RIGHT);
		
		panel5.add(uCC);
		panel5.add(ultCompressionCap);
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
//		new DSMCOMPRESSION("DSM COMPRESSION");
//		
//	}

	
	public void actionPerformed(ActionEvent e) {
		if(e.getActionCommand().equals("Generate Table for Different Effective Lengths")){
			
			String heading = secCombo.getSelectedItem().toString();
			new TableCompressionFrameDSM(heading,this);
			
		}else if(e.getActionCommand().equals("Add Material")){
			
			new AddMaterialFrameComp("Add Material",this);
			
		}else if(e.getActionCommand().equals("Add Section")){
			
			new AddSectionTypeFrameComp("Section Type",this);
			
		}else if(e.getActionCommand().equals("Set Load Factors")){
			
			new LoadFactorFrameComp("Set Load Factors",this);
			
		}else if(e.getActionCommand().equals("Calculate")){
			
			DecimalFormat df = new DecimalFormat("#.####");
			String secName = secCombo.getSelectedItem().toString();
			String matName = matCombo.getSelectedItem().toString();			
			double Lx = Double.parseDouble(Length.getText());
			double fol = Double.parseDouble(Fol.getText());
			double fod = Double.parseDouble(Fod.getText());
			double maxAppliedForce = Double.parseDouble(appliedForce.getText());
			double fy = 0.0;			
			double[]Moed = new double[lfcufsm.getLoadFactors().length];
			
			for(int i = 0;i<lfcufsm.getLoadFactors().length;i++){
				Moed[i] = lfcufsm.getLoadFactors()[i];
			}
			
			double Mo =1.0;
			int multiples = (int)lfcufsm.getMultiples();
			int turningpoint = (int)lfcufsm.getTurningpoint();
			
			LoadFactorsCUFSM MoeNow = new LoadFactorsCUFSM(Moed,Mo,multiples,turningpoint);  //this will go and find the appropriate factor as listed in Moed[] for the determination of Mo
			DSMCompression DSM = new DSMCompression(secName,matName,maxAppliedForce,MoeNow.getLoadFactor(Lx),fol,fod,Lx,Lx,Lx,0.0,fy);
			
			ultCompressionCap.setText(df.format(DSM.getDesignCapacity()));
			
			if(DSM.getDesignCapacity()<=Math.abs(maxAppliedForce)){
				Fail.setText("YES");
			}else{
				Fail.setText("NO");
			}			
		}
		
	}

}
