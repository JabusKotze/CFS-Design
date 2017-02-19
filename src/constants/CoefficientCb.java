package constants;
/*
 * @author JJR Kotze (email 15692183@sun.ac.za)
 * For Department of Civil Engineering, Stellenbosch University
 * 29 October 2012 (Version 1.0 of CFS design)
 */
/*
 * THIS CLASS CONTAINS THE METHODS AND CONSTANTS FROM TABLE 3.3.3.2
 * TO DETERMINE Cb: COEFFIECIENT DEPENDING ON MOMENT DISTRIBUTION IN THE
 * LATERALLY UNBRACED SEGMENT
 */
public class CoefficientCb {
	
	private double Mmax,M3,M4,M5;
	private String loadDistr,loadAppl,bracing;
	
	public CoefficientCb(double Mmax,double M3,double M4,double M5,String loadDistr, String loadAppl, String bracing){
		this.bracing = bracing;
		this.M3 = M3;
		this.M4 = M4;
		this.M5 = M5;
		this.Mmax = Mmax;
		this.loadAppl = loadAppl;
		this.loadDistr = loadDistr;
	}
	
	public double getCb(){
		double Cd;
		
		if(Mmax == 0.0){
			if(loadDistr.equals("uniSS")){
				if(loadAppl.equals("TF")){
					if(bracing.equals("No")){
						return 1.92;
					}else if(bracing.equals("C")){
						return 1.59;
					}else if(bracing.equals("T")){
						return 1.47;
					}else{
						return 1.0;
					}						
				}else if(loadAppl.equals("SC")){
					if(bracing.equals("No")){
						return 1.22;
					}else if(bracing.equals("C")){
						return 1.37;
					}else if(bracing.equals("T")){
						return 1.37;
					}else{
						return 1.0;
					}	
				}else if(loadAppl.equals("CF")){
					if(bracing.equals("No")){
						return 0.77;
					}else if(bracing.equals("C")){
						return 1.19;
					}else if(bracing.equals("T")){
						return 1.28;
					}else{
						return 1.0;
					}	
				}else{
					return 1.0;
				}
			}else{
				return 1.0;
			}
		}else{
			Cd = 12.5*Mmax/(2.5*Mmax + 3.*M3 + 4.*M4 + 3.*M5 );  //eqn 3.3.3.2(9)
			return Cd;
		}
	}

}
