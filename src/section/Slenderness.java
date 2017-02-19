package section;
/*
 * @author JJR Kotze (email 15692183@sun.ac.za)
 * For Department of Civil Engineering, Stellenbosch University
 * 29 October 2012 (Version 1.0 of CFS design)
 */

/*
 * THIS CLASS CONTAINS THE METHODS TO CALCULATE THE SLENDERNESS FOR MEMBERS SUBJECT TO:
 * COMPRESSION OR TENSION: FROM SANS 10162-2:2011
 * 
 * COMPRESSION: KL/r <= 200
 * TENSION:     KL/r <= 300
 */

public class Slenderness {
	
	private double Lx,Ly;		//effective lengths with respect to their axis
	private double rx,ry;		//polar radius of gyration with respect to their axis
	
	public Slenderness(double Lx,double Ly,double rx,double ry){
		this.Lx = Lx;
		this.Ly = Ly;
		this.rx = rx;
		this.ry = ry;	
	}
	
	
	public double MaxSlenderness(){
		double slenderx = Lx/rx;
		double slendery = Ly/ry;
		double slenderMax = Math.max(slenderx, slendery);
		
		return slenderMax;				
	}
	
	public boolean checkSlenderCompression(){
		boolean check;
		
		if(MaxSlenderness()<=200.0){
			check = true;				//satisfy the compression slender limit of 200
		}else{
			check = false;				//does not satisfy compression slender limit of 200
		}
		
		return check;
	}
	
	public boolean checkSlenderTension(){
		boolean check;
		
		if(MaxSlenderness()<=300.0){
			check = true;				//satisfy the tension slender limit of 300
		}else{
			check = false;				//does not satisfy tension slender limit of 300
		}
		
		return check;
	}
	
	public double maxEffLengthCompression(){		//returns the max allowable effective length of member in compression
		double effLength = 200.0*Math.min(rx, ry);
		
		return effLength;
	}
	
	public double maxEffLengthTension(){			//returns the max allowable effective length of member in Tension
		double effLength = 300.0*Math.min(rx, ry);
		return effLength;
	}
	
	

}
