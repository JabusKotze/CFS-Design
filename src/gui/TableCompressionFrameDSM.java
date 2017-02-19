package gui;
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

public class TableCompressionFrameDSM extends JFrame{
	
		protected DSMCOMPRESSION dsmc;
		protected String title;
		protected JTable table;
		protected LoadFactorsCufsm lfcufsm = Parameters.getLoadFactorsCufsm();
		
		public TableCompressionFrameDSM(String title,DSMCOMPRESSION dsmc ){
			super(title);
			this.dsmc = dsmc;
			this.setVisible(true);
			this.setLayout(new BorderLayout());
			this.setPreferredSize(new Dimension(800,700));
			DecimalFormat df = new DecimalFormat("#.####");
			
			JPanel panel1 = new JPanel();
			panel1.setLayout(new BorderLayout());
			panel1.setPreferredSize(new Dimension(800,700));
			
			//Panel 1 calculations
			String secName = dsmc.secCombo.getSelectedItem().toString();
			String matName = dsmc.matCombo.getSelectedItem().toString();
			
			double Fol = Double.parseDouble(dsmc.Fol.getText());
			double Fod = Double.parseDouble(dsmc.Fod.getText());
			double appliedForce = Double.parseDouble(dsmc.appliedForce.getText());
			double fy = 0.0;
			double area = 0.0;
			
			double[]Moed = new double[lfcufsm.getLoadFactors().length];
			for(int i = 0;i<lfcufsm.getLoadFactors().length;i++){
				Moed[i] = lfcufsm.getLoadFactors()[i];
			}
			
			double Mo =1.0;
			int multiples = (int)lfcufsm.getMultiples();
			int turningpoint = (int)lfcufsm.getTurningpoint();
			double maxlength = lfcufsm.getMaxlength();
			LoadFactorsCUFSM MoeNow = new LoadFactorsCUFSM(Moed,Mo,multiples,turningpoint);  //this will go and find the appropriate factor as listed in Moed[] for the determination of Mo
			
			String[] columnNames = {"Length(mm)","foc(MPa)","fol(MPa)","fod(MPa)", "Nce(kN)" , "Ncl(kN)", "Ncd(kN)", "Nc(kN)", "N(kN)"};
			String[][] data = new String[(int)maxlength/multiples+1][9];
			
			for(int i=1;i<=maxlength/multiples;i++){
				double length = i*multiples;
				DSMCompression DSM = new DSMCompression(secName,matName,appliedForce,MoeNow.getLoadFactor(length),Fol,Fod,length,length,length,area,fy);
				double foc = DSM.getFoc();
				double fol = DSM.getFol();
				double fod = DSM.getFod();
				double Nce = DSM.memberCapacityEuler();
				double Ncl = DSM.memberCapacityLocal();
				double Ncd = DSM.memberCapacityDistortional();
				double Nc = DSM.getNc();
				double N = DSM.getDesignCapacity();
				
				data[i][0] = df.format(length);
				data[i][1] = df.format(foc);
				data[i][2] = df.format(fol);
				data[i][3] = df.format(fod);
				data[i][4] = df.format(Nce/1000.);
				data[i][5] = df.format(Ncl/1000.);
				data[i][6] = df.format(Ncd/1000.);
				data[i][7] = df.format(Nc/1000.);
				data[i][8] = df.format(N);
				
				
			}
			
			table = new JTable(data,columnNames);
			table.setFillsViewportHeight(true);
			JScrollPane scrollPane = new JScrollPane(table);
			
			
			panel1.add(scrollPane,BorderLayout.CENTER);
			
			this.add(panel1,BorderLayout.CENTER);
			this.setLocation(100, 100);
			pack();
			
		}
		
}
