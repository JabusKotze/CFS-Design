package effectiveWidth;
/*
 * @author JJR Kotze (email 15692183@sun.ac.za)
 * For Department of Civil Engineering, Stellenbosch University
 * 29 October 2012 (Version 1.0 of CFS design)
 */
import section.Parameters;
import materials.*;

/*Clause 2.4 from SANS 10162-2:2011 ; for uniformly compressed
 * Elements with an edge stiffener
*/

public class UniCompressedElemWithEdgeStif { 
	
	
	private double stressF,t,b,d,theta;		 	// t=thickness ; b=width ; d = lip length ; theta = angle of lip
	private String matProp, secName;			// this is the name of the material and section respectively
	private MaterialDatabase md = Parameters.getMaterialDatabase();				// this refers to the material database
	private UniCompressedStifElements sE;		// effective width method to be used in conjunction with this clause
	
	private double stressF1, stressF2;			//These are the stresses on the edge stiffener
	private String facing;						//specified as either "Inward" or "outward" facing lip
	private String stressAtEdge;				//refers to if f1 or f2 will be at the edge of the edge stiffener
	
	
	
	
	public UniCompressedElemWithEdgeStif(double stressF, double stressF1, double stressF2 ,String facing,String stressAtEdge, double t, double b, double d, double theta, String matProp, String secName){
		this.stressF = stressF;
		this.stressF1 = stressF1;
		this.stressF2 = stressF2;
		this.stressAtEdge = stressAtEdge;
		this.facing = facing;
		this.t = t;
		this.b = b;
		this.d = d;
		this.matProp =matProp;
		this.theta = theta;
		this.secName = secName;
	}
	
	
	public double slendernessFactor(){ 			//based on eqn 2.4.2(10) in SANS 10162-2
		double s;
		s = 1.28*Math.pow(md.getMatProp(matProp).getYoung()/stressF, 0.5);
		
		//System.out.println("The Slenderness Factor from eqn 2.4.2(10); S = "+s);
		return s;
	}	
	
	public double calcIs(){						//this method returns second moment of inertia for stiffener as from eqn 2.4.2(10)
		double term1 = Math.pow(d, 3)*t*Math.pow(Math.sin(theta*Math.PI/180),2)/12;
		if(term1<=calcIa()){ 					//this is to limit Is, as Is<=Ia
			return term1;
		}else{
			return calcIa();
		}		
	}	
	
	public double calcIa(){ //this method returns the second moment of inertia based on eqn 2.4.2(11)
		
		double term1 = 399*Math.pow(t,4);
		double term2 = Math.pow((b/t)/slendernessFactor()-0.328,3);
		double term3 = term1*term2;
		
		double term4 = Math.pow(t,4);
		double term5 = 115*(b/t)/4/slendernessFactor()+5;
		double term6 = term4*term5;
		
		if(term3<=term6){
			return term3;
		}else{
			return term6;
		}				
	}	
	
	public double calcB1(){  //this method returns the effective width at the web/flange interface
		
		double term1 = 0.328*slendernessFactor();  //This is the limits to be checked on the width/thickness ratio
		
		if(b/t<=term1){								//returns eqn 2.4.2(2)
			return setEffB()/2;					
		}else{
			return setEffB()/2*(calcIs()/calcIa());	//returns eqn 2.4.2(5)
		}		
	}	
	
	public double calcB2(){ //this method returns the effective width at the edge stiffener side
		double term1 = 0.328*slendernessFactor();  //This is the limits to be checked on the width/thickness ratio
		
		if(b/t<=term1){
			return setEffB()/2;						//returns eqn 2.4.2(2)
		}else{
			return setEffB()-calcB1();				//returns eqn 2.4.2(6)
		}
	}	
	
	public double calcD(){ // this is the effective lip length
		double term1 = 0.328*slendernessFactor();  //This is the limits to be checked on the width/thickness ratio
		
		if(b/t<=term1){
			return setEffD();						//returns eqn 2.4.2(2)
		}else{
			return setEffD()*calcIs()/calcIa();				//returns eqn 2.4.2(6)
		}
	}	
	
	public double calcK(){ //this is the slenderness factor from table 2.4.2
		
		double term1 = 3.57*Math.pow(calcIs()/calcIa(), calcN())+0.43;
		double dl = d+t/2;
		double term2 = (4.82-5*dl/b)*Math.pow(calcIs()/calcIa(), calcN())+0.43;
		if(dl/b<=0.25){
		if(term1<=4){
			return term1;
		}else{return 4.0;}
		}else if(dl>0.25 && dl<=0.8){
			if(term2<=4){
				return term2;
			}else{return 4.0;}
			
		}else {return term1;}		
	}	
	
	public double setEffB(){ //the effective width can be obtained from SANS 2.2.1.2
		sE = new UniCompressedStifElements(stressF,calcK(),0.0,t,b,matProp,secName); //initialising method for uniformed compressed stiffened elements with p set to 0
		return sE.effectiveWidth();
	}	
	
	public double setEffD(){ //the effective width of the lip is returned using clause 2.3.2.2 for Gradient unstiffened elements and edge stiffeners
		
		if(stressF1 == stressF2){  // this is for when pure compression is applied
			UniCompressedUnstiffElements uCSE = new UniCompressedUnstiffElements(stressF1,t,d,matProp,secName);
			return uCSE.calcEffwidth();
		}else{
			GradientUnstifElemAndEdgeStiffener gUEES = new GradientUnstifElemAndEdgeStiffener(stressF1, stressF2,facing,stressAtEdge,t,d, matProp,secName);
			return gUEES.effWidth();
		}		
	}	
	
	
	public double calcN(){     //Power constant to be used in table 2.4.2 for determining the k factor
		double term1 = 0.582 - (b/t)/4/slendernessFactor();
		
		if(term1 >= 1/3){
			return term1;
		}else{return 1/3;}	
	}	
	}
