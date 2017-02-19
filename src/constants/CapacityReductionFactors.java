package constants;
/*
 * @author JJR Kotze (email 15692183@sun.ac.za)
 * For Department of Civil Engineering, Stellenbosch University
 * 29 October 2012 (Version 1.0 of CFS design)
 */


/*
 * THE CAPACITY REDUCTION FACTORS ARE FROM TABLE 1.6 OF SANS 10162-2:2011
 */

public class CapacityReductionFactors {
	
	private final double compression = 0.85; //for concentrically loaded compression members: table 1.6 (d)
	private final double bendingMember = 0.9; //for members subject to bending: for member moment capacity: table 1.6(c)
	private final double comDSMtrue = 0.85; 	//for the direct strength method for compression members limited to table 7.1.1
	private final double members = 0.8;			//for the DSM compression members not limited to the sections prescribed in table 7.1.1: from clause 1.6.3(c)(i)
	private final double bendDSMtrue = 0.9;		//for the direct strength method for bending members limited to table 7.1.2
	private final double tension = 0.9;			//From table 1.6 for members subject to tension
	
	public CapacityReductionFactors(){		
		}
		
	public double getCompression(){
		return compression;
	}
	public double getComDSMtrue(){
		return comDSMtrue;
	}
	public double getMembers(){
		return members;
	}
	public double getBendingMember(){
		return bendingMember;
	}
	public double getBendDSMtrue(){
		return bendDSMtrue;
	}
	public double getTension(){
		return tension;
	}
}
