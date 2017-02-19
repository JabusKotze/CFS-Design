package dSM;
/*
 * @author JJR Kotze (email 15692183@sun.ac.za)
 * For Department of Civil Engineering, Stellenbosch University
 * 29 October 2012 (Version 1.0 of CFS design)
 */

import materials.MaterialDatabase;
import section.Parameters;
import section.SectionDatabase;
/*
 * THIS CLASS CONTAINS THE METHODS TO CHECK IF THE GIVEN SECTION FALLS WITHIN THE LIMITS
 * PRESCRIBED IN TABLE 7.1.2 OF SANS:10162-2:2011
 */
public class BendingSectionReq {
	private String secName, matName;
	private SectionDatabase sd = Parameters.getSectionDatabase();
	private MaterialDatabase md = Parameters.getMaterialDatabase();
	
	public BendingSectionReq(String secName, String matName){
		this.secName =secName;
		this.matName = matName;
	}
	
	
	/*
	 * THE FOLLOWING METHOD RETURNS TRUE OR FALSE FOR A LIPPED CHANNEL WITH 90 DEGREE ANGLES
	 */	
	public boolean lippedChannelS(){
		double b = sd.getLCSection(secName).getWidth();
		double h = sd.getLCSection(secName).getHeight();
		double L = sd.getLCSection(secName).getLipLength();
		double t = sd.getLCSection(secName).getBaseThickness();
		double r = sd.getLCSection(secName).getangleRadius();
		double fy = md.getMatProp(matName).getFy(t);
		double E = md.getMatProp(matName).getYoung();
		
		boolean limit = true;
		
		if(h/t < 321.){
			
		}else{
			limit = false;
		}
		
		
		if(b/t < 75.){
			
		}else{
			limit = false;
		}
		
		if(0.0<L/t && L/t < 34){
			
		}else{
			limit = false;
		}
		
		if(1.5<h/b && h/b<17.0){
			
		}else{
			limit = false;
		}
		
		if(0.0 <L/b && L/b < 0.7){
			
		}else{
			limit = false;
		}
		
		if(E/fy > 421.){
			
		}else{
			limit = false;
		}
		
		if(r/t < 10){
			
		}else{
			limit = false;
		}
		
		return limit;		
	}
	
	/*
	 * THE FOLLOWING METHOD RETURNS TRUE OR FALSE FOR A LIPPED CHANNEL WITH
	 * ONE OR TWO INTERMEDIATE STIFFENERS
	 */
	public boolean lippedChannelIntStiffener(){
		boolean limit = true;
		//limits still needs to be programmed
		return limit;
	}
	
	/*
	 * THE FOLLOWING METHOD RETURNS TRUE OR FALSE FOR A LIPPED Z SECTION WITH 
	 * LIP ANGLES OF 50 DEGREES TO THE HORIZON
	 */
	public boolean lippedZsection(){
		double b = sd.getZLSection(secName).getWidth();
		double h = sd.getZLSection(secName).getHeight();
		double L = sd.getZLSection(secName).getLipLength();
		double t = sd.getZLSection(secName).getBaseThickness();
		double r = sd.getZLSection(secName).getangleRadius();
		double fy = md.getMatProp(matName).getFy(t);
		double E = md.getMatProp(matName).getYoung();
		
		boolean limit = true;
		
		if(h/t < 183.){
			
		}else{
			limit = false;
		}
		
		
		if(b/t < 58.){
			
		}else{
			limit = false;
		}
		
		if(10.<L/t && L/t < 16.){
			
		}else{
			limit = false;
		}
		
		if(2.5<h/b && h/b<4.1){
			
		}else{
			limit = false;
		}
		
		if(0.15 <L/b && L/b < 0.43){
			
		}else{
			limit = false;
		}
		
		if(E/fy > 400.){
			
		}else{
			limit = false;
		}
		
		if(r/t < 10){
			
		}else{
			limit = false;
		}
		
		return limit;	
	}
	
	
	/*
	 * THE FOLLOWING METHOD RETURNS TRUE OR FALSE FOR A HAT SECTION WITH 
	 * STIFFENED FLANGES IN COMPRESSION
	 */
	public boolean stifHatDecks(){
		boolean limit = true;
		//limits still needs to be programmed
		return limit;
	}
	/*
	 * THE FOLLOWING METHOD RETURNS TRUE OR FALSE FOR A TRAPEZOID WITH
	 * STIFFENED FLANGE IN COMPRESSION
	 */
	public boolean stifTrapezoids(){
		boolean limit = true;
		//limits still needs to be programmed
		return limit;
	}
}
