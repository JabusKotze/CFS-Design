package capacityDesign;
/*
 * @author JJR Kotze (email 15692183@sun.ac.za)
 * For Department of Civil Engineering, Stellenbosch University
 * 29 October 2012 (Version 1.0 of CFS design)
 */
import materials.*;
import section.*;
import effectiveWidth.*;
import distortional.DistortionalLCSection;
import constants.*;

/*
 * This class contains the methods to evaluate the compression capacity for Lipped Channel sections
 * According SANS 10162-2:2011, from Clause 3.4, whereby 3.4.3 is applied to determine the Foc value.
 */

public class LCCompression  {
	
	private String secName, matName;						//the Name of the section and material respectively
	private double dF, effLengthX, effLengthY, effLengthZ;	//the applied force, effective length in the x direction, effective length in the y direction, effective length in the z direction  ; respectively 
	private MaterialDatabase mb = Parameters.getMaterialDatabase();	//Initiating the Material Database to receive the material properties as from the material name
	private SectionDatabase sb = Parameters.getSectionDatabase();		//Initiating the Section Database to receive the section properties as from the section name
	private DistortionalLCSection dist;						//Initialising the distortional buckling stress
	private double thickness;
	private boolean distCheck;								//This requires if a distortional check should be done on the section?
	private CapacityReductionFactors cRF = new CapacityReductionFactors();
	private double reductionFactor = cRF.getCompression();
	private double bF,dL,bW;
	/*
	 * Initiating the Class with its variables
	 */
	public LCCompression(String secName, String matProp, double designForce, double effLengthX, double effLengthY,double effLengthZ,boolean distCheck){
		this.secName = secName;
		this.matName = matProp;
		this.dF = designForce;
		this.effLengthX = effLengthX;
		this.effLengthY = effLengthY;
		this.effLengthZ = effLengthZ;
		this.distCheck = distCheck;
		thickness = sb.getLCSection(secName).getBaseThickness();
		bF = sb.getLCSection(this.secName).getWidth();
		dL = sb.getLCSection(this.secName).getLipLength();
		bW = sb.getLCSection(this.secName).getHeight();
		dist = new DistortionalLCSection( bF,dL,thickness,bW,  this.matName);
		
	}
	
	
	
	/*
	 * The following method determines the elastic buckling stress for the x axis
	 */
	
	public double elasticBucklingStressFox(){		
		double fox;
		double term1 = Math.pow(Math.PI,2)*mb.getMatProp(matName).getYoung();
		double term2 = Math.pow(effLengthX/sb.getLCSection(secName).getPolarRX(), 2);		
		fox = term1/term2;			//eqn 3.4.2(1)
		
		return fox;		//returns the elastic buckling stress about the strong axis
	}
	
	
	
	/*
	 * The following method determines the elastic buckling stress for the y axis
	 */	
	public double elasticBucklingStressFoy(){
		double foy;
		double term1 = Math.pow(Math.PI,2)*mb.getMatProp(matName).getYoung();
		double term2 = Math.pow(effLengthY/sb.getLCSection(secName).getPolarRY(), 2);		
		foy = term1/term2;			//eqn 3.4.2(1)	
		
		return foy;		//returns the elastic buckling stress about the weak axis
	}
	
	
	
	
	/*
	 * the following method determines the torsional-flexural buckling stress in the xz plane.
	 */	
	public double torsionalFlexuralBucklingStressFoxz(){
		double foxz;
		double term1 = elasticBucklingStressFoz() +elasticBucklingStressFox();
		double term2 = Math.pow(Math.pow(term1, 2)-4*elasticBucklingStressFoz()*elasticBucklingStressFox(), 0.5);
		double term3 = 1-Math.pow(shearCentreDistanceXo()/polarRadiusShear(), 2);		//Constant from eqn 3.4.3(3)
		foxz = 0.5*(term1 - term2)/term3;				//eqn 3.4.3(1)
		
		return foxz;		//returns the torsional-flexural buckling stress.
	}
	
	
	
	
	/*
	 * The following method determines the elastic buckling stress for the z axis
	 */	
	public double elasticBucklingStressFoz(){
		double foz;
		double term1 = mb.getMatProp(matName).getG()*sb.getLCSection(secName).getJ() ;
		double term2 = Math.pow(Math.PI, 2)*mb.getMatProp(matName).getYoung()*sb.getLCSection(secName).getWarping();
		double term3 = mb.getMatProp(matName).getG()*sb.getLCSection(secName).getJ()*Math.pow(effLengthZ, 2);
		double term4 = sb.getLCSection(secName).getArea()*Math.pow(polarRadiusShear(), 2);
		foz = term1/term4*(1+term2/term3);			//eqn 3.3.3.2(12)
		
		return foz;
	}
	
	
	
	
	/*
	 * The following method determines the Polar Radius of gyration about the shear centre according eqn 3.3.3.2(10)
	 */	
	public double polarRadiusShear(){
		double ro1;
		double term1 = Math.pow(sb.getLCSection(secName).getPolarRX(),2);		//Polar Radius about the x axis
		double term2 = Math.pow(sb.getLCSection(secName).getPolarRY(), 2);		//Polar Radius about the y axis
		double term3 = Math.pow(shearCentreDistanceXo(), 2);					//Distance from centre point to shear centre in the x direction.
		ro1 = Math.pow(term1+term2+term3, 0.5);			//eqn 3.3.3.2(10)......Also note the Yo is equal zero.
		
		return ro1;  		//returns the Polar radius
	}
	
	
	
	
	/*
	 * The following method determines the shear centre distance for the section for the x direction
	 */	
	public double shearCentreDistanceXo(){
		double xo;
		xo = sb.getLCSection(secName).getCentroidY()+sb.getLCSection(secName).getShearCentreY();
		return xo;
	}
	
	
	
	/*
	 * The following method determines the least of the elastic buckling stress Foy and Foxz
	 * This is according SANS clause 3.4.3
	 */		
	public double elasticBucklingStressFoc(){
		double min = Math.min(torsionalFlexuralBucklingStressFoxz(), elasticBucklingStressFoy()); //this is foc for eqn 3.4.1(5)
		return min;
	}
	
	
	
	
	
	/*
	 * The following method determines the critical stress from Clause 3.4.1
	 */
	
	public double CriticalEulerStressFn(){
		double slenderness = Math.pow(mb.getMatProp(matName).getFy(thickness)/elasticBucklingStressFoc(), 0.5);
		double term1 = Math.pow(0.658,Math.pow(slenderness,2));
		double term2 = mb.getMatProp(matName).getFy(thickness);
		
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
		double t = sb.getLCSection(secName).getBaseThickness();
		double r = sb.getLCSection(secName).getangleRadius();
		/*
		 * the following row evaluates the effective width of the web, 
		 * using k=4 and p set to 0 to calculate the effective width factor according eqn 2.2.1.2(3)
		 */
		UniCompressedStifElements effAWeb = new UniCompressedStifElements(CriticalEulerStressFn(), 4.0, 0.0, sb.getLCSection(secName).getBaseThickness(), sb.getLCSection(secName).getHeight() -2*sb.getLCSection(secName).getangleRadius(), matName,secName);
		area1web = effAWeb.effectiveWidth()*sb.getLCSection(secName).getBaseThickness();		
		
		UniCompressedElemWithEdgeStif effAFlange = new UniCompressedElemWithEdgeStif(CriticalEulerStressFn(),CriticalEulerStressFn(),CriticalEulerStressFn(),"Inward","F2", sb.getLCSection(secName).getBaseThickness(), sb.getLCSection(secName).getWidth()-2*sb.getLCSection(secName).getangleRadius(), sb.getLCSection(secName).getLipLength()- sb.getLCSection(secName).getangleRadius(), 90, matName,secName);
		area2flange = effAFlange.setEffB()*sb.getLCSection(secName).getBaseThickness();
		area3Lip = effAFlange.calcD()*sb.getLCSection(secName).getBaseThickness();
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
		double fy = mb.getMatProp(matName).getFy(thickness);
		double t = sb.getLCSection(secName).getBaseThickness();
		double r = sb.getLCSection(secName).getangleRadius();
		/*
		 * the following row evaluates the effective width of the web, 
		 * using k=4 and p set to 0 to calculate the effective width factor according eqn 2.2.1.2(3)
		 */
		UniCompressedStifElements effAWeb = new UniCompressedStifElements(mb.getMatProp(matName).getFy(thickness), 4.0, 0.0, sb.getLCSection(secName).getBaseThickness(), sb.getLCSection(secName).getHeight() -2*sb.getLCSection(secName).getangleRadius(), matName,secName);
		area1web = effAWeb.effectiveWidth()*sb.getLCSection(secName).getBaseThickness();
		
		
		UniCompressedElemWithEdgeStif effAFlange = new UniCompressedElemWithEdgeStif(fy,fy,fy,"Inward","F2", sb.getLCSection(secName).getBaseThickness(), sb.getLCSection(secName).getWidth()-2*sb.getLCSection(secName).getangleRadius(), sb.getLCSection(secName).getLipLength()- sb.getLCSection(secName).getangleRadius(), 90.0, matName,secName);
		area2flange = effAFlange.setEffB()*sb.getLCSection(secName).getBaseThickness();
		area3Lip = effAFlange.calcD()*sb.getLCSection(secName).getBaseThickness();
		area4Angles = 4.*(Math.PI*Math.pow(r+t/2., 2)/4. - Math.PI*Math.pow(r - t/2., 2)/4.);		
				
		effA = area1web + 2*area2flange + 2*area3Lip + area4Angles;
		
		return effA;		
	}
	
	
	public double nominalMemberCapacityNoDistCheck(){ //SANS 3.4.1 (b)
		double nC = effectiveAreaFn()*CriticalEulerStressFn()/1000;	
		return nC;
	}
	
	public double nominalSectionCapacity(){ //SANS 3.4.1 (a)
		double nS = mb.getMatProp(matName).getFy(thickness)*effectiveAreaFy()/1000;		
		return nS;		
	}
	
	/*
	 * C SECTION SUBJECT TO DISTORTIONAL BUCKLING
	 * AN ADDITIONAL CHECK ACCORDING SANS 10162-2:2011 clause 3.4.6
	 */
	
	public double distMemberCapacity(){
		double nC;
		double fod = dist.distBucklingfod();
		double fy = mb.getMatProp(matName).getFy(thickness);
		double A = sb.getLCSection(secName).getArea();
		
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
		Slenderness S = new Slenderness(effLengthX,effLengthY,sb.getLCSection(secName).getPolarRX(),sb.getLCSection(secName).getPolarRY() );
		return S.maxEffLengthCompression();
	}
	public double slenderness(){
		Slenderness S = new Slenderness(effLengthX,effLengthY,sb.getLCSection(secName).getPolarRX(),sb.getLCSection(secName).getPolarRY() );
		return S.MaxSlenderness();
	}
	public boolean checkSlender(){
		Slenderness S = new Slenderness(effLengthX,effLengthY,sb.getLCSection(secName).getPolarRX(),sb.getLCSection(secName).getPolarRY() );
		return S.checkSlenderCompression();
	}
	
	
	/*
	 * THE FOLLOWING METHOD PRINTS OUT THE RESULTS FOR THE EFFECTIVE WIDTH METHOD CALCULATIONS
	 * FOR THE LIPPED CHANNEL SECTION
	 */
	
	public void printEWMlippedCCompression(){
		String foc;
		String Ncd;
		String Nc;
		String fails;
		String slender;
		
		if(elasticBucklingStressFoc() == torsionalFlexuralBucklingStressFoxz()){
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
		System.out.println("Torsional-Flexural Buckling Stress about xz plane:      foxz = "+torsionalFlexuralBucklingStressFoxz()+" MPa");
		System.out.println("The shear polar radius:                                  ro1 = "+polarRadiusShear()+" mm");
		System.out.println("The shear centre distance:                                xo = "+shearCentreDistanceXo()+" mm");
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
