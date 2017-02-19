package capacityDesign;
/*
 * @author JJR Kotze (email 15692183@sun.ac.za)
 * For Department of Civil Engineering, Stellenbosch University
 * 29 October 2012 (Version 1.0 of CFS design)
 */


import constants.CapacityReductionFactors;
import distortional.DistortionalLCSection;
import effectiveWidth.UniCompressedElemWithEdgeStif;
import effectiveWidth.UniCompressedStifElements;
import section.*;
import materials.*;
/*
 * THIS CLASS CONTAINS ALL THE METHODS ASSOSIATED WITH THE COMPRESSION CAPACITY DESIGN OF
 * A LIPPED Z SECTION: SANS 10162-2:2011 CLAUSE 3.4.4 (POINT SYMMETRIC SECTIONS)
 */
public class LZCompression {
	
	private String secName, matName;						//the Name of the section and material respectively
	private double dF, effLengthX, effLengthY, effLengthZ;	//the applied force, effective length in the x direction, effective length in the y direction, effective length in the z direction  ; respectively 
	private SectionDatabase sd = Parameters.getSectionDatabase();
	private MaterialDatabase md = Parameters.getMaterialDatabase();
	private double thickness;
	private boolean distCheck;
	private DistortionalLCSection dist;
	private CapacityReductionFactors cRF = new CapacityReductionFactors();  //reduction factors from table 1.6 SANS to evaluate the ultimate design capacity
	private double reductionFactor = cRF.getCompression();
	private double bF,dL,bW;
	/*
	 * Initiating the Class with its variables
	 */
	public LZCompression(String secName, String matProp, double designForce, double effLengthX, double effLengthY,double effLengthZ,boolean distCheck){
		this.secName = secName;
		this.matName = matProp;
		this.dF = designForce;
		this.effLengthX = effLengthX;
		this.effLengthY = effLengthY;
		this.effLengthZ = effLengthZ;
		this.distCheck = distCheck;
		
		thickness = sd.getZLSection(this.secName).getBaseThickness();
		bF = sd.getZLSection(this.secName).getWidth();
		dL = sd.getZLSection(this.secName).getLipLength();
		bW = sd.getZLSection(this.secName).getHeight();
		dist = new DistortionalLCSection( bF,dL,thickness,bW,  this.matName);
	}
	

	/*
	 * The following method determines the elastic buckling stress for the x axis
	 */
	
	public double elasticBucklingStressFox(){		
		double fox;
		double term1 = Math.pow(Math.PI,2)*md.getMatProp(matName).getYoung();
		double term2 = Math.pow(effLengthX/sd.getZLSection(secName).getPolarRX(), 2);		
		fox = term1/term2;			//eqn 3.4.2(1)
		
		return fox;		//returns the elastic buckling stress about the strong axis
	}
	
	
	
	/*
	 * The following method determines the elastic buckling stress for the y axis
	 */	
	public double elasticBucklingStressFoy(){
		double foy;
		double term1 = Math.pow(Math.PI,2)*md.getMatProp(matName).getYoung();
		double term2 = Math.pow(effLengthY/sd.getZLSection(secName).getPolarRY(), 2);		
		foy = term1/term2;			//eqn 3.4.2(1)	
		
		return foy;		//returns the elastic buckling stress about the weak axis
	}
	
	
	/*
	 * The following method determines the elastic buckling stress for the z axis
	 */	
	public double elasticBucklingStressFoz(){
		double foz;
		double term1 = md.getMatProp(matName).getG()*sd.getZLSection(secName).getJ() ;
		double term2 = Math.pow(Math.PI, 2)*md.getMatProp(matName).getYoung()*sd.getZLSection(secName).getWarping();
		double term3 = md.getMatProp(matName).getG()*sd.getZLSection(secName).getJ()*Math.pow(effLengthZ, 2);
		double term4 = sd.getZLSection(secName).getArea()*Math.pow(polarRadiusShear(), 2);
		foz = term1/term4*(1+term2/term3);			//eqn 3.3.3.2(12)
		
		return foz;
	}
	
	
	
	
	/*
	 * The following method determines the Polar Radius of gyration about the shear centre according eqn 3.3.3.2(10)
	 */	
	public double polarRadiusShear(){
		double ro1;
		double term1 = Math.pow(sd.getZLSection(secName).getPolarRX(),2);		//Polar Radius about the x axis
		double term2 = Math.pow(sd.getZLSection(secName).getPolarRY(), 2);		//Polar Radius about the y axis
		
		ro1 = Math.pow(term1+term2, 0.5);	                             		//eqn 3.3.3.2(10)......Also note the Yo is equal zero.
		
		return ro1;  		//returns the Polar radius
	}
	
			
	/*
	 * The following method determines the least of the elastic buckling stress Foy and Foxz
	 * This is according SANS clause 3.4.3
	 */		
	public double elasticBucklingStressFoc(){
		double min = Math.min(elasticBucklingStressFoz(), elasticBucklingStressFoy()); //this is foc for eqn 3.4.1(5)
		return min;
	}
	
	
	
	
	
	/*
	 * The following method determines the critical stress from Clause 3.4.1
	 */
	
	public double CriticalEulerStressFn(){
		double slenderness = Math.pow(md.getMatProp(matName).getFy(thickness)/elasticBucklingStressFoc(), 0.5);
		double term1 = Math.pow(0.658,Math.pow(slenderness,2));
		double term2 = md.getMatProp(matName).getFy(thickness);
		
		double term3 = 0.877/Math.pow(slenderness, 2);
		if(slenderness <=1.5){
			return term1*term2;						//this is from eqn 3.4.1(3)
		}else{
			return term3*term2;						//this is from eqn 3.4.1(4)
		}
	}
	
	
	
	/*
	 * The following method evaluates the effective area of the section for the nominal member capacity
	 * at a stress Fn (as calculated in method CriticalEulerStressFn())
	 */
	public double effectiveAreaFn(){
		double area1web;	//the area of the web
		double area2flange;	//the area of the flange
		double area3Lip;	//the area of the lips
		double area4Angles; // this is the area for the angles
		double effA;
		double t = sd.getZLSection(secName).getBaseThickness();
		double r = sd.getZLSection(secName).getangleRadius();
		/*
		 * the following row evaluates the effective width of the web, 
		 * using k=4 and p set to 0 to calculate the effective width factor according eqn 2.2.1.2(3)
		 */
		UniCompressedStifElements effAWeb = new UniCompressedStifElements(CriticalEulerStressFn(), 4.0, 0.0, t, sd.getZLSection(secName).getHeight() -2*r, matName,secName);
		area1web = effAWeb.effectiveWidth()*t;		
		
		UniCompressedElemWithEdgeStif effAFlange = new UniCompressedElemWithEdgeStif(CriticalEulerStressFn(),CriticalEulerStressFn(),CriticalEulerStressFn(),"Inward","F2", t, sd.getZLSection(secName).getWidth()-2*r, sd.getZLSection(secName).getLipLength()- r, 90, matName,secName);
		area2flange = effAFlange.setEffB()*t;
		area3Lip = effAFlange.calcD()*t;
		area4Angles = 4.*(Math.PI*Math.pow(r+t/2., 2)/4. - Math.PI*Math.pow(r - t/2., 2)/4.);		
			
		effA = area1web + 2.*area2flange + 2.*area3Lip + area4Angles;
		
		return effA;
		
	}
	
	
	/*
	 * The following method evaluates the effective area of the section for the nominal section capacity
	 * at a yield stress Fy (as supplied within the material database)
	 */
	public double effectiveAreaFy(){
		double area1web;
		double area2flange;
		double area3Lip;
		double area4Angles; // this is the area for the angles
		double effA;
		double fy = md.getMatProp(matName).getFy(thickness);
		double t = sd.getZLSection(secName).getBaseThickness();
		double r = sd.getZLSection(secName).getangleRadius();
		/*
		 * the following row evaluates the effective width of the web, 
		 * using k=4 and p set to 0 to calculate the effective width factor according eqn 2.2.1.2(3)
		 */
		UniCompressedStifElements effAWeb = new UniCompressedStifElements(fy, 4.0, 0.0, t, sd.getZLSection(secName).getHeight() -2*r, matName,secName);
		area1web = effAWeb.effectiveWidth()*t;
		
		
		UniCompressedElemWithEdgeStif effAFlange = new UniCompressedElemWithEdgeStif(fy,fy,fy,"Inward","F2", t, sd.getZLSection(secName).getWidth()-2*r, sd.getZLSection(secName).getLipLength()- r, 90.0, matName,secName);
		area2flange = effAFlange.setEffB()*t;
		area3Lip = effAFlange.calcD()*t;
		area4Angles = 4.*(Math.PI*Math.pow(r+t/2., 2)/4. - Math.PI*Math.pow(r - t/2., 2)/4.);		
				
		effA = area1web + 2*area2flange + 2*area3Lip + area4Angles;
		
		return effA;		
	}
	
	
	public double nominalMemberCapacityNoDistCheck(){ //SANS 3.4.1 (b)
		double nC = effectiveAreaFn()*CriticalEulerStressFn()/1000.;	
		return nC;
	}
	
	public double nominalSectionCapacity(){ //SANS 3.4.1 (a)
		double nS = md.getMatProp(matName).getFy(thickness)*effectiveAreaFy()/1000.;		
		return nS;		
	}
	
	/*
	 * C SECTION SUBJECT TO DISTORTIONAL BUCKLING
	 * AN ADDITIONAL CHECK ACCORDING SANS 10162-2:2011 clause 3.4.6
	 */
	
	public double distMemberCapacity(){
		double nC;
		double fod = dist.distBucklingfod();
		double fy = md.getMatProp(matName).getFy(thickness);
		double A = sd.getZLSection(secName).getArea();
		
		if(fod>fy/2){
			nC = A*fy*(1-fy/(4*fod))/1000.;
			return nC;
		}else if(fod<=fy/2 && fod>=fy/13){
			nC = A*fy*(0.055*Math.pow(Math.pow(fy/fod, 0.5)-3.6,2)+0.237)/1000.;
			return nC;
		}else{
			return 0.0;
		}		
	}
	
	public double nominalMemberCapacityWithDistCheck(){ //returns Nc if there is a distortion check required
		return Math.min(nominalMemberCapacityNoDistCheck(), distMemberCapacity());
	}
	
	
	/*
	 * THE FOLLOWING METHOD DETERMINES THE ULTIMATE DESIGN COMPRESSION AXIAL FORCE
	 * TAKING INTO ACCOUNT THE CAPACITY REDUCTION FACTOR
	 */
	public double ultimateDesignForce(){
		double Nc;
		double Ns = nominalSectionCapacity();
		double N;
		
		if(distCheck){
			Nc = nominalMemberCapacityWithDistCheck();
		}else{
			Nc = nominalMemberCapacityNoDistCheck();
		}
		
		N = reductionFactor*Math.min(Nc, Ns);
		
		return N;		
	}
	
	/*
	 * THE FOLLOWING METHOD DETERMINES IF THE SECTION WILL FAIL UNDER THE GIVEN
	 * AXIAL COMPRESSION FORCE
	 */
	public boolean willItFail(){
		double capacity = ultimateDesignForce();
		if(dF<= capacity){
			return false;       //The section will not fail under the given axial force
		}else{
			return true;		//The section will fail under the given axial force
		}		
	}
	
	/*
	 * SLENDERNESS CALCULATIONS OF SECTION
	 */
	public double maxLength(){
		Slenderness S = new Slenderness(effLengthX,effLengthY,sd.getZLSection(secName).getPolarRX(),sd.getZLSection(secName).getPolarRY() );
		return S.maxEffLengthCompression();
	}
	public double slenderness(){
		Slenderness S = new Slenderness(effLengthX,effLengthY,sd.getZLSection(secName).getPolarRX(),sd.getZLSection(secName).getPolarRY() );
		return S.MaxSlenderness();
	}
	public boolean checkSlender(){
		Slenderness S = new Slenderness(effLengthX,effLengthY,sd.getZLSection(secName).getPolarRX(),sd.getZLSection(secName).getPolarRY() );
		return S.checkSlenderCompression();
	}
	
	
	/*
	 * THE FOLLOWING METHOD PRINTS OUT THE RESULTS FOR THE EFFECTIVE WIDTH METHOD CALCULATIONS
	 * FOR THE LIPPED CHANNEL SECTION
	 */
	
	public void printEWMlippedZCompression(){
		String foc;
		String Ncd;
		String Nc;
		String fails;
		String slender;
		
		if(elasticBucklingStressFoc() == elasticBucklingStressFoz()){
			foc = "The critical Euler Stress Fn will fail under torsional buckling";
		}else{
			foc = "The critical Euler stress Fn will fail under Flexural buckling";
		}
		
		if(distCheck){
			Nc =           "The Nominal member capacity with distortional check:      Nc = "+nominalMemberCapacityWithDistCheck()+" kN";
			Ncd =          "*   The Distortional member capacity used:                 Ncd = "+distMemberCapacity()+" kN";
		}else{
			Nc =           "The Nominal member capacity without distortional check:   Nc = "+nominalMemberCapacityNoDistCheck()+" kN";
			Ncd =          "*   There were no distortional check required                Ncd = N.a.  *";
		}
		
		if(willItFail()){
			fails =        "*NOT OK: Section Will Fail for applied load of:           F = "+dF+" kN";
		}else{
			fails =        "*OK: Section Will Not Fail for applied load of:           F = "+dF+" kN";
		}
		
		if(checkSlender()){
			slender =      "Member satisfy the compression slender limit:             KL/r = "+slenderness()+" <= 200.0";			
		}else{
			slender =      "!!!!Member not satisfying the compression slender limit:  KL/r = "+slenderness()+" > 200.0"; 
		}
		
		
		
		System.out.println("\n******************************************************************************************");
		System.out.println("EFFECTIVE WIDTH METHOD RESULTS FOR THE "+secName+" IS AS FOLLOW:\nWITH A EFFECTIVE LENGTH OF: Le = "+effLengthX+" mm");
		System.out.println("********************************************************************************************");
		System.out.println(slender);
		System.out.println("The maximum allowed effective length                        L = "+maxLength()+" mm");
		System.out.println("*********************************************************************************************");
		System.out.println("Elastic buckling stress about the x-x axis:              fox = "+elasticBucklingStressFox()+" MPa");
		System.out.println("Elastic buckling stress about the y-y axis:              foy = "+elasticBucklingStressFoy()+" MPa");
		System.out.println("Elastic buckling stress about the z-z axis:              foz = "+elasticBucklingStressFoz()+" MPa");
		System.out.println("The shear polar radius:                                  ro1 = "+polarRadiusShear()+" mm");
		System.out.println("********************************************************************************************");
		System.out.println("*                      THE CRITICAL STRESSES:                                              *");
		System.out.println("********************************************************************************************");
		System.out.println("The Elastic Buckling stress Foc = min(foxz ; foy):       foc = "+elasticBucklingStressFoc()+" MPa");
		System.out.println("The Critical Euler Stress:                                Fn = "+CriticalEulerStressFn()+" MPa");
		System.out.println(foc);
		System.out.println("********************************************************************************************");
		System.out.println("*                         EFFECTIVE AREAS:                                                 *");
		System.out.println("********************************************************************************************");
		System.out.println("The effective area with compression fiber of Fn:          Ae = "+effectiveAreaFn()+" mm2");
		System.out.println("The effective area with compression fiber of Fy:          Ae = "+effectiveAreaFy()+" mm2");
		System.out.println("********************************************************************************************");
		System.out.println("*                        NOMINAL CAPACITYS:                                                *");
		System.out.println("********************************************************************************************");
		System.out.println("The Nominal section compression capacity:                 Ns = "+nominalSectionCapacity()+" kN");
		System.out.println(Nc);
		System.out.println("********************************************************************************************");
		System.out.println(Ncd);
		System.out.println("********************************************************************************************");
		System.out.println("*                           FINAL RESULT:                                                  *");
		System.out.println("*The Ultimate Design Compression Capacity of the section: N = "+ultimateDesignForce()+" kN");
		System.out.println(fails);
		System.out.println("********************************************************************************************");
		
	}
	
	

}
