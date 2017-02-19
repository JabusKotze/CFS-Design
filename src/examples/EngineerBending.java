package examples;
/*
 * @author JJR Kotze (email 15692183@sun.ac.za)
 * For Department of Civil Engineering, Stellenbosch University
 * 29 October 2012 (Version 1.0 of CFS design)
 */
import section.*;
import dSM.*;
import java.text.*;
import capacityDesign.*;
import loadFactorscufsm.LoadFactorsCUFSM;

public class EngineerBending {

	public static double maxlength;
	public static void main(String[] args) {
			 
		 DSMBending DSMB;
		 DecimalFormat df = new DecimalFormat("#.###");
		 
		/*
		 * SPECIFY THE FOLLOWING PARAMETERS		 
		 */
		 String secName = "ZL 89x41x10x0.8";		//choose name from section database
		 String matName = "550Mpa";					//choose material from material database
		 String axis = "X";							//specify as either "X" (symmetric axis) or "Y"
		 String loadDistr = "uniSS";				//specify as either "uniSS" (uniformly distributed simply supported) or "Other"
		 String loadAppl = "SC";					//specify as either "SC" (applied to shear centre) ; "TF" (load applied to Tension Flange) ; "CF" (load applied to compression flange)
		 String bracing = "No";						//bracing spacing for SS: specify as either "No" (no bracing) ; "C" (bracing at half span) ; "T" (Bracing at third points: this applies for centre unbraced section)
		
		 double maxAppliedMoment = 1.0;				//Note for Y-axis bending:specify as positive if max moment generates compression at lips ; negative if max moment generates compression at the web:
		 boolean distCheck = false;					//False: don't do distortional check ; True: include distortional check.
		 double Lx = 1000.0;						//effective lengths
		 double Ly = Lx;
		 double Lz = Lx;
		 		 
		 double Mmax = 0.0;							//
		 double M1 = 0.0;
		 double M2 = 0.0;
		 double M3 = 0.0;
		 double M4 = 0.0;
		 double M5 = 0.0;	
		 
		 //DSM PARAMETERS		 
		//y-bending: ZL section
			double[] loadFactors = {1.3001,1.2769,1.1954,1.09,0.9834,0.88491,0.79713,0.72004,0.65266,0.5938,0.54227,0.49703,0.45716,0.42189,0.39056,0.36264,0.33765,0.31522,0.295,0.27672,0.26015,0.24508,0.23133,0.21876,0.20723,0.19665,0.18689,0.17788,0.16955,0.16184};   
			int turningpoint = 1100;				//the length at which the graph peaks between distortional and global buckling: specify in multiples of "multiples"
			int multiples = 100;					//The length multiples at which you want the output in the table
			double Mol = 0.94;
			double Mod = 0.63;
		 double Mo = 1.0;						//specify as 0.0 or 1.0.		0.0: uses EW method to determin Mo ; else if set to 1.0 uses the factors from cufsm to determine Mo
		 double fy = 0.0;						//specify as 0.0 if the yield stress of the material is in the database, otherwise specify the yield stress
		
		 /*
		  * END OF PARAMETERS
		  */
		
		 
		 /*
		  * PRINT RESULTS
		  */
		 //EFFECTIVE WIDTH METHOD: CAPACITY FOR A SPECIFIED EFFECTIVE LENGTH
		 SectionDatabase sd = Parameters.getSectionDatabase();
		 LoadFactorsCUFSM currrentLoadFactor = new LoadFactorsCUFSM(loadFactors,Mo,multiples,turningpoint);  //this will go and find the appropriate factor as listed in Moed[] for the determination of Mo
		 String digit2 = secName.substring(0,2);
		 		
		if(digit2.equals("CL")){
			LCBending LCB = new LCBending(secName,matName,maxAppliedMoment,axis,Lx,Ly,Lz,distCheck,Mmax,M1,M2,M3,M4,M5,loadDistr,loadAppl,bracing);
			sd.getLCSection(secName).printSectionProperties();
			if(axis == "X"){
				LCB.printResultsAboutX();
			}else{
				if(maxAppliedMoment<0.0){
					LCB.printResultsAboutYTension();
				}else{
					LCB.printResultsAboutYCompression();
				}
			}
			maxlength = LCB.maxLength()/multiples;
		}else if(digit2.equals("ZL")){
			LZBending LZB = new LZBending(secName,matName,maxAppliedMoment,axis,Lx,Ly,Lz,distCheck,Mmax,M1,M2,M3,M4,M5,loadDistr,loadAppl,bracing);
			sd.getZLSection(secName).printSectionProperties();
			if(axis.equals("X")){
				LZB.printResultsAboutX();
			}else{
				LZB.printResultsAboutY();
			}
			maxlength = LZB.maxLength()/multiples;
		}else if(digit2.equals("GS")){
			maxlength = sd.getGSSection(secName).getMaxlength();
		}
		
		//PRINT DSM RESULTS FOR A SPECIFIED EFFECTIVE LENGTH
		DSMB = new DSMBending(secName,matName,axis,maxAppliedMoment,currrentLoadFactor.getLoadFactor(Lx),Mol,Mod,Lx,Ly,Lz,fy,Mmax, M1, M2, M3, M4, M5, loadDistr, loadAppl, bracing);
		DSMB.printDSMBendingResult();
		
		
		
		
		
		/*
		 * CREATING TABLES
		 * Iterative procedure for calculating capacity for different effective lengths
		 * FIRST: EFFECTIVE WIDTH METHOD RESULTS FOR BENDING
		 */
		String orientation;
		if(digit2.equals("CL")){
			if(axis.equals("X")){
				orientation = "ORIENTATION: N.A";
			}else{
				if(maxAppliedMoment<0.0){
					orientation = "COMPRESSION OCCURS AT THE WEB";
				}else{
					orientation = "COMPRESSION OCCURS AT THE EDGE STIFFENER";
				}
			}	
		}else if(digit2.equals("ZL")){
			orientation = "ORIENTATION: N.A";
		}else if(digit2.equals("GS")){
			orientation = "ORIENTATION: N.A";
		}else{
			orientation = "";
		}			
		
		System.out.println("\n\n\n\n\nBENDING EFFECTIVE WIDTH RESULTS FOR THE "+secName+" SECTION:");
		System.out.println("BENDING ABOUT THE : "+axis+" AXIS");
		System.out.println(orientation);
		System.out.println("USING LENGTH INTERVALS OF "+multiples+" mm");
		System.out.println("*************************************************************");
		System.out.println("Length    Mb       Mbd       Ms        M       Fail");
		System.out.println(" (mm)    (kNm)    (kNm)     (kNm)     (kNm)     ()");
		System.out.println("*************************************************************");
		
		if(digit2.equals("CL")){
			for(int i=1;i<maxlength;i++){
				double length = i*multiples;
				LCBending EW = new LCBending(secName,matName,maxAppliedMoment,axis,length,length,length,distCheck,Mmax,M1,M2,M3,M4,M5,loadDistr,loadAppl,bracing);
				double mb = EW.nominalMemberMomentCapMbNoDistCheck()/1000000.;
				double mbd = EW.distMemberCapacity()/1000000.;
				double ms = EW.nominalSectionMomentCapMs()/1000000.;
				double m = EW.UltimateMemberCapacity()/1000000.;
				String fail;
				if(EW.willItfail()){
					fail ="YES";
				}else{
					fail = "NO";
				}
				
				System.out.println("");
				System.out.print(df.format(length)+"      ");
				System.out.print(df.format(mb)+"      ");
				System.out.print(df.format(mbd)+"      ");
				System.out.print(df.format(ms)+"      ");
				System.out.print(df.format(m)+"      ");
				System.out.print(fail);		
			}
			
		}else if(digit2.equals("ZL")){
			for(int i=1;i<maxlength;i++){
				double length = i*multiples;
				LZBending EW = new LZBending(secName,matName,maxAppliedMoment,axis,length,length,length,distCheck,Mmax,M1,M2,M3,M4,M5,loadDistr,loadAppl,bracing);
				double mb = EW.nominalMemberMomentCapMbNoDistCheck()/1000000.;
				double mbd = EW.distMemberCapacity()/1000000.;
				double ms = EW.nominalSectionMomentCapMs()/1000000.;
				double m = EW.UltimateMemberCapacity()/1000000.;
				String fail;
				if(EW.willItfail()){
					fail ="YES";
				}else{
					fail = "NO";
				}
				
				System.out.println("");
				System.out.print(df.format(length)+"      ");
    			System.out.print(df.format(mb)+"      ");
				System.out.print(df.format(mbd)+"      ");
				System.out.print(df.format(ms)+"      ");
				System.out.print(df.format(m)+"      ");
				System.out.print(fail);		
			}
		}else if(digit2.equals("GS")){
			//Cannot be betermined according Effective width method
			System.out.println("THIS SECTION CANNOT BE DETERMINED ACCORDING THE EFFECTIVE WIDTH METHOD");
		}else{
			//UNKNOWN SECTION
			System.out.println("THIS SECTION IS UNKNOWN FOR THE EFFECTIVE WIDTH METHOD");
		}
		
		
		
		/*
		 * PART2: DIRECT STRENGTH METHOD TABLE PRINT
		 */
		
		System.out.println("\n\n\n\n\nBENDING DIRECT STRENGTH METHOD RESULTS FOR THE "+secName+" SECTION:");
		System.out.println("BENDING ABOUT THE : "+axis+" AXIS");
		System.out.println(orientation);
		System.out.println("USING LENGTH INTERVALS OF "+multiples+" mm");
		System.out.println("*************************************************************");
		System.out.println("Length   Moc       Mol      Mod       Mbe       Mbl      Mbd        Mb         M");
		System.out.println(" (mm)   (MPa)     (MPa)    (MPa)     (kNm)     (kNm)    (kNm)      (kNm)     (kNm)");
		System.out.println("*************************************************************");
		for(int i=1;i<maxlength;i++){
			double length = i*multiples;
			 DSMBending DSM = new DSMBending(secName,matName,axis,maxAppliedMoment,currrentLoadFactor.getLoadFactor(length),Mol,Mod,length,length,length,fy,Mmax, M1, M2, M3, M4, M5, loadDistr, loadAppl, bracing);
			double moc = DSM.getMo()/1000000.;
			double mol = DSM.getMol()/1000000.;
			double mod = DSM.getMod()/1000000.;
			double mbe = DSM.nominalMemberMomentCapEuler()/1000000.;
			double mbl = DSM.nominalMemberMomentCapLocal()/1000000.;
			double mbd = DSM.nominalMemberMomentCapDistortional()/1000000.;
			double mb = DSM.getMb()/1000000.;
			double m = DSM.getDesignCapacity()/1000000.;
			
			System.out.println("");
//			System.out.print(df.format(length)+"     ");
//			System.out.print(df.format(moc)+"     ");
//			System.out.print(df.format(mol)+"     ");
//			System.out.print(df.format(mod)+"     ");
//			System.out.print(df.format(mbe)+"     ");
//			System.out.print(df.format(mbl)+"     ");
//			System.out.print(df.format(mbd)+"     ");
//			System.out.print(df.format(mb)+"     ");
			System.out.print(df.format(m)+"     ");
			
		}
		
	}
	
	/*
	 * CL 89X41X10X0.8 CUFSM DATA
	 */
	
//	 //x-bending
//	 double[] loadFactors = {0.95287,0.93553,0.87204,0.7916,0.71195,0.63924,0.57483,0.51841,0.46916,0.42616,0.38855,0.35556,0.32651,0.30086,0.27811,0.25786,0.23977,0.22356,0.20899,0.19583,0.18393};
//	 int turningpoint = 1000;				//the length at which the graph peaks between distortional and global buckling: specify in multiples of "multiples"
//	 int multiples = 100;					//The length multiples at which you want the output in the table
//	 double Mol = 0.53;
//	 double Mod = 0.56;   
	
	 //y-bending: lip in compression		 
//	 double[] loadFactors = {1.1775,1.1162,1.0341,0.94503,0.85762,0.7762,0.70253,0.63693,0.57897,0.52794,0.48304,0.44352,0.40858,0.37768,0.35025,0.32581,0.30397,0.28439,0.26679,0.25088,0.23648};   
//	 int turningpoint = 1000;				//the length at which the graph peaks between distortional and global buckling: specify in multiples of "multiples"
//	 int multiples = 100;					//The length multiples at which you want the output in the table
//	 double Mol = 1.22;
//	 double Mod = 0.62;
	 
	 
	 
	 //y-bending: web in compression
	 	//double[] loadFactors = {7.7474,7.5659,7.1946,6.8437,6.512,6.1985};   
	 	//int turningpoint = 2500;				//the length at which the graph peaks between distortional and global buckling: specify in multiples of "multiples"
	 	//int multiples = 100;					//The length multiples at which you want the output in the table
	 	//double Mol = 0.29;
	 	//double Mod = 7.7474;	 
	
	
	/*
	 * ZL 89X41X10X0.8 CUFSM DATA
	 */
	
	//x-bending: ZL section
		//double[] loadFactors = {1.0607,0.97021,0.84574,0.73364,0.6395,0.56133,0.49625,0.4417,0.39563,0.35642,0.32279,0.29375,0.26852,0.24645,0.22705,0.20991,0.19468,0.1811,0.16894,0.158,0.14814,0.1392,0.13109,0.12369,0.11694,0.11075,0.10507,0.099838,0.095012,0.090552,0.086419};   
		//int turningpoint = 1000;				//the length at which the graph peaks between distortional and global buckling: specify in multiples of "multiples"
		//int multiples = 100;					//The length multiples at which you want the output in the table
		//double Mol = 0.53;
		//double Mod = 0.56;
	
	//y-bending: ZL section
		//double[] loadFactors = {1.3001,1.2769,1.1954,1.09,0.9834,0.88491,0.79713,0.72004,0.65266,0.5938,0.54227,0.49703,0.45716,0.42189,0.39056,0.36264,0.33765,0.31522,0.295,0.27672,0.26015,0.24508,0.23133,0.21876,0.20723,0.19665,0.18689,0.17788,0.16955,0.16184};   
		//int turningpoint = 1100;				//the length at which the graph peaks between distortional and global buckling: specify in multiples of "multiples"
		//int multiples = 100;					//The length multiples at which you want the output in the table
		//double Mol = 0.94;
		//double Mod = 0.63;

}
