package guiEWComp;
/*
 * @author JJR Kotze (email 15692183@sun.ac.za)
 * For Department of Civil Engineering, Stellenbosch University
 * 29 October 2012 (Version 1.0 of CFS design)
 */


import java.awt.BorderLayout;
import javax.swing.*;
import dSM.*;
import java.awt.*;
import section.*;
import loadFactorscufsm.LoadFactorsCUFSM;
import java.text.*;
import guiMemory.*;
import capacityDesign.*;

public class TableCompFrameEWM extends JFrame{
	
		protected EWCompFrame cF;
		protected String title;
		protected JTable table;
		protected Compression C = GetMemory.getComp();
		protected Lengths L = GetMemory.getLengths();
		protected SectionDatabase sd = Parameters.getSectionDatabase();
		
		public TableCompFrameEWM(String title,EWCompFrame cF){
			super(title);
			this.cF = cF;
			this.setVisible(true);
			this.setLayout(new BorderLayout());
			this.setPreferredSize(new Dimension(800,700));
			DecimalFormat df = new DecimalFormat("#.####");
			
			JPanel panel1 = new JPanel();
			panel1.setLayout(new BorderLayout());
			panel1.setPreferredSize(new Dimension(800,700));
			
			//Panel 1 calculations
			String secName = cF.secCombo.getSelectedItem().toString();
			String matName = cF.matCombo.getSelectedItem().toString();			
			
			double designForce = C.getMaxComp();
			boolean distCheck = cF.distCheck.isSelected();			
			int multiples = L.getMult();			
			double maxlength = getMaxLength();
			
			String[] columnNames = {"Length(mm)","AeFn(mm2)","AeFy(mm2)","Ns(kN)", "Ncd(kN)" , "Nc(kN)", "N(kN)"};
			String[][] data = new String[(int)maxlength/multiples+1][9];
			String digit2 = secName.substring(0,2);
			
			if(digit2.equals("CL")){
				for(int i=1;i<=maxlength/multiples;i++){
					double length = i*multiples;
					LCCompression lC = new LCCompression(secName, matName,designForce, length, length, length,distCheck);
					double AeFn = lC.effectiveAreaFn();
					double AeFy = lC.effectiveAreaFy();
					double Ns = lC.nominalSectionCapacity();
					double Ncd = lC.distMemberCapacity();
					double Nc;
					if(distCheck){
						Nc = lC.nominalMemberCapacityWithDistCheck();
					}else{
						Nc = lC.nominalMemberCapacityNoDistCheck();
					}
					
					double N = lC.ultimateDesignForce();
					
					
					data[i][0] = df.format(length);
					data[i][1] = df.format(AeFn);
					data[i][2] = df.format(AeFy);
					data[i][3] = df.format(Ns);
					data[i][4] = df.format(Ncd);
					data[i][5] = df.format(Nc);
					data[i][6] = df.format(N);			
				}
			}else if(digit2.equals("ZL")){
				for(int i=1;i<=maxlength/multiples;i++){
					double length = i*multiples;
					LZCompression lC = new LZCompression(secName, matName,designForce, length, length, length,distCheck);
					double AeFn = lC.effectiveAreaFn();
					double AeFy = lC.effectiveAreaFy();
					double Ns = lC.nominalSectionCapacity();
					double Ncd = lC.distMemberCapacity();
					double Nc;
					if(distCheck){
						Nc = lC.nominalMemberCapacityWithDistCheck();
					}else{
						Nc = lC.nominalMemberCapacityNoDistCheck();
					}
					
					double N = lC.ultimateDesignForce();
					
					
					data[i][0] = df.format(length);
					data[i][1] = df.format(AeFn);
					data[i][2] = df.format(AeFy);
					data[i][3] = df.format(Ns);
					data[i][4] = df.format(Ncd);
					data[i][5] = df.format(Nc);
					data[i][6] = df.format(N);			
				}				
			}else{
				for(int i = 0;i<1000;i++){
					for(int j=0;j<7;j++){
						data[i][j] = "N.A";
					}
				}
			}
			
			
			table = new JTable(data,columnNames);
			table.setFillsViewportHeight(true);
			JScrollPane scrollPane = new JScrollPane(table);
			
			
			panel1.add(scrollPane,BorderLayout.CENTER);
			
			this.add(panel1,BorderLayout.CENTER);
			this.setLocation(100, 100);
			pack();
			
		}
		
		public double getMaxLength(){
			String sname = cF.secCombo.getSelectedItem().toString();
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
		
}
