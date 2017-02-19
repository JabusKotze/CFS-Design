package constants;
/*
 * @author JJR Kotze (email 15692183@sun.ac.za)
 * For Department of Civil Engineering, Stellenbosch University
 * 29 October 2012 (Version 1.0 of CFS design)
 */

public class GeneralConstants {
	
	public static final double bTReduction = 0.04;		//From SANS 517:2009 for determining the Base metal thickness (clause 5.2.2)
	
	public static double slendernessLimitDSMEuler = 1.5; //limit on slenderness for direct strength method for calculating the compression member capacity considering Euler buckling
	public static double slendernessLimitDSMLocal = 0.776; //limit on slenderness for direct strength method for calculating the compression member capacity considering Local buckling
	public static double slendernessLimitDSMDist = 0.561; //limit on slenderness for direct strength method for calculating the compression member capacity considering Distortional buckling
	
	
	public static double slendernessLimitDSMLocalBend = 0.776; //limit on slenderness for direct strength method for calculating the bending member capacity considering Local buckling
	public static double slendernessLimitDSMDistBend = 0.673; //limit on slenderness for direct strength method for calculating the bending member capacity considering Distortional buckling
	
	
	
	public static double bendingSlendernessLimitBottom = 0.6; //limit on slenderness for bending used in eqn 3.3.3.2(3) and (4)
	public static double bendingSlendernessLimitTop = 1.336;	//limit on slenderness for bending used in eqn 3.3.3.2(4) and (5)
	
	
	public static double radiusThicknessRatio = 3.0;  //the ratio of the radius of angles with respect to thickness
	
	public static double plateBucklingCoeffStiffElem = 4.0;
	public static double plateBucklingCoeffUnStiffElem = 0.43;
	
	
	public GeneralConstants(){
		
	}
	
	public double getBTReduction(){
		return bTReduction;
	}
	public double getSLDSMEuler(){
		return slendernessLimitDSMEuler;
	}
	public double getSLDSMLocal(){
		return slendernessLimitDSMLocal;
	}
	public double getSLDSMDist(){
		return slendernessLimitDSMDist;
	}
	public double getRadiusThicknessRatio(){
		return radiusThicknessRatio;
	}
	public double getBendingSlendernessLimitBottom(){
		return bendingSlendernessLimitBottom;
	}
	public double getBendingSlendernessLimitTop(){
		return bendingSlendernessLimitTop;
	}
	public double getPlateBucklingCoeffStiffElem(){
		return plateBucklingCoeffStiffElem;
	}
	public double getPlateBucklingCoeffUnStiffElem(){
		return plateBucklingCoeffUnStiffElem;
	}
	public double getSlendernessLimitDSMLocalBend(){
		return slendernessLimitDSMLocalBend;
	}
	public double getSlendernessLimitDSMDistBend(){
		return slendernessLimitDSMDistBend;
	}

}
