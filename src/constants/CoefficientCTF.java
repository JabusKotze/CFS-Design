package constants;
/*
 * @author JJR Kotze (email 15692183@sun.ac.za)
 * For Department of Civil Engineering, Stellenbosch University
 * 29 October 2012 (Version 1.0 of CFS design)
 */
/*
 * THIS CLASS CONTAINS THE CONSTANTS AND METHODS TO BRING FORTH THE Ctf VALUE:
 * Ctf: COEFFICIENT FOR UNEQUAL END MOMENT eqn 3.3.3.2(15)
 */
public class CoefficientCTF {
	private double Mmax,M1,M2;  //if M1 and M2 is specified as 0.0 then the assumption is that it is simply supported
	private String loadDistr;	//load distribution: either "uniSS" or "Other"
	
	public CoefficientCTF(double Mmax, double M1, double M2, String loadDistr){
		this.M1 = M1;
		this.M2 = M2;
		this.Mmax = Mmax;
		this.loadDistr = loadDistr;
	}
	
	public double getCtf(){
		double Ctf;
		
		if(M1 == 0.0 && M2 == 0.0){
			return 1.0;			
		}else if(loadDistr.equals("uniSS")){
			if(M1==M2){
				return 0.2;
			}else{
				return 1.0;
			}			
		}else if(loadDistr.equals("Other")){			
			if(Mmax >= Math.abs(M2)){
				return 1.0;
			}else{
				Ctf = 0.6 - 0.4*(M1/M2);			//eqn 3.3.3.2(15)
				return Ctf;
			}
		}else if(M1 == M2){
			return 0.2;
		}else{
			return 1.0;
		}
	}
	
}
