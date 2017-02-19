package examples;
/*
 * @author JJR Kotze (email 15692183@sun.ac.za)
 * For Department of Civil Engineering, Stellenbosch University
 * 29 October 2012 (Version 1.0 of CFS design)
 */
import section.*;
import loadFactorscufsm.LoadFactorsCUFSM;
import materials.*;
import dSM.*;
import java.text.*;
import capacityDesign.*;

public class EngineerCompression {

	public static double maxlength;
	
	public static void main(String[] args) {
		
		DecimalFormat df = new DecimalFormat("#.###");
		
		boolean distCheck = true;						//If a distortional check should be done: true = yes; False = no.
		double appliedForce = 20.0;						//specify this force in kN: this is the force obtained from a force analysis program
		double lx = 1000;								//effective length in x direction
		double ly = lx;									//effective length in y direction
		double lz = lx;									//effective length in z direction
		String matName = "550Mpa";						//Name of material
		String secName = "CL 89x41x10x0.8";				//name of section: check database:  "CL 75x50x20x2.0"  ;  "CL 175x50x20x2.0 new"  ;  "CL 175x50x20x2.0"
				
		//DSM PARAMETERS
		double[] loadFactors = {0.5865,0.5192,0.44557,0.3853,0.33597,0.29537,0.26168,0.23348,0.20968,0.18943,0.17206,0.15708,0.14405,0.13267,0.12266,0.11382,0.10596,0.09896,0.092689,0.08705,0.081963};
		int turningpoint = 1000;				//the length at which the graph peaks between distortional and global buckling: specify in multiples of "multiples"
		int multiples = 100;					//The length multiples at which you want the output in the table
		double fol = 0.14;						//the local buckling load factor from CUFSM analysis: this minimum at the first turning point of the CUFSM output graph for the appropriate section
		double fod = 0.33;						//the distortional buckling load factor from CUFSM analysis: this minimum occurs at the second turning point of the CUFSM output graph for the appropriate section		
			
		
		double foe = 1.0;						//specify as 0.0 or 1.0.: 1.0 implies that foe(elastic load factors) will be determined from the load factors supplied in foed[]; 0.0 implies that foe will be determined according the effective width method(i.e: not very accurate for the use in DSM)
		double area = 0.0;
		double fy = 0.0;
		
		
		
		
		/*
		 * RESULTS: TO BE PRINTED:
		 */
		
		SectionDatabase sd = Parameters.getSectionDatabase();	   	//section database
		MaterialDatabase md = Parameters.getMaterialDatabase();	//material database
		LoadFactorsCUFSM currentLoadFactor = new LoadFactorsCUFSM(loadFactors,foe,multiples,turningpoint);
		String digit2 = secName.substring(0,2);
		
		//PRINT RESULTS FOR SINGULAR EFFECTIVE LENGTHS	: EFFECTIVE WIDTH METHOD
		if(digit2.equals("CL")){
			LCCompression lCC = new LCCompression(secName, matName,appliedForce, lx, ly, lz,distCheck);		//Initiates a Lipped Channel compression design
			System.out.println("\n");
			sd.getLCSection(secName).printSectionProperties();  //prints the section properties
			md.getMatProp(matName).printMatProp(sd.getLCSection(secName).getBaseThickness()); //prints the used material properties
			lCC.printEWMlippedCCompression(); 	//Prints the results of the effective width method	
			maxlength = lCC.maxLength()/multiples;
		}else if(digit2.equals("ZL")){
			LZCompression lZC = new LZCompression(secName, matName,appliedForce, lx, ly, lz,distCheck);		//Initiates a Lipped Channel compression design
			System.out.println("\n");
			sd.getZLSection(secName).printSectionProperties();  //prints the section properties
			md.getMatProp(matName).printMatProp(sd.getZLSection(secName).getBaseThickness()); //prints the used material properties
			lZC.printEWMlippedZCompression(); 	//Prints the results of the effective width method
			maxlength = lZC.maxLength()/multiples;
		}else if(digit2.equals("GS")){
			maxlength = sd.getGSSection(secName).getMaxlength()/multiples;
		}
		//PRINT RESULTS FOR SINGULAR EFFECTIVE LENGTHS: DIRECT STRENGTH METHOD
		DSMCompression dsmC = new DSMCompression(secName,matName,appliedForce,currentLoadFactor.getLoadFactor(lx),fol,fod,lx,ly,lz,area,fy);
		dsmC.printDSMcompressionResults();	//prints the results for the direct strength method
	
		
	//EFFECTIVE WIDTH METHOD TABLE RESULTS	
	//iterative process to determine capacity for different EFFECTIVE lengths: Effective width method
	System.out.println("\n\n\n\n\nCOMPRESSION EFFECTIVE WIDTH RESULTS FOR THE "+secName+" SECTION:");
	System.out.println("USING LENGTH INTERVALS OF "+multiples+" mm");
	System.out.println("*************************************************************");
	System.out.println("Length   AeFn     Aefy       Ns        Ncd      Nc       N");
	System.out.println(" (mm)    (mm2)    (mm2)     (kN)      (kN)     (kN)     (kN)");
	System.out.println("*************************************************************");
		
	if(digit2.equals("CL")){
		for(int i=1;i<maxlength;i++){
			double length = i*multiples;
			LCCompression lC = new LCCompression(secName, matName,appliedForce, length, length, length,distCheck);
			double AeFn = lC.effectiveAreaFn();
			double AeFy = lC.effectiveAreaFy();
			double Ns = lC.nominalSectionCapacity();
			double Ncd = lC.distMemberCapacity();
			double Nc;
			if(distCheck){
				Nc = lC.nominalMemberCapacityWithDistCheck();
			}else{
				Nc = lC.nominalMemberCapacityNoDistCheck();
			}
			
			double N = lC.ultimateDesignForce();
			
			System.out.println("");
			System.out.print(df.format(length)+"     ");
			System.out.print(df.format(AeFn)+"     ");
			System.out.print(df.format(AeFy)+"     ");
			System.out.print(df.format(Ns)+"     ");
			System.out.print(df.format(Ncd)+"     ");
			System.out.print(df.format(Nc)+"     ");
			System.out.print(df.format(N));
		}
		System.out.println("\n*************************************************************");
	}else if(digit2.equals("ZL")){
		for(int i=1;i<maxlength;i++){
			double length = i*multiples;
			LZCompression lC = new LZCompression(secName, matName,appliedForce, length, length, length,distCheck);
			double AeFn = lC.effectiveAreaFn();
			double AeFy = lC.effectiveAreaFy();
			double Ns = lC.nominalSectionCapacity();
			double Ncd = lC.distMemberCapacity();
			double Nc;
			if(distCheck){
				Nc = lC.nominalMemberCapacityWithDistCheck();
			}else{
				Nc = lC.nominalMemberCapacityNoDistCheck();
			}
			double N = lC.ultimateDesignForce();
			
			System.out.println("");
			System.out.print(df.format(length)+"     ");
			System.out.print(df.format(AeFn)+"     ");
			System.out.print(df.format(AeFy)+"     ");
			System.out.print(df.format(Ns)+"     ");
			System.out.print(df.format(Ncd)+"     ");
			System.out.print(df.format(Nc)+"     ");
			System.out.print(df.format(N));
		}
		System.out.println("\n*************************************************************");
	}else if(digit2.equals("GS")){
		//Cannot be betermined according Effective width method
		System.out.println("THIS SECTION CANNOT BE DETERMINED ACCORDING THE EFFECTIVE WIDTH METHOD");
	}else{
		//UNKNOWN SECTION
		System.out.println("THIS SECTION IS UNKNOWN FOR THE EFFECTIVE WIDTH METHOD");
	}
	
	
	
	//RESULTS PRINTED FOR DIRECT STRENGTH METHOD
	//iterative process to determine capacity for different EFFECTIVE lengths: Direct strength method
		System.out.println("\n\n\n\n\nCOMPRESSION DIRECT STRENGTH METHOD RESULTS FOR THE "+secName+" SECTION:");
		System.out.println("USING LENGTH INTERVALS OF "+multiples+" mm");
		System.out.println("***************************************************************************");
		System.out.println("Length   foc     fol      fod       Nce      Ncl      Ncd       Nc       N");
		System.out.println(" (mm)   (MPa)   (MPa)    (MPa)     (kN)     (kN)      (kN)     (kN)     (kN)");
		System.out.println("***************************************************************************");
		for(int i=1;i<maxlength;i++){
			double length = i*multiples;
			DSMCompression DSM = new DSMCompression(secName,matName,appliedForce,currentLoadFactor.getLoadFactor(length),fol,fod,length,length,length,area,fy);
			double foc = DSM.getFoc();
			double Fol = DSM.getFol();
			double Fod = DSM.getFod();
			double Nce = DSM.memberCapacityEuler();
			double Ncl = DSM.memberCapacityLocal();
			double Ncd = DSM.memberCapacityDistortional();
			double Nc = DSM.getNc();
			double N = DSM.getDesignCapacity();
			
			System.out.println("");
			System.out.print(df.format(length)+"    ");
			System.out.print(df.format(foc)+"    ");
			System.out.print(df.format(Fol)+"    ");
			System.out.print(df.format(Fod)+"    ");
			System.out.print(df.format(Nce/1000.)+"    ");
			System.out.print(df.format(Ncl/1000.)+"    ");
			System.out.print(df.format(Ncd/1000.)+"    ");
			System.out.print(df.format(Nc/1000.)+"    ");
			System.out.print(df.format(N)+"    ");
			
		}
		System.out.println("\n***************************************************************************");
		
		
		
	}
	
	
	/*
	 * CL 89X41X10X0.8 CUFSM DATA
	 */
//	double[] loadFactors = {0.5865,0.5192,0.44557,0.3853,0.33597,0.29537,0.26168,0.23348,0.20968,0.18943,0.17206,0.15708,0.14405,0.13267,0.12266,0.11382,0.10596,0.09896,0.092689,0.08705,0.081963};
//	int turningpoint = 1000;				//the length at which the graph peaks between distortional and global buckling: specify in multiples of "multiples"
//	int multiples = 100;					//The length multiples at which you want the output in the table
//	double fol = 0.14;						//the local buckling load factor from CUFSM analysis: this minimum at the first turning point of the CUFSM output graph for the appropriate section
//	double fod = 0.33;						//the distortional buckling load factor from CUFSM analysis: this minimum occurs at the second turning point of the CUFSM output graph for the appropriate section		
		
	
	/*
	 * ZL 89X41X10X0.8 CUFSM DATA
	 */
	
//	double[] loadFactors = {0.57876,0.52524,0.4518,0.38884,0.33716,0.29476,0.2597,0.23046,0.20584,0.18493,0.16703,0.1516,0.1382,0.1265,0.11622,0.10714,0.099082,0.091896,0.085467,0.079687,0.074474,0.069755,0.065472,0.061568,0.058008,0.054745,0.051746,0.048987,0.046445,0.044097,0.041925};
//	int turningpoint = 1000;				//the length at which the graph peaks between distortional and global buckling: specify in multiples of "multiples"
//	int multiples = 100;					//The length multiples at which you want the output in the table
//	double fol = 0.14;						//the local buckling load factor from CUFSM analysis: this minimum at the first turning point of the CUFSM output graph for the appropriate section
//	double fod = 0.33;						//the distortional buckling load factor from CUFSM analysis: this minimum occurs at the second turning point of the CUFSM output graph for the appropriate section		
		
}
