package section;
/*
 * @author JJR Kotze (email 15692183@sun.ac.za)
 * For Department of Civil Engineering, Stellenbosch University
 * 29 October 2012 (Version 1.0 of CFS design)
 */
import constants.*;

public class GeneralSection {
	private String secName;					//these sections should always have the prefix "GS"
	private double Area, coatedThickness, I; //I: second moment of area
	private double y;       // distance from extreme compression fiber to centroid to calculate Z
	private GeneralConstants gc = new GeneralConstants();
	/*
	 * I:  should be specified as ex: 23.6 if it is 23,600,000.00mm4 thus it will be multiplied by 1E6
	 */
	
	public GeneralSection(String name, double area, double coatedThickness, double I, double y){
		this.secName = name;
		this.Area = area;
		this.coatedThickness = coatedThickness;
		this.I = I;
		this.y = y;
	}
	
	public String getName(){
		return secName;
	}
	public double getArea(){
		return Area;
	}
	public double getBaseThickness(){
		return coatedThickness - gc.getBTReduction();
	}
	public double getI(){
		return I*1000000;
	}
	public double getY(){
		return y;
	}
	public double getPolarR(){
		return Math.sqrt(getI()/getArea());
	}
	public double calcZ(){	//this is the section modulus for the considered direction of bending
		return getI()/getY();
	}
	public double getMaxlength(){
		return 200.0*getPolarR();
	}
	
	
}
