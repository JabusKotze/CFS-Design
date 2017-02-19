package gui;
/*
 * @author JJR Kotze (email 15692183@sun.ac.za)
 * For Department of Civil Engineering, Stellenbosch University
 * 29 October 2012 (Version 1.0 of CFS design)
 */
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import section.*;

public class LoadFactorListener implements ActionListener{

	private LoadFactorFrame lfFrame;
	private LoadFactorsCufsm setloadfactors = Parameters.getLoadFactorsCufsm();
	
	public LoadFactorListener(LoadFactorFrame lfFrame){
		this.lfFrame = lfFrame;
		
	}
	
	
	public void actionPerformed(ActionEvent e) {
		if(e.getActionCommand().equals("Submit")){
			double multiples = Double.parseDouble(lfFrame.multiples.getText());
			double turningP = Double.parseDouble(lfFrame.turningPoint.getText());
			double maxlength = Double.parseDouble(lfFrame.maxlength.getText());
			String array = lfFrame.loadFactors.getText();
			int size = (int)(((maxlength-turningP)/multiples +1));
			
			
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
			setloadfactors.setMaxlength(maxlength);
			setloadfactors.setMultiples(multiples);
			setloadfactors.setTurningpoint(turningP);		
			
			for(int i = 0;i<loadFactorarray.length;i++){
				System.out.println(loadFactorarray[i]);
			}
			double mL = setloadfactors.getMaxlength();
			double turningpoint = setloadfactors.getTurningpoint();
			double mult = setloadfactors.getMultiples();
			//double[] fact = LoadFact.getLoadFactors(); 
			
			System.out.println("maxLength     = "+mL);
			System.out.println("Turning Point = "+turningpoint);
			System.out.println("Multiples     = "+mult);
			
			lfFrame.setVisible(false);
		
		}
		
	}

}
