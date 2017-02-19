package effectiveWidth;
/*
 * @author JJR Kotze (email 15692183@sun.ac.za)
 * For Department of Civil Engineering, Stellenbosch University
 * 29 October 2012 (Version 1.0 of CFS design)
 */

import section.Parameters;
import materials.*;
import constants.*;

/*this is from Clause 2.2.1 (SANS 10162-2)
 * This Class contains the methods to calculate the effective width
 * of a Uniformly compressed stiffened element
 */


public class UniCompressedStifElements {
	
	private double stressF;
	private double p; // effective width factor to be received from appropriate clause if this is passed as 0.0 then the factor of this class will be used
	private double k; // plate buckling coefficient: if it was passed on as 0.0 then it will use k = 4.0 from this clause.
	private double t,b; // thickness and width respectively
	private String matProp,secName;
	private MaterialDatabase md = Parameters.getMaterialDatabase();
	private GeneralConstants gc = new GeneralConstants();
	
	
	public UniCompressedStifElements(double stressF, double k, double p, double t, double b, String matProp, String secName){			
		this.stressF = stressF;
		this.k = k;
		this.t = t;
		this.b = b;
		this.matProp = matProp;	
		this.secName = secName;
		this.p = p;
	}
	
	public double slendernessRatio(){
		double sR = Math.pow(stressF/plateElasticBucklingStress(), 0.5);	
		
		//System.out.println("The slenderness Ratio for stiffened element = "+sR);
		return sR;
	}
	
	public  double plateElasticBucklingStress(){
		
		double fcr;
		double v1 = k * Math.pow(Math.PI, 2) * md.getMatProp(matProp).getYoung();
		double v2 = 12*(1-Math.pow(md.getMatProp(matProp).getPoisson(),2));
		double v3 = Math.pow(t/b, 2);		
		fcr = (v1/v2)*v3;
		/*System.out.println("the height is "+b);
		*System.out.println("the thickness is "+t);
		*System.out.println("the k is "+k);
		*System.out.println("the youngs is "+md.getMatProp(matProp,t).getYoung());
		*System.out.println("the poisson is "+md.getMatProp(matProp,t).getPoisson());
		*System.out.println("\n\nThe critical plate elastic buckling stress fcr = "+fcr+" MPa");
		*/
		return fcr;	
	}
	
	public double effectiveWidthFactor(){
		double pNow = (1- 0.22/slendernessRatio())/slendernessRatio();  //this is the effective width factor as per eqn 2.2.1.2(4)
		if(p==0){
		if(pNow<=1){
			//System.out.println("\nThe effective width factor has been taken as = "+pNow);
			return pNow;
		}else{
			//System.out.println("\nThe effective width factor has been taken as unity");
			return 1.0;
		}
		}else {
			//System.out.println("\nThe effective width factor has been taken as = "+p);
			return p;
		}
	}
	
	public double getK(){		//returns the k value to be used if it was given as 0.0 then it will use k=4.0
		if(k == 0.0){
			return gc.getPlateBucklingCoeffStiffElem();
		}else{
			return k;
		}
	}
	
	public double effectiveWidth(){
		if(slendernessRatio()<=0.673){
			return b;
		}else{
			return b*effectiveWidthFactor();
		}
	}
	
	
			

}
