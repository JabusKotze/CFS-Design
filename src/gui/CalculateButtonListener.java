package gui;
/*
 * @author JJR Kotze (email 15692183@sun.ac.za)
 * For Department of Civil Engineering, Stellenbosch University
 * 29 October 2012 (Version 1.0 of CFS design)
 */
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import dSM.DSMBending;
import loadFactorscufsm.LoadFactorsCUFSM;
import section.*;
import java.text.*;

import javax.swing.*;


public class CalculateButtonListener extends JPanel implements ActionListener {
	protected DSMBENDING dsmb;
	LoadFactorsCufsm lfcufsm = Parameters.getLoadFactorsCufsm();
	
	public CalculateButtonListener(DSMBENDING dsmb){
		this.dsmb = dsmb;
	}
	
	public void actionPerformed(ActionEvent e) {
		if(e.getActionCommand().equals("Calculate")){
			DecimalFormat df = new DecimalFormat("#.####");
			String secName = dsmb.secCombo.getSelectedItem().toString();
			String matName = dsmb.matCombo.getSelectedItem().toString();
			String axis = dsmb.Axis.getSelectedItem().toString();
			double Lx = Double.parseDouble(dsmb.Length.getText());
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
			
			LoadFactorsCUFSM MoeNow = new LoadFactorsCUFSM(Moed,Mo,multiples,turningpoint);  //this will go and find the appropriate factor as listed in Moed[] for the determination of Mo
			DSMBending DSMB = new DSMBending(secName,matName,axis,maxAppliedMoment,MoeNow.getLoadFactor(Lx),Mol,Mod,Lx,Lx,Lx,fy,0.0, 0.0, 0.0, 0.0, 0.0, 0.0, "", "", "");
			
			dsmb.ultMomentCap.setText(df.format(DSMB.getDesignCapacity()/1000000.));
			
			if(DSMB.getDesignCapacity()/1000000.<=Math.abs(maxAppliedMoment)){
				dsmb.Fail.setText("YES");
			}else{
				dsmb.Fail.setText("NO");
			}			
		}
		
	}

}
