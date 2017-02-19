package effectiveWidth;
/*
 * @author JJR Kotze (email 15692183@sun.ac.za)
 * For Department of Civil Engineering, Stellenbosch University
 * 29 October 2012 (Version 1.0 of CFS design)
 */

/*This class is for Clause 2.3.2.2 of SANS 10162-2:2011
 * for Unstiffened elements and edge stiffeners with stress gradient
 */

public class GradientUnstifElemAndEdgeStiffener {
	
	private double stressF1,stressF2,t,b; //stressF1 = the Maximum compression fibre stress , stressF2 = the minimum compressio/tension fiber stress at either edge, t = thickness, b= width
	
	private String matProp, secName; 	// this is the name of the material used and section respectively
	private String stressAtEdge; 		//Specified as either "F1" or "F2" 
										//if "F1" then the max compression stress is at the edge of unstiffened element
										//if "F2" then the max compression stress is at the stiffened side of unstiffened element
	private String facing;				//Specified as either "Inward" or "Upward"
										//"Inward" : then we have to do with decreasing stress
										//"Outward" : then we have to do with increasing stress; thus "F1" will always be present at edge
	
	public GradientUnstifElemAndEdgeStiffener(double stressF1,double stressF2,String facing,String stressAtEdge, double t, double b, String matProp, String secName ){
				
		this.stressF1 = stressF1;
		this.stressF2 = stressF2;
		this.facing = facing;
		this.stressAtEdge = stressAtEdge;
		this.t = t;
		this.b = b;
		this.matProp= matProp;
		this.secName =secName;
	}
	
	
	
	public double effWidth(){
		double effW;
		
		if(facing.equals("Inward")){			//lips facing inward; i.e. example a lipped c section
			if(stressF2 < 0.0){
				if(stressAtEdge.equals("F1")){
					effW = effWidthB1();	//F1 at the unsupported edge
				}else{
					effW = effWidthB2(); //F1 at the supported edge 
				}
			}else{
				effW = effWidthA1();
			}
		}else{
			effW = effWidthA2();
		}
		return effW;
		
	}
	
	public double stressRatio(){	//This method returns the stress ratio as per eqn 2.3.2.2(1)
		return stressF2/stressF1;
	}	
	
	/*
	 * This method returns the effective width of the member with stress gradient as per 
	 * Clause 2.3.2.2(a)i
	 */
	public double effWidthA1(){
		UniCompressedStifElements sE = new UniCompressedStifElements(stressF1,bothCompKvalueA1(),0,t,b,matProp,secName);   //should make use of clause 2.2.1.2 to determine effective width
		return sE.effectiveWidth();
	}
	
	
	/*
	 * This method returns the effective width of the member with stress gradient as per 
	 * Clause 2.3.2.2(a)ii
	 */
	public double effWidthA2(){
		UniCompressedStifElements sE = new UniCompressedStifElements(stressF1,bothCompKvalueA2(),0,t,b,matProp,secName);   //should make use of clause 2.2.1.2 to determine effective width
		return sE.effectiveWidth();
	}
	
	/*
	 * This method returns the effective width of the member with stress gradient as per 
	 * Clause 2.3.2.2(b)i
	 */
	public double effWidthB1(){
		UniCompressedStifElements sE = new UniCompressedStifElements(stressF1,compAndTensionKValueB1(),0.0,t,b,matProp,secName);   //should make use of clause 2.2.1.2 to determine effective width;
		double effWF = (1-stressRatio())*(1-0.22*(1-stressRatio())/sE.slendernessRatio())/sE.slendernessRatio();	//effective width factor from eqn 2.3.2.2(4)
		double term1 = 0.673*(1-stressRatio());  //this is the limit on the slendernessRatio
		
		if(sE.slendernessRatio()<=term1){ //then p=1.0
			sE = new UniCompressedStifElements(stressF1,compAndTensionKValueB1(),1.0,t,b,matProp,secName);
			return sE.effectiveWidth();
		}else{								//then p = effWF
		sE = new UniCompressedStifElements(stressF1,compAndTensionKValueB1(),effWF,t,b,matProp,secName);   //should make use of clause 2.2.1.2 to determine effective width;
		return sE.effectiveWidth();
		}
		}

	/*
	 * This method returns the effective width of the member with stress gradient as per 
	 * Clause 2.3.2.2(b)ii
	 */
	public double effWidthB2(){
		UniCompressedStifElements sE = new UniCompressedStifElements(stressF1,compAndTensionKValueB2(),0.0,t,b,matProp,secName);   //should make use of clause 2.2.1.2 to determine effective width;
		double effWF = (1+stressRatio())*(1-0.22/sE.slendernessRatio())/sE.slendernessRatio()-stressRatio();	//effective width factor from eqn 2.3.2.2(6)
		double term1 = 0.673;
		
		if(stressRatio()>-1 && stressRatio()<0){  	//sets the limits on the stressRatio
			if(sE.slendernessRatio()<=term1){		//sets the limits on the slenderness Ratio
				sE = new UniCompressedStifElements(stressF1,compAndTensionKValueB2(),1.0,t,b,matProp,secName);
				return sE.effectiveWidth();  //then returns effective width for a effective width factor of 1
			}else{
				sE = new UniCompressedStifElements(stressF1,compAndTensionKValueB2(),effWF,t,b,matProp,secName);
				return sE.effectiveWidth();  //then returns effective width for a effective width factor from eqn 2.3.2.2(6)
			}
		}else{
			sE = new UniCompressedStifElements(stressF1,compAndTensionKValueB2(),1.0,t,b,matProp,secName);
			return sE.effectiveWidth();
		}
		
	}
	
	
	
	
	/*returns the plate buckling coefficient according clause 2.3.2.2(a)i 
	 * This is when both edges are subjected to compression and where the stress
	 * decreases towards unstiffened side (this is generally for c section lips)
	 */
	public double bothCompKvalueA1(){ 
		return 0.578/(stressRatio()+0.34);
	}
	
	
	
	
	
	
	/*returns the plate buckling coefficient according clause 2.3.2.2(a)ii
	 * This is when both edges are subjected to compression and where the stress
	 * increases towards unstiffened side (this is generally for hat section lips)
	 */
	public double bothCompKvalueA2(){
		return 0.57-0.21*stressRatio()+0.07*Math.pow(stressRatio(), 2);
	}
	
	
	
	
	
	/*returns the plate buckling coefficient according clause 2.3.2.2(b)i
	 * This is when the unstiffened edge is subjected to compression 
	 */	
	public double compAndTensionKValueB1(){
		return 0.57-0.21*stressRatio()+0.07*Math.pow(stressRatio(), 2);
	}
	
	
	
	
	
	
	/*returns the plate buckling coefficient according clause 2.3.2.2(b)ii
	 * This is when the unstiffened edge is subjected to Tension
	 */	
	public double compAndTensionKValueB2(){
		return (1.7-5*stressRatio()+17.1*Math.pow(stressRatio(), 2));
	}

	
	
	
	
	
}
