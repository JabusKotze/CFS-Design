package capacityDesign;
/*
 * @author JJR Kotze (email 15692183@sun.ac.za)
 * For Department of Civil Engineering, Stellenbosch University
 * 29 October 2012 (Version 1.0 of CFS design)
 */
import section.*;
import materials.*;
import constants.*;
import effectiveWidth.*;
import java.text.*;
import distortional.*;



/*
 * THIS CLASS CONTAINS ALL THE METHODS FOR CALCULATING BENDING CAPACITY OF A LIPPED Z SECTION
 * ABOUT THE X OR Y AXIS:
 * THIS WAS DONE WITH REFERENCE TO SANS:10162-2:2011 CLAUSE 3.3 AND SUB CLAUSE 3.3.3.2.1(b)
 */
public class LZBending {
	DecimalFormat df = new DecimalFormat("#.###");
	
	private String secName,matName; // The name of section and material respectively
	private double maxAppliedMoment; 	/*The maximum moment from analysis:  
	 									*for lipped c sections this moment should be given as negative if max moment produces compression at the web side
	 									*for lipped c sections this moment should be given as positive if max moment produces compression at the lip side
	 									*/
	private String axis;			//the axis of bending: should be given as either "X" or "Y"
	private double Lx,Ly,Lz;		//effective lengths in the 3 major directions
	private boolean distCheck;		//if a distortional check should be carried out?
	private MaterialDatabase md = Parameters.getMaterialDatabase();
	private SectionDatabase sd = Parameters.getSectionDatabase();
	private double thickness; 		//thickness of the steel used in the section: Base thickness.
	private double Mmax,M1,M2,M3,M4,M5;
	private String loadDistr,loadAppl,bracing;
	private CoefficientCb  Ccb;
	
	private GeneralConstants gC = new GeneralConstants();
	private CapacityReductionFactors cRF = new CapacityReductionFactors();
	private DistortionalLCSection dist;
	private double bF,dL,bW;
	/*
	 * ATTRIBUTE DECLARATION:
	 * maxAppliedMoment: moment applied determined from analysis
	 * axis: the axis of bending: should be given as either "X" or "Y"
	 * Lx,ly,lz: are the unbraced lengths
	 * distCheck: if a distortional check will be done
	 * Mmax: maximum absolute moment in unbraced length (can be given as 0.0 if you don't know this force)
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
	public LZBending(String secName,String matName, double maxAppliedMoment, String axis, double Lx, double Ly, double Lz, boolean distCheck, double Mmax, double M1, double M2, double M3, double M4, double M5, String loadDistr, String loadAppl, String bracing){
		this.secName = secName;
		this.matName = matName;
		this.maxAppliedMoment = maxAppliedMoment;
		this.axis = axis;
		this.Lx = Lx;
		this.Ly = Ly;
		this.Lz = Lz;
		this.distCheck = distCheck;
		this.bracing = bracing;
		this.Mmax = Mmax;
		this.M1 = M1;
		this.M2 = M2;
		this.M3 = M3;
		this.M4 = M4;
		this.M5 = M5;
		this.loadAppl = loadAppl;
		this.loadDistr = loadDistr;
		
		thickness = sd.getZLSection(this.secName).getBaseThickness();
		Ccb = new CoefficientCb(this.Mmax,this.M3,this.M4,this.M5,this.loadDistr,this.loadAppl,this.bracing);
		
		bF = sd.getZLSection(this.secName).getWidth();
		dL = sd.getZLSection(this.secName).getLipLength();
		bW = sd.getZLSection(this.secName).getHeight();
		dist = new DistortionalLCSection( bF,dL,thickness,bW,  this.matName);
	}
	
	
	/*
	 * THE FOLLOWING METHOD DETERMINES IF THE SECTION WILL FAIL UNDER THE MAX MOMENT
	 * OBTAINED FROM FORCE ANALYSIS
	 */
	
	public boolean willItfail(){
		double moment = Math.abs(maxAppliedMoment);
		
		if(moment <= UltimateMemberCapacity()/1000000.){
			return false; //will not fail
		}else{
			return true; //will fail
		}
	}
	
	/*
	 * THE FOLLOWING METHOD DETERMINES THE ULTIMATE DESIGN BENDING FORCE
	 * TAKING INTO ACCOUNT THE CAPACITY REDUCTION FACTOR
	 */
	public double UltimateMemberCapacity(){
		double M;
		double Ms = nominalSectionMomentCapMs();
		double Mb;
		double reductionFactor = cRF.getBendingMember();
		
		if(distCheck){
			Mb = nominalMemberMomentCapMbWithDistCheck();
		}else{
			Mb = nominalMemberMomentCapMbNoDistCheck();
		}
		M = reductionFactor*Math.min(Mb, Ms);
		
		return M;
	}
	
	public double nominalMemberMomentCapMbWithDistCheck(){	//CLAUSE 3.3.3.3(a)
		return Math.min(distMemberCapacity(), nominalMemberMomentCapMbNoDistCheck());
	}
	
	public double nominalMemberMomentCapMbNoDistCheck(){	//CLAUSE 3.3.3.2.1
		double Mb;			
		double Zc;
		double fc = criticalStressFc();		
		
		if(axis.equals("X")){
			Zc = calcEffZcAboutX(fc);
		}else{			
				Zc = calcEffZcAboutY(fc);			
		}
		
		Mb = Zc*fc;			//eqn 3.3.3.2(1)
		return Mb;
	}
	
	public double nominalSectionMomentCapMs(){			//CLAUSE 3.3.2.2
		double Ms;
		double Ze;
		double t = sd.getZLSection(secName).getBaseThickness();
		double fy = md.getMatProp(matName).getFy(t);
		

		if(axis.equals("X")){
			Ze = calcEffZcAboutX(fy);
		}else{			
     			Ze = calcEffZcAboutY(fy);			
		}		
		Ms = Ze*fy;		//eqn 3.3.2.2
		return Ms;
	}
	
	
	
	/*
	 * elastic buckling moment: calculated in accordance of clause 3.3.3.2.1(a)(i)
	 * THIS IS FOR BENDING ABOUT THE SYMMETRIE AXIS ("X")
	 */
	public double xAxisMo(){	//elastic buckling moment: calculated in accordance of clause 3.3.3.2.1(a)(i)
		double Mo;
		double area = sd.getZLSection(secName).getArea();
		double ro1 = polarRadiusShear();
		double foy = elasticBucklingStressFoy();
		double foz = elasticBucklingStressFoz();
		double Cb = Ccb.getCb();  
		Mo = Cb*area*ro1*Math.sqrt(foy*foz);		//eqn 3.3.3.2(8)
		return Mo;    
	}	
	
	
	
	
	/*
	 * THE FOLLOWING METHOD DETERMINES THE ELASTIC BUCKLING MOMENT: CLAUSE 3.3.3.2.1(b)
	 * THIS IS FOR BENDING ABOUT THE Y-AXIS: for point symmetric sections
	 * 													
	 */	
	public double yAxisMo(){
		double Mo;		
		double pi = Math.PI;
		double E = md.getMatProp(matName).getYoung();
		double Cb = Ccb.getCb();
		double d = sd.getZLSection(secName).getWidth();
		double iyc = calcIYC();
		
		Mo = Math.pow(pi, 2)*E*Cb*d*iyc/(2*Ly*Ly);		//eqn 3.3.3.2(17)	
		
		return Mo;		
	}
	public double calcIYC(){  //this is the second moment of inertia of the section in compression about the axis parralel to the web: i.e the y axis;
		double iyc;
		double w = sd.getZLSection(secName).getWidth();
		double l = sd.getZLSection(secName).getLipLength();
		double r = sd.getZLSection(secName).getangleRadius();
		double t = sd.getZLSection(secName).getBaseThickness();
		double lip = (l-r)*Math.pow(t, 3)/12. + (l-r)*t*(w*w);
		double web = (t)*Math.pow(w-2*r,3)/12. + (w-2*r)*t*Math.pow(w/2, 2);
		
		double cornerATop = Math.PI*Math.pow(r+t/2., 4)/16. + Math.PI*(Math.pow(r+t/2., 2)/4)*Math.pow(w-r , 2);
		double cornerBTop = Math.PI*Math.pow(r-t/2., 4)/16. + Math.PI*(Math.pow(r-t/2., 2)/4)*Math.pow(w-r, 2);
		double cornerABot = Math.PI*Math.pow(r+t/2., 4)/16. + Math.PI*(Math.pow(r+t/2., 2)/4)*Math.pow(r , 2);
		double cornerBBot = Math.PI*Math.pow(r-t/2., 4)/16. + Math.PI*(Math.pow(r-t/2., 2)/4)*Math.pow(r, 2);
		
		double term7 = (cornerATop-cornerBTop) + (cornerABot - cornerBBot);
		iyc = lip+web+term7;
		return iyc;
	}
	
	
	
	
	
	/*
	 * 
	 * 
	 */
	public double slendernessForMc(){
		double My;
		double Mo;
		double slenderness;
		
		if(axis.equals("X")){
			Mo = xAxisMo();
			My = sd.getZLSection(secName).getSectionModulusX()*md.getMatProp(matName).getFy(thickness);	//eqn 3.3.3.2(7)
			slenderness = Math.sqrt(My/Mo);		//eqn 3.3.3.2(6)
		}else{				
				Mo = yAxisMo();
				My =sd.getZLSection(secName).getSectionModulusY()*md.getMatProp(matName).getFy(thickness);//eqn 3.3.3.2(7)
				slenderness = Math.sqrt(My/Mo);		//eqn 3.3.3.2(6)	
		}
		
		return slenderness;
	}
	
	
	
	
	/*
	 * THE FOLLOWING METHOD DETERMINES THE CRITICAL MOMENT Mc
	 * FROM eqns: 3.3.3.2(3);(4) AND (5)
	 */
	public double critMomentMc(){
		double Mc;
		double My;
		double sR = slendernessForMc(); //slenderness ratio
		
		if(axis.equals("X")){  //for bending about x
			My = sd.getZLSection(secName).getSectionModulusX()*md.getMatProp(matName).getFy(thickness);
			if(sR<=gC.getBendingSlendernessLimitBottom()){				
				Mc = My;
			}else if(sR > gC.getBendingSlendernessLimitBottom() && sR < gC.getBendingSlendernessLimitTop()){
				Mc = 1.11*My*(1-(10.*Math.pow(sR, 2))/36.);
			}else{
				Mc = My*(1/Math.pow(sR,2));
			}
		}else{		//for bending about y axis			
				My =sd.getZLSection(secName).getSectionModulusY()*md.getMatProp(matName).getFy(thickness);//eqn 3.3.3.2(7)
						
			if(sR<=gC.getBendingSlendernessLimitBottom()){				
				Mc = My;
			}else if(sR > gC.getBendingSlendernessLimitBottom() && sR < gC.getBendingSlendernessLimitTop()){
				Mc = 1.11*My*(1-(10.*Math.pow(sR, 2))/36.);
			}else{
				Mc = My*(1/Math.pow(sR,2));
			}
		}
		
		return Mc;
	}
	
	
	/*
	 * THE FOLLOWING METHOD DETERMINES THE ELASTIC CRITICAL STRESS fc:
	 * THIS WILL BE USED TO DETERMINE THE EFFECTIVE WIDTHS AND EFFECTIVE SECTION MODULUS
	 */
	
	
	public double criticalStressFc(){
		double Mc = critMomentMc();
		double Zf;
		double fc;
		
		if(axis.equals("X")){  //for bending about the x axis
			Zf=sd.getZLSection(secName).getSectionModulusX();
		}else{					//for bending about the y-xis			
			Zf = sd.getZLSection(secName).getSectionModulusY();						
		}		
		fc = Mc/Zf;				//eqn 3.3.3.2(2)
		
		return fc;
	}
	
	
	
	
	
	
	/*
	 * THE FOLLOWING METHODS ARE THERE TO DETERMINE THE EFFECTIVE SECTION PROPERTIES SUCH AS:
	 * EFFECTIVE SECTION MODULUS: Zc
	 * EFFECTIVE WIDTHS OF THE WEB, FLANGE AND LIPS
	 * 
	 * THESE METHODS ALSO INCLUDE THE DETERMINATION OF F1* AND F2* TO BE USED IN CONJUCTION
	 * WITH THE CLAUSES 2.4.2 ; 2.2.3 AND 2.3.2
	 * 
	 * PART 1: FOR BENDING ABOUT THE X AXIS:
	 */
	
	public double f1F2WebX(double stress){ //web stresses f1*(compression) and f2*(tension) about the x axis
		double f;
		double h = sd.getZLSection(secName).getHeight();
		double t = sd.getZLSection(secName).getBaseThickness();
		double r = sd.getZLSection(secName).getangleRadius();
		double fc = stress;
		
		f = fc*((h/2. - r)/((h+t)/2.));
		return f;
	}	
	public double f1LipX(double stress){	//Max compression stress at stiffened edge of edge stiffener
		return f1F2WebX(stress);
	}	
	public double f2LipX(double stress){	//Min compression stress at unstiffened adge of stiffener
		double f;
		double h = sd.getZLSection(secName).getHeight();
		double t = sd.getZLSection(secName).getBaseThickness();
		double r = sd.getZLSection(secName).getangleRadius();
		double l = sd.getZLSection(secName).getLipLength();
		double fc = stress;
		
		f = fc*((h/2. - r - l)/((h+t)/2.));
		return f;
	}	
	
	public double getB1Flange(double stress){ // length on the stiffener side
		double r = sd.getZLSection(secName).getangleRadius();
		double t = sd.getZLSection(secName).getBaseThickness();
		double l = sd.getZLSection(secName).getLipLength() - r;
		double b = sd.getZLSection(secName).getWidth() -2.*r;
		UniCompressedElemWithEdgeStif uCEES = new UniCompressedElemWithEdgeStif(stress,f1LipX(stress),f2LipX(stress),"Inward","F2",t,b,l,90.0,matName,secName);
		
		return uCEES.calcB1();
	}
	public double getB2Flange(double stress){	//length on the web side
		double r = sd.getZLSection(secName).getangleRadius();
		double t = sd.getZLSection(secName).getBaseThickness();
		double l = sd.getZLSection(secName).getLipLength() - r;
		double b = sd.getZLSection(secName).getWidth()-2*r;
		UniCompressedElemWithEdgeStif uCEES = new UniCompressedElemWithEdgeStif(stress,f1LipX(stress),f2LipX(stress),"Inward","F2",t,b,l,90.0,matName,secName);
		
		return uCEES.calcB2();
	}
	public double geteffLipLength(double stress){	//effective lip length
		double r = sd.getZLSection(secName).getangleRadius();
		double t = sd.getZLSection(secName).getBaseThickness();
		double l = sd.getZLSection(secName).getLipLength()-r;
		double b = sd.getZLSection(secName).getWidth()-2*r;
		UniCompressedElemWithEdgeStif uCEES = new UniCompressedElemWithEdgeStif(stress,f1LipX(stress),f2LipX(stress),"Inward","F2",t,b,l,90.0,matName,secName);
		
		return uCEES.calcD();
	}
	
	public double getB1Web(double stress){	//length from stress F1 edge
		double r = sd.getZLSection(secName).getangleRadius();
		double t = sd.getZLSection(secName).getBaseThickness();
		double h = sd.getZLSection(secName).getHeight() - 2*r;
		
		GradientStiffElem gSE =  new GradientStiffElem(f1F2WebX(stress),-1*f1F2WebX(stress),t,h,h/2.,secName,matName);
		
		return gSE.getBe1();
	}
	public double getB2Web(double stress){	//length from stress F2 edge
		double r = sd.getZLSection(secName).getangleRadius();
		double t = sd.getZLSection(secName).getBaseThickness();
		double h = sd.getZLSection(secName).getHeight() - 2*r;
		
		GradientStiffElem gSE =  new GradientStiffElem(f1F2WebX(stress),-1*f1F2WebX(stress),t,h,h/2.,secName,matName);
		
		return gSE.getBe2();
	}
	
	public double effAreaAboutX(double stress){
		double r = sd.getZLSection(secName).getangleRadius();
		double t = sd.getZLSection(secName).getBaseThickness();
		double halfArea = sd.getZLSection(secName).getArea()/2.;
		double effBareas = t*(getB1Flange(stress)+getB2Flange(stress)+getB1Web(stress)+getB2Web(stress)+geteffLipLength(stress));
		double angleArea = Math.PI*(Math.pow(r+t/2., 2) - Math.pow(r-t/2., 2))/4.;
		
		return halfArea + effBareas + 2.*angleArea;		
	}
	
	public double calcEffCentroidYaboutX(double stress){		//calculates the new effective centroid of section for bending about x axis: measured from centre of flange in tension
		double centroidY;
		double r = sd.getZLSection(secName).getangleRadius();
		double t = sd.getZLSection(secName).getBaseThickness();
		double h = sd.getZLSection(secName).getHeight();
		double l = sd.getZLSection(secName).getLipLength();
		
		double term1 = (l-r)*t*((l-r)/2. + r);
		double term3 = t*(h/2. - r + getB2Web(stress))*(h/4. + r/2. + getB2Web(stress)/2.);
		double term4 = t*getB1Web(stress)*(h-r-getB1Web(stress)/2.);
		double term5 = t*(getB2Flange(stress) + getB1Flange(stress))*h;
		double term7 = t*geteffLipLength(stress)*(h-r-geteffLipLength(stress)/2.);
		double angles;
		double totalArea = effAreaAboutX(stress);
		
		double cornersTop1 = (h - r + 4.*(r+t/2.)/(3.*Math.PI))*((Math.PI*Math.pow(r+t/2., 2)/4.));
		double cornersTop2 = (h - r + 4.*(r-t/2.)/(3.*Math.PI))*((Math.PI*Math.pow(r-t/2., 2)/4.));
		
		double cornersBottom1 = (r - 4.*(r+t/2.)/(3.*Math.PI))*((Math.PI*Math.pow(r+t/2., 2)/4.));
		double cornersBottom2 = (r - 4.*(r-t/2.)/(3.*Math.PI))*((Math.PI*Math.pow(r-t/2., 2)/4.));
		
		angles = 2*(cornersTop1 - cornersTop2) + 2*(cornersBottom1 - cornersBottom2);
		centroidY = (term1 + term3 + term4 + term5 + term7 + angles)/totalArea;
		
		return centroidY;			//measured from the centre line of the bottom flange (tension flange).
	}
	
	public double effIXXaboutX(double stress){				//Calculates the effective moment of inertia for bending about the x axis
		double iXX;
		double ax = calcEffCentroidYaboutX(stress);
		double r = sd.getZLSection(secName).getangleRadius();
		double t = sd.getZLSection(secName).getBaseThickness();
		double h = sd.getZLSection(secName).getHeight();
		double l = sd.getZLSection(secName).getLipLength();
		double b = sd.getZLSection(secName).getWidth();
		
		double term1 = t*Math.pow(l-r, 3)/12. + t*(l-r)*Math.pow(ax-l/2.-r/2., 2);
		double term2 = (b-2*r)*Math.pow(t, 3)/12. + t*(b-2*r)*Math.pow(ax,2);
		double term3 = t*Math.pow(h/2. - r + getB2Web(stress),3)/12. + t*(h/2. - r + getB2Web(stress))*Math.pow(ax-(h/4.+r/2.+getB2Web(stress)/2.),2);
		double term4 = t*Math.pow(getB1Web(stress),3)/12. + t*getB1Web(stress)*Math.pow(h-r-getB1Web(stress)/2. - ax,2);
		double term5 = (getB2Flange(stress)+getB1Flange(stress))*t/12. + t*(getB2Flange(stress)+getB1Flange(stress))*Math.pow(h-ax,2);
		double term6 = t*Math.pow(geteffLipLength(stress),3)/12. + t*geteffLipLength(stress)*Math.pow(h-r-geteffLipLength(stress)/2. - ax,2);
		
		double cornerATop = Math.PI*Math.pow(r+t/2., 4)/16. + Math.PI*(Math.pow(r+t/2., 2)/4)*Math.pow(h-r - ax , 2);
		double cornerBTop = Math.PI*Math.pow(r-t/2., 4)/16. + Math.PI*(Math.pow(r-t/2., 2)/4)*Math.pow(h-r - ax, 2);
		double cornerABot = Math.PI*Math.pow(r+t/2., 4)/16. + Math.PI*(Math.pow(r+t/2., 2)/4)*Math.pow(r - ax , 2);
		double cornerBBot = Math.PI*Math.pow(r-t/2., 4)/16. + Math.PI*(Math.pow(r-t/2., 2)/4)*Math.pow(r - ax, 2);
		
		double term7 = 2*(cornerATop-cornerBTop) + 2*(cornerABot - cornerBBot);
		
		iXX = term1+term2+term3+term4+term5+term6+term7;
		return iXX;		
	}
	public double calcEffZcAboutX(double stress){
		double effZc;
		double h= sd.getZLSection(secName).getHeight();
		double iXX = effIXXaboutX(stress);
		double centroid = calcEffCentroidYaboutX(stress);
		double y = h-centroid;
		
		effZc = iXX/y;
		return effZc;		
	}
	public void printResultsAboutX(){
		String Mb;
		String slender;
		String fail;
		
		if(distCheck){
			Mb = "Nominal Member Moment Capacity with distortion check:  Mb = "+df.format(nominalMemberMomentCapMbWithDistCheck()/1000000.)+" kNm";
		}else{
			Mb = "Nominal Member Moment Capacity without distortion check: Mb = "+df.format(nominalMemberMomentCapMbNoDistCheck()/1000000.)+" kNm";
		}
		
		if(checkSlender()){
			slender =      "Member satisfy the compression slender limit:             KL/r = "+df.format(slenderness())+" <= 200.0";			
		}else{
			slender =      "!!!!Member not satisfying the compression slender limit:  KL/r = "+df.format(slenderness())+" > 200.0"; 
		}
		
		if(willItfail()){
			fail =        "!!!Not OK: Section will fail for Moment of:                M = "+df.format(maxAppliedMoment)+" kNm";
		}else{
			fail =        "OK: Section will not fail for Moment of:                   M = "+df.format(maxAppliedMoment)+" kNm";
		}
		
		
		System.out.println("\n\n\n****************************************************************************************");
		System.out.println("* CALCULATIONS AND RESULTS FOR BENDING A "+secName+" ABOUT THE X-AXIS");
		System.out.println("**********************************************************************************************");		
		System.out.println(slender);
		System.out.println("The maximum allowed effective length                        L = "+maxLength()+" mm");
		System.out.println("*********************************************************************************************");
		System.out.println("Elastic buckling moment:                   Mo = "+df.format(xAxisMo()/1000000.)+" kNm");
		System.out.println("Slenderness Ratio:                         SR = "+df.format(slendernessForMc()));
		System.out.println("Critical Moment:                           Mc = "+df.format(critMomentMc()/1000000.0)+" kNm");
		System.out.println("Critical elastic buckling stress           fc = "+df.format(criticalStressFc())+" MPa");
		System.out.println("Yield Stress:                              fy = "+df.format(md.getMatProp(matName).getFy(thickness))+" MPa");
		System.out.println("Effective Area at stress fc:                A = "+df.format(effAreaAboutX(criticalStressFc()))+" mm2");
		System.out.println("Effective Area at stress fy:                A = "+df.format(effAreaAboutX(md.getMatProp(matName).getFy(thickness)))+" mm2");
		System.out.println("Effective Centroid Ydir at stress fc:      ax = "+df.format(calcEffCentroidYaboutX(criticalStressFc()))+" mm");
		System.out.println("Effective Centroid Ydir at stress fy:      ax = "+df.format(calcEffCentroidYaboutX(md.getMatProp(matName).getFy(thickness)))+" mm");
		System.out.println("Effective Inertia Moment at stress fc:    IXX = "+df.format(effIXXaboutX(criticalStressFc()))+" mm4");
		System.out.println("Effective Inertia Moment at stress fy:    IXX = "+df.format(effIXXaboutX(md.getMatProp(matName).getFy(thickness)))+" mm4");
		System.out.println("Effective Section Modulus at stress fc:    Zc = "+df.format(calcEffZcAboutX(criticalStressFc()))+" mm3");
		System.out.println("Effective Section Modulus at stress fy:    Ze = "+df.format(calcEffZcAboutX(md.getMatProp(matName).getFy(thickness)))+" mm3");
		System.out.println(Mb);
		System.out.println("Nominal Section Moment Capacity:           Ms = "+df.format(nominalSectionMomentCapMs()/1000000.)+" kNm");
		System.out.println("Ultimate Bending Capacity:                  M = "+df.format(UltimateMemberCapacity()/1000000.)+" kNm");
		System.out.println("**********************************************************************************************");
		System.out.println(fail);
		System.out.println("**********************************************************************************************");
	}
	
	/*
	 * END OF METHODS FOR EFFECTIVE SECTION CALCULATIONS FOR BENDING ABOUT THE X-AXIS
	 */
	
	
	
	/*
	 *  
	 * PART 2 : FOR BENDING ABOUT THE Y AXIS:
	 */
	
	
	public double f1FlangeY(double stress){ //flange stress f1 for compression: lip side
		double f1;
		double r = sd.getZLSection(secName).getangleRadius();
		double b = sd.getZLSection(secName).getWidth();
		
		f1 = stress*((b-r)/(b));		//interpolated value
		
		return f1;
	}
	public double f2FlangeY(double stress){ //flange stress f2 for compression : web side
		double f2;
		double r = sd.getZLSection(secName).getangleRadius();
		double b = sd.getZLSection(secName).getWidth();		
		
		f2 = stress*((r)/(b));		//interpolated value
		
		return f2;
	}
	public double getEffLipLengthY(double stress){ //Edge stiffener effective width.
		double effl;
		double r = sd.getZLSection(secName).getangleRadius();
		double t = sd.getZLSection(secName).getBaseThickness();
		double l = sd.getZLSection(secName).getLipLength() -r ;
		UniCompressedUnstiffElements uCUE  = new UniCompressedUnstiffElements(stress,t,l,matName,secName);
		
		effl = uCUE.calcEffwidth();
		return effl;
	}
	
	public double getB1FlangeY(double stress){ //measured from the compression stress(f1) stiffened side
		double b1f; 
		double r = sd.getZLSection(secName).getangleRadius();
		double t = sd.getZLSection(secName).getBaseThickness();
		double b = sd.getZLSection(secName).getWidth() -2.*r;
		
		double stressF1 = f1FlangeY(stress);
		double stressF2 = f2FlangeY(stress);
		double compPortionLength = b;			//compression portion length
		
		GradientStiffElem gSE = new GradientStiffElem(stressF1,stressF2,t,b,compPortionLength,secName,matName);
		b1f = gSE.getBe1();
		
		return b1f;
	}
	
	public double getB2FlangeY(double stress){	//measured from the centroid axis
		double b2f; 
		double r = sd.getZLSection(secName).getangleRadius();
		double t = sd.getZLSection(secName).getBaseThickness();
		double b = sd.getZLSection(secName).getWidth() -2.*r;
		
		double stressF1 = f1FlangeY(stress);
		double stressF2 = f2FlangeY(stress);
		double compPortionLength = b;			//compression portion length
		
		GradientStiffElem gSE = new GradientStiffElem(stressF1,stressF2,t,b,compPortionLength,secName,matName);
		b2f = gSE.getBe2();
		
		return b2f;
	}
	
	public double effAreaAboutY(double stress){ //effective area
		double area;
		double r = sd.getZLSection(secName).getangleRadius();
		double t = sd.getZLSection(secName).getBaseThickness();
		double be1 = getB1FlangeY(stress);
		double be2 = getB2FlangeY(stress);
		double de = getEffLipLengthY(stress);
		double d= sd.getZLSection(secName).getLipLength()-r;
		double w =sd.getZLSection(secName).getWidth()-2*r;
		double h= sd.getZLSection(secName).getHeight() -2*r;
		
		
		double areaFlange = t*(be2+be1+w);
		double areaWeb = t*h;
		double areaLip = t*(de+d);
		double angleArea = Math.PI*(Math.pow(r+t/2., 2) - Math.pow(r-t/2., 2))/4.;
		
		area = areaFlange + areaLip + areaWeb + 4*angleArea;
		return area;
	}
	public double calcEffCentroidXaboutY(double stress){ //effective centroid calculations measured from web centre
		double eax;
		double r = sd.getZLSection(secName).getangleRadius();
		double t = sd.getZLSection(secName).getBaseThickness();
		double be1 = getB1FlangeY(stress);
		double be2 = getB2FlangeY(stress);
		double de = getEffLipLengthY(stress);
		double d = sd.getZLSection(secName).getLipLength()-r;		
		double b = sd.getZLSection(secName).getWidth();		
		double effArea = effAreaAboutY(stress);
		
		double lips = t*(de + d)*b;
		double b1 = t*be1*(b-r-be1/2.);
		double b2 = t*(be2)*(be2/2.+r);
		double fullflange = t*(b-2*r)*(b/2.);
		double angles;
		double cornersTop1 = (b - r + 4.*(r+t/2.)/(3.*Math.PI))*((Math.PI*Math.pow(r+t/2., 2)/4.));
		double cornersTop2 = (b - r + 4.*(r-t/2.)/(3.*Math.PI))*((Math.PI*Math.pow(r-t/2., 2)/4.));
		
		double cornersBottom1 = (r - 4.*(r+t/2.)/(3.*Math.PI))*((Math.PI*Math.pow(r+t/2., 2)/4.));
		double cornersBottom2 = (r - 4.*(r-t/2.)/(3.*Math.PI))*((Math.PI*Math.pow(r-t/2., 2)/4.));
		
		angles = 2*(cornersTop1 - cornersTop2) + 2*(cornersBottom1 - cornersBottom2);
		
		eax = (lips + b1 + b2+fullflange + angles)/effArea;
		return eax;
	}
	public double effIYYaboutY(double stress){	//effective section moment of inertia
		double iYY;
		double r = sd.getZLSection(secName).getangleRadius();
		double t = sd.getZLSection(secName).getBaseThickness();
		double be1 = getB1FlangeY(stress);
		double be2 = getB2FlangeY(stress);
		double de = getEffLipLengthY(stress);
		double b = sd.getZLSection(secName).getWidth();	
		double d = sd.getZLSection(secName).getLipLength();
		double eax = calcEffCentroidXaboutY(stress);
		double h= sd.getZLSection(secName).getHeight() -2*r;
		
		double deIyy = (de)*Math.pow(t,3)/12. + t*(de)*Math.pow(b+eax,2); 
		double dIyy = (d)*Math.pow(t, 3)/12. + t*d*Math.pow(b-eax,2);
		double b1iYY = t*Math.pow(be1, 3)/12. + be1*t*Math.pow((b-r-be1/2.)+eax,2);
		double b2iYY = t*Math.pow(be2, 3)/12. + t*(be2)*Math.pow(eax + (be2)/2. +r, 2);
		double biYY = t*Math.pow(b-2*r,3)/12. + t*(b-2*r)*Math.pow(b/2.-eax,2);
		double webIYY = (h)*Math.pow(t, 3)/12. + t*(h)*eax*eax;
		
		double cornerATop = 2*Math.PI*Math.pow(r+t/2., 4)/16. + Math.PI*(Math.pow(r+t/2., 2)/4)*Math.pow(b-r - eax , 2) + Math.PI*(Math.pow(r+t/2., 2)/4)*Math.pow(b-r + eax , 2);
		double cornerBTop = 2*Math.PI*Math.pow(r-t/2., 4)/16. + Math.PI*(Math.pow(r-t/2., 2)/4)*Math.pow(b-r - eax, 2) + Math.PI*(Math.pow(r-t/2., 2)/4)*Math.pow(b-r + eax, 2);
		double cornerABot = 2*Math.PI*Math.pow(r+t/2., 4)/16. + Math.PI*(Math.pow(r+t/2., 2)/4)*Math.pow(r - eax , 2) + Math.PI*(Math.pow(r+t/2., 2)/4)*Math.pow(r + eax , 2);
		double cornerBBot = 2*Math.PI*Math.pow(r-t/2., 4)/16. + Math.PI*(Math.pow(r-t/2., 2)/4)*Math.pow(r - eax, 2) + Math.PI*(Math.pow(r-t/2., 2)/4)*Math.pow(r + eax, 2);
		
		double angleIYY = (cornerATop-cornerBTop) + (cornerABot - cornerBBot);
		iYY = deIyy + b1iYY +b2iYY + dIyy + biYY + webIYY + angleIYY;
		
		return iYY;
	}
	public double calcEffZcAboutY(double stress){ //effective section modulus
		double effZc;
		double b = sd.getZLSection(secName).getWidth();
		double iYY = effIYYaboutY(stress);
		double centroid = calcEffCentroidXaboutY(stress);
		double y = b+centroid;
		
		effZc = iYY/y;
		return effZc;
	}
	
	public void printResultsAboutY(){
		String Mb;
		String slender;
		String fail;
		
		if(distCheck){
			Mb = "Nominal Member Moment Capacity with distortion check:  Mb = "+df.format(nominalMemberMomentCapMbWithDistCheck()/1000000.)+" kNm";
		}else{
			Mb = "Nominal Member Moment Capacity without distortion check: Mb = "+df.format(nominalMemberMomentCapMbNoDistCheck()/1000000.)+" kNm";
		}
		
		if(checkSlender()){
			slender =      "Member satisfy the compression slender limit:             KL/r = "+df.format(slenderness())+" <= 200.0";			
		}else{
			slender =      "!!!!Member not satisfying the compression slender limit:  KL/r = "+df.format(slenderness())+" > 200.0"; 
		}
		
		if(willItfail()){
			fail =        "!!!Not OK: Section will fail for Moment of:                M = "+df.format(maxAppliedMoment)+" kNm";
		}else{
			fail =        "OK: Section will not fail for Moment of:                   M = "+df.format(maxAppliedMoment)+" kNm";
		}
		
		System.out.println("\n\n\n**************************************************************************************");
		System.out.println("* CALCULATIONS AND RESULTS FOR BENDING A "+secName+" ABOUT THE Y-AXIS");
		System.out.println("* COMPRESSION AT THE EDGE STIFFENER");
		System.out.println("********************************************************************************************");
		System.out.println(slender);
		System.out.println("The maximum allowed effective length                        L = "+maxLength()+" mm");
		System.out.println("*********************************************************************************************");
		System.out.println("Elastic buckling moment:                   Mo = "+df.format(xAxisMo()/1000000.)+" kNm");
		System.out.println("Slenderness Ratio:                         SR = "+df.format(slendernessForMc()));
		System.out.println("Critical Moment:                           Mc = "+df.format(critMomentMc()/1000000)+" kNm");
		System.out.println("Critical elastic buckling stress           fc = "+df.format(criticalStressFc())+" MPa");
		System.out.println("Yield Stress:                              fy = "+df.format(md.getMatProp(matName).getFy(thickness))+" MPa");
		System.out.println("Effective Area at stress fc:                A = "+df.format(effAreaAboutY(criticalStressFc()))+" mm2");
		System.out.println("Effective Area at stress fy:                A = "+df.format(effAreaAboutY(md.getMatProp(matName).getFy(thickness)))+" mm2");
		System.out.println("Effective Centroid Xdir at stress fc:      ax = "+df.format(calcEffCentroidXaboutY(criticalStressFc()))+" mm");
		System.out.println("Effective Centroid Xdir at stress fy:      ax = "+df.format(calcEffCentroidXaboutY(md.getMatProp(matName).getFy(thickness)))+" mm");
		System.out.println("Effective Inertia Moment at stress fc:    IYY = "+df.format(effIYYaboutY(criticalStressFc()))+" mm4");
		System.out.println("Effective Inertia Moment at stress fy:    IYY = "+df.format(effIYYaboutY(md.getMatProp(matName).getFy(thickness)))+" mm4");
		System.out.println("Effective Section Modulus at stress fc:    Zc = "+df.format(calcEffZcAboutY(criticalStressFc()))+" mm3");
		System.out.println("Effective Section Modulus at stress fy:    Ze = "+df.format(calcEffZcAboutY(md.getMatProp(matName).getFy(thickness)))+" mm3");
		System.out.println(Mb);
		System.out.println("Nominal Section Moment Capacity:           Ms = "+df.format(nominalSectionMomentCapMs()/1000000.)+" kNm");
		System.out.println("Ultimate Bending Capacity:                  M = "+df.format(UltimateMemberCapacity()/1000000)+" kNm");
		System.out.println("***********************************************************************************");
		System.out.println(fail);
		System.out.println("**********************************************************************************************");
	}
	
	
	
	/*
	 * END OF METHODS FOR EFFECTIVE SECTION CALCULATIONS FOR BENDING ABOUT THE Y-AXIS:
	 * COMPRESSION AT THE LIPS.
	 */
	
	
	
	
	
	
	
	
	/*
	 * The following method determines the Polar Radius of gyration about the shear centre according eqn 3.3.3.2(10)
	 */	
	public double polarRadiusShear(){
		double ro1;
		double term1 = Math.pow(sd.getZLSection(secName).getPolarRX(),2);		//Polar Radius about the x axis
		double term2 = Math.pow(sd.getZLSection(secName).getPolarRY(), 2);		//Polar Radius about the y axis
		ro1 = Math.pow(term1+term2, 0.5);			//eqn 3.3.3.2(10)......Also note the Yo is equal zero.
		
		return ro1;  		//returns the Polar radius
	}
		
	/*
	 * The following method determines the elastic buckling stress for the z axis
	 * Foz: ELASTIC TORSIONAL BUCKLING
	 */	
	public double elasticBucklingStressFoz(){
		double foz;
		double term1 = md.getMatProp(matName).getG()*sd.getZLSection(secName).getJ() ;
		double term2 = Math.pow(Math.PI, 2)*md.getMatProp(matName).getYoung()*sd.getZLSection(secName).getWarping();
		double term3 = md.getMatProp(matName).getG()*sd.getZLSection(secName).getJ()*Math.pow(Lz, 2);
		double term4 = sd.getZLSection(secName).getArea()*Math.pow(polarRadiusShear(), 2);
		foz = term1/term4*(1+term2/term3);			//eqn 3.3.3.2(12)
		
		return foz;
	}	
	/*
	 * The following method determines the elastic buckling stress for the y axis
	 */	
	public double elasticBucklingStressFoy(){
		double foy;
		double term1 = Math.pow(Math.PI,2)*md.getMatProp(matName).getYoung();
		double term2 = Math.pow(Ly/sd.getZLSection(secName).getPolarRY(), 2);		
		foy = term1/term2;			//eqn 3.3.3.2(11)
		
		return foy;		//returns the elastic buckling stress about the weak axis
	}
	
	/*
	 * The following method determines the elastic buckling stress for the x axis
	 */
	
	public double elasticBucklingStressFox(){		
		double fox;
		double term1 = Math.pow(Math.PI,2)*md.getMatProp(matName).getYoung();
		double term2 = Math.pow(Lx/sd.getZLSection(secName).getPolarRX(), 2);		
		fox = term1/term2;			//eqn 3.4.2(1)
		
		return fox;		//returns the elastic buckling stress about the strong axis
	}	
	
	/*
	 * DISTORTIONAL CALCULATIONS
	 * SANS 10162-2:2011 CLAUSE 3.3.3.3(a)
	 */
	
	public double distMemberCapacity(){
		double Mb;
		double Z;		
		double k = dist.k();
		double fc = getFcDist();
		
		if(k<0.0){
			Z=getZcDist();			//if k as calculated in distortional stress is negative use effective section modulus
		}else{			
			Z = getZfDistortional();
		}
		Mb = Z*fc;
		
		return Mb;
	}
	
	public double getZcDist(){  //if k as calculated in distortional stress is negative use effective section modulus
		double Zc;
		
		if(axis.equals("X")){
			Zc = calcEffZcAboutX(getFcDist());
		}else{												
			Zc = calcEffZcAboutY(getFcDist());			
		}		
		return Zc;
	}
	
	public double getZfDistortional(){
		double Zf;
		
		if(axis.equals("X")){
			Zf = sd.getZLSection(secName).getSectionModulusX();
		}else{							
			Zf = sd.getZLSection(secName).getSectionModulusY();		
		}		
		return Zf;
	}
	
	public double getFcDist(){
		double fc;
		double Mc = getMcDist();
		double Zf = getZfDistortional();
		fc = Mc/Zf;						//eqn 3.3.3.3(2)
		return fc;
	}
	
	public double getMcDist(){		
		double Mc;
		double slenderness = getSlendernessDist();
		double My =getMyDist();
		
		if(slenderness <= 0.674){
			Mc = My;									//eqn 3.3.3.3(3)
		}else{
			Mc = (My/slenderness)*(1-0.22/slenderness);	//eqn 3.3.3.3(4)
		}		
		return Mc;
	}
	public double getModDist(){
		double fod = dist.distBucklingfod();
		double Zf = getZfDistortional();
		return Zf*fod;
	}
	public double getMyDist(){
		double fy = md.getMatProp(matName).getFy(thickness);
		double Zf = getZfDistortional();
		return Zf*fy;
	}
	public double getSlendernessDist(){
		return Math.sqrt(getMyDist()/getModDist());
	}
	
	/*
	 * SLENDERNESS CALCULATIONS OF SECTION
	 */
	public double maxLength(){
		Slenderness S = new Slenderness(Lx,Ly,sd.getZLSection(secName).getPolarRX(),sd.getZLSection(secName).getPolarRY() );
		return S.maxEffLengthCompression();
	}
	public double slenderness(){
		Slenderness S = new Slenderness(Lx,Ly,sd.getZLSection(secName).getPolarRX(),sd.getZLSection(secName).getPolarRY() );
		return S.MaxSlenderness();
	}
	public boolean checkSlender(){
		Slenderness S = new Slenderness(Lx,Ly,sd.getZLSection(secName).getPolarRX(),sd.getZLSection(secName).getPolarRY() );
		return S.checkSlenderCompression();
	}
	
	
	
	
	
}

