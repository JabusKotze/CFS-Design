package distortional;
/*
 * @author JJR Kotze (email 15692183@sun.ac.za)
 * For Department of Civil Engineering, Stellenbosch University
 * 29 October 2012 (Version 1.0 of CFS design)
 */
import section.*;
import materials.*;



/*
 * This class contains all the methods and calculations to determine the distortional buckling stress
 * of a simple lipped channel in compression: According SANS 10162-2:2011 Appendix D2
 */
public class DistortionalLCSection {
	
	private String  matName;
	
	private MaterialDatabase md = Parameters.getMaterialDatabase();
	
	private double bF;		//width of flange
	private double dL;		//Lip length
	private double t;		//Base thickness of section
	private double bW;		//Height of web
	private double E;		//youngs modulus of material E
	
	public DistortionalLCSection(double bF,double dL,double t, double bW, String matName){
		
		this.matName = matName;		
		this.bF = bF;				//Setting the dimensions and properties
		this.dL = dL;
		this.t = t;
		this.bW = bW;
		E = md.getMatProp(this.matName).getYoung();
	}
	
	
	/* 
	 * SECTION PROPERTIES OF ONLY THE FLANGE AND LIP ARE CALCULATED USING THE FEW METHODS BELOW
	 * 
	 * 
	 * The first method calculates the area 
	 */	
	public double area(){		
		double area = (bF+dL)*t;							//eqn D2(10)
		
		return area;
	}
	
	/*
	 * The following method determines the centroid in the x direction.
	 */
	public double centroidX(){
		double x = (Math.pow(bF, 2)+2*bF*dL)/(2*(bF+dL));	//eqn D2(11)
		
		return x;
	}
	
	/*
	 * The following method determines the centroid in the y direction.
	 */
	public double centroidY(){
		double y = Math.pow(dL, 2)/(2*(dL+bF));				//eqn D2(12)
		
		return y;
	}
	
	/*
	 * The following method determines the torsion constant for the flange and lip interaction
	 * for pure St Venant torsion
	 */
	public double torsionConstantJ(){
		double j = (Math.pow(t, 3)*(bF+dL))/3;				//eqn D2(13)
		
		return j;
	}
	
	/*
	 * The following method returns the second moment of inertia about the X-axis
	 */
	public double secondMomentXX(){
		double iXX;
		double term1 = bF*Math.pow(t, 3)/12;
		double term2 = t*Math.pow(dL, 3)/12;
		double term3 = bF*t*Math.pow(centroidY(), 2);
		double term4 = dL*t*Math.pow(dL/2-centroidY(), 2);
		iXX = term1+term2+term3+term4;						//eqn D2(14)
		
		return iXX;
		
	}

	/*
	 * The following method returns the second moment of inertia about the Y-axis
	 */
	public double secondMomentYY(){
		double iYY;
		double term1 = t*Math.pow(bF, 3);
		double term2 = dL*Math.pow(t, 3);
		double term3 = dL*t*Math.pow((bF-centroidX()),2);
		double term4 = bF*t*Math.pow(centroidX()-bF/2, 2);
		iYY = term1+term2+term3+term4;						//eqn D2(15)
		
		return iYY;
	}	
	
	/*
	 * The following method returns the second moment of inertia about the Y-axis
	 */
	public double torsionalMomentIxy(){
		double iXY;
		double term1 = bF*t*(bF/2-centroidX())*-1*centroidY();
		double term2 = dL*t*(dL/2-centroidY())*(bF-centroidX());
		iXY = term1+term2;
		
		return iXY;
	}
	/*
	 * END OF LIP AND FLANGE INTERFACE PROPERTIES
	 */
	
	
	/*
	 * FOLLOWING IS THE METHODS TO CALCULATE THE ELASTIC DISTORTIONAL BUCKLING STRESS fod
	 */
	
	public double sigma1(){ //eqn D2(2)
		double s1;
		double term1 = n()/beta1();
		double term2 = secondMomentXX()*Math.pow(bF, 2)+0.039*torsionConstantJ()*Math.pow(lambda(),2);
		double term3 = k()/(beta1()*n()*E);
		s1 = term1*term2 + term3;
		
		return s1;		
	}
	
	
	public double sigma2(){ //eqn D2(3)
		double s2 = n()*(secondMomentYY() + 2/beta1()*centroidY()*bF*torsionalMomentIxy());
		return s2;
	}
	
	
	public double sigma3(){ //eqn D2(4)
		double s3 = n()*(sigma1()*secondMomentYY()-n()/beta1()*Math.pow(torsionalMomentIxy(), 2)*Math.pow(bF, 2));
		
		return s3;
	}
	
	
	public double beta1(){	//eqn D2(5)
		double b = Math.pow(centroidX(), 2) + ((secondMomentXX()+secondMomentYY())/area());
		return b;
	}
	
	
	public double lambda(){	//eqn D2(6)
		double lam = 4.8*Math.pow(secondMomentXX()*Math.pow(bF, 2)*bW/Math.pow(t, 3), 0.25);
	
		return lam;
	}
	
	
	public double n(){		//eqn D2(7)
		double n = Math.pow(Math.PI/lambda(), 2);
		
		return n;
	}
	
	
	public double k(){		//eqn D2(8)
		double k;
		double term1 = E*Math.pow(t, 3)/(5.46*(bW+0.06*lambda()));
		double term2 = 1.11*distBucklingfodAccent()/(E*Math.pow(t, 2));
		double term3 = Math.pow(Math.pow(bW,2)*lambda()/(Math.pow(bW,2)+Math.pow(lambda(),2)), 2);
		
		k= term1*(1-term2*term3);
		
		return k;		
	}
	
	
	public double sigma1alt(){	//eqn D2(9)
		double s1 = n()/beta1()*(secondMomentXX()*Math.pow(bF, 2) + 0.039*torsionConstantJ()*Math.pow(lambda(),2));
		
		return s1;
	}
	
	public double sigma3alt(){
		double s3 = n()*(sigma1alt()*secondMomentYY()-n()/beta1()*Math.pow(torsionalMomentIxy(), 2)*Math.pow(bF, 2));
		
		return s3;
	}
	
	
	public double distBucklingfod(){	//eqn D2(1)
		double fod;
		double term1 = E/(2*area());
		double term2 = sigma1()+sigma2() - Math.pow(Math.pow(sigma1()+sigma2(),2)-4*sigma3(), 0.5);
		fod = term1*term2;
			
		return fod;
	}
	
	
	public double distBucklingfodAccent(){	//eqn D2(1) but with eqn D2(9)
		double fod;
		double term1 = E/(2*area());
		double term2 = sigma1alt()+sigma2() - Math.pow(Math.pow(sigma1alt()+sigma2(),2)-4*sigma3alt(), 0.5);
		fod = term1*term2;
		
		return fod;
	}
}