package gui;
/*
 * @author JJR Kotze (email 15692183@sun.ac.za)
 * For Department of Civil Engineering, Stellenbosch University
 * 29 October 2012 (Version 1.0 of CFS design)
 */
import java.awt.BorderLayout;
import javax.swing.*;
import dSM.DSMBending;
import java.awt.*;
import section.*;
import loadFactorscufsm.LoadFactorsCUFSM;
import materials.*;
import java.text.*;

public class TableBendingFrame extends JFrame{
	
		protected DSMBENDING dsmb;
		protected String title;
		protected JTable table;
		protected LoadFactorsCufsm lfcufsm = Parameters.getLoadFactorsCufsm();
		
		public TableBendingFrame(String title,DSMBENDING dsmb ){
			super(title);
			this.dsmb = dsmb;
			this.setVisible(true);
			this.setLayout(new BorderLayout());
			this.setPreferredSize(new Dimension(800,700));
			DecimalFormat df = new DecimalFormat("#.####");
			
			JPanel panel1 = new JPanel();
			panel1.setLayout(new BorderLayout());
			panel1.setPreferredSize(new Dimension(800,700));
			
			//Panel 1 calculations
			String secName = dsmb.secCombo.getSelectedItem().toString();
			String matName = dsmb.matCombo.getSelectedItem().toString();
			String axis = dsmb.Axis.getSelectedItem().toString();
			double Mol = Double.parseDouble(dsmb.Mol.getText());
			double Mod = Double.parseDouble(dsmb.Mod.getText());
			double maxAppliedMoment = Double.parseDouble(dsmb.maxMoment.getText());
			double fy = 0.0;
			
			double[]Moed = new double[lfcufsm.getLoadFactors().length];
			for(int i = 0;i<lfcufsm.getLoadFactors().length;i++){
				Moed[i] = lfcufsm.getLoadFactors()[i];
			}
			
			double Mo =1.0;
			int multiples = (int)lfcufsm.getMultiples();
			int turningpoint = (int)lfcufsm.getTurningpoint();
			double maxlength = lfcufsm.getMaxlength();
			LoadFactorsCUFSM MoeNow = new LoadFactorsCUFSM(Moed,Mo,multiples,turningpoint);  //this will go and find the appropriate factor as listed in Moed[] for the determination of Mo
			
			String[] columnNames = {"Length(mm)","Moc(kNm)","Mol(kNm)","Mod(kNm)", "Mbe(kNm)" , "Mbl(kNm)", "Mbd(kNm)", "Mb(kNm)", "M(kNm)"};
			String[][] data = new String[(int)maxlength/multiples+1][9];
			
			for(int i=1;i<=maxlength/multiples;i++){
				double length = i*multiples;
				 DSMBending DSM = new DSMBending(secName,matName,axis,maxAppliedMoment,MoeNow.getLoadFactor(length),Mol,Mod,length,length,length,fy,0.0, 0.0, 0.0, 0.0, 0.0, 0.0, "", "", "");
				double moc = DSM.getMo()/1000000.;
				double mol = DSM.getMol()/1000000.;
				double mod = DSM.getMod()/1000000.;
				double mbe = DSM.nominalMemberMomentCapEuler()/1000000.;
				double mbl = DSM.nominalMemberMomentCapLocal()/1000000.;
				double mbd = DSM.nominalMemberMomentCapDistortional()/1000000.;
				double mb = DSM.getMb()/1000000.;
				double m = DSM.getDesignCapacity()/1000000.;
				
				data[i][0] = df.format(length);
				data[i][1] = df.format(moc);
				data[i][2] = df.format(mol);
				data[i][3] = df.format(mod);
				data[i][4] = df.format(mbe);
				data[i][5] = df.format(mbl);
				data[i][6] = df.format(mbd);
				data[i][7] = df.format(mb);
				data[i][8] = df.format(m);
				
				
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
