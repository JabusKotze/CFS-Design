package guiEWComp;
/*
 * @author JJR Kotze (email 15692183@sun.ac.za)
 * For Department of Civil Engineering, Stellenbosch University
 * 29 October 2012 (Version 1.0 of CFS design)
 */
import gui.TableBendingFrame;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import javax.swing.*;
import capacityDesign.LCCompression;
import capacityDesign.LZCompression;
import java.util.*;
import section.*;
import java.awt.*;
import materials.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import guiMemory.*;
import java.text.*;

public class EWCompFrame extends JFrame implements ActionListener {
	protected String title;
	protected JTextField Capacity,Fail;
	protected JComboBox secCombo,matCombo;
	protected JCheckBox distCheck;
	protected SectionDatabase sd = Parameters.getSectionDatabase();
	protected MaterialDatabase md = Parameters.getMaterialDatabase();
	protected DecimalFormat df = new DecimalFormat("#.####");
	public EWCompFrame(String title){
		super(title);
		this.setVisible(true);
		this.setLayout(new BorderLayout());
		
		
		JPanel panel1 = new JPanel();
		JPanel panel2 = new JPanel();
		JPanel panel3 = new JPanel();
		
		panel1.setLayout(new GridLayout(3,3));
		panel2.setLayout(new GridLayout(3,1));
		panel3.setLayout(new BorderLayout());
		
		panel1.setPreferredSize(new Dimension(450,160));
		panel2.setPreferredSize(new Dimension(450,100));
		panel1.setPreferredSize(new Dimension(450,120));
		
		
		//North Layout
		distCheck = new JCheckBox();
		
		JLabel sname = new JLabel("Section Name: ");
		JLabel mname = new JLabel("Material Name: ");		
		JLabel distcheck = new JLabel("Distortional Check");		
		JLabel none2 = new JLabel("");
		
		sname.setHorizontalAlignment(SwingConstants.RIGHT);
		mname.setHorizontalAlignment(SwingConstants.RIGHT);		
		distcheck.setHorizontalAlignment(SwingConstants.RIGHT);		
		none2.setHorizontalAlignment(SwingConstants.RIGHT);
		
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
		panel1.add(distcheck);
		panel1.add(distCheck);
		panel1.add(none2);
		
		this.add(panel1,BorderLayout.NORTH);
		
		//Centre
		JButton lengths = new JButton("Lengths");		
		JButton calc = new JButton("Calculate");
		JButton comp = new JButton("Compression to Resist");
		comp.setHorizontalAlignment(SwingConstants.CENTER);	
		lengths.setHorizontalAlignment(SwingConstants.CENTER);		
		calc.setHorizontalAlignment(SwingConstants.CENTER);
		lengths.addActionListener(this);
		comp.addActionListener(this);
		calc.addActionListener(this);
		
		panel2.add(lengths);	
		panel2.add(comp);
		panel2.add(calc);
		
		this.add(panel2,BorderLayout.CENTER);
		
		//South layout
		JPanel pane1 = new JPanel();
		JPanel pane2 = new JPanel();
		pane1.setLayout(new GridLayout(2,3));
		pane2.setLayout(new GridLayout(1,1));
		
		
		//pane1
		JLabel capacity = new JLabel("Compression Capacity: ");
		JLabel fail = new JLabel("Will Section Fail: ");
		JLabel knm = new JLabel("kN");
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
			new AddSectionTypeFrameEWComp("Choose Section Type",this);
		}else if(e.getActionCommand().equals("Add Material")){
			new AddMatFrameComp("Add Material",this);
		}else if(e.getActionCommand().equals("Lengths")){
			new LengthsFrameComp("Effective Lengths");
		}else if(e.getActionCommand().equals("Calculate")){
			Compression C = GetMemory.getComp();
			Lengths L = GetMemory.getLengths();
			double Lx = L.getLx();
			double Ly = L.getLy();
			double Lz = L.getLz();
			double appliedForce = C.getMaxComp();
			String secName = secCombo.getSelectedItem().toString();
			String matName = matCombo.getSelectedItem().toString();
			boolean dCheck = distCheck.isSelected();
			String digit2 = secName.substring(0,2);
			
			if(digit2.equals("CL")){
				LCCompression lCC = new LCCompression(secName, matName,appliedForce, Lx, Ly, Lz,dCheck);		//Initiates a Lipped Channel compression design
				double capacity = lCC.ultimateDesignForce();
				boolean fail = lCC.willItFail();
				Capacity.setText(df.format(capacity));
				if(fail){
					Fail.setText("YES");
				}else{
					Fail.setText("NO");
				}
				
				
			}else if(digit2.equals("ZL")){
				LZCompression lZC = new LZCompression(secName, matName,appliedForce, Lx, Ly, Lz,dCheck);		//Initiates a Lipped Channel compression design
				double capacity = lZC.ultimateDesignForce();
				boolean fail = lZC.willItFail();
				Capacity.setText(df.format(capacity));
				if(fail){
					Fail.setText("YES");
				}else{
					Fail.setText("NO");
				}
			}else if(digit2.equals("GS")){
				Capacity.setText("N.A");
				Fail.setText("N.A");
			}
			
		}else if(e.getActionCommand().equals("Generate Effective Length Table")){
			String heading = secCombo.getSelectedItem().toString();
			new TableCompFrameEWM(heading,this);
		}else if(e.getActionCommand().equals("Compression to Resist")){
			new CompFrame("Compression to Resist");
		}
		
	}

}
