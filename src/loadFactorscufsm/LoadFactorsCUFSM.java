package loadFactorscufsm;
/*
 * @author JJR Kotze (email 15692183@sun.ac.za)
 * For Department of Civil Engineering, Stellenbosch University
 * 29 October 2012 (Version 1.0 of CFS design)
 */
/*
 * GET THE APPROPRIATE LOAD FACTORS FOR THE EFFECTIVE LENGTH 
 */
public class LoadFactorsCUFSM {
	private double[] loadfactors;	
	private int multiples;
	private int turningpoint;
	private double foe;
	
	
	
	public LoadFactorsCUFSM(double[] foed,double foe, int multiples, int turningpoint){
		this.loadfactors=foed;		
		this.multiples = multiples;
		this.turningpoint = turningpoint;
		this.foe = foe;
	}
	
	
	public double getLoadFactor(double length){

		if(foe == 0.0){
			return foe;			
		}else{
			if(length <= turningpoint){
				return loadfactors[0];  
			}else{
				int i = (int)((length - turningpoint)/multiples);
				return loadfactors[i];
			}
		}
		
		
	}

}
