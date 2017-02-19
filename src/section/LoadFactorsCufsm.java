package section;
/*
 * @author JJR Kotze (email 15692183@sun.ac.za)
 * For Department of Civil Engineering, Stellenbosch University
 * 29 October 2012 (Version 1.0 of CFS design)
 */

public class LoadFactorsCufsm {
	
	private double[] factors;
	private double maxLength, turningpoint, multiples;
	
	public LoadFactorsCufsm(){
		
	}
	
	
	public double[] getLoadFactors(){
		return factors;
	}
	
	public double getMultiples(){
		return multiples;
	}
	
	public double getMaxlength(){
		return maxLength;
	}
	public double getTurningpoint(){
		return turningpoint;
	}
	
	//Set the values
	public void setLoadFactors(double[] array){
		factors = new double[array.length];
		for(int i =0; i<array.length; i++){
			factors[i] = array[i];
		}		
	}
	
	public void setMultiples(double mult){
		multiples = mult;
	}
	
	public void setMaxlength(double mL){
		maxLength = mL;
	}
	public void setTurningpoint(double tp){
		turningpoint = tp;
	}

}
