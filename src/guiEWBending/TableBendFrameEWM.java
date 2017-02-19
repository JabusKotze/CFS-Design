package guiEWBending;
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

public class TableBendFrameEWM extends JFrame{
	
		protected EWbendingFrame bF;
		protected String title;
		protected JTable table;
		protected Moments B = GetMemory.getMoments();
		protected Lengths L = GetMemory.getLengths();
		protected Loads loads = GetMemory.getLoads();
		protected SectionDatabase sd = Parameters.getSectionDatabase();
		
		public TableBendFrameEWM(String title,EWbendingFrame bF){
			super(title);
			this.bF = bF;
			this.setVisible(true);
			this.setLayout(new BorderLayout());
			this.setPreferredSize(new Dimension(800,700));
			DecimalFormat df = new DecimalFormat("#.####");
			
			JPanel panel1 = new JPanel();
			panel1.setLayout(new BorderLayout());
			panel1.setPreferredSize(new Dimension(800,700));
			
			//Panel 1 calculations
			String secName = bF.secCombo.getSelectedItem().toString();
			String matName = bF.matCombo.getSelectedItem().toString();			
			
			double Mresist = B.getMaxAppliedMoment();
			boolean distCheck = bF.distCheck.isSelected();	
			double M1 = B.getM1();
			double M2 = B.getM2();
			double M3 = B.getM3();
			double M4 = B.getM4();
			double M5 = B.getM5();
			double mmax = B.getMmax();
			int multiples = L.getMult();			
			double maxlength = getMaxLength();
			String axis = bF.Axis.getSelectedItem().toString();
			String loadDistr = loads.getDistr();
			String loadAppl = loads.getLoadApp();
			String bracing = loads.getBracing();
			String[] columnNames = {"Length(mm)","Mb(kNm)","Mbd(kNm)","Ms(kNm)", "M(kNm)"};
			String[][] data = new String[(int)maxlength/multiples+1][5];
			String digit2 = secName.substring(0,2);
			
			if(digit2.equals("CL")){
				for(int i=1;i<=maxlength/multiples;i++){
					double length = i*multiples;
					LCBending LCB = new LCBending(secName,matName,Mresist,axis,length,length,length,distCheck,mmax,M1,M2,M3,M4,M5,loadDistr,loadAppl,bracing);
					double Mb = LCB.nominalMemberMomentCapMbNoDistCheck()/1000000.;
					double Mbd = LCB.distMemberCapacity()/1000000.;
					double Ms = LCB.nominalSectionMomentCapMs()/1000000.;
					double M = LCB.UltimateMemberCapacity()/1000000.;
					
					
					
					data[i][0] = df.format(length);
					data[i][1] = df.format(Mb);
					data[i][2] = df.format(Mbd);
					data[i][3] = df.format(Ms);
					data[i][4] = df.format(M);								
				}
			}else if(digit2.equals("ZL")){
				for(int i=1;i<=maxlength/multiples;i++){
					double length = i*multiples;
					LZBending LZB = new LZBending(secName,matName,Mresist,axis,length,length,length,distCheck,mmax,M1,M2,M3,M4,M5,loadDistr,loadAppl,bracing);
					double Mb = LZB.nominalMemberMomentCapMbNoDistCheck()/1000000.;
					double Mbd = LZB.distMemberCapacity()/1000000.;
					double Ms = LZB.nominalSectionMomentCapMs()/1000000.;
					double M = LZB.UltimateMemberCapacity()/1000000.;
					
					
					
					data[i][0] = df.format(length);
					data[i][1] = df.format(Mb);
					data[i][2] = df.format(Mbd);
					data[i][3] = df.format(Ms);
					data[i][4] = df.format(M);								
				}				
			}else{
				for(int i = 0;i<1000;i++){
					for(int j=0;j<5;j++){
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
			String sname = bF.secCombo.getSelectedItem().toString();
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
