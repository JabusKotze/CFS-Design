package dSM;
/*
 * @author JJR Kotze (email 15692183@sun.ac.za)
 * For Department of Civil Engineering, Stellenbosch University
 * 29 October 2012 (Version 1.0 of CFS design)
 */
import section.*;
import materials.*;
import capacityDesign.LCCompression;
import capacityDesign.LZCompression;
import constants.*;
/*
 * THIS CLASS REPRESENTS THE METHODS ASSOSIATED WITH THE 
 * DIRECT STRENGTH METHOD FOR COMPRESSION MEMBERS:
 * SANS 10162-2:2011 CLAUSE 7.2.1
 */

public class DSMCompression {
	private double foc,fol,fod;		//foc: Resembles the euler buckling stress; fol: resembles the local buckling stress from CUFSM;  fod: resembles the distortional buckling stress from cufsm
	private String secName, matName;
	private double Lx,Ly,Lz;	//the effective length in the x, y and z direction respectively
	private double appliedForce; //The applied force onto the section generated from an force analysis.
	private double area;		//Area of the custom section designed in cufsm
	private double fy;			//yield stress for custom section. 
	private SectionDatabase sd = Parameters.getSectionDatabase();
	private MaterialDatabase md = Parameters.getMaterialDatabase();
	private LCCompression LCC;
	private LZCompression LZC;
	private String digit2;		//digit2 resembles a substring of the given section name to distinguish its type
	private GeneralConstants gc = new GeneralConstants();
	private CapacityReductionFactors cRF = new CapacityReductionFactors();
	/*
	 * Note:
	 * set fy, area, foc to 0.0 if the section used in the design is present in the section database
	 * otherwise for custom sections these parameters should be obtained through the use of cufsm or any other method
	 * 
	 * The values foc,fol and fod should be given in load factors; i.e. these ratios will be multiplied by the applied yield stress
	 */
	
	public DSMCompression(String secName, String matName, double appliedForce, double foc, double fol, double fod, double effLengthX, double effLengthY, double effLengthZ, double area, double fy){
		this.foc= foc;
		this.fod= fod;
		this.fol= fol;
		this.secName = secName;
		this.matName = matName;		
		this.Lx = effLengthX;
		this.Ly = effLengthY;
		this.Lz = effLengthZ;
		this.area = area;
		this.fy = fy;
		this.appliedForce = appliedForce;
		
		
		digit2 = secName.substring(0, 2);										//digit2 resembles a substring of the given section name to distinguish its type
	}
	
	
	public double memberCapacityEuler(){	//Clause 7.2.1.2----SANS 10162-2:2011
		double Nce;			// nominal member capacity for Euler buckling
		double Noc = getArea()*getFoc();		//eqn 7.2.1.2(4)
		double Ny = getArea()*getFy();			//eqn 7.2.1.2(5)
		double slenderness = Math.sqrt(Ny/Noc);	//eqn 7.2.1.2(3)
		
		if(slenderness<=gc.getSLDSMEuler()){
			Nce = Ny*Math.pow(0.658, Math.pow(slenderness,2));		//eqn 7.2.1.2(1)
			return Nce;
		}else{
			Nce = 0.877/Math.pow(slenderness,2)*Ny;					//eqn 7.2.1.2(2)
			return Nce;
		}
		
	}
	
	public double memberCapacityLocal(){	//Clause 7.2.1.3----SANS 10162-2:2011
		double Ncl;			//nominal member compression capacity for Local buckling consideration
		double Nol = getArea()*fol*getFy();					//eqn 7.2.1.3(4)
		double Nce = memberCapacityEuler();
		double slenderness = Math.sqrt(Nce/Nol);	//eqn 7.2.1.3(3)
		
		if(slenderness<=gc.getSLDSMLocal()){
			Ncl = Nce;								//eqn 7.2.1.3(1)
			return Ncl;
		}else{
			Ncl = (1-0.15*Math.pow(Nol/Nce, 0.4))*Math.pow(Nol/Nce, 0.4)*Nce;
			return Ncl;
		}
		
	}
	public double memberCapacityDistortional(){	//Clause 7.2.1.4-----SANS 10162-2:2011
		double Ncd; 		//nominal member compression capacity for Distortional buckling consideration
		double Nod = getArea()*fod*getFy();					//eqn 7.2.1.4(4)
		double Ny = getArea()*getFy();				//eqn 7.2.1.2(5)
		double slenderness = Math.sqrt(Ny/Nod);		//eqn 7.2.1.4(3)
		
		if(slenderness<=gc.getSLDSMDist()){
			Ncd = Ny;
			return Ncd;
		}else{
			Ncd = (1-0.25*Math.pow(Nod/Ny,0.6))*Math.pow(Nod/Ny,0.6)*Ny;
			return Ncd;
		}
	}
	
	
	public double getNc(){
		double Nc;
		double hold;
		double Nce = memberCapacityEuler();
		double Ncl = memberCapacityLocal();
		double Ncd = memberCapacityDistortional();
		
		hold = Math.min(Nce, Ncl);
		Nc = Math.min(hold, Ncd);
		
		return Nc;		
	}
	
	
	
	
	/*
	 * The following method gets the yield stress for the appropriate given section;
	 * Note that the yield stress is limited to the thickness of section due to SANS 517:2009
	 * currently it only takes Lipped channels and Z sections into consideration 
	 * But it could be extended for other sections such as ex. Hat sections.
	 * 
	 * If the given section could not be found it returns fy as 0.0
	 */
	public double getFy(){ 
		
		if(fy == 0.0){
			if(digit2.equals("CL")){				//lipped c sections
				return md.getMatProp(matName).getFy(sd.getLCSection(secName).getBaseThickness());
			}else if(digit2.equals("ZL")){			//lipped z sections
				return md.getMatProp(matName).getFy(sd.getZLSection(secName).getBaseThickness());
			}else if(digit2.equals("GS")){				
				return md.getMatProp(matName).getFy(sd.getGSSection(secName).getBaseThickness());
			}else{
				return 0.0;
			}
		}else{
			return fy;
		}
	}
	
	
	
	/*
	 * The following method returns the Area of the appropriate section
	 */
	
	public double getArea(){
		if(area == 0.0){
			if(digit2.equals("CL")){
				return sd.getLCSection(secName).getArea();
			}else if(digit2.equals("ZL")){
				return sd.getZLSection(secName).getArea();
			}else if(digit2.equals("GS")){				
				return sd.getGSSection(secName).getArea();
			}else{
				return 0.0;
			}
		}else{
			return area;
		}
		
	}
	public double getFol(){
		double f = fol*getFy();
		return f;
	}
	public double getFod(){
		double f = fod*getFy();
		return f;
	}
	
	/*
	 * This method returns the appropriate elastic compression member buckling stress in flexural, torsional
	 * and flexural-torsional buckling.
	 * 
	 * Note:
	 * if the foc was specified as 0.0 this method will determine the foc from the appropriate subclause 
	 * in clause 3.4 of SANS 10162-2:2011.
	 * 
	 * otherwise it will return the given foc obtained from cufsm for sections not listed in the section database
	 */
	public double getFoc(){
		
		if(foc == 0.0){
			if(digit2.equals("CL")){	//for lipped c sections
				LCC = new LCCompression(secName,matName,0.0,Lx,Ly,Lz,false);	//creating a design for lipped channel for compression to determine the foc; note: no distortional check is required
				return LCC.elasticBucklingStressFoc();
			}else if(digit2.equals("ZL")){	//for lipped z sections
				LZC = new LZCompression(secName,matName,0.0,Lx,Ly,Lz,false);
				return LZC.elasticBucklingStressFoc();  
			}else{
				return 0.0;
			}			
		}else{
			return foc*getFy();  //returns the given foc for sections not listed in the section database and for custom sections
		}
		
	}
	
	/*
	 * THE ULTIMATE DESIGN CAPACITY ACCORDING THE DSM METHOD IS CALCULATED IN THE FOLLOWING METHOD
	 */
	
	public double getDesignCapacity(){
		double N;
		double capReductionF;  //capacity reduction factor
		CompressionSectionReq cSR = new CompressionSectionReq(secName,matName); //capacity reduction factor
		String digit2 = secName.substring(0, 2);
		
		if(digit2.equals("CL")){						//Lipped channel
			if(cSR.lippedChannelS()){
				capReductionF = cRF.getComDSMtrue();
			}else{
				capReductionF = cRF.getMembers();
			}
		}else if(digit2.equals("ZL")){					//lipped z section
			if(cSR.lippedZsection()){
				capReductionF = cRF.getComDSMtrue();
			}else{
				capReductionF = cRF.getMembers();
			}
		}else if(digit2.equals("HS")){					//Hat section
			if(cSR.hatSection()){
				capReductionF = cRF.getComDSMtrue();
			}else{
				capReductionF = cRF.getMembers();
			}
		}else if(digit2.equals("SC")){					//lipped channel with intermediate stiffener
			if(cSR.lippedChannelIntStiffener()){
				capReductionF = cRF.getComDSMtrue();
			}else{
				capReductionF = cRF.getMembers();
			}
		}else if(digit2.equals("RU")){					//Rack Upright
			if(cSR.rackUpright()){
				capReductionF = cRF.getComDSMtrue();
			}else{
				capReductionF = cRF.getMembers();
			}
		}else{											//For all other sections
			capReductionF = cRF.getMembers();
		}
		
		
		N = getNc()*capReductionF;
		return N/1000.;		
	}
	
	
	/*
	 * THE FOLLOWING METHOD DETERMINES IF THE SECTION WILL FAIL UNDER THE GIVVEN COMPRESSION FORCE 
	 */
	public boolean willItFail(){
		boolean fail;
		
		if(appliedForce <= getDesignCapacity()){
			fail = false; //No it will not fail
		}else{
			fail = true; //It will fail.
		}
		
		return fail;
	}
	
	/*
	 * THE FOLLOWING METHOD PRINTS ALL INFORMATION AND RESULTS REGARDING THE DSM DESIGN
	 */
	
	public void printDSMcompressionResults(){
		String fail;
		
		if(willItFail()){
			fail = "Not OK: the section will fail for applied load of:     F = "+appliedForce+" kN";
		}else{
			fail = "OK: the section will not fail for applied load of:     F = "+appliedForce+" kN";
		}
		
		
		
		
		System.out.println("\n\n\nDIRECT STRENGTH METHOD RESULTS FOR THE "+secName+" IS AS FOLLOW");
		System.out.println("*******************************************************************************");
		System.out.println("The Euler member compression capacity:          Nce = "+memberCapacityEuler()/1000+" kN");
		System.out.println("The Local member compression capacity:          Ncl = "+memberCapacityLocal()/1000+" kN");
		System.out.println("The Distortional member compression capacity:   Ncd = "+memberCapacityDistortional()/1000+" kN");
		System.out.println("The NOMINAL MEMBER CAPACITY:                     Nc  = "+getNc()/1000+" kN");
		System.out.println("*******************************************************************************");
		System.out.println("THE PROPERTIES USED TO CALCULATE THE DSM CAPACITY:");
		System.out.println("*******************************************************************************");
		System.out.println("The Yield stress used:                          fy = "+getFy()+" MPa");
		System.out.println("The Area used:                                   A = "+getArea()+" mm2");
		System.out.println("The Euler buckling stress used:                foc = "+getFoc()+" MPa");
		System.out.println("The Local buckling stress used:                fol = "+fol*getFy()+" MPa");
		System.out.println("The distortional buckling stress used:         fod = "+fod*getFy()+" MPa");
		System.out.println("*******************************************************************************");
		System.out.println("*                            FINAL RESULT:                                    *");
		System.out.println("*******************************************************************************");
		System.out.println("The Ultimate Design Compression Capacity for DSM:      N = "+getDesignCapacity()+" kN");
		System.out.println(fail);
		System.out.println("*******************************************************************************");
		System.out.println("");
		System.out.println("");
		
		
	}

}
