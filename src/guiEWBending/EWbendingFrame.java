package guiEWBending;
/*
 * @author JJR Kotze (email 15692183@sun.ac.za)
 * For Department of Civil Engineering, Stellenbosch University
 * 29 October 2012 (Version 1.0 of CFS design)
 */
import java.awt.BorderLayout;
import java.awt.Dimension;
import javax.swing.*;
import capacityDesign.LCBending;
import capacityDesign.LZBending;
import java.util.*;
import section.*;
import java.awt.*;
import materials.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import guiEWComp.*;
import guiMemory.*;
import java.text.*;

public class EWbendingFrame extends JFrame implements ActionListener {
	protected String title;
	protected JTextField Capacity,Fail;
	protected JComboBox secCombo,matCombo,Axis;
	protected JCheckBox distCheck;
	protected SectionDatabase sd = Parameters.getSectionDatabase();
	protected MaterialDatabase md = Parameters.getMaterialDatabase();
	protected Moments M = GetMemory.getMoments();
	protected Lengths Lengths = GetMemory.getLengths();
	protected Loads L = GetMemory.getLoads();
	protected DecimalFormat df = new DecimalFormat("#.####");
	
	public EWbendingFrame(String title){
		super(title);
		this.setVisible(true);
		this.setLayout(new BorderLayout());
		//this.setPreferredSize(new Dimension(800,400));
		
		JPanel panel1 = new JPanel();
		JPanel panel2 = new JPanel();
		JPanel panel3 = new JPanel();
		
		panel1.setLayout(new GridLayout(4,3));
		panel2.setLayout(new GridLayout(4,1));
		panel3.setLayout(new BorderLayout());
		
		panel1.setPreferredSize(new Dimension(450,200));
		panel2.setPreferredSize(new Dimension(450,150));
		panel1.setPreferredSize(new Dimension(450,120));
		
		
		//North Layout
		distCheck = new JCheckBox();
		
		JLabel sname = new JLabel("Section Name: ");
		JLabel mname = new JLabel("Material Name: ");
		JLabel axis = new JLabel("Axis: ");
		JLabel distcheck = new JLabel("Distortional Check");
		JLabel none1 = new JLabel("");
		JLabel none2 = new JLabel("");
		
		sname.setHorizontalAlignment(SwingConstants.RIGHT);
		mname.setHorizontalAlignment(SwingConstants.RIGHT);
		axis.setHorizontalAlignment(SwingConstants.RIGHT);
		distcheck.setHorizontalAlignment(SwingConstants.RIGHT);
		none1.setHorizontalAlignment(SwingConstants.RIGHT);
		none2.setHorizontalAlignment(SwingConstants.RIGHT);
		
		secCombo = new JComboBox();
		matCombo = new JComboBox();
		Axis = new JComboBox();
		
		
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
		
		
		JButton addsec = new JButton("Add Section");
		JButton addmat = new JButton("Add Material");
		addsec.setHorizontalAlignment(SwingConstants.CENTER);
		addmat.setHorizontalAlignment(SwingConstants.CENTER);
		addsec.addActionListener(this);
		addmat.addActionListener(this);
		
		panel1.add(sname);
		panel1.add(secCombo);
		panel1.add(addsec);
		panel1.add(mname);
		panel1.add(matCombo);
		panel1.add(addmat);
		panel1.add(axis);
		panel1.add(Axis);
		panel1.add(none1);
		panel1.add(distcheck);
		panel1.add(distCheck);
		panel1.add(none2);
		
		this.add(panel1,BorderLayout.NORTH);
		
		//Centre
		JButton lengths = new JButton("Lengths");
		JButton loadappl = new JButton("Load Application");
		JButton moments = new JButton("Moments From Analysis");
		JButton calc = new JButton("Calculate");
		lengths.setHorizontalAlignment(SwingConstants.CENTER);
		loadappl.setHorizontalAlignment(SwingConstants.CENTER);
		moments.setHorizontalAlignment(SwingConstants.CENTER);
		calc.setHorizontalAlignment(SwingConstants.CENTER);
		lengths.addActionListener(this);
		loadappl.addActionListener(this);
		moments.addActionListener(this);
		calc.addActionListener(this);
		
		panel2.add(lengths);
		panel2.add(loadappl);
		panel2.add(moments);
		panel2.add(calc);
		
		this.add(panel2,BorderLayout.CENTER);
		
		//South layout
		JPanel pane1 = new JPanel();
		JPanel pane2 = new JPanel();
		pane1.setLayout(new GridLayout(2,3));
		pane2.setLayout(new GridLayout(1,1));
		//pane1.setPreferredSize(new Dimension(150,400));
		//pane1.setPreferredSize(new Dimension(40,400));
		
		//pane1
		JLabel capacity = new JLabel("Bending Capacity: ");
		JLabel fail = new JLabel("Will Section Fail: ");
		JLabel knm = new JLabel("kNm");
		JLabel none3 = new JLabel("");
		
		capacity.setHorizontalAlignment(SwingConstants.RIGHT);
		fail.setHorizontalAlignment(SwingConstants.RIGHT);
		knm.setHorizontalAlignment(SwingConstants.LEFT);
		none3.setHorizontalAlignment(SwingConstants.LEFT);
		
		Capacity = new JTextField("");
		Fail = new JTextField("");
		
		pane1.add(capacity);
		pane1.add(Capacity);
		pane1.add(knm);
		pane1.add(fail);
		pane1.add(Fail);
		pane1.add(none3);
		
		//pane2
		JButton genT = new JButton("Generate Effective Length Table");
		genT.setHorizontalAlignment(SwingConstants.CENTER);
		genT.addActionListener(this);
		pane2.add(genT);
		
		panel3.add(pane1,BorderLayout.NORTH);
		panel3.add(pane2,BorderLayout.SOUTH);
		
		this.add(panel3,BorderLayout.SOUTH);
		
		this.setLocation(10, 10);
		pack();
		
		
		
	}
	
	public void actionPerformed(ActionEvent e) {
		if(e.getActionCommand().equals("Add Section")){
			new AddSectionTypeFrameBend("Section Type",this);
		}else if(e.getActionCommand().equals("Add Material")){
			new AddMatFrameBend("Add Material",this);
		}else if(e.getActionCommand().equals("Lengths")){
			new LengthsFrameComp("Effective Lengths");
		}else if(e.getActionCommand().equals("Load Application")){
			new LoadApplicationFrameBend("Load Applications");
		}else if(e.getActionCommand().equals("Moments From Analysis")){
			new MomentsFrameBend("Set Moments");
		}else if(e.getActionCommand().equals("Calculate")){
			String matName = matCombo.getSelectedItem().toString();
			String secName = secCombo.getSelectedItem().toString();
			String axis = Axis.getSelectedItem().toString();
			double Mresist = M.getMaxAppliedMoment();
			double M1 =M.getM1();
			double M2 = M.getM2();
			double M3 = M.getM3();
			double M4 = M.getM4();
			double M5 = M.getM5();
			double Mmax = M.getMmax();
			double Lx = Lengths.getLx();
			double Ly = Lengths.getLy();
			double Lz = Lengths.getLz();
			boolean distC = distCheck.isSelected();
			
			String loadDistr = L.getDistr();
			String loadAppl = L.getLoadApp();
			String bracing = L.getBracing();
			String digit2 = secName.substring(0,2);
		 		
				if(digit2.equals("CL")){
					LCBending LCB = new LCBending(secName,matName,Mresist,axis,Lx,Ly,Lz,distC,Mmax,M1,M2,M3,M4,M5,loadDistr,loadAppl,bracing);
									
					double cap = LCB.UltimateMemberCapacity()/1000000.;						
					Capacity.setText(df.format(cap));
						
						if(cap<=Mresist){
							Fail.setText("YES");
						}else{
							Fail.setText("NO");
						}	
					
				}else if(digit2.equals("ZL")){
					LZBending LZB = new LZBending(secName,matName,Mresist,axis,Lx,Ly,Lz,distC,Mmax,M1,M2,M3,M4,M5,loadDistr,loadAppl,bracing);
					
					double cap = LZB.UltimateMemberCapacity()/1000000.;
					Capacity.setText(df.format(cap));
					
						if(cap<=Mresist){
							Fail.setText("YES");
						}else{
							Fail.setText("NO");
						}	
					
					
				}else if(digit2.equals("GS")){
					Capacity.setText("Not Possible");
					Fail.setText("Not Possible");					
				}
				
		}else if(e.getActionCommand().equals("Generate Effective Length Table")){
			String name = secCombo.getSelectedItem().toString();
			new TableBendFrameEWM(name,this);
		}
		
	}

}
