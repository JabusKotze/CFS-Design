package dSM;
/*
 * @author JJR Kotze (email 15692183@sun.ac.za)
 * For Department of Civil Engineering, Stellenbosch University
 * 29 October 2012 (Version 1.0 of CFS design)
 */
import materials.*;
import section.*;
import capacityDesign.*;
import constants.*;

public class DSMBending {
	private String secName,matName;
	private double Lx,Ly,Lz;
	private double Mo;			/*elastic buckling moment: can be given as 0.0 for a known section such as lipped channel or Z section
								*							or as a factor from cufsm for a general section	
								*/				
	private double Mol;			//local buckling moment factor:  Should be given as load factor as obtained from cufsm
	private double Mod; 		//distortional buckling moment factor: should be given as load factor as obtained from cufsm
	private double maxMoment;	/*The maximum moment from analysis:  
							     *for lipped c sections this moment should be given as negative if max moment produces compression at the web side
							     *for lipped c sections this moment should be given as positive if max moment produces compression at the lip side
							     */
	private String digit2;
	private double fy;			//Yield stress: it can be given as 0.0 if section used is listed in section database; otherwise it should be given
	private String axis;		/*axis about which bending occurs for known sections: lipped channel or Z sections.
								 *given as "X" or "Y"
								*/
	
	private MaterialDatabase md = Parameters.getMaterialDatabase();
	private SectionDatabase sd = Parameters.getSectionDatabase();
	private GeneralConstants gc = new GeneralConstants();
	private LCBending LCB;		//This is to calculate Mo for lipped c section if Mo ratio was given as 0.0
	private LZBending LZB;
	private CapacityReductionFactors cRF = new CapacityReductionFactors();
	
	
	private double Mmax,M1,M2,M3,M4,M5;
	private String loadDistr,loadAppl,bracing;
	
			/* Mmax: maximum absolute moment in unbraced length (can be given as 0.0 if you don't know this force)
			 * M1: smaller absolute Moment at the ends (please specify negative or positive moment to distinguish curvature)
			 * M2: larger absolute moment at the ends	(please specify negative or positive moment to distinguish curvature)
			 * M3: absolute moment at quarter point of unbraced length
			 * M4: absolute unbraced midspan moment 
			 * M5: absolute moment at 3quarters of unbraced length
			 * loadDistr: 	load distribution type: please specify as: "uniSS" (uniformly distributed simply supported: makes use of table 3.3.3.2) 
			 * 				or "Other" (if you don't know the distribution type and it sends back Cb as unity)
			 * loadAppl: 	verifies where the load is applied please specify as:
			 * 				"TF" (Tension Flange) or
			 * 				"SC" (shear centre) or
			 * 				"CF" (compression flange)
			 * bracing: 	if it was a uniformly distributed load and simply supported member then please specify the
			 * 				bracing intervals as:
			 * 				"No" (No bracing)
			 * 				"C" (braced at centre: thus overall length = 2*effective length)
			 * 				"T" (braced at thirds: overall length = 3*effective length:   only for the central section)
			 */
	
	
	public DSMBending(String secName, String matName,String axis, double maxMoment, double Mo, double Mol, double Mod, double LX, double LY, double LZ,double fy, double Mmax,double M1,double M2,double M3,double M4,double M5,String loadDistr,String loadAppl,String bracing){
		this.secName = secName;
		this.matName = matName;
		this.fy = fy;
		this.maxMoment = maxMoment;
		this.Lx = LX;
		this.Ly = LY;
		this.Lz = LZ;
		this.Mo = Mo;
		this.Mol = Mol;
		this.Mod = Mod;
		this.axis = axis;
		
		this.bracing = bracing;
		this.Mmax = Mmax;
		this.M1 = M1;
		this.M2 = M2;
		this.M3 = M3;
		this.M4 = M4;
		this.M5 = M5;
		this.loadAppl = loadAppl;
		this.loadDistr = loadDistr;
		
		digit2 = this.secName.substring(0, 2);		//digit2 resembles a substring of the given section name to distinguish its type
	}
	
	public double getDesignCapacity(){
		double M;
		double capReductionF;  //capacity reduction factor
		BendingSectionReq cSR = new BendingSectionReq(secName,matName); //capacity reduction factor
		String digit2 = secName.substring(0, 2);
		
		if(digit2.equals("CL")){						//Lipped channel
			if(cSR.lippedChannelS()){
				capReductionF = cRF.getBendDSMtrue();
			}else{
				capReductionF = cRF.getMembers();
			}
		}else if(digit2.equals("ZL")){					//lipped z section
			if(cSR.lippedZsection()){
				capReductionF = cRF.getBendDSMtrue();
			}else{
				capReductionF = cRF.getMembers();
			}
		}else if(digit2.equals("HD")){					//Hat Deck section : with intermediate stiffeners
			if(cSR.stifHatDecks()){
				capReductionF = cRF.getBendDSMtrue();
			}else{
				capReductionF = cRF.getMembers();
			}
		}else if(digit2.equals("SC")){					//lipped channel with intermediate stiffener
			if(cSR.lippedChannelIntStiffener()){
				capReductionF = cRF.getBendDSMtrue();
			}else{
				capReductionF = cRF.getMembers();
			}
		}else if(digit2.equals("TD")){					//Trapezoids with intermediate stiffeners
			if(cSR.stifTrapezoids()){
				capReductionF = cRF.getBendDSMtrue();
			}else{
				capReductionF = cRF.getMembers();
			}
		}else{											//For all other sections
			capReductionF = cRF.getMembers();
		}
		
		
		M = getMb()*capReductionF;
		return M;
	}
	
	
	public double getMb(){			//returns the minimum of global,local and distortional capacitys
		double Mb;
		double hold;
		double Mbe = nominalMemberMomentCapEuler();
		double Mbl = nominalMemberMomentCapLocal();
		double Mbd = nominalMemberMomentCapDistortional();
		
		hold = Math.min(Mbe, Mbl);
		Mb = Math.min(hold, Mbd);
		
		return Mb;		
	}
	
	public double nominalMemberMomentCapEuler(){	//CLAUSE 7.2.2.2 for flexural-torsional buckling
		double Mbe;
		double mo = getMo();
		double My = getMy();
		
		if(mo<0.56*My){
			Mbe = mo;								//eqn 7.2.2.2(1)
		}else if(mo >= 0.56*My  && mo <=2.78*My){
			Mbe = (10./9.)*My*(1.-10*My/(36*My));	//eqn 7.2.2.2(2)
		}else{
			Mbe = My;								//eqn 7.2.2.2(3)
		}
		
		return Mbe;
	}
	public double nominalMemberMomentCapLocal(){	//CLAUSE 7.2.2.3 for local buckling 
		double Mbl;
		double Mbe = nominalMemberMomentCapEuler();
		double mol = getMol();
		double slenderness = Math.sqrt(Mbe/mol);
		
		if(slenderness <= gc.getSlendernessLimitDSMLocalBend()){
			Mbl = Mbe;															//eqn 7.2.2.3(1)
		}else{
			Mbl = (1-0.15*Math.pow(mol/Mbe , 0.4))*Math.pow(mol/Mbe,0.4)*Mbe;	//eqn 7.2.2.3(2)
		}
		
		return Mbl;	
	}
	
	
	public double nominalMemberMomentCapDistortional(){	//CLAUSE 7.2.2.4 for distortional buckling
		double Mbd;
		double My = getMy();
		double mod = getMod();
		double slenderness = Math.sqrt(My/mod);
		
		if(slenderness <= gc.getSlendernessLimitDSMDistBend()){
			Mbd = My;
		}else{
			Mbd = (1-0.22*Math.pow(mod/My , 0.5))*Math.pow(mod/My,0.5)*My;	//eqn 7.2.2.3(2)
		}
		
		return Mbd;		
	}
	
	
	
	public double getMo(){//elastic buckling moment
		double mo;
		double My = getMy();
		
		if(digit2.equals("CL")){
			LCB = new LCBending(secName,matName,maxMoment,axis,Lx,Ly,Lz,false,Mmax,M1,M2,M3,M4,M5,loadDistr,loadAppl,bracing);
			if(Mo == 0.0 ){
				if(axis.equals("X")){
					mo = LCB.xAxisMo();
				}else{
					mo = LCB.yAxisMo();
				}
			}else{
				mo = Mo*My;
			}
		}else if(digit2.equals("ZL")){
			LZB = new LZBending(secName,matName,maxMoment,axis,Lx,Ly,Lz,false,Mmax,M1,M2,M3,M4,M5,loadDistr,loadAppl,bracing);
			if(Mo == 0.0 ){
				if(axis.equals("X")){
					mo = LZB.xAxisMo();
				}else{
					mo = LZB.yAxisMo();
				}
			}else{
				mo = Mo*My;
			}
		}else{
			mo = Mo*My;
		}
		
		
		return mo;
	}
	
	
	
	public double getMol(){
		return Mol*getMy();
	}
	public double getMod(){
		return Mod*getMy();
	}
	
	
	
	
	public double getMy(){
		double fy = getFy();
		double Zf;
		double My;
		
		if(digit2.equals("CL")){
			if(axis.equals("X")){
				Zf = sd.getLCSection(secName).getSectionModulusX();
			}else{
				if(maxMoment<0.0){
					Zf = sd.getLCSection(secName).getSectionModulusYWebTop();
				}else{
					Zf = sd.getLCSection(secName).getSectionModulusYLipTop();
				}
			}
		}else if(digit2.equals("ZL")){
			if(axis.equals("X")){
				Zf = sd.getZLSection(secName).getSectionModulusX();
			}else{
				Zf = sd.getZLSection(secName).getSectionModulusY();
			}
		}else{
			Zf = sd.getGSSection(secName).calcZ();				//This is for custom general section: Note to check the database for different axis of bending
		}
		
		My = Zf*fy;
		
		return My;		
	}
	
	
	
	public double getFy(){
		if(fy == 0.0){
			if(digit2.equals("CL")){				//lipped c sections
				return md.getMatProp(matName).getFy(sd.getLCSection(secName).getBaseThickness());
			}else if(digit2.equals("ZL")){		//lipped z sections
				return md.getMatProp(matName).getFy(sd.getZLSection(secName).getBaseThickness());
			}else if(digit2.equals("GS")){		//general sections
				return md.getMatProp(matName).getFy(sd.getGSSection(secName).getBaseThickness());
			}else{
				return 0.0;
			}
		}else{
			return fy;
		}
	}
	
	public void printDSMBendingResult(){
		System.out.println("\n\n*******************************************************************************");
		System.out.println("*DIRECT STRENGTH METHOD RESULTS OF A "+secName+" SECTION BENT ABOUT THE "+axis+"-AXIS");
		System.out.println("*EFFECTIVE LENGTH USED: "+Lx+" mm");
		System.out.println("*******************************************************************************");
		System.out.println("Nominal Member Moment Capacity for Euler consideration:       Mbe = "+nominalMemberMomentCapEuler()/1000000+" kNm");
		System.out.println("Nominal Member Moment Capacity for Local consideration:       Mbl = "+nominalMemberMomentCapLocal()/1000000+" kNm");
		System.out.println("Nominal Member Moment Capacity for distortional consideration:Mbd = "+nominalMemberMomentCapDistortional()/1000000+" kNm");
		System.out.println("Ultimate Design Moment Capacity:                                M = "+getDesignCapacity()/1000000+" kNm");
		System.out.println("");
		System.out.println("");
		System.out.println("");
		System.out.println("");
		System.out.println("");
		System.out.println("");
		System.out.println("");
		System.out.println("");
		
	}

}
