package guiComp;
/*
 * @author JJR Kotze (email 15692183@sun.ac.za)
 * For Department of Civil Engineering, Stellenbosch University
 * 29 October 2012 (Version 1.0 of CFS design)
 */
import java.awt.BorderLayout;
import javax.swing.*;

import java.text.*;
import java.awt.*;
import section.*;
import materials.*;
import gui.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
public class LoadFactorFrameComp extends JFrame implements ActionListener{
	
	protected JTextField turningPoint, multiples, loadFactors,maxlength,fy,r,t;
	private DSMCOMPRESSION dsmc;
	private SectionDatabase sd = Parameters.getSectionDatabase();
	private MaterialDatabase md = Parameters.getMaterialDatabase();
	private DecimalFormat df = new DecimalFormat("#.###");
	private LoadFactorsCufsm setloadfactors = Parameters.getLoadFactorsCufsm();
	
	public LoadFactorFrameComp(String title,DSMCOMPRESSION dsmc){
		super(title);
		this.dsmc = dsmc;
		this.setVisible(true);		
		this.setLayout(new BorderLayout());
		
		
		JPanel panel1 = new JPanel();
		JPanel panel2 = new JPanel();
		
		panel1.setLayout(new GridLayout(7,3));
		panel2.setLayout(new GridLayout(2,1));
		
		
		panel1.setPreferredSize(new Dimension(350,200));
		panel2.setPreferredSize(new Dimension(350,70));
	
		
		//textfields
		 
		r = new JTextField(df.format(getRadius()));
		t = new JTextField(df.format(getThickness()));
		turningPoint = new JTextField("");
		multiples = new JTextField("");
		loadFactors = new JTextField("");
		maxlength = new JTextField(df.format(getMaxLength()));
		fy = new JTextField(Double.toString(getFy()));
		
		r.setHorizontalAlignment(SwingConstants.LEFT);
		t.setHorizontalAlignment(SwingConstants.LEFT);
		fy.setHorizontalAlignment(SwingConstants.LEFT);
		maxlength.setHorizontalAlignment(SwingConstants.LEFT);
		turningPoint.setHorizontalAlignment(SwingConstants.LEFT);
		multiples.setHorizontalAlignment(SwingConstants.LEFT);
		loadFactors.setHorizontalAlignment(SwingConstants.LEFT);
		
		//tooltip text
		turningPoint.setToolTipText("The length at which a peak is reached between Distortional and Global buckling from graph produced through cufsm buckling analysis");
		multiples.setToolTipText("The multiples used in which the load factors where supplied under 'Load Factors'");
		loadFactors.setToolTipText("Factors supplied from turning point till max length: i.e. supply in multiples of 'multiples'");
		maxlength.setToolTipText("Please change this to desired length less than what was given: i.e. length should be multples of 'multiples'");
		
		
		//Labels
		JLabel mm1 = new JLabel("mm");
		JLabel mm2 = new JLabel("mm");
		JLabel mm3 = new JLabel("mm");
		JLabel mm4 = new JLabel("mm");
		JLabel mm5 = new JLabel("mm");		
		JLabel mpa = new JLabel("MPa");
		JLabel none = new JLabel("");
		
		JLabel radius = new JLabel("Angle Radius =");
		JLabel thickness = new JLabel("Base Thickness =");
		JLabel fylabel = new JLabel("Fy =");
		JLabel tPlabel = new JLabel("Turning Point = ");
		JLabel mLabel = new JLabel("Multiples = ");
		JLabel lflabel = new JLabel("Load Factors = ");
		JLabel Llabel = new JLabel("Max Length = ");
		
		mm1.setHorizontalAlignment(SwingConstants.LEFT);
		mm2.setHorizontalAlignment(SwingConstants.LEFT);
		mm3.setHorizontalAlignment(SwingConstants.LEFT);
		mm4.setHorizontalAlignment(SwingConstants.LEFT);
		mm5.setHorizontalAlignment(SwingConstants.LEFT);		
		mpa.setHorizontalAlignment(SwingConstants.LEFT);
		none.setHorizontalAlignment(SwingConstants.LEFT);
		
		radius.setHorizontalAlignment(SwingConstants.RIGHT);
		thickness.setHorizontalAlignment(SwingConstants.RIGHT);
		fylabel.setHorizontalAlignment(SwingConstants.RIGHT);
		tPlabel.setHorizontalAlignment(SwingConstants.RIGHT);
		mLabel.setHorizontalAlignment(SwingConstants.RIGHT);
		lflabel.setHorizontalAlignment(SwingConstants.RIGHT);
		Llabel.setHorizontalAlignment(SwingConstants.RIGHT);
		
		//buttons
		JButton submit = new JButton("Submit");
		submit.setHorizontalAlignment(SwingConstants.CENTER);
		//JButton openCufsm = new JButton("Open CUFSM");
		//openCufsm.setHorizontalAlignment(SwingConstants.CENTER);
		//openCufsm.addActionListener(new OpenCufsmButtonListener());
		
		//label for cufsm
		JLabel cfsm = new JLabel("Note: use cufsm4 to obtain load factors");
		cfsm.setHorizontalAlignment(SwingConstants.CENTER);
		
		//Paneladds
		panel1.add(radius);
		panel1.add(r);
		panel1.add(mm1);
		panel1.add(thickness);
		panel1.add(t);
		panel1.add(mm2);		
		panel1.add(fylabel);
		panel1.add(fy);
		panel1.add(mpa);
		panel1.add(Llabel);
		panel1.add(maxlength);
		panel1.add(mm3);
		panel1.add(tPlabel);
		panel1.add(turningPoint);
		panel1.add(mm4);
		panel1.add(mLabel);
		panel1.add(multiples);
		panel1.add(mm5);
		panel1.add(lflabel);
		panel1.add(loadFactors);
		panel1.add(none);
		this.add(panel1,BorderLayout.NORTH);
		
		submit.addActionListener(this);
		
		panel2.add(submit);
		panel2.add(cfsm);
		this.add(panel2,BorderLayout.SOUTH);
		
		this.setLocation(300, 300);
		pack();
		
	}
	
	public double getFy(){		
				
		String mname = dsmc.matCombo.getSelectedItem().toString();
		double thickness = getThickness();
		double yield;		
	
		yield = md.getMatProp(mname).getFy(thickness);
		
		return yield;
	}
	
	public double getMaxLength(){
		String sname = dsmc.secCombo.getSelectedItem().toString();
		String digit2 = sname.substring(0, 2);
		
		double rx;
		double ry;
		
		if(digit2.equals("CL")){
			rx = sd.getLCSection(sname).getPolarRX();
			ry = sd.getLCSection(sname).getPolarRY();
		}else if(digit2.equals("ZL")){
			rx =sd.getZLSection(sname).getPolarRX();
			ry = sd.getZLSection(sname).getPolarRY();
		}else if(digit2.equals("GS")){
			rx = sd.getGSSection(sname).getPolarR();
			ry = sd.getGSSection(sname).getPolarR();
		}else{
			rx = 0.0;
			ry = 0.0;
		}
		
		Slenderness s = new Slenderness(0.0,0.0,rx,ry);
		
		return s.maxEffLengthCompression();
	}
	
	public double getThickness(){		
		String sname = dsmc.secCombo.getSelectedItem().toString();
		String digit2 = sname.substring(0, 2);		
		double thickness;
		
		if(digit2.equals("CL")){
			thickness = sd.getLCSection(sname).getBaseThickness();			
		}else if(digit2.equals("ZL")){
			thickness = sd.getZLSection(sname).getBaseThickness();
		}else if(digit2.equals("GS")){
			thickness = sd.getGSSection(sname).getBaseThickness();
		}else{
			thickness = 0.0;
		}
		
		return thickness;
	}
	public double getRadius(){
		String sname = dsmc.secCombo.getSelectedItem().toString();
		String digit2 = sname.substring(0, 2);		
		double radius;
		
		if(digit2.equals("CL")){
			radius = sd.getLCSection(dsmc.secCombo.getSelectedItem().toString()).getangleRadius();			
		}else if(digit2.equals("ZL")){
			radius = sd.getZLSection(sname).getangleRadius();;
		}else if(digit2.equals("GS")){
			radius = 0.0;
		}else{
			radius = 0.0;
		}
		
		return radius;
	}

	
	public void actionPerformed(ActionEvent e) {
		if(e.getActionCommand().equals("Submit")){
			double mult = Double.parseDouble(multiples.getText());
			double tP = Double.parseDouble(turningPoint.getText());
			double mlength = Double.parseDouble(maxlength.getText());
			String array = loadFactors.getText();
			int size = (int)(((mlength-tP)/mult +1));
			
			
			double[] loadFactorarray = new double[size];
			
			int j=0;
			int hold = 0;
			for(int i =0; i<array.length()-1 ; i++){
				if(array.substring(i,i+1).equals(",")){
					j++;
					hold = i+1;
				}else{
					loadFactorarray[j] = Double.parseDouble(array.substring(hold,i+1));
					
				}
				
			}			
			setloadfactors.setLoadFactors(loadFactorarray);
			setloadfactors.setMaxlength(mlength);
			setloadfactors.setMultiples(mult);
			setloadfactors.setTurningpoint(tP);		
			
			for(int i = 0;i<loadFactorarray.length;i++){
				System.out.println(loadFactorarray[i]);
			}
			double mL = setloadfactors.getMaxlength();
			double turningpoint = setloadfactors.getTurningpoint();
			double multi = setloadfactors.getMultiples();
			//double[] fact = LoadFact.getLoadFactors(); 
			
			System.out.println("maxLength     = "+mL);
			System.out.println("Turning Point = "+turningpoint);
			System.out.println("Multiples     = "+multi);
			
			this.setVisible(false);
		
		}
	}
	
	
	

}
