package effectiveWidth;
/*
 * @author JJR Kotze (email 15692183@sun.ac.za)
 * For Department of Civil Engineering, Stellenbosch University
 * 29 October 2012 (Version 1.0 of CFS design)
 */
/*
 * THIS CLASS CONTAINS THE METHODS TO DETERMINE THE EFFECTIVE WIDTHS FOR A 
 * STIFFENED ELEMENT WITH A STRESS GRADIENT: SANS 10162-2:2011 CLAUSE 2.2.3.2
 */
public class GradientStiffElem {
	private double stressF1,stressF2;   //F1 is always positive (compression), but F2 is negative (tension), but can also be positive. f1 shall always be greater than or equal to f2
	private double t; //thickess
	private double b; //plate width
	private double compPortionLength;  //indicates the length portion of the plate under compression
	private String matName,secName;
	private UniCompressedStifElements uniformBe;
	
	
	public GradientStiffElem(double stressF1, double stressF2, double t, double b,double compPortionLength, String secName, String matName){
		this.secName = secName;
		this.matName = matName;
		this.stressF1 = stressF1;
		this.stressF2 = stressF2;
		this.b = b;
		this.t = t;
		this.compPortionLength = compPortionLength;
		
		uniformBe = new UniCompressedStifElements(this.stressF1, this.getK(), 0.0, this.t,this.b, this.matName, this.secName);
	}
	
	public double getStressRatio(){  //eqn 2.2.3.2(5)
		double ratio = stressF2/stressF1;
		return ratio;
	}
	
	public double getK(){  //plate buckling coefficient
		double k = 4.0 + 2*Math.pow(1-getStressRatio(),3) + 2.0*(1-getStressRatio());  //eqn 2.2.3.2(4)
		return k;
	}
	
	public double getBe1(){ //from the larger stress side
		double be1 = getBe()/(3.0 - getStressRatio());		//eqn 2.2.3.2(1)
		return be1;
	}
	
	public double getBe2(){  //from the smaller stress side
		double be2;
		
		if(getStressRatio() <= -0.236){
			be2 = getBe()/2;								//eqn 2.2.3.2(2)
			if((getBe1()+be2) > compPortionLength){
				be2 = compPortionLength - getBe1();			
			}else{
				//do nothing
			}
		}else{
			be2 = getBe() - getBe1();						//eqn 2.2.3.2(3)
			if((getBe1()+be2) > compPortionLength){
				be2 = compPortionLength - getBe1();
			}else{
				//do nothing
			}
		}
		
		return be2;
	}
	
	public double getBe(){
		double be = uniformBe.effectiveWidth();
		return be;
	}
	
	
}
